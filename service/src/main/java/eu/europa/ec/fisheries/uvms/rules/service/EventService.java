package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;

import eu.europa.ec.fisheries.uvms.rules.message.event.*;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;

@Local
public interface EventService {

    void pingReceived(@Observes @PingReceivedEvent EventMessage eventMessage);

    void setMovementReportReceived(@Observes @SetMovementReportReceivedEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void getCustomRule(@Observes @GetCustomRuleReceivedEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void getTicketsByMovements(@Observes @GetTicketsByMovementsEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void countTicketsByMovementsEvent(@Observes @CountTicketsByMovementsEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void getTicketsAndRulesByMovementsEvent(@Observes @GetTicketsAndRulesByMovementsEvent EventMessage message);
}
