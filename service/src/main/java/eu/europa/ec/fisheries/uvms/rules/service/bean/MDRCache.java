/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller.unmarshallTextMessage;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

/**
 * @author Gregory Rinaldi
 */
@Singleton
@Startup
@Slf4j
public class MDRCache {

    @Getter
    private LoadingCache<MDRAcronymType, List<ObjectRepresentation>> cache;

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    @PostConstruct
    public void init(){
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(100)
                .initialCapacity(80)
                .recordStats()
                .build(
                        new CacheLoader<MDRAcronymType, List<ObjectRepresentation>>() {
                            @Override
                            public List<ObjectRepresentation> load(MDRAcronymType acronymType) throws Exception {
                                return mdrCodeListByAcronymType(acronymType);
                            }
                        }
                );
    }

    @Lock(LockType.READ)
    public List<ObjectRepresentation> getEntry(MDRAcronymType acronymType) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<ObjectRepresentation> result = emptyList();
        if (acronymType != null) {
            result = cache.getUnchecked(acronymType);
        }

        long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
        if (elapsed > 0.25){
            log.info("Loading " + acronymType + " took " + stopwatch);
        }

        return result;
    }

    @SneakyThrows
    private List<ObjectRepresentation> mdrCodeListByAcronymType(MDRAcronymType acronym) {
        String request = MdrModuleMapper.createFluxMdrGetCodeListRequest(acronym.name());
        log.info("Send MdrGetCodeListRequest message to MDR. Acronym: " + acronym.name());
        String corrId = producer.sendDataSourceMessage(request, DataSourceQueue.MDR_EVENT);
        long timeoutMillis = 30 * 60 * 1000;
        TextMessage message = consumer.getMessage(corrId, TextMessage.class, timeoutMillis);
        log.info("Received response message");
        if (message != null) {
            MdrGetCodeListResponse response = unmarshallTextMessage(message.getText(), MdrGetCodeListResponse.class);
            return response.getDataSets();
        }
        return new ArrayList<>();
    }

}