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

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.*;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.*;
import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.schema.rules.movement.v1.*;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalUnmarshallException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.exception.VesselModelMapperException;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.vessel.model.mapper.VesselModuleResponseMapper;
import eu.europa.ec.fisheries.wsdl.vessel.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
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
        SetMovementReportResponse exchangeResponse = new SetMovementReportResponse();

        LOG.info("Validating movement from Exchange Module");
        try {

            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);

            if (baseRequest.getMethod() != RulesModuleMethod.SET_MOVEMENT_REPORT) {
                message.setErrorMessage(" [ Error, Set Movement Report invoked but it is not the intended method, caller is trying: "
                        + baseRequest.getMethod().name() + " ]");
                errorEvent.fire(message);
            }

            try {
                LOG.info("myggan - incoming message from Exchange Module:{}", message.getJmsMessage().getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }

            SetMovementReportRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetMovementReportRequest.class);
            RawMovementType rawMovementType = request.getRequest();

            Date auditTimestamp = new Date();

            // Get Mobile Terminal
            MobileTerminalType mobileTerminal = getMobileTerminalByRawMovement(rawMovementType.getMobileTerminal().getMobileTerminalIdList());
            auditTimestamp = auditLog("Time to fetch from Mobile Terminal Module:", auditTimestamp);

            // Wrap incoming raw movement in a fact and validate
            String pluginType = request.getType().name();

            // Get some mobile terminal data to validate
            String mobileTerminalDnid = null;
            String mobileTerminalMemberNumber = null;
            String mobileTerminalSerialNumber = null;
            String connectId = null;
            if (mobileTerminal != null) {
                List<ComChannelType> channels = mobileTerminal.getChannels();
                for (ComChannelType channel : channels) {
                    List<ComChannelAttribute> chanAttributes = channel.getAttributes();
                    for (ComChannelAttribute chanAttribute : chanAttributes) {
                        if (chanAttribute.getType().equals("DNID")) {
                            mobileTerminalDnid = chanAttribute.getValue();
                        }
                        if (chanAttribute.getType().equals("MEMBER_NUMBER")) {
                            mobileTerminalMemberNumber = chanAttribute.getValue();
                        }
                    }
                }

                List<MobileTerminalAttribute> attributes = mobileTerminal.getAttributes();
                for (MobileTerminalAttribute attribute : attributes) {
                    if (attribute.getType().equals("SERIAL_NUMBER")) {
                        mobileTerminalMemberNumber = attribute.getValue();
                    }
                }

                connectId = mobileTerminal.getConnectId();
                LOG.info("myggan - connectId:{}", connectId);
                rawMovementType.setConnectId(connectId);
            }
            RawMovementFact rawFact = RulesUtil.mapRawMovementFact(rawMovementType, pluginType, mobileTerminalDnid, mobileTerminalMemberNumber);

            rulesValidator.evaluate(rawFact);
            auditTimestamp = auditLog("Time to validate sanity:", auditTimestamp);

            // MovementComChannelType.FLUX -> assetId
            // MovementComChannelType.MOBILE_TERMINAL -> mobileTerminalId
            // assetId | mobileTerminalId -> connectId
            // TODO: Get more stuff; MobileTerminal guid, finns asset (tex
            // IRCS), ...

            if (rawFact.isOk()) {
                LOG.info("Send the validated raw position to Movement");
                MovementBaseType movementBaseType = RulesMapper.getInstance().getMapper().map(rawMovementType, MovementBaseType.class);

                movementBaseType.setConnectId(connectId);
                LOG.info("myggan - movementBaseType.getConnectId():{}", movementBaseType.getConnectId());

                String createMovementRequest = MovementModuleRequestMapper.mapToCreateMovementRequest(movementBaseType);

                LOG.info("myggan - outgoing message to Movement Module:{}", createMovementRequest);

                String messageId = producer.sendDataSourceMessage(createMovementRequest, DataSourceQueue.MOVEMENT);
                TextMessage response = consumer.getMessage(messageId, TextMessage.class);
                auditTimestamp = auditLog("Time to get movement from Movement Module:", auditTimestamp);

                try {
                    LOG.info("myggan - incoming message from Movement Module:{}", response.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                if (response != null) {
                    MovementType createdMovement = RulesMapper.mapCreateMovementToMovementType(response);
                    validateMovementReportReceived(createdMovement, mobileTerminalDnid, mobileTerminalMemberNumber, mobileTerminalSerialNumber, connectId);

                    // Tell Exchange that a movement was persisted in Movement
                    MovementRefType ref = new MovementRefType();
                    ref.setMovementRefGuid(createdMovement.getGuid());
                    ref.setType("MOVEMENT");
                    exchangeResponse.setMovementRef(ref);
                    String exchangeResponseText = JAXBMarshaller.marshallJaxBObjectToString(exchangeResponse);
                    LOG.info("myggan - status response sent to Exchange", exchangeResponseText);
                    producer.sendModuleResponseMessage(message.getJmsMessage(), exchangeResponseText);
                } else {
                    LOG.error("[ Error when getting movement from Movement , response from JMS Queue is null ]");
                }
            } else {
                // Tell Exchange that the report caused an alarm
                MovementRefType ref = new MovementRefType();
                ref.setMovementRefGuid(rawFact.getMovementGuid());
                ref.setType("ALARM");
                exchangeResponse.setMovementRef(ref);
                String exchangeResponseText = JAXBMarshaller.marshallJaxBObjectToString(exchangeResponse);
                LOG.info("myggan - status response sent to Exchange", exchangeResponseText);
                producer.sendModuleResponseMessage(message.getJmsMessage(), exchangeResponseText);
            }
        } catch (RulesModelMapperException | MessageException | ModelMapperException | MobileTerminalModelMapperException | MobileTerminalUnmarshallException | JMSException e) {
            LOG.error("[ Error when creating movement ] {}", e.getMessage());
            errorEvent.fire(message);
        }

    }

    private void validateMovementReportReceived(MovementType movement, String mobileTerminalDnid, String mobileTerminalMemberNumber, String mobileTerminalSerialNumber, String connectId) {
        Date auditTimestamp = new Date();
        try {
            LOG.info("Validating movement from Movement Module");

//            Vessel vessel = getVesselByMovement(movement);
            Vessel vessel = getVesselByConnectId(connectId);
            auditTimestamp = auditLog("Time to fetch from Vessel Module:", auditTimestamp);

            String externalMarking = null;
            String flagState = null;
            String vesselName = null;
            String assetGroup = null;
            String vesselGuid = null;
            if (vessel != null) {
                // Enrich with extra vessel data
                externalMarking = vessel.getExternalMarking();
                // TODO:
                flagState = "vessel.getCountryCode() ???";  // Det finns en tabell flagstate i vessel, kolla upp
                vesselName = vessel.getName();
                // TODO:
                assetGroup = "GO_GET_IT!!!";
                vesselGuid = vessel.getVesselId().getGuid();  // guid
            }
            // TODO Decide if we really want to verify stuff from MobileTerminal
            // Enrich with extra mobile terminal data

            // TODO:
            // ??? Maybe Movement?
            String vecinityOf = "GO_GET_IT!!!";

            // TODO:
            // Spatial integration

            // TODO:
            // Enrich with extra spatial data

            MovementFact movementFact = RulesUtil.mapMovementFact(movement, externalMarking, flagState, mobileTerminalDnid, mobileTerminalMemberNumber,
                    mobileTerminalSerialNumber, vesselName, vesselGuid);

            LOG.info("myggan - movementFact:{}", movementFact);

            // TODO:
            // Verify how spatial data can be validated

            rulesValidator.evaluate(movementFact);
            auditTimestamp = auditLog("Time to validate rules:", auditTimestamp);

            persistThisMovementReport(vesselGuid, movement);
            auditTimestamp = auditLog("Time to persist the position time:", auditTimestamp);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


/*
    // TODO: This wont work, I used old wsdl in test... A movement have no vessel data.
    private Vessel getVesselByMovement(MovementType movement) throws VesselModelMapperException, MessageException {
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
        LOG.info("myggan - vesselIdType/vesselIdValue:{}/{}", vesselIdType, vesselIdValue);

        String getVesselRequest = VesselModuleRequestMapper.createGetVesselModuleRequest(vesselIdValue, vesselIdType);
        LOG.info("myggan - getVesselRequest:{}", getVesselRequest);
        String getVesselMessageId = producer.sendDataSourceMessage(getVesselRequest, DataSourceQueue.VESSEL);
        TextMessage getVesselResponse = consumer.getMessage(getVesselMessageId, TextMessage.class);

        try {
            LOG.info("myggan - getVesselResponse:{}", getVesselResponse.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return  VesselModuleResponseMapper.mapToVesselFromResponse(getVesselResponse, getVesselMessageId);
    }
*/

    private Vessel getVesselByConnectId(String connectId) throws VesselModelMapperException, MessageException {

        // TODO: Verify that connectId is the same as vessel guid!

        LOG.info("myggan - search with connectId.{}", connectId);

        VesselListQuery query = new VesselListQuery();
        VesselListCriteria criteria = new VesselListCriteria();
        VesselListCriteriaPair criteriaPair = new VesselListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(connectId);
        criteria.getCriterias().add(criteriaPair);
        criteria.setIsDynamic(true);

        query.setVesselSearchCriteria(criteria);

        VesselListPagination pagination = new VesselListPagination();
        pagination.setListSize(10);
        pagination.setPage(1);
        query.setPagination(pagination);

        String getVesselRequest = VesselModuleRequestMapper.createVesselListModuleRequest(query);
        LOG.info("myggan - getVesselRequest:{}", getVesselRequest);

        String getVesselMessageId = producer.sendDataSourceMessage(getVesselRequest, DataSourceQueue.VESSEL);
        TextMessage getVesselResponse = consumer.getMessage(getVesselMessageId, TextMessage.class);

        try {
            LOG.info("myggan - getVesselResponse:{}", getVesselResponse.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

        List<Vessel> resultList = VesselModuleResponseMapper.mapToVesselListFromResponse(getVesselResponse, getVesselMessageId);
        Vessel result = resultList.isEmpty()?null:resultList.get(0);

        return  result;
    }

    // TODO: Simplify!!!!!!!!!!!!!!!!!
    private MobileTerminalType getMobileTerminalByRawMovement(List<IdList> ids) throws MessageException, MobileTerminalModelMapperException, MobileTerminalUnmarshallException, JMSException {
//        eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.MobileTerminalType mobileTerminal = rawMovement.getMobileTerminal();

        MobileTerminalListQuery query = new MobileTerminalListQuery();

//        List<IdList> ids = mobileTerminal.getMobileTerminalIdList();
        MobileTerminalSearchCriteria criteria = new MobileTerminalSearchCriteria();
        for (IdList id : ids) {
            ListCriteria crit = new ListCriteria();
            switch (id.getType()) {
                case DNID:
                    crit.setKey(SearchKey.DNID);
                    crit.setValue(id.getValue());
                    criteria.getCriterias().add(crit);
                    break;
                case MEMBER_NUMBER:
                    crit.setKey(SearchKey.MEMBER_NUMBER);
                    crit.setValue(id.getValue());
                    criteria.getCriterias().add(crit);
                    break;
                case SERIAL_NUMBER:
                case LES:
                default:
                    LOG.error("[ Unhandled Mobile Terminal id: {} ]", id.getType());
                    break;
            }
        }
        query.setMobileTerminalSearchCriteria(criteria);
        ListPagination pagination = new ListPagination();
        pagination.setListSize(10);
        pagination.setPage(1);
        query.setPagination(pagination);

        String getMobileTerminalListRequest = MobileTerminalModuleRequestMapper.createMobileTerminalListRequest(query);
        String getMobileTerminalMessageId = producer.sendDataSourceMessage(getMobileTerminalListRequest, DataSourceQueue.MOBILE_TERMINAL);
        TextMessage getMobileTerminalResponse = consumer.getMessage(getMobileTerminalMessageId, TextMessage.class);

        try {
            LOG.info("myggan - getMobileTerminalResponse:{}", getMobileTerminalResponse.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

        List<MobileTerminalType> resultList = MobileTerminalModuleResponseMapper.mapToMobileTerminalListResponse(getMobileTerminalResponse);
        MobileTerminalType result = resultList.isEmpty()?null:resultList.get(0);

        return result;

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

    private Date auditLog(String msg, Date lastTimestamp) {
        Date newTimestamp = new Date();
        long duration = newTimestamp.getTime() - lastTimestamp.getTime();
        LOG.info("--> AUDIT - {} {}ms", msg, duration);
        return newTimestamp;
    }

}
