package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import java.util.UUID;

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
import org.slf4j.MDC;

import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.rules.message.event.CountTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetCustomRuleReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsAndRulesByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFLUXFAReportMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFLUXMDRSyncMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ValidateMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.ModuleResponseMapper;

@MessageDriven(mappedName = MessageConstants.RULES_MESSAGE_IN_QUEUE, activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME)
})
public class RulesEventConsumerBean implements MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(RulesEventConsumerBean.class);

    @Inject
    @SetMovementReportReceivedEvent
    Event<EventMessage> setMovementReportRecievedEvent;

    @Inject
    @GetTicketsByMovementsEvent
    Event<EventMessage> getTicketsByMovementsEvent;

    @Inject
    @CountTicketsByMovementsEvent
    Event<EventMessage> countTicketByMovementsEvent;

    @Inject
    @GetCustomRuleReceivedEvent
    Event<EventMessage> getCustomRuleRecievedEvent;

    @Inject
    @GetTicketsAndRulesByMovementsEvent
    Event<EventMessage> getTicketsAndRulesByMovementsEvent;

    @Inject
    @ValidateMovementReportReceivedEvent
    Event<EventMessage> validateMovementReportReceivedEvent;

    @Inject
    @PingReceivedEvent
    Event<EventMessage> pingReceivedEvent;

    @Inject
    @SetFLUXFAReportMessageReceivedEvent
    Event<EventMessage> setFLUXFAReportMessageReceivedEvent;
    
    @Inject
    @SetFLUXMDRSyncMessageReceivedEvent
    Event<EventMessage> setFLUXMDRSyncMessageReceivedEvent;

    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        String id = UUID.randomUUID().toString();
        MDC.put(MessageConstants.MDC_IDENTIFIER, id);

        LOG.info("Message received in rules");

        TextMessage textMessage = (TextMessage) message;
        try {

            RulesBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, RulesBaseRequest.class);

            switch (request.getMethod()) {
                case SET_MOVEMENT_REPORT:
                    setMovementReportRecievedEvent.fire(new EventMessage(textMessage));
                    break;
                case PING:
                    pingReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_CUSTOM_RULE:
                    getCustomRuleRecievedEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_TICKETS_BY_MOVEMENTS:
                    getTicketsByMovementsEvent.fire(new EventMessage(textMessage));
                    break;
                case COUNT_TICKETS_BY_MOVEMENTS:
                    countTicketByMovementsEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_TICKETS_AND_RULES_BY_MOVEMENTS:
                    getTicketsAndRulesByMovementsEvent.fire(new EventMessage(textMessage));
                    break;
                case SET_FLUX_FA_REPORT:
                    setFLUXFAReportMessageReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case SET_FLUX_MDR_SYNC_REQUEST:
                	setFLUXMDRSyncMessageReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                default:
                    LOG.error("[ Request method '{}' is not implemented ]", request.getMethod().name());
                    errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Method not implemented:" + request.getMethod().name())));
                    break;
            }
            if (request.getMethod() == null) {
                LOG.error("[ Request method is null ]");
                errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Error when receiving message in rules: Request method is null")));
            }

        } catch (NullPointerException | RulesModelMarshallException e) {
            LOG.error("[ Error when receiving message in rules: {}]", e.getMessage());
            errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Error when receiving message in rules:" + e.getMessage())));
        } finally {
            MDC.remove(MessageConstants.MDC_IDENTIFIER);
        }
    }

}
