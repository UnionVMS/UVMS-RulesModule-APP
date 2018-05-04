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

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message driven bean that receives all messages that
 * have a message selector, for which no other MDB has been defined.
 */
@MessageDriven(mappedName = MessageConstants.QUEUE_MODULE_RULES, activationConfig = {
        @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR, propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME),
        @ActivationConfigProperty(propertyName = "maxMessagesPerSessions", propertyValue = "10"), // default: 10
        @ActivationConfigProperty(propertyName = "initialRedeliveryDelay", propertyValue = "1000"), // default: 1000
        @ActivationConfigProperty(propertyName = "maximumRedeliveries", propertyValue = "10"), // default: 5
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "1"), // default: 10
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "messageSelector NOT IN ('ReceiveSalesReportRequest')")
})
public class RulesDefaultSelectorEventConsumerBean implements MessageListener {

    private final static Logger LOG = LoggerFactory.getLogger(RulesDefaultSelectorEventConsumerBean.class);

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
        MDC.remove("requestId");
        LOG.debug("Message received in rules. Times redelivered: " + getTimesRedelivered(message));
        TextMessage textMessage = (TextMessage) message;
        MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext(textMessage);
        try {
            RulesBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, RulesBaseRequest.class);
            RulesModuleMethod method = request.getMethod();
            LOG.info("Request message method: " + method.value());
            switch (method) {
                case RECEIVE_SALES_QUERY:
                    receiveSalesQueryEvent.fire(new EventMessage(textMessage));
                    break;
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
                default:
                    LOG.error("[ Request method '{}' is not implemented ]", method.name());
                    errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Method not implemented:" + method.name())));
                    break;
            }
            if (method == null) {
                LOG.error("[ Request method is null ]");
                errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Error when receiving message in rules: Request method is null")));
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
