package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;

@Local
public interface EventService {

    public void pingReceived(@Observes @PingReceivedEvent EventMessage eventMessage);

    public void setMovementReportReceived(@Observes @SetMovementReportReceivedEvent EventMessage message);

}
