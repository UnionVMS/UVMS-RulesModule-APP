/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.exchange.module.v1.*;
import eu.europa.ec.fisheries.schema.exchange.v1.TypeRefType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.Collections;

import static eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils.marshallJaxBObjectToString;
import static eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils.unMarshallMessage;
import static eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue.EXCHANGE;

@Stateless
@Slf4j
public class ExchangeRuleServiceBean implements ExchangeRuleService {

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    @Override
    public boolean identificationRefExists(String refGuid, String... typeRefType) {
        Boolean faQueryIdentificationExists = false;
        try {
            LogRefIdByTypeExistsRequest existsRequest = new LogRefIdByTypeExistsRequest();
            for (String refType : typeRefType){
                existsRequest.getRefTypes().add(EnumUtils.getEnum(TypeRefType.class, refType));
            }
            existsRequest.getRefTypes().removeAll(Collections.singleton(null));
            existsRequest.setRefGuid(refGuid);
            existsRequest.setMethod(ExchangeModuleMethod.LOG_REF_ID_BY_TYPE_EXISTS);
            String jaxBObjectToString = marshallJaxBObjectToString(existsRequest);
            String jmsMessageID = producer.sendDataSourceMessage(jaxBObjectToString, EXCHANGE);
            TextMessage message = consumer.getMessage(jmsMessageID, TextMessage.class);
            String text = message.getText();
            LogRefIdByTypeExistsResponse response = unMarshallMessage(text, LogRefIdByTypeExistsResponse.class);
            faQueryIdentificationExists = response.getRefGuid() != null;

        } catch (JAXBException | MessageException | JMSException e) {
            log.error(e.getMessage(), e);
        }
        return faQueryIdentificationExists;
    }

    @Override
    public boolean identificationExists(String messageGuid, String typeRefType) {
        Boolean messageGuidIdentificationExists = false;
        try {
            LogIdByTypeExistsRequest existsRequest = new LogIdByTypeExistsRequest();
            existsRequest.setRefType(EnumUtils.getEnum(TypeRefType.class, typeRefType));
            existsRequest.setMessageGuid(messageGuid);
            existsRequest.setMethod(ExchangeModuleMethod.LOG_ID_BY_TYPE_EXISTS);
            String jaxBObjectToString = marshallJaxBObjectToString(existsRequest);
            String jmsMessageID = producer.sendDataSourceMessage(jaxBObjectToString, EXCHANGE);
            TextMessage message = consumer.getMessage(jmsMessageID, TextMessage.class);
            String text = message.getText();
            LogIdByTypeExistsResponse response = unMarshallMessage(text, LogIdByTypeExistsResponse.class);
            messageGuidIdentificationExists = response.getMessageGuid() != null;

        } catch (JAXBException | MessageException | JMSException e) {
            log.error(e.getMessage(), e);
        }
        return messageGuidIdentificationExists;
    }
}
