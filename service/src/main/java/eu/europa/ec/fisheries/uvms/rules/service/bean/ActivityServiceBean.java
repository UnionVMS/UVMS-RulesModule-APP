/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMapperException;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.IsUniqueIdResponse;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.TextMessage;

import static eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper.mapIsUniqueIdRequestRequest;
import static eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleResponseMapper.mapToIsUniqueIdResponseFromResponse;

@Stateless
@LocalBean
@Slf4j
public class ActivityServiceBean {

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    public boolean isUnique(ActivityTableType tableType, String identifierId, String identifierSchemeId){

        boolean result = false;
        try {
            String request = mapIsUniqueIdRequestRequest(tableType, identifierId, identifierSchemeId);
            String s = producer.sendDataSourceMessage(request, DataSourceQueue.ACTIVITY);
            TextMessage message = consumer.getMessage(s, TextMessage.class);
            IsUniqueIdResponse isUniqueIdResponse = mapToIsUniqueIdResponseFromResponse(message, s);
            result = isUniqueIdResponse.isValue();
        } catch (MessageException | ActivityModelMapperException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
