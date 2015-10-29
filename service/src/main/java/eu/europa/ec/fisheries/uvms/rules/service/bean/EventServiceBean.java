package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalUnmarshallException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
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
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselIdType;

import java.util.Date;
import java.util.List;

@Stateless
public class EventServiceBean implements EventService {
    final static Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);

    // TODO: Where is the observer of this?
    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Inject
    RulesValidator rulesValidator;

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
    public void setMovementReportReceived(@Observes @SetMovementReportReceivedEvent EventMessage message) {
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
                    validateMovementReportReceived(createdMovement);
                } else {
                    LOG.error("[ Error when getting movement from Movement , response from JMS Queue is null ]");
                }
            }

        } catch (RulesModelMapperException | MessageException | ModelMapperException ex) {
            LOG.error("[ Error when creating movement ] {}", ex.getMessage());
            errorEvent.fire(message);
        }

    }

    private void validateMovementReportReceived(MovementType movement) {
        Date timestamp = new Date();
        try {
            LOG.info("Validating movement from Movement Module");

            Vessel vessel = getVessel(movement);
            timestamp = auditLog("Time to fetch from Vessel Module:", timestamp);

            // Enrich with extra vessel data
            String externalMarking = vessel.getExternalMarking();
            // TODO:
            String flagState = "vessel.getCountryCode() ???";  // Det finns en tabell flagstate i vessel, kolla upp
            String vesselName = vessel.getName();
            // TODO:
            String assetGroup = "GO_GET_IT!!!";
            String vesselGuid = vessel.getVesselId().getGuid();

            MobileTerminalType mobileTerminal = getMobileTerminal(movement);
            timestamp = auditLog("Time to fetch from MobileTerminal Module:", timestamp);

            // Enrich with extra vessel data
            String mobileTerminalDnid = null;
            String mobileTerminalMemberNumber = null;
            String mobileTerminalSerialNumber = null;

            List<MobileTerminalAttribute> a = mobileTerminal.getAttributes();
            for (MobileTerminalAttribute mobileTerminalAttribute : a) {
                String type = mobileTerminalAttribute.getType();
                switch (type) {
                    case "SERIAL_NUMBER":
                        mobileTerminalSerialNumber = mobileTerminalAttribute.getValue();
                        break;
                    case "MEMBER_NUMBER":
                        mobileTerminalMemberNumber = mobileTerminalAttribute.getValue();
                        break;
                    case "DNID":
                        mobileTerminalDnid = mobileTerminalAttribute.getValue();
                        break;
                    default:
                        LOG.error("[ No such mobile terminal attribute type exists! {} ]", type);
                        break;
                }
            }

            // TODO:
            // ??? Maybe Movement?
            String vecinityOf = "GO_GET_IT!!!";

            // TODO:
            // Spatial integration

            // TODO:
            // Enrich with extra spatial data

            MovementFact movementFact = RulesUtil.mapFact(movement, externalMarking, flagState, mobileTerminalDnid, mobileTerminalMemberNumber,
                    mobileTerminalSerialNumber, vesselName, vesselGuid);

            //LOG.info("myggan - movementFact:{}", movementFact);

            // TODO:
            // Verify how spatial data can be validated

            rulesValidator.evaluate(movementFact);
            timestamp = auditLog("Time to validate rules:", timestamp);

            persistThisMovementReport(vesselGuid, movement);
            timestamp = auditLog("Time to persist the position time:", timestamp);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Vessel getVessel(MovementType movement) throws VesselModelMapperException, MessageException {
        VesselIdType vesselIdType = null;
        switch (movement.getAssetId().getIdType()) {
            case CFR:
                vesselIdType = VesselIdType.CFR;
                break;
            case GUID:
                vesselIdType = VesselIdType.GUID;
                break;
            case ID:
                vesselIdType = VesselIdType.INTERNAL_ID;
                break;
            case IMO:
                vesselIdType = VesselIdType.IMO;
                break;
            case IRCS:
                vesselIdType = VesselIdType.IRCS;
                break;
            case MMSI:
                vesselIdType = VesselIdType.MMSI;
                break;
            default:
                LOG.error("[ No such Asset Type exist: {} ]", movement.getAssetId().getIdType());
                break;
        }
        String vesselIdValue = movement.getAssetId().getValue();

        String getVesselRequest = VesselModuleRequestMapper.createGetVesselModuleRequest(vesselIdValue, vesselIdType);
        String getVesselMessageId = producer.sendDataSourceMessage(getVesselRequest, DataSourceQueue.VESSEL);
        TextMessage getVesselResponse = consumer.getMessage(getVesselMessageId, TextMessage.class);

//        try {
//            LOG.info("myggan - getVesselResponse:{}", getVesselResponse.getText());
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }

        return  VesselModuleResponseMapper.mapToVesselFromResponse(getVesselResponse, getVesselMessageId);
    }

    private MobileTerminalType getMobileTerminal(MovementType movement) throws MobileTerminalModelMapperException, MessageException, MobileTerminalUnmarshallException, JMSException {
        // TODO Get mobterm id from Vessel (since it doesn't exists in movement)
        // connectId mappar vessel med dess terminaler
        // todo Hur får man fatt i connectId från vessel?
        // todo Hur söker man terminaler på connectId?
        String connectId = "MOBTERM_CONN_VALUE";

        String mobileTerminalGuid = "MOBILE_TERMINAL_GUID";
        MobileTerminalId mobileTerminalId = new MobileTerminalId();
        mobileTerminalId.setGuid(mobileTerminalGuid);

        String getMobileTerminalRequest = MobileTerminalModuleRequestMapper.createGetMobileTerminalRequest(mobileTerminalId);
        String getMobileTerminalMessageId = producer.sendDataSourceMessage(getMobileTerminalRequest, DataSourceQueue.MOBILE_TERMINAL);
        TextMessage getMobileTerminalResponse = consumer.getMessage(getMobileTerminalMessageId, TextMessage.class);

//        try {
//            LOG.info("myggan - getMobileTerminalResponse:{}", getMobileTerminalResponse.getText());
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }

        return MobileTerminalModuleResponseMapper.mapToMobileTerminalResponse(getMobileTerminalResponse);
    }

    private void persistThisMovementReport(String vesselGuid, MovementType movement) throws MessageException, RulesModelMapperException {
        // TODO Persist time of the movement
        // I assume we will aways have a guid for vessel
        PreviousReportType thisReport = new PreviousReportType();
        thisReport.setMovementGuid(movement.getGuid());
        thisReport.setPositionTime(movement.getPositionTime());
        thisReport.setVesselGuid(vesselGuid);

        String upsertPreviousReportequest = RulesDataSourceRequestMapper.mapUpsertPreviousReport(thisReport);
        producer.sendDataSourceMessage(upsertPreviousReportequest, DataSourceQueue.INTERNAL);
    }

    private RawMovementFact generateRawFact(RawMovementType rawMovementType) {
        RawMovementFact fact = new RawMovementFact();
        fact.setRawMovementType(rawMovementType);
        fact.setOk(true);
        return fact;
    }

    public Date auditLog(String msg, Date lastTimestamp) {
        Date newTimestamp = new Date();
        long duration = newTimestamp.getTime() - lastTimestamp.getTime();
        LOG.info("--> AUDIT - {} {}ms", msg, duration);
        return newTimestamp;
    }

}
