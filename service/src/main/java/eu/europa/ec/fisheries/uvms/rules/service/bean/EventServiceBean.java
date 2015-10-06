package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
import eu.europa.ec.fisheries.uvms.rules.message.event.MessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesValidator;

@Stateless
public class EventServiceBean implements EventService {
    final static Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);

    @Inject
    RulesValidator rulesValidator;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void messageReceived(@Observes @MessageReceivedEvent EventMessage message) {
        try {
            LOG.info("Received MessageRecievedEvent");

            // TODO: A MovementBaseType arrives. Here is a dummy.
            MovementBaseType movementBaseType = generateDummyMovementBaseType(message.getJmsMessage().getText());

            // Wrap incoming raw movement in a fact and validate
            RawFact raw = generateRawFact(movementBaseType);
            rulesValidator.evaluate(raw);

            if (raw.isOk()) {
                LOG.info("Send the validated raw position to Movement - NOT IMPLEMENTED");
            }

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

    private RawFact generateRawFact(MovementBaseType movementBaseType) {
        RawFact raw = new RawFact();
        raw.setMovementBaseType(movementBaseType);
        raw.setOk(true);
        return raw;
    }

    private MovementBaseType generateDummyMovementBaseType(String movementGuid) {
        MovementBaseType movementBaseType = new MovementBaseType();

        MovementActivityType activity = new MovementActivityType();
        activity.setCallback("CALLBACK");
        activity.setMessageId("MESSAGE_ID");
        activity.setMessageType(MovementActivityTypeType.ANC);
        movementBaseType.setActivity(activity);
        AssetId assetId = new AssetId();
        assetId.setAssetType(AssetType.VESSEL);
        assetId.setIdType(AssetIdType.CFR);
        assetId.setValue("SWE111222");
        movementBaseType.setAssetId(assetId);
        movementBaseType.setConnectId("CONNECT_ID");
        movementBaseType.setGuid(movementGuid);
        movementBaseType.setMovementType(MovementTypeType.POS);
        MovementPoint position = new MovementPoint();
        position.setAltitude(0.0d);
        position.setLatitude(null); // ERROR
        position.setLongitude(2.2d);
        movementBaseType.setPosition(position);
        // 2015-10-15 00:00:00
        Date date = new Date(1444867200000l);
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(date);
        XMLGregorianCalendar positionTime;
        try {
            positionTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
            movementBaseType.setPositionTime(positionTime);
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        movementBaseType.setReportedCourse(22.5);
        movementBaseType.setReportedSpeed(99.9);
        movementBaseType.setSource(MovementSourceType.INMARSAT_C);
        movementBaseType.setStatus("STATUS");

        return movementBaseType;
    }

}
