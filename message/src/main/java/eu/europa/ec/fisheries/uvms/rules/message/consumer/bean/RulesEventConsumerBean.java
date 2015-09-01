package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.MessageRecievedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;

@MessageDriven(mappedName = MessageConstants.RULES_MESSAGE_IN_QUEUE, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME)
})
public class RulesEventConsumerBean implements MessageListener {

    final static Logger LOG = LoggerFactory.getLogger(RulesEventConsumerBean.class);

    @Inject
    @MessageRecievedEvent
    Event<EventMessage> messageRecievedEvent;

    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            LOG.info("Message received in rules");
            // GetDataRequest request =
            // JAXBMarshaller.unmarshallTextMessage(textMessage,
            // GetDataRequest.class);
            // messageRecievedEvent.fire(new EventMessage(textMessage,
            // request.getId().toString()));
            messageRecievedEvent.fire(new EventMessage(textMessage));
        } catch (NullPointerException e) {
            LOG.error("[ Error when receiving message in rules: ]", e);
            errorEvent.fire(new EventMessage(textMessage, "Error when receiving message in rules: " + e.getMessage()));
        }
    }

}
