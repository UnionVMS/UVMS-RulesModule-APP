/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller.unmarshallTextMessage;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Gregory Rinaldi, Andi Kovi
 */
@Singleton
@Startup
@Slf4j
public class MDRCache {

    @Getter
    private Map<MDRAcronymType, List<ObjectRepresentation>> cache;

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    // This variable will store the date that the last entity in mdr (module) was refreshed.
    // @Info : refresh to 'last_success' column of 'mdr_codelist_status' table in mdr schema.
    private Date mdrRefreshDate;

    private Map<String, EnrichedBRMessage> errorMessages;

    private boolean alreadyLoadedOnce = false;

    private Date lastTimeRefreshDateWasRetrieved;

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>();
        errorMessages = new ConcurrentHashMap<>();
    }

    /**
     * It fetches the latest refresh date from MDR and checks if the local cache needs to be refreshed.
     */
    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.WRITE)
    public void loadAllMdrCodeLists(boolean isFromReport) {
        if (!alreadyLoadedOnce) {
            populateMdrCacheDateAndCheckIfRefreshDateChanged();
            populateAllMdr();
        } else if (isFromReport && oneMinuteHasPassed() && populateMdrCacheDateAndCheckIfRefreshDateChanged()) { // We fetch MdrCacheDate only once per minute.
            populateAllMdr();
        }
    }

    private boolean oneMinuteHasPassed() {
        return (Math.abs(new Date().getTime() - lastTimeRefreshDateWasRetrieved.getTime()) / 1000) > 59;
    }

    private void populateAllMdr() {
        log.info("Loading MDR");
        for (final MDRAcronymType type : MDRAcronymType.values()) {
            cache.put(type, mdrCodeListByAcronymType(type));
        }
        log.info("{} lists cached", cache.size());
        alreadyLoadedOnce = true;
        log.info("MDR refresh Date {}", mdrRefreshDate);
    }

    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.READ)
    public List<ObjectRepresentation> getEntry(MDRAcronymType acronymType) {
        List<ObjectRepresentation> result;
        if (acronymType != null) {
            result = cache.get(acronymType);
            if (CollectionUtils.isEmpty(result)) {
                log.warn(" Reloading {}", acronymType);
                cache.put(acronymType, mdrCodeListByAcronymType(acronymType));
                result = cache.get(acronymType);
                if (CollectionUtils.isEmpty(result)) {
                    log.error(" Failed to reload {}", acronymType);
                }
            }
            return result;
        }
        return emptyList();
    }

    /**
     * Get one mdr codeList from MDR.
     *
     * @param acronymType
     * @return
     */
    @SneakyThrows
    private List<ObjectRepresentation> mdrCodeListByAcronymType(MDRAcronymType acronymType) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String request = MdrModuleMapper.createFluxMdrGetCodeListRequest(acronymType.name());
        String corrId = producer.sendDataSourceMessage(request, DataSourceQueue.MDR_EVENT, 300000L, DeliveryMode.NON_PERSISTENT);
        TextMessage message = consumer.getMessage(corrId, TextMessage.class, 300000L);
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        if (elapsed > 100) {
            log.info("Loading {} took {} ", acronymType, stopwatch);
        }
        if (message != null) {
            MdrGetCodeListResponse response = unmarshallTextMessage(message.getText(), MdrGetCodeListResponse.class);
            return response.getDataSets();
        }
        return new ArrayList<>();
    }

    /**
     * Checks if the refresh date from mdr module has changed.
     * If it has changed sets cacheDateChanged=true; and refreshed the mdrRefreshDate with the new date.
     */
    private boolean populateMdrCacheDateAndCheckIfRefreshDateChanged() {
        boolean cacheDateChanged = false;
        try {
            Date mdrDate = getLastTimeMdrWasRefreshedFromMdrModule();
            lastTimeRefreshDateWasRetrieved = new Date();
            if (mdrDate != null && !mdrDate.equals(mdrRefreshDate)) { // This means we have a new date from MDR module..
                mdrRefreshDate = mdrDate;
                cacheDateChanged = true;
            }
        } catch (MessageException e) {
            log.error(" Couldn't populate MDR Refresh date.. MDR Module is deployed?");
        }
        return cacheDateChanged;
    }

    /**
     * Calls MDR module to get the latest date the MDR lists werisPresentInMDRListe refreshed.
     *
     * @return
     * @throws MessageException
     */
    private Date getLastTimeMdrWasRefreshedFromMdrModule() throws MessageException {
        try {
            String corrId = producer.sendDataSourceMessage(MdrModuleMapper.createMdrGetLastRefreshDateRequest(), DataSourceQueue.MDR_EVENT, 30000L, DeliveryMode.NON_PERSISTENT);
            TextMessage message = consumer.getMessage(corrId, TextMessage.class, 30000L);
            if (message != null) {
                MdrGetLastRefreshDateResponse response = JAXBUtils.unMarshallMessage(message.getText(), MdrGetLastRefreshDateResponse.class);
                return response.getLastRefreshDate() != null ? DateUtils.xmlGregorianCalendarToDate(response.getLastRefreshDate()) : null;
            }
            throw new MessageException("[FATAL-ERROR] Couldn't get LastRefreshDate from MDR Module! Mdr is deployed?");
        } catch (MdrModelMarshallException | JAXBException | JMSException e) {
            throw new MessageException("[FATAL-ERROR] Couldn't get LastRefreshDate from MDR Module! Mdr is deployed?");
        }
    }

    /**
     * This function maps all the error messages to the ones defined in MDR;
     */
    public void loadCacheForFailureMessages() {
        if (!MapUtils.isEmpty(errorMessages)) {
            return;
        }
        errorMessages = new HashMap<>();
        final List<ObjectRepresentation> objRapprList = new ArrayList<ObjectRepresentation>() {{
            addAll(getEntry(MDRAcronymType.FA_BR_DEF));
            addAll(getEntry(MDRAcronymType.SALE_BR_DEF));
        }};
        final String MESSAGE_COLUMN = "messageIfFailing";
        final String BR_ID_COLUMN = "code";
        final String BR_NOTE_COLUMN = "note";
        for (ObjectRepresentation objectRepr : objRapprList) {
            String brId = null;
            String errorMessage = null;
            String note = null;
            for (ColumnDataType field : objectRepr.getFields()) {
                final String columnName = field.getColumnName();
                if (MESSAGE_COLUMN.equals(columnName)) {
                    errorMessage = field.getColumnValue();
                }
                if (BR_ID_COLUMN.equals(columnName)) {
                    brId = field.getColumnValue();
                }
                if (BR_NOTE_COLUMN.equals(columnName)) {
                    note = field.getColumnValue();
                }
            }
            errorMessages.put(brId, new EnrichedBRMessage(note, errorMessage));
        }
    }

    @AccessTimeout(value = 10, unit = MINUTES)
    public EnrichedBRMessage getErrorMessage(String brId) {
        return errorMessages.get(brId);
    }

    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.READ)
    public boolean isMdrCacheLoaded() {
        return cache.size() > 10;
    }

}