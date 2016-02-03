package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;

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

    @Inject
    JMSConnectorBean connector;

    private static final int CONFIG_TTL = 30000;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException {
        LOG.debug("Sending message to {}", queue.name());

        try {
            Session session = connector.getNewSession();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);
            MessageProducer producer;

            switch (queue) {
                case INTERNAL:
                    producer = session.createProducer(localDbQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case MOVEMENT:
                    producer = session.createProducer(movementQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case CONFIG:
                    producer = session.createProducer(configQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case ASSET:
                    producer = session.createProducer(assetQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case MOBILE_TERMINAL:
                    producer = session.createProducer(mobileTerminalQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case EXCHANGE:
                    producer = session.createProducer(exchangeQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case USER:
                    producer = session.createProducer(userQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
                case AUDIT:
                    producer = session.createProducer(auditQueue);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                    break;
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
            MessageProducer producer = session.createProducer(message.getJMSReplyTo());
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
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
            MessageProducer producer = session.createProducer(message.getJmsMessage().getJMSReplyTo());
            producer.send(response);
        } catch (RulesModelMarshallException | JMSException e) {
            LOG.error("Error when returning Error message to recipient");
        }
    }

}
