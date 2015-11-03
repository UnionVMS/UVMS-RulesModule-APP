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

import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetCustomRuleReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ValidateMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;

@MessageDriven(mappedName = MessageConstants.RULES_MESSAGE_IN_QUEUE, activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME)
})
public class RulesEventConsumerBean implements MessageListener {

    final static Logger LOG = LoggerFactory.getLogger(RulesEventConsumerBean.class);

    @Inject
    @SetMovementReportReceivedEvent
    Event<EventMessage> setMovementReportRecievedEvent;
    
    @Inject
    @GetCustomRuleReceivedEvent
    Event<EventMessage> getCustomRuleRecievedEvent;

    @Inject
    @ValidateMovementReportReceivedEvent
    Event<EventMessage> validateMovementReportReceivedEvent;

    @Inject
    @PingReceivedEvent
    Event<EventMessage> pingReceivedEvent;

    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        LOG.info("Message received in rules");
        try {

            RulesBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, RulesBaseRequest.class);

            switch (request.getMethod()) {
            case SET_MOVEMENT_REPORT:
                setMovementReportRecievedEvent.fire(new EventMessage(textMessage));
                break;
            case VALIDATE_MOVEMENT_REPORT:
                setMovementReportRecievedEvent.fire(new EventMessage(textMessage));
                break;
            case PING:
                pingReceivedEvent.fire(new EventMessage(textMessage));
                break;
            case GET_CUSTOM_RULE: 
            	getCustomRuleRecievedEvent.fire(new EventMessage(textMessage));
            default:
                LOG.error("[ Request method '{}' is not implemented ]", request.getMethod().name());

                // ???:
                // errorEvent.fire(new EventMessage(textMessage,
                // "[ Request method " + request.getMethod().name() +
                // "  is not implemented ]"));
                break;
            }
            // if (request.getMethod() == null) {
            // LOG.error("[ Request method is null ]");
            // errorEvent.fire(new EventMessage(textMessage,
            // "Error when receiving message in movement: "));
            // }

        } catch (NullPointerException | RulesModelMarshallException e) {
            LOG.error("[ Error when receiving message in rules: {}]", e.getMessage());
            errorEvent.fire(new EventMessage(textMessage, "Error when receiving message in rules: " + e.getMessage()));
        }
    }

}
