/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.jms.JMSException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class RulesMessageProducerBean extends AbstractProducer implements RulesMessageProducer, ConfigMessageProducer {

    private final static Logger LOG = LoggerFactory.getLogger(RulesMessageProducerBean.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException  {
        LOG.debug("Sending message to {}", queue.name());
        String replyQueue = MessageConstants.QUEUE_RULES;
        String destination = null;
        try {
            switch (queue) {
                case MOVEMENT:
                    destination = MessageConstants.QUEUE_MODULE_MOVEMENT;
                    break;
                case CONFIG:
                    destination = MessageConstants.QUEUE_CONFIG;
                    break;
                case ASSET:
                    destination = MessageConstants.QUEUE_ASSET_EVENT;
                    break;
                case MOBILE_TERMINAL:
                    destination = MessageConstants.QUEUE_MOBILE_TERMINAL_EVENT;
                    break;
                case EXCHANGE:
                    destination = MessageConstants.QUEUE_EXCHANGE_EVENT;
                    break;
                case USER:
                    destination = MessageConstants.QUEUE_USM;
                    break;
                case AUDIT:
                    destination = MessageConstants.QUEUE_AUDIT_EVENT;
                    break;
                case ACTIVITY:
                    destination = MessageConstants.QUEUE_MODULE_ACTIVITY;
                    break;
                case MDR_EVENT:
                    destination = MessageConstants.QUEUE_MDR_EVENT;
                    break;
                case SALES:
                    destination = MessageConstants.QUEUE_SALES_EVENT;
                    break;
                default:
                    break;
            }
            if(StringUtils.isNotEmpty(destination)){
                return this.sendMessageToSpecificQueue(text, destination, replyQueue);
            }
            return null;
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendConfigMessage(String text) throws ConfigMessageException {
        try {
            return sendDataSourceMessage(text, DataSourceQueue.CONFIG);
        } catch (MessageException  e) {
            LOG.error("[ Error when sending config message. ] {}", e.getMessage());
            throw new ConfigMessageException("[ Error when sending config message. ]");
        }
    }

    @Override
    public void sendModuleErrorResponseMessage(@Observes @ErrorEvent EventMessage message) {
        try {
            LOG.debug("Sending error message back from Rules module to recipient on JMS Queue with correlationID: {} ", message.getJmsMessage().getJMSMessageID());
            String data = JAXBMarshaller.marshallJaxBObjectToString(message.getFault());
            this.sendModuleResponseMessage(message.getJmsMessage(), data, "Rules");
        } catch (RulesModelMarshallException | JMSException e) {
            LOG.error("Error when returning Error message to recipient");
        }
    }

    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_RULES;
    }

}

