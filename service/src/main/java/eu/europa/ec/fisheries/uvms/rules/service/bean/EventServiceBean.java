package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.ModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.slf4j.MDC;

import java.util.UUID;

@Stateless
public class EventServiceBean implements EventService {
    private final static Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);

    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @EJB
    RulesMessageProducer producer;

    @EJB
    RulesService rulesService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void pingReceived(@Observes @PingReceivedEvent EventMessage eventMessage) {
        try {
            PingResponse pingResponse = new PingResponse();
            pingResponse.setResponse("pong");
            String pingResponseText = JAXBMarshaller.marshallJaxBObjectToString(pingResponse);
            producer.sendModuleResponseMessage(eventMessage.getJmsMessage(), pingResponseText);
        } catch (RulesModelMarshallException | MessageException e) {
            LOG.error("[ Error when responding to ping. ] {}", e.getMessage());
            errorEvent.fire(eventMessage);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setMovementReportReceived(@Observes @SetMovementReportReceivedEvent EventMessage message) {
        SetMovementReportResponse exchangeResponse = new SetMovementReportResponse();

        LOG.info("Validating movement from Exchange Module");
        try {

            String id = UUID.randomUUID().toString();
            MDC.put("clientName", id);

            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);

            if (baseRequest.getMethod() != RulesModuleMethod.SET_MOVEMENT_REPORT) {
                String s = " [ Error, Set Movement Report invoked but it is not the intended method, caller is trying: "
                        + baseRequest.getMethod().name() + " ]";
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, s)));

            }

            SetMovementReportRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetMovementReportRequest.class);
            RawMovementType rawMovementType = request.getRequest();

            String pluginType = request.getType().name();

            MovementRefType ref = rulesService.setMovementReportReceived(rawMovementType, pluginType);

            exchangeResponse.setMovementRef(ref);
            String exchangeResponseText = JAXBMarshaller.marshallJaxBObjectToString(exchangeResponse);
            producer.sendModuleResponseMessage(message.getJmsMessage(), exchangeResponseText);

        } catch (RulesModelMapperException | MessageException | RulesServiceException e) {
            LOG.error("[ Error when creating movement ] {}", e.getMessage());
            errorEvent.fire(message);
        } finally {
            MDC.remove("clientName");
        }

    }

}
