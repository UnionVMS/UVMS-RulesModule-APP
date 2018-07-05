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
package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import eu.europa.ec.fisheries.uvms.rules.message.event.CountTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetCustomRuleReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetFLUXMDRSyncMessageResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsAndRulesByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.PingReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.RcvFluxResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ReceiveSalesQueryEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ReceiveSalesReportEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ReceiveSalesResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendFaQueryEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendFaReportEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendSalesReportEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SendSalesResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFLUXFAReportMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFLUXMDRSyncMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetFluxFaQueryMessageReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.SetMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ValidateMovementReportReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.ModuleResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Message driven bean that receives all messages that
 * have no message selector.
 */
@MessageDriven(mappedName = MessageConstants.QUEUE_MODULE_RULES, activationConfig = {
        @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR, propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME),
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "messageSelector IS NULL"),
        @ActivationConfigProperty(propertyName = "maxMessagesPerSessions", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "initialRedeliveryDelay", propertyValue = "600000"),
        @ActivationConfigProperty(propertyName = "maximumRedeliveries", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "1"),
})
public class RulesEventMessageConsumerBean implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEventMessageConsumerBean.class);

    @Inject
    @SetMovementReportReceivedEvent
    private Event<EventMessage> setMovementReportRecievedEvent;

    @Inject
    @GetTicketsByMovementsEvent
    private Event<EventMessage> getTicketsByMovementsEvent;

    @Inject
    @CountTicketsByMovementsEvent
    private Event<EventMessage> countTicketByMovementsEvent;

    @Inject
    @GetCustomRuleReceivedEvent
    private Event<EventMessage> getCustomRuleRecievedEvent;

    @Inject
    @GetTicketsAndRulesByMovementsEvent
    private Event<EventMessage> getTicketsAndRulesByMovementsEvent;

    @Inject
    @ValidateMovementReportReceivedEvent
    private Event<EventMessage> validateMovementReportReceivedEvent;

    @Inject
    @PingReceivedEvent
    private Event<EventMessage> pingReceivedEvent;

    @Inject
    @SetFLUXFAReportMessageReceivedEvent
    private Event<EventMessage> setFLUXFAReportMessageReceivedEvent;

    @Inject
    @SendFaReportEvent
    private Event<EventMessage> sendFLUXFAReportMessageReceivedEvent;

    @Inject
    @SetFluxFaQueryMessageReceivedEvent
    private Event<EventMessage> setFaQueryReceivedEvent;

    @Inject
    @SendFaQueryEvent
    private Event<EventMessage> sendFaQueryReceivedEvent;

    @Inject
    @RcvFluxResponseEvent
    private Event<EventMessage> rcvFluxResponse;

    @Inject
    @SetFLUXMDRSyncMessageReceivedEvent
    private Event<EventMessage> setFLUXMDRSyncMessageReceivedEvent;

    @Inject
    @GetFLUXMDRSyncMessageResponseEvent
    private Event<EventMessage> getFluxMdrSynchMessageResponse;

    @Inject
    @ReceiveSalesQueryEvent
    private Event<EventMessage> receiveSalesQueryEvent;

    @Inject
    @ReceiveSalesReportEvent
    private Event<EventMessage> receiveSalesReportEvent;

    @Inject
    @ReceiveSalesResponseEvent
    private Event<EventMessage> receiveSalesResponseEvent;

    @Inject
    @SendSalesReportEvent
    private Event<EventMessage> sendSalesReportEvent;

    @Inject
    @SendSalesResponseEvent
    private Event<EventMessage> sendSalesResponseEvent;

    @Inject
    @ErrorEvent
    private Event<EventMessage> errorEvent;

    @Override
    public void onMessage(Message message) {
        String id = UUID.randomUUID().toString();
        MDC.put("clientName", id);
        MDC.remove("requestId");
        LOG.debug("Message received in rules. Times redelivered: " + getTimesRedelivered(message));
        TextMessage textMessage = (TextMessage) message;
        MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext(textMessage);
        try {
            RulesBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, RulesBaseRequest.class);
            RulesModuleMethod method = request.getMethod();
            LOG.info("\n\nRequest message method: {}", method.value());
            switch (method) {
                case SET_MOVEMENT_REPORT:
                    setMovementReportRecievedEvent.fire(new EventMessage(textMessage));
                    break;
                case PING:
                    pingReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_CUSTOM_RULE:
                    getCustomRuleRecievedEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_TICKETS_BY_MOVEMENTS:
                    getTicketsByMovementsEvent.fire(new EventMessage(textMessage));
                    break;
                case COUNT_TICKETS_BY_MOVEMENTS:
                    countTicketByMovementsEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_TICKETS_AND_RULES_BY_MOVEMENTS:
                    getTicketsAndRulesByMovementsEvent.fire(new EventMessage(textMessage));
                    break;
                case SET_FLUX_FA_REPORT :
                    setFLUXFAReportMessageReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case SEND_FLUX_FA_REPORT :
                    sendFLUXFAReportMessageReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case SET_FLUX_FA_QUERY :
                    setFaQueryReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case SEND_FLUX_FA_QUERY :
                    sendFaQueryReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case RCV_FLUX_RESPONSE:
                    rcvFluxResponse.fire(new EventMessage(textMessage));
                    break;
                case SET_FLUX_MDR_SYNC_REQUEST :
                    setFLUXMDRSyncMessageReceivedEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_FLUX_MDR_SYNC_RESPONSE :
                    getFluxMdrSynchMessageResponse.fire(new EventMessage(textMessage));
                    break;
                case RECEIVE_SALES_QUERY:
                    receiveSalesQueryEvent.fire(new EventMessage(textMessage));
                    break;

                /** @deprecated
                 * This code has moved to RulesDefaultSelectorEventConsumer.
                 *  If you use the latest version of Exchange and Sales, this code will not be called anymore.
                 *  For the parties that still use an older version of Exchange and Sales, this code is kept.
                 *  Please upgrade as soon as possible. We'll remove this code in a next release.
                 */
                case RECEIVE_SALES_RESPONSE:
                    receiveSalesResponseEvent.fire(new EventMessage(textMessage));
                    break;
                case RECEIVE_SALES_REPORT:
                    receiveSalesReportEvent.fire(new EventMessage(textMessage));
                    break;
                case SEND_SALES_REPORT:
                    sendSalesReportEvent.fire(new EventMessage(textMessage));
                    break;
                case SEND_SALES_RESPONSE:
                    sendSalesResponseEvent.fire(new EventMessage(textMessage));
                    break;
                /** end deprecation **/
                default:
                    LOG.error("[ Request method '{}' is not implemented ]", method.name());
                    errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Method not implemented:" + method.name())));
                    break;
            }
        } catch (NullPointerException | RulesModelMarshallException e) {
            LOG.error("[ Error when receiving message in rules: {}]", e.getMessage());
            errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Error when receiving message in rules:" + e.getMessage())));
        } finally {
            MDC.remove("clientName");
        }
    }

    private int getTimesRedelivered(Message message) {
        try {
            return (message.getIntProperty("JMSXDeliveryCount") - 1);
        } catch (Exception e) {
            return 0;
        }
    }

}
