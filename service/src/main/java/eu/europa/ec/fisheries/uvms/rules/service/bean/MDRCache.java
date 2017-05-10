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

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.TextMessage;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.EnumUtils;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

/**
 * @author Gregory Rinaldi
 */
@Singleton
public class MDRService {

    private LoadingCache<MDRAcronymType, List<String>> cache;

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    public MDRService() {
        if (cache == null) {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    //.refreshAfterWrite(1, TimeUnit.HOURS)
                    .build(
                            new CacheLoader<MDRAcronymType, List<String>>() {
                                @Override
                                public List<String> load(MDRAcronymType acronymType) throws Exception {
                                    return mdrCodeListByAcronymType(acronymType);
                                }
                            }
                    );
        }
    }

    private List<String> getEntry(MDRAcronymType acronymType) {
        List<String> result = emptyList();
        if (acronymType != null) {
            result = cache.getUnchecked(acronymType);
        }
        return result;
    }

    public boolean isPresentInList(String listName, String codeValue){
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        return getEntry(anEnum).contains(codeValue);
    }

    @SneakyThrows
    private List<String> mdrCodeListByAcronymType(MDRAcronymType acronym) {

        String request = MdrModuleMapper.createFluxMdrGetCodeListRequest(acronym.name());
        String s = producer.sendDataSourceMessage(request, DataSourceQueue.MDR_EVENT);
        TextMessage message = consumer.getMessage(s, TextMessage.class);

        List<String> stringList = emptyList();

        if (message != null) {
            MdrGetCodeListResponse response = unmarshallTextMessage(message.getText(), MdrGetCodeListResponse.class);
            for (ObjectRepresentation objectRep : response.getDataSets()) {
                extractCodes(stringList, objectRep);
            }
        }
        return stringList;
    }

    private void extractCodes(List<String> stringList, ObjectRepresentation objectRep) {
        for (ColumnDataType nameVal : objectRep.getFields()) {
            if ("code".equals(nameVal.getColumnName())) {
                stringList.add(nameVal.getColumnValue());
            }
        }
    }
}