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
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.module.v1.CountTicketsByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetCustomRuleRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXMDRSyncMessageRulesRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXMDRSyncMessageRulesResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.message.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.RulesMessageEvent;
import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@MessageDriven(mappedName = MessageConstants.RULES_MESSAGE_IN_QUEUE, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = MessageConstants.CONNECTION_FACTORY)
})
@Slf4j
public class RulesEventConsumerBean implements MessageListener {

    @EJB
    private RulesMessageProducer producer;

    @EJB
    private PingReceivedMessageBean pingReceivedBean;

    @EJB
    private GetCustomRuleRequestMessageBean customRuleBean;

    @EJB
    private SetMovementReportRequestMessageBean movementReportBean;

    @EJB
    private GetTicketsByMovementsRequestMessageBean movementTicketBean;

    @EJB
    private CountTicketsByMovementsRequestMessageBean countTicketsBean;

    @EJB
    private GetTicketsAndRulesByMovementsRequestMessageBean ticketsAndRulesMovementBean;

    @EJB
    private SetFLUXFAReportMessageRequestMessageBean faReportMessageBean;

    @EJB
    private SetFLUXMDRSyncMessageRulesRequestMessageBean mdrSyncRequestBean;

    @EJB
    private SetFLUXMDRSyncMessageRulesResponseMessageBean mdrSyncResponseBean;

    @EJB
    private ReceiveSalesQueryRequestMessageBean salesQueryRequestBean;

    @EJB
    private ReceiveSalesReportRequestMessageBean receiveSalesReportRequestBean;

    @EJB
    private SendSalesReportRequestMessageBean sendSalesReportRequestBean;

    @EJB
    private SendSalesResponseRequestMessageBean sendSalesresponseRequestBean;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {

        String id = UUID.randomUUID().toString();
        MDC.put(MessageConstants.MDC_IDENTIFIER, id);

        log.info("Message received in rules");

        TextMessage textMessage = (TextMessage) message;

        try {

            RulesBaseRequest request = JAXBUtils.unMarshallMessage(textMessage.getText(), RulesBaseRequest.class);
            RulesModuleMethod method = request.getMethod();
            String username = request.getUsername();

            switch (method) {
                case SET_MOVEMENT_REPORT:
                    movementReportBean.processMessage(textMessage, username, SetMovementReportRequest.class);
                    break;
                case PING:
                    pingReceivedBean.pingReceived(new RulesMessageEvent(textMessage), username); // Generify
                    break;
                case GET_CUSTOM_RULE:
                    customRuleBean.processMessage(textMessage, username, GetCustomRuleRequest.class);
                    break;
                case GET_TICKETS_BY_MOVEMENTS:
                    movementTicketBean.processMessage(textMessage, username, GetTicketsByMovementsRequest.class);
                    break;
                case COUNT_TICKETS_BY_MOVEMENTS:
                    countTicketsBean.processMessage(textMessage, username, CountTicketsByMovementsRequest.class);
                    break;
                case GET_TICKETS_AND_RULES_BY_MOVEMENTS:
                    ticketsAndRulesMovementBean.processMessage(textMessage, username, GetTicketsAndRulesByMovementsRequest.class);
                    break;
                case SET_FLUX_FA_REPORT:
                    faReportMessageBean.processMessage(textMessage, username, SetFLUXFAReportMessageRequest.class);
                    break;
                case SET_FLUX_MDR_SYNC_REQUEST:
                    mdrSyncRequestBean.processMessage(textMessage, username, SetFLUXMDRSyncMessageRulesRequest.class);
                    break;
                case GET_FLUX_MDR_SYNC_RESPONSE:
                    mdrSyncResponseBean.processMessage(textMessage, username, SetFLUXMDRSyncMessageRulesResponse.class);
                    break;
                case RECEIVE_SALES_QUERY:
                    salesQueryRequestBean.processMessage(textMessage, username, ReceiveSalesQueryRequest.class);
                    break;
                case RECEIVE_SALES_REPORT:
                    receiveSalesReportRequestBean.processMessage(textMessage, username, ReceiveSalesReportRequest.class);
                    break;
                case RECEIVE_SALES_RESPONSE:
                    receiveSalesReportRequestBean.processMessage(textMessage, username, ReceiveSalesResponseRequest.class);
                    break;
                case SEND_SALES_REPORT:
                    sendSalesReportRequestBean.processMessage(textMessage, username, SendSalesReportRequest.class);
                    break;
                case SEND_SALES_RESPONSE:
                    sendSalesresponseRequestBean.processMessage(textMessage, username, SendSalesReportRequest.class);
                    break;

                default:
                    String errMsg = "[ Request method '{}' is not implemented ]";
                    log.error(errMsg, request.getMethod().name());
                    producer.sendModuleErrorResponseMessage(getEventMessage(textMessage, errMsg));
                    break;
            }
            if (request.getMethod() == null) {
                String errMsg = "[ Request method is null ]";
                log.error(errMsg);
                producer.sendModuleErrorResponseMessage(getEventMessage(textMessage, errMsg));
            }

        } catch (JAXBException | JMSException e) {
            String errMsg = "[ Error when receiving message in Rules. ]";
            log.error(errMsg, e.getMessage());
            producer.sendModuleErrorResponseMessage(getEventMessage(textMessage, errMsg));

        } catch (NullPointerException e) {
            String errMsg = "[ Error when receiving message in Rules: {}]";
            log.error(errMsg, e.getMessage());
            producer.sendModuleErrorResponseMessage(getEventMessage(textMessage, errMsg));

        } finally {
            MDC.remove(MessageConstants.MDC_IDENTIFIER);
        }
    }

    private EventMessage getEventMessage(TextMessage textMessage, String errMsg) {
        RulesFault rulesFault = new RulesFault();
        rulesFault.setCode(FaultCode.RULES_EVENT_SERVICE.getCode());
        rulesFault.setMessage(errMsg);
        return new EventMessage(textMessage, rulesFault);
    }

}
