package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.MessageRecievedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;

@Local
public interface EventService {

    public void getData(@Observes @MessageRecievedEvent EventMessage message);

    public void returnError(@Observes @ErrorEvent EventMessage message);

}
