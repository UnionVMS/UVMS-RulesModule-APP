/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.message.event.GetValidationResultsByRawGuid;
import eu.europa.ec.fisheries.uvms.rules.message.event.RcvFluxResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendFaQueryEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendFaReportEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFluxFaQueryMessageReceivedEvent;
import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;

import eu.europa.ec.fisheries.uvms.rules.message.event.CountTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetCustomRuleReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetFLUXMDRSyncMessageResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsAndRulesByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ReceiveSalesQueryEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ReceiveSalesReportEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ReceiveSalesResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendSalesReportEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendSalesResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFLUXFAReportMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFLUXMDRSyncMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
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

    void setFLUXFAReportMessageReceived(@Observes @SetFLUXFAReportMessageReceivedEvent EventMessage message);

    void setFaQueryMessageReceived(@Observes @SetFluxFaQueryMessageReceivedEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendFaQueryMessageReceived(@Observes @SendFaQueryEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendFaReportMessageReceived(@Observes @SendFaReportEvent EventMessage message);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void setFluxFaResponseMessageReceived(@Observes @RcvFluxResponseEvent EventMessage message);

    void setFLUXMDRSyncRequestMessageReceivedEvent(@Observes @SetFLUXMDRSyncMessageReceivedEvent EventMessage message);

    void getFLUXMDRSyncResponseMessageReceivedEvent(@Observes @GetFLUXMDRSyncMessageResponseEvent EventMessage message);

    void receiveSalesQueryEvent(@Observes @ReceiveSalesQueryEvent EventMessage message);

    void receiveSalesReportEvent(@Observes @ReceiveSalesReportEvent EventMessage message);

    void receiveSalesResponseEvent(@Observes @ReceiveSalesResponseEvent EventMessage message);

    void sendSalesReportEvent(@Observes @SendSalesReportEvent EventMessage message);

    void sendSalesResponseEvent(@Observes @SendSalesResponseEvent EventMessage message);

    void getValidationResultsByRawGuid(@Observes @GetValidationResultsByRawGuid EventMessage message);
}
