package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

import eu.europa.ec.fisheries.uvms.rules.message.event.MessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;

@Local
public interface EventService {

    public void messageReceived(@Observes @MessageReceivedEvent EventMessage message);

}
