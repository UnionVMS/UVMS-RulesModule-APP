package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetCustomRuleRequest;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.module.v1.PingResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetCustomRuleReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesMapper;

@Stateless
public class EventServiceBean implements EventService {
    final static Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);

    // TODO: Where is the observer of this?
    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Inject
    RulesValidator rulesValidator;

    @Inject
    RulesService rulseService;

    @EJB
    RulesMessageProducer producer;

    @EJB
    RulesResponseConsumer consumer;

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
    public void setMovementReportRecieved(@Observes @SetMovementReportReceivedEvent EventMessage message) {
        LOG.info("Validating movement from Exchange Module");
        try {

            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);

            if (baseRequest.getMethod() != RulesModuleMethod.SET_MOVEMENT_REPORT) {
                message.setErrorMessage(" [ Error, Set Movement Report invoked but it is not the intended method, caller is trying: "
                        + baseRequest.getMethod().name() + " ]");
                errorEvent.fire(message);
            }

            SetMovementReportRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetMovementReportRequest.class);
            RawMovementType rawMovementType = request.getRequest();

            // Wrap incoming raw movement in a fact and validate
            RawMovementFact rawFact = generateRawFact(rawMovementType);
            rulesValidator.evaluate(rawFact);

            // MovementComChannelType.FLUX -> assetId
            // MovementComChannelType.MOBILE_TERMINAL -> mobileTerminalId
            // assetId | mobileTerminalId -> connectId
            // TODO: Get more stuff; MobileTerminal guid, finns asset (tex
            // IRCS), ...

            if (rawFact.isOk()) {
                LOG.info("Send the validated raw position to Movement");
                MovementBaseType movementBaseType = RulesMapper.getInstance().getMapper().map(rawMovementType, MovementBaseType.class);
                String createMovementRequest = MovementModuleRequestMapper.mapToCreateMovementRequest(movementBaseType);

                String messageId = producer.sendDataSourceMessage(createMovementRequest, DataSourceQueue.MOVEMENT);
                TextMessage response = consumer.getMessage(messageId, TextMessage.class);

                if (response != null) {
                    MovementType createdMovement = RulesMapper.mapCreateMovementToMovementType(response);
                    validateMovementReportRecieved(createdMovement);
                } else {
                    LOG.error("[ Error when getting movement from Movement , response from JMS Queue is null ]");
                }
            }

        } catch (RulesModelMapperException | MessageException | ModelMapperException ex) {
            LOG.error("[ Error when creating movement ] {}", ex.getMessage());
            errorEvent.fire(message);
        }

    }

    private void validateMovementReportRecieved(MovementType movement) {
        try {
            LOG.info("Validating movement from Movement Module");

            // TODO: Enrich with extra data

            // Vessel
            String externalMarking = "GO_GET_IT!!!";
            String flagState = "GO_GET_IT!!!";
            String vesselName = "GO_GET_IT!!!";
            String assetGroup = "GO_GET_IT!!!";
            String vesselGuid = "GO_GET_IT!!!";

            // String getVesselRequest = null;
            // String messageId =
            // producer.sendDataSourceMessage(getVesselRequest,
            // DataSourceQueue.VESSEL);
            // TextMessage response = consumer.getMessage(messageId,
            // TextMessage.class);

            // Mobile terminal
            String mobileTerminalDnid = "GO_GET_IT!!!";
            String mobileTerminalMemberNumber = "GO_GET_IT!!!";
            String mobileTerminalSerialNumber = "GO_GET_IT!!!";

            // ??? Maybe Movement?
            String vecinityOf = "GO_GET_IT!!!";

            MovementFact movementFact = RulesUtil.mapFact(movement, externalMarking, flagState, mobileTerminalDnid, mobileTerminalMemberNumber,
                    mobileTerminalSerialNumber, vesselName, vesselGuid);

            rulesValidator.evaluate(movementFact);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RawMovementFact generateRawFact(RawMovementType rawMovementType) {
        RawMovementFact fact = new RawMovementFact();
        fact.setRawMovementType(rawMovementType);
        fact.setOk(true);
        return fact;
    }
}
