package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.movement.mobileterminal.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementComChannelType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementMetaData;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ValidateMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.MovementMapper;

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
    RulesMessageProducer messageProducer;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setMovementReportRecieved(@Observes @SetMovementReportReceivedEvent EventMessage message) {
        LOG.info("Received SetMovementReportReceivedEvent");
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
            RawFact rawFact = generateRawFact(rawMovementType);
            rulesValidator.evaluate(rawFact);

            // MovementComChannelType.FLUX -> assetId
            // MovementComChannelType.MOBILE_TERMINAL -> mobileTerminalId
            // assetId | mobileTerminalId -> connectId
            // TODO: Get more stuff; MobileTerminal guid, finns asset (på tex
            // IRCS), ...

            if (rawFact.isOk()) {

                LOG.info("Send the validated raw position to Movement");
                MovementBaseType movementBaseType = MovementMapper.getMapper().map(rawMovementType, MovementBaseType.class);
                String movement = MovementModuleRequestMapper.mapToCreateMovementRequest(movementBaseType);

                messageProducer.sendDataSourceMessage(movement, DataSourceQueue.MOVEMENT);

                // TODO: Do I want an answer here? Or asych?
                // rulseService.setMovementReport(m);
            }

        } catch (RulesModelMapperException | ModelMarshallException | MessageException ex) {
            LOG.error("[ Error when creating movement ] {}", ex.getMessage());
            errorEvent.fire(message);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void validateMovementReportRecieved(@Observes @ValidateMovementReportReceivedEvent EventMessage message) {
        try {
            LOG.info("Received ValidateMovementReportReceivedEvent");

            // TODO: Later, a MovementType arrives. Here is a dummy.

            // Wrap incoming movement in a fact and validate
            MovementFact movementFact = new MovementFact();
            MovementType movementType = generateDummyMovementType(message.getJmsMessage().getText());
            movementFact.setMovementType(movementType);

            rulesValidator.evaluate(movementFact);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private MovementType generateDummyMovementType(String movementGuid) {
        MovementType movementType = new MovementType();

        // MovementBaseType part
        MovementActivityType activity = new MovementActivityType();
        activity.setCallback("CALLBACK");
        activity.setMessageId("MESSAGE_ID");
        activity.setMessageType(MovementActivityTypeType.ANC);
        movementType.setActivity(activity);
        AssetId assetId = new AssetId();
        assetId.setAssetType(AssetType.VESSEL);
        assetId.setIdType(AssetIdType.CFR);
        assetId.setValue("SWE111222");
        movementType.setAssetId(assetId);
        movementType.setConnectId("CONNECT_ID");
        movementType.setGuid(movementGuid);
        movementType.setMovementType(MovementTypeType.POS);
        MovementPoint position = new MovementPoint();
        position.setAltitude(0.0d);
        position.setLatitude(null); // ERROR
        position.setLongitude(2.2d);
        movementType.setPosition(position);
        // 2015-10-15 00:00:00
        Date date = new Date(1444867200000l);
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(date);
        XMLGregorianCalendar positionTime;
        try {
            positionTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
            movementType.setPositionTime(positionTime);
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        movementType.setReportedCourse(22.5);
        movementType.setReportedSpeed(99.9);
        movementType.setSource(MovementSourceType.INMARSAT_C);
        movementType.setStatus("STATUS");

        // MovementType part
        movementType.setCalculatedCourse(89.5);
        movementType.setCalculatedSpeed(100.1);
        movementType.setComChannelType(MovementComChannelType.MOBILE_TERMINAL);
        MovementMetaData metaData = new MovementMetaData();
        metaData.setClosestCountryCoast("SWE");
        metaData.setClosestPort("PEARL_HARBOR");
        metaData.setDistanceToClosestPort(11.1);
        metaData.setDistanceToCountryCoast(22.2);
        movementType.setMetaData(metaData);
        MobileTerminalId mobileTerminal = new MobileTerminalId();
        mobileTerminal.setId("MOBILE_TERMINAL_ID");
        movementType.setMobileTerminal(mobileTerminal);
        movementType.setWkt("WKT");
        movementType.getSegmentIds().add("SEGMENT_ID");

        return movementType;
    }

    private RawFact generateRawFact(RawMovementType rawMovementType) {
        RawFact raw = new RawFact();
        raw.setRawMovementType(rawMovementType);
        raw.setOk(true);
        return raw;
    }

}
