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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.TextMessage;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetAllCodeListsResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;
import un.unece.uncefact.data.standard.mdr.communication.SingleCodeListRappresentation;

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
                .refreshAfterWrite(1, TimeUnit.HOURS)
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

        //loadAllMdrCache();
    }

    private void loadAllMdrCache(){
        try {
            log.info("[START] Loading All MDR CodeLists...");
            long start = System.currentTimeMillis();
            String request = MdrModuleMapper.createFluxMdrGetAllCodeListRequest();
            String corrId = producer.sendDataSourceMessage(request, DataSourceQueue.MDR_EVENT);
            TextMessage message = consumer.getMessage(corrId, TextMessage.class);
            if (message != null) {
                MdrGetAllCodeListsResponse response = eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller.unmarshallTextMessage(message, MdrGetAllCodeListsResponse.class);
                List<SingleCodeListRappresentation> codeLists = response.getCodeLists();
                if(CollectionUtils.isNotEmpty(codeLists)){
                    for(SingleCodeListRappresentation singleList : codeLists){
                        cache.put(MDRAcronymType.fromValue(singleList.getAcronym()), singleList.getDataSets());
                    }
                }
            }
            long end = System.currentTimeMillis() - start;
            log.info("[FINISH] It took : " + end / 1000 + " seconds to load all MDR Cache (CodeLists)...");
        } catch (MessageException | MdrModelMarshallException | RulesModelMarshallException e) {
            log.error("[ERROR] Error while trying to get all mdr codelists!", e);
        }
    }

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
        String s = producer.sendDataSourceMessage(request, DataSourceQueue.MDR_EVENT);
        TextMessage message = consumer.getMessage(s, TextMessage.class);
        if (message != null) {
            MdrGetCodeListResponse response = unmarshallTextMessage(message.getText(), MdrGetCodeListResponse.class);
            return response.getDataSets();

        }
        return null;
    }

}