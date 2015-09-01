package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.MessageRecievedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;

@Stateless
public class EventServiceBean implements EventService {

    final static Logger LOG = LoggerFactory.getLogger(EventServiceBean.class);

    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getData(@Observes @MessageRecievedEvent EventMessage message) {
        LOG.info("Received MessageRecievedEvent but no logic is implemented yet");

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void returnError(@Observes @ErrorEvent EventMessage message) {
        LOG.info("Received Error RecievedEvent but no logic is implemented yet");
    }

}
