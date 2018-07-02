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
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.jms.JMSException;
import javax.jms.Queue;

@Stateless
@LocalBean
public class RulesMessageProducerBean extends AbstractProducer implements RulesMessageProducer, ConfigMessageProducer {

    private final static Logger LOG = LoggerFactory.getLogger(RulesMessageProducerBean.class);

    private Queue rulesResponseQueue;
    private Queue movementQueue;
    private Queue configQueue;
    private Queue assetQueue;
    private Queue mobileTerminalQueue;
    private Queue exchangeQueue;
    private Queue userQueue;
    private Queue auditQueue;
    private Queue activityQueue;
    private Queue mdrEventQueue;
    private Queue salesQueue;

    @PostConstruct
    public void init() {
        rulesResponseQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_RULES);
        movementQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_MODULE_MOVEMENT);
        configQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_CONFIG);
        assetQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_ASSET_EVENT);
        mobileTerminalQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_MOBILE_TERMINAL_EVENT);
        exchangeQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_EXCHANGE_EVENT);
        userQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_USM);
        auditQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_AUDIT_EVENT);
        activityQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_MODULE_ACTIVITY);
        mdrEventQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_MDR_EVENT);
        salesQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_SALES_EVENT);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException  {
        LOG.debug("Sending message to {}", queue.name());
        try {
            Queue destination = getDestinationQueue(queue);
            if(destination != null){
                return sendMessageToSpecificQueue(text, destination, rulesResponseQueue);
            }
            return null;
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue, long timeToLiveInMillis) throws MessageException  {
        LOG.debug("Sending message to {}", queue.name());
        try {
            Queue destination = getDestinationQueue(queue);
            if(destination != null){
                return sendMessageToSpecificQueue(text, destination, rulesResponseQueue, timeToLiveInMillis);
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
            this.sendResponseMessageToSender(message.getJmsMessage(), data, "Rules");
        } catch (RulesModelMarshallException | JMSException | MessageException e) {
            LOG.error("Error when returning Error message to recipient");
        }
    }

    private Queue getDestinationQueue(DataSourceQueue queue) {
        Queue destination = null;
        switch (queue) {
            case MOVEMENT:
                destination = movementQueue;
                break;
            case CONFIG:
                destination = configQueue;
                break;
            case ASSET:
                destination = assetQueue;
                break;
            case MOBILE_TERMINAL:
                destination = mobileTerminalQueue;
                break;
            case EXCHANGE:
                destination = exchangeQueue;
                break;
            case USER:
                destination = userQueue;
                break;
            case AUDIT:
                destination = auditQueue;
                break;
            case ACTIVITY:
                destination = activityQueue;
                break;
            case MDR_EVENT:
                destination = mdrEventQueue;
                break;
            case SALES:
                destination = salesQueue;
                break;
            default:
                break;
        }
        return destination;
    }

    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_RULES;
    }

}

