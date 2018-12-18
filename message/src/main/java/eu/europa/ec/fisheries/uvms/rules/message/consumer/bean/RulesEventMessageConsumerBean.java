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
import eu.europa.ec.fisheries.uvms.rules.message.event.*;
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
        @ActivationConfigProperty(propertyName = "maxMessagesPerSessions", propertyValue = "3"),
        @ActivationConfigProperty(propertyName = "initialRedeliveryDelay", propertyValue = "120000"),
        @ActivationConfigProperty(propertyName = "maximumRedeliveries", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "3"),
})
public class RulesEventMessageConsumerBean implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEventMessageConsumerBean.class);

    @Inject
    @SetMovementBatchReportReceivedEvent
    private Event<EventMessage> setMovementReportBatchRecievedEvent;

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
    @ErrorEvent
    private Event<EventMessage> errorEvent;

    @Override
    public void onMessage(Message message) {
        String id = UUID.randomUUID().toString();
        MDC.put("clientName", id);
        MDC.remove("requestId");
        LOG.debug("Message received in rules. Times redelivered: ", getTimesRedelivered(message));
        TextMessage textMessage = (TextMessage) message;
        MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext(textMessage);
        try {
            RulesBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, RulesBaseRequest.class);
            RulesModuleMethod method = request.getMethod();
            LOG.info("\n\nRequest message method: {}", method.value());
            switch (method) {
                case RECEIVE_MOVEMENT_BATCH:
                    setMovementReportBatchRecievedEvent.fire(new EventMessage(textMessage));
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
