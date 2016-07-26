package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;
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

import eu.europa.ec.fisheries.uvms.config.constants.ConfigConstants;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;

@Stateless
public class RulesMessageProducerBean implements RulesMessageProducer, ConfigMessageProducer {

    private final static Logger LOG = LoggerFactory.getLogger(RulesMessageProducerBean.class);

    @Resource(mappedName = MessageConstants.QUEUE_DATASOURCE_INTERNAL)
    private Queue localDbQueue;

    @Resource(mappedName = MessageConstants.RULES_RESPONSE_QUEUE)
    private Queue responseQueue;

    @Resource(mappedName = MessageConstants.MOVEMENT_MESSAGE_IN_QUEUE)
    private Queue movementQueue;

    @Resource(mappedName = ConfigConstants.CONFIG_MESSAGE_IN_QUEUE)
    private Queue configQueue;

    @Resource(mappedName = MessageConstants.ASSET_MESSAGE_IN_QUEUE)
    private Queue assetQueue;

    @Resource(mappedName = MessageConstants.MOBILE_TERMINAL_MESSAGE_IN_QUEUE)
    private Queue mobileTerminalQueue;

    @Resource(mappedName = MessageConstants.EXCHANGE_MESSAGE_IN_QUEUE)
    private Queue exchangeQueue;

    @Resource(mappedName = MessageConstants.USER_MESSAGE_IN_QUEUE)
    private Queue userQueue;

    @Resource(mappedName = MessageConstants.AUDIT_MESSAGE_IN_QUEUE)
    private Queue auditQueue;


    /*@Resource(mappedName = MessageConstants.ACTIVITY_MESSAGE_IN_QUEUE)
    private Queue activityQueue;*/


    @Inject
    JMSConnectorBean connector;

    private static final int CONFIG_TTL = 30000;

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

        try {
            Session session = connector.getNewSession();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
                case INTERNAL:
                    getProducer(session, localDbQueue).send(message);
                    break;
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

               /* case ACTIVITY:
                    getProducer(session, activityQueue).send(message);
                    break;*/

                default:
                    break;
            }
            return message.getJMSMessageID();
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MessageException("[ Error when sending message. ]", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(TextMessage message, String text) throws MessageException {
        try {
            LOG.info("Sending message back to recipient from RulesModule with correlationId {} on queue: {}", message.getJMSMessageID(),
                    message.getJMSReplyTo());
            Session session = connector.getNewSession();
            TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());
            MessageProducer producer = getProducer(session, message.getJMSReplyTo());
            producer.send(response);
        } catch (JMSException e) {
            LOG.error("[ Error when returning module rules request. ] {}", e.getMessage());
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
        try {
            LOG.debug("Sending error message back from Rules module to recipient on JMS Queue with correlationID: {} ", message.getJmsMessage().getJMSMessageID());

            Session session = connector.getNewSession();
            String data = JAXBMarshaller.marshallJaxBObjectToString(message.getFault());
            TextMessage response = session.createTextMessage(data);
            response.setJMSCorrelationID(message.getJmsMessage().getJMSMessageID());
            MessageProducer producer = getProducer(session, message.getJmsMessage().getJMSReplyTo());
            producer.send(response);
        } catch (RulesModelMarshallException | JMSException e) {
            LOG.error("Error when returning Error message to recipient");
        }
    }

}
