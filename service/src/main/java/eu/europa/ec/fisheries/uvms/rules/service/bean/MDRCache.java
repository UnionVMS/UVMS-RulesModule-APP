package eu.europa.ec.fisheries.uvms.rules.service.bean;

/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.concurrent.*;

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

    // This variable will store the Date last time this @cache was refreshed.
    private Date cacheRefreshDate;

    // This is the date that the last entity in mdr (module) was refreshed.
    private Date mdrRefreshDate;

    // This will avoid 70 date comparisons, otherwise unavoidable..
    private boolean cacheDateChanged;

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>();
    }


    /**
     *  It fetches the latest refresh date from MDR and checks if the local cache needs to be refreshed.
     *
     */
    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.WRITE)
    public void loadAllMdrCodeLists() {
        try {
            populateMdrCacheDateAndCheckIfRefreshDateChanged();
            if(cacheDateChanged){
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                List<Callable<List<ObjectRepresentation>>> callableList = new ArrayList<>();
                for (final MDRAcronymType type : MDRAcronymType.values()) {
                    callableList.add(new Callable<List<ObjectRepresentation>>() {
                        @Override
                        public List<ObjectRepresentation> call() {
                            return mdrCodeListByAcronymType(type);
                        }
                    });
                }
                executorService.invokeAll(callableList);
                awaitTerminationAfterShutdown(executorService);
                log.info("[INFO] MDR Cache Refresh was Needed and done already! Last time MDRs (CodeLists) were refreshed : [ " + mdrRefreshDate + " ]..");
                cacheRefreshDate = new Date(mdrRefreshDate.getTime());
                log.info("MDRCache size: " + cache.size());
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<ObjectRepresentation> getEntry(MDRAcronymType acronymType) {
        List<ObjectRepresentation> result;
        if (acronymType != null) {
            result = cache.get(acronymType);
            if(CollectionUtils.isEmpty(result)){ // reload if its empty! Shouldn't happen, but if it does we have the meccanism set up!
                cache.put(acronymType,mdrCodeListByAcronymType(acronymType));
                result = cache.get(acronymType);
                if(CollectionUtils.isEmpty(result)){
                    log.error("[ERROR] Tried to reload (since it was empty) acronym [ "+acronymType+" ] but in the end it is again empty! Is it a good acronym that exists in MDR??" +
                            "Please Have a look at MDRAcronymType!");
                }
            }
            return result;
        }
        return emptyList();
    }

    /**
     *  Gtes an mdr codelist from MDR modules.
     *
     * @param acronymType
     * @return
     */
    @SneakyThrows
    private List<ObjectRepresentation> mdrCodeListByAcronymType(MDRAcronymType acronymType) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String request = MdrModuleMapper.createFluxMdrGetCodeListRequest(acronymType.name());
        String corrId = producer.sendDataSourceMessage(request, DataSourceQueue.MDR_EVENT);
        TextMessage message = consumer.getMessage(corrId, TextMessage.class, 300000L);
        long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
        if (elapsed > 0.5) {
            log.info("Loading " + acronymType + " took " + stopwatch);
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
    private void populateMdrCacheDateAndCheckIfRefreshDateChanged() {
        cacheDateChanged = false;
        // TODO : optimize for : if at least 5 minutes have passed...
        try {
            Date mdrDate = getLastTimeMdrWasRefreshedFromMdrModule();
            if (mdrRefreshDate == null || !mdrDate.equals(mdrRefreshDate)) { // This means we have a new date from MDR module..
                mdrRefreshDate = mdrDate;
                cacheDateChanged = true;
            }
        } catch (MessageException e) {
            log.error("[ERROR] Couldn't populate MDR Refresh date.. MDR Module is deployed?");
        }
    }

    /**
     * Calls MDR module to get the latest date the MDR lists werisPresentInMDRListe refreshed.
     *
     * @return
     * @throws MessageException
     */
    private Date getLastTimeMdrWasRefreshedFromMdrModule() throws MessageException {
        try {
            String corrId = producer.sendDataSourceMessage(MdrModuleMapper.createMdrGetLastRefreshDateRequest(), DataSourceQueue.MDR_EVENT);
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

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.MINUTES)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
