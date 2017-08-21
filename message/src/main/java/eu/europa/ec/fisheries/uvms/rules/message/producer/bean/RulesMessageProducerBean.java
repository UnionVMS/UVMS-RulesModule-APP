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

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.config.constants.ConfigConstants;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import eu.europa.ec.fisheries.uvms.message.JMSUtils;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class RulesMessageProducerBean implements RulesMessageProducer, ConfigMessageProducer {

    private final static Logger LOG = LoggerFactory.getLogger(RulesMessageProducerBean.class);
    private Queue responseQueue;
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
    private ConnectionFactory connectionFactory;

    @PostConstruct
    public void init() {
    	connectionFactory = JMSUtils.lookupConnectionFactory();
        responseQueue = JMSUtils.lookupQueue(MessageConstants.RULES_RESPONSE_QUEUE);
        movementQueue = JMSUtils.lookupQueue(MessageConstants.MOVEMENT_MESSAGE_IN_QUEUE);
        configQueue = JMSUtils.lookupQueue(ConfigConstants.CONFIG_MESSAGE_IN_QUEUE);
        assetQueue = JMSUtils.lookupQueue(MessageConstants.ASSET_MESSAGE_IN_QUEUE);
        mobileTerminalQueue = JMSUtils.lookupQueue(MessageConstants.MOBILE_TERMINAL_MESSAGE_IN_QUEUE);
        exchangeQueue = JMSUtils.lookupQueue(MessageConstants.EXCHANGE_MESSAGE_IN_QUEUE);
        userQueue = JMSUtils.lookupQueue(MessageConstants.USER_MESSAGE_IN_QUEUE);
        auditQueue = JMSUtils.lookupQueue(MessageConstants.AUDIT_MESSAGE_IN_QUEUE);
        activityQueue = JMSUtils.lookupQueue(MessageConstants.ACTIVITY_MESSAGE_IN_QUEUE);
        mdrEventQueue = JMSUtils.lookupQueue(MessageConstants.MDR_EVENT);
        salesQueue = JMSUtils.lookupQueue(MessageConstants.SALES_QUEUE);
    }

    private MessageProducer getProducer(Session session, Destination destination) throws JMSException {
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(60000L);
        return producer;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException {
        LOG.debug("Sending message to {}", queue.name());

        Connection connection=null;
        try {
            connection = connectionFactory.createConnection();
            final Session session = JMSUtils.connectToQueue(connection);
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
                case MOVEMENT:
                    getProducer(session, movementQueue).send(message);
                    break;
                case CONFIG:
                    getProducer(session, configQueue).send(message);
                    break;
                case ASSET:
                    getProducer(session, assetQueue).send(message);
                    break;
                case MOBILE_TERMINAL:
                    getProducer(session, mobileTerminalQueue).send(message);
                    break;
                case EXCHANGE:
                    getProducer(session, exchangeQueue).send(message);
                    break;
                case USER:
                    getProducer(session, userQueue).send(message);
                    break;
                case AUDIT:
                    getProducer(session, auditQueue).send(message);
                    break;
                case ACTIVITY:
                    getProducer(session, activityQueue).send(message);
                    break;
                case MDR_EVENT:
                    getProducer(session, mdrEventQueue).send(message);
                    break;
                case SALES:
                    getProducer(session, salesQueue).send(message);
                    break;
                default:
                    break;
            }
            return message.getJMSMessageID();
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        } finally {
        	JMSUtils.disconnectQueue(connection);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(TextMessage message, String text) throws MessageException {
        Connection connection=null;
    	try {
            LOG.info("Sending message back to recipient from RulesModule with correlationId {} on queue: {}", message.getJMSMessageID(),
                    message.getJMSReplyTo());
            connection = connectionFactory.createConnection();
            final Session session = JMSUtils.connectToQueue(connection);
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            MessageProducer producer = getProducer(session, message.getJMSReplyTo());
            producer.send(response);
        } catch (JMSException e) {
            LOG.error("[ Error when returning module rules request. ] {}", e.getMessage());
        } finally {
        	JMSUtils.disconnectQueue(connection);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendConfigMessage(String text) throws ConfigMessageException {
        try {
            return sendDataSourceMessage(text, DataSourceQueue.CONFIG);
        } catch (MessageException e) {
            LOG.error("[ Error when sending config message. ] {}", e.getMessage());
            throw new ConfigMessageException("[ Error when sending config message. ]");
        }
    }

    @Override
    public void sendModuleErrorResponseMessage(@Observes @ErrorEvent EventMessage message) {
    	Connection connection=null;
        try {
            LOG.debug("Sending error message back from Rules module to recipient on JMS Queue with correlationID: {} ", message.getJmsMessage().getJMSMessageID());

            connection = connectionFactory.createConnection();
            final Session session = JMSUtils.connectToQueue(connection);
            String data = JAXBMarshaller.marshallJaxBObjectToString(message.getFault());
            TextMessage response = session.createTextMessage(data);
            response.setJMSCorrelationID(message.getJmsMessage().getJMSMessageID());
            MessageProducer producer = getProducer(session, message.getJmsMessage().getJMSReplyTo());
            producer.send(response);
        } catch (RulesModelMarshallException | JMSException e) {
            LOG.error("Error when returning Error message to recipient");
        } finally {
        	JMSUtils.disconnectQueue(connection);
        }
    }

}

