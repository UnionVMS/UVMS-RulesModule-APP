/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesMdrProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.MdrLoadingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.mdr.communication.*;
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
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesMdrProducerBean mdrProducer;

    // This variable will store the date that the last entity in mdr (module) was refreshed.
    // @Info : refresh to 'last_success' column of 'mdr_codelist_status' table in mdr schema.
    private Date mdrRefreshDate;

    private Map<String, EnrichedBRMessage> enrichedBRMessageMap;

    private boolean alreadyLoadedOnce = false;

    private Date lastTimeRefreshDateWasRetrieved;

    private static final int MB = 1024 * 1024;

    private static final long ONE_MINUTES_IN_MILLIS = 60000L;
    private static final long TWO_MINUTES_IN_MILLIS = 120000L;
    private static final long FIVE_MINUTES_IN_MILLIS = 300000L;



    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>();
        enrichedBRMessageMap = new ConcurrentHashMap<>();
    }

    /**
     * It fetches the latest refresh date from MDR and checks if the local cache needs to be refreshed.
     */
    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.WRITE)
    public void loadAllMdrCodeLists(boolean isFromReport) {
        try {
            if (!alreadyLoadedOnce) {
                log.info("MDR Cache was never loaded, loading now..");
                populateMdrCacheDateAndCheckIfRefreshDateChanged();
                populateMdrCache();
            } else if (isFromReport && oneMinuteHasPassed() && populateMdrCacheDateAndCheckIfRefreshDateChanged()) { // We fetch MdrCacheDate only once per minute.
                log.info("MDR Cache in MDR was updated.. Going to refresh Rules mdr cahce now...");
                populateMdrCache();
            }
        } catch (MdrLoadingException e) {
            log.error("Exception while trying to loadAllMdrCodeLists...", e);
        }
    }

    /**
     * Chooses which way to load mdr cache (at once/one by one) depending on the free memory available!
     * In this way we avoid an OutOfMemory Exception of the heapSpace. :)
     */
    private void populateMdrCache() throws MdrLoadingException {
        long freeMemory = Runtime.getRuntime().freeMemory() / MB;
        log.info("Free Memory : [{}] MB", freeMemory);
        if ((!alreadyLoadedOnce && freeMemory < 800) || freeMemory < 150) {
            populateAllMdrOneByOne();
        } else {
            populateAllMdrOneByOne(); //populateAllMdrAtOnce(); This one sometimes is to heavy for activemq!
        }
        overrideBRMessages();
    }

    private boolean oneMinuteHasPassed() {
        return (Math.abs(new Date().getTime() - lastTimeRefreshDateWasRetrieved.getTime()) / 1000) > 59;
    }

    /**
     * It loads all the mdr codelists one by one from the mdr module [GET_MDR_CODE_LIST] Request Type.
     */
    private void populateAllMdrOneByOne() {
        log.info("Loading MDR..");
        for (final MDRAcronymType type : MDRAcronymType.values()) {
            try {
                cache.put(type, mdrCodeListByAcronymType(type));
            } catch (MdrLoadingException e) {
                log.error(e.getMessage());
            }
        }
        log.info("{} lists cached", cache.size());
        alreadyLoadedOnce = true;
        log.info("MDR refresh Date {}", mdrRefreshDate);
    }

    /**
     * It loads all the mdr codelists at once from the mdr module [GET_ALL_MDR_CODE_LIST] Request Type.
     */
    private void populateAllMdrAtOnce() throws MdrLoadingException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Loading All MDR...");
        MdrGetAllCodeListsResponse allMdrCodeLists = getAllMdrCodeLists();
        if (allMdrCodeLists != null && CollectionUtils.isNotEmpty(allMdrCodeLists.getCodeLists())) {
            for (SingleCodeListRappresentation codeList : allMdrCodeLists.getCodeLists()) {
                cache.put(MDRAcronymType.valueOf(codeList.getAcronym()), codeList.getDataSets());
            }
        } else {
            log.warn("Mdr Response resulted null/empty! Check if MDR is deployed!");
        }
        log.info("Nr. {} lists were cached.", cache.size());
        alreadyLoadedOnce = true;
        log.info("MDR refresh Date {}", mdrRefreshDate);
        long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
        log.info("Loading and caching took [{}] seconds!", elapsed);
    }

    private MdrGetAllCodeListsResponse getAllMdrCodeLists() throws MdrLoadingException {
        MdrGetAllCodeListsResponse response;
        String request;
        try {
            request = MdrModuleMapper.createFluxMdrGetAllCodeListRequest();
            String corrId = mdrProducer.sendModuleMessageNonPersistent(request, rulesConsumer.getDestination(), FIVE_MINUTES_IN_MILLIS);
            TextMessage message = rulesConsumer.getMessage(corrId, TextMessage.class, ONE_MINUTES_IN_MILLIS);
            if (message != null) {
                response = unmarshallTextMessage(message.getText(), MdrGetAllCodeListsResponse.class);
                return response;
            }
        } catch (MdrModelMarshallException | JMSException | ActivityModelMarshallException | MessageException e) {
            log.error("Some very bad error happened while trying to get the MDR Codelists {}", e);
            throw new MdrLoadingException(e.getMessage());
        }
        return null;
    }

    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.READ)
    public List<ObjectRepresentation> getEntry(MDRAcronymType acronymType) {
        List<ObjectRepresentation> result;
        if (acronymType != null) {
            result = cache.get(acronymType);
            if (CollectionUtils.isEmpty(result)) {
                log.warn(" Reloading {}", acronymType);
                try {
                    cache.put(acronymType, mdrCodeListByAcronymType(acronymType));
                } catch (MdrLoadingException e) {
                    log.error("Error when trying to refresh codelist {}", acronymType);
                    return null;
                }
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
    private List<ObjectRepresentation> mdrCodeListByAcronymType(MDRAcronymType acronymType) throws MdrLoadingException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            String request = MdrModuleMapper.createFluxMdrGetCodeListRequest(acronymType.name());
            String corrId = mdrProducer.sendModuleMessageNonPersistent(request, rulesConsumer.getDestination(), FIVE_MINUTES_IN_MILLIS);
            TextMessage message = rulesConsumer.getMessage(corrId, TextMessage.class, ONE_MINUTES_IN_MILLIS);
            long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if (elapsed > 100) {
                log.info("Loading {} took {} ", acronymType, stopwatch);
            }
            if (message != null) {
                MdrGetCodeListResponse response = unmarshallTextMessage(message.getText(), MdrGetCodeListResponse.class);
                return response.getDataSets();
            }
        } catch (MessageException | MdrModelMarshallException | JMSException | ActivityModelMarshallException ex) {
            throw new MdrLoadingException("Error while trying to load mdr!", ex);
        }
        return new ArrayList<>();
    }

    /**
     * Checks if the refresh date from mdr module has changed.
     * If it has changed sets cacheDateChanged=true; and refreshed the mdrRefreshDate with the new date.
     */
    private boolean populateMdrCacheDateAndCheckIfRefreshDateChanged() throws MdrLoadingException {
        boolean cacheDateChanged = false;
        try {
            Date mdrDate = getLastTimeMdrWasRefreshedFromMdrModule();
            lastTimeRefreshDateWasRetrieved = new Date();
            if (mdrDate != null && !mdrDate.equals(mdrRefreshDate)) { // This means we have a new date from MDR module..
                mdrRefreshDate = mdrDate;
                cacheDateChanged = true;
            }
        } catch (MessageException e) {
            log.error(" Couldn't populate MDR Refresh date.. MDR Module is deployed?", e);
            throw new MdrLoadingException(e.getMessage());
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
            String corrId = mdrProducer.sendModuleMessageNonPersistent(MdrModuleMapper.createMdrGetLastRefreshDateRequest(), rulesConsumer.getDestination(), ONE_MINUTES_IN_MILLIS);
            TextMessage message = rulesConsumer.getMessage(corrId, TextMessage.class, ONE_MINUTES_IN_MILLIS);
            if (message != null) {
                MdrGetLastRefreshDateResponse response = JAXBUtils.unMarshallMessage(message.getText(), MdrGetLastRefreshDateResponse.class);
                return response.getLastRefreshDate() != null ? DateUtils.xmlGregorianCalendarToDate(response.getLastRefreshDate()) : null;
            }
            throw new MessageException("[FATAL-ERROR] Couldn't get LastRefreshDate from MDR Module! Mdr is deployed?");
        } catch (MdrModelMarshallException | JAXBException | JMSException e) {
            throw new MessageException("[FATAL-ERROR] Couldn't get LastRefreshDate from MDR Module! Mdr is deployed?", e);
        }
    }

    /**
     * This function overrides business rules messages to the ones defined in MDR;
     */
    private void overrideBRMessages() {
        enrichedBRMessageMap = new HashMap<>();
        final List<ObjectRepresentation> objRapprList = new ArrayList<>();
        List<ObjectRepresentation> brDef = getEntry(MDRAcronymType.FA_BR);
        List<ObjectRepresentation> saleBrDef = getEntry(MDRAcronymType.SALE_BR);
        // For start up non reachable MDR purposes :)
        if (CollectionUtils.isNotEmpty(brDef)) {
            objRapprList.addAll(brDef);
        }
        if (CollectionUtils.isNotEmpty(saleBrDef)) {
            objRapprList.addAll(saleBrDef);
        }
        objRapprList.removeAll(Collections.singleton(null));
        if (CollectionUtils.isEmpty(objRapprList)) {
            return;
        }
        final String MESSAGE_COLUMN = "messageIfFailing";
        final String BR_ID_COLUMN = "code";
        final String BR_NOTE_COLUMN = "note";
        final String BR_ERROR_TYPE_COLUMN = "fluxGpValidationTypeCode";
        final String START_DATE = "startDate";
        final String END_DATE = "endDate";
        for (ObjectRepresentation objectRepr : objRapprList) {
            String brId = null;
            String errorMessage = null;
            String note = null;
            String errType = null;
            String startDate = null;
            String endDate = null;
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
                if (BR_ERROR_TYPE_COLUMN.equals(columnName)) {
                    errType = field.getColumnValue();
                }
                if (START_DATE.equals(columnName)) {
                    startDate = field.getColumnValue();
                }
                if (END_DATE.equals(columnName)) {
                    endDate = field.getColumnValue();
                }
            }
            EnrichedBRMessage err = new EnrichedBRMessage(note, errorMessage, errType.contains("ERR") ? ErrorType.ERROR.value() : ErrorType.WARNING.value());
            err.setEndDate(DateUtils.parseToUTCDate(endDate, "yyyy-MM-dd HH:mm:ss.S"));
            err.setStartDate(DateUtils.parseToUTCDate(startDate, "yyyy-MM-dd HH:mm:ss.S"));
            enrichedBRMessageMap.put(brId, err);
        }
    }

    public EnrichedBRMessage getErrorMessage(String brId) {
        return enrichedBRMessageMap.get(brId);
    }

    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.READ)
    public boolean isMdrCacheLoaded() {
        return cache.size() > 10;
    }

}