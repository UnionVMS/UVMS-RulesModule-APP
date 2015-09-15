package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.message.event.MessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.business.DummyMovement;
import eu.europa.ec.fisheries.uvms.rules.service.business.PositionEvent;
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

            // There will arrive some kind of report, for now a dummy report
            DummyMovement dummyMovement = createDummyMovementReport(message.getJmsMessage().getText());

            sanitycheck(dummyMovement);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private DummyMovement createDummyMovementReport(String guid) {
        String assetName = "assetName";
        String country = "country";
        Double latitude = null; // Error in latitude
        Double longitude = null; // Error in longitude
        Date timestamp = new Date();
        Double calculatedSpeed = 1000d;

        DummyMovement dummyMovement = new DummyMovement();
        dummyMovement.setAssetName(assetName);
        dummyMovement.setCountry(country);
        dummyMovement.setGuid(guid);
        dummyMovement.setLatitude(latitude);
        dummyMovement.setLongitude(longitude);
        dummyMovement.setTimestamp(timestamp);
        dummyMovement.setCalculatedSpeed(calculatedSpeed);

        return dummyMovement;
    }

    private void sanitycheck(DummyMovement dummyMovement) {
        PositionEvent pe = new PositionEvent();

        pe.setAssetName(dummyMovement.getAssetName());
        pe.setCountry(dummyMovement.getCountry());
        pe.setGuid(dummyMovement.getGuid());
        pe.setLatitude(dummyMovement.getLatitude());
        pe.setLongitude(dummyMovement.getLongitude());
        pe.setTimestamp(dummyMovement.getTimestamp());
        pe.setCalculatedSpeed(dummyMovement.getCalculatedSpeed());

        pe.setComment(dummyMovement.getGuid());

        rulesValidator.evaluate(pe);
    }

}
