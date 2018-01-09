/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils.marshallJaxBObjectToString;
import static eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils.unMarshallMessage;
import static eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue.EXCHANGE;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import eu.europa.ec.fisheries.schema.exchange.module.v1.LogRefIdByTypeExistsRequest;
import eu.europa.ec.fisheries.schema.exchange.module.v1.LogRefIdByTypeExistsResponse;
import eu.europa.ec.fisheries.schema.exchange.v1.TypeRefType;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.ExchangeRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;

@Singleton
@Slf4j
public class ExchangeRuleServiceBean implements ExchangeRuleService {

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    @Override
    public boolean faQueryIdentificationExists(String refGuid, String typeRefType) {
        Boolean faQueryIdentificationExists = false;
        try {
            LogRefIdByTypeExistsRequest existsRequest = new LogRefIdByTypeExistsRequest();
            TypeRefType refType = EnumUtils.getEnum(TypeRefType.class, typeRefType);
            existsRequest.setRefGuid(refGuid);
            existsRequest.setRefType(refType);
            String jaxBObjectToString = marshallJaxBObjectToString(existsRequest);
            String jmsMessageID = producer.sendDataSourceMessage(jaxBObjectToString, EXCHANGE);
            TextMessage message = consumer.getMessage(jmsMessageID, TextMessage.class);
            String text = message.getText();
            LogRefIdByTypeExistsResponse response = unMarshallMessage(text, LogRefIdByTypeExistsResponse.class);
            faQueryIdentificationExists = response.getRefGuid() != null;
        } catch (JAXBException | MessageException | JMSException e) {
            e.printStackTrace();
        }
        return faQueryIdentificationExists;
    }
}
