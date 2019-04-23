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

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.event.*;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesAuditProducerBean;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.ModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.EventService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFAResponseServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFaQueryServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFaReportServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.MdrRulesMessageServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.movement.RulesMovementProcessorBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesRulesMessageServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceTechnicalException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@Stateless
@Slf4j
public class RulesEventServiceBean implements EventService {
    
    private static final String ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST = " Error when un marshalling RulesBaseRequest {}";

    @Inject
    @ErrorEvent
    private Event<EventMessage> errorEvent;

    @EJB
    private GetValidationResultService getValidationResultService;

    @EJB
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesAuditProducerBean auditProducer;

    @EJB
    private RulesMovementProcessorBean rulesService;

    @EJB
    private RulesFaReportServiceBean faReportRulesMessageServiceBean;

    @EJB
    private RulesFAResponseServiceBean faResponseRulesMessageServiceBean;

    @EJB
    private RulesFaQueryServiceBean faQueryRulesMessageServiceBean;

    @EJB
    private MdrRulesMessageServiceBean mdrRulesMessageServiceBean;

    @EJB
    private SalesRulesMessageServiceBean salesRulesMessageServiceBean;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void pingReceived(@Observes @PingReceivedEvent EventMessage eventMessage) {
        try {
            PingResponse pingResponse = new PingResponse();
            pingResponse.setResponse("pong");
            String pingResponseText = JAXBMarshaller.marshallJaxBObjectToString(pingResponse);
            auditProducer.sendResponseMessageToSender(eventMessage.getJmsMessage(), pingResponseText);
        } catch (RulesModelMarshallException | MessageException e) {
            log.error(" Error when responding to ping {}", e.getMessage());
            errorEvent.fire(eventMessage);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setMovementReportReceived(@Observes @SetMovementBatchReportReceivedEvent EventMessage message) {
        log.info(" Validating movement from Received from Exchange Module..");
        try {
            TextMessage jmsMessage = message.getJmsMessage();
            SetFLUXMovementReportRequest request = JAXBMarshaller.unmarshallTextMessage(jmsMessage, SetFLUXMovementReportRequest.class);
            rulesService.setMovementReportReceived(request, jmsMessage.getJMSMessageID());
        } catch (RulesModelMapperException | RulesServiceException | JMSException e) {
            log.error(" Error when creating movement {}", e.getMessage());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getCustomRule(@Observes @GetCustomRuleReceivedEvent EventMessage message) {
        log.info(" Get custom rule by guid..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.GET_CUSTOM_RULE) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        " Error, Get Custom Rule invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            GetCustomRuleRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), GetCustomRuleRequest.class);
            CustomRuleType response = rulesService.getCustomRuleByGuid(request.getGuid());
            auditProducer.sendResponseMessageToSender(message.getJmsMessage(), RulesModuleResponseMapper.mapToGetCustomRuleResponse(response));
        } catch (RulesModelMapperException | RulesServiceException | MessageException e) {
            log.error(" Error when fetching rule by guid {}", e.getMessage());
            errorEvent.fire(message);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getTicketsByMovements(@Observes @GetTicketsByMovementsEvent EventMessage message) {
        log.info(" Fetch tickets by movements..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.GET_TICKETS_BY_MOVEMENTS) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        " Error, Get Tickets By Movements invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            GetTicketsByMovementsRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), GetTicketsByMovementsRequest.class);
            GetTicketListByMovementsResponse response = rulesService.getTicketsByMovements(request.getMovementGuids());
            String responseString = RulesModuleResponseMapper.mapToGetTicketListByMovementsResponse(response.getTickets());
            auditProducer.sendResponseMessageToSender(message.getJmsMessage(), responseString);
        } catch (RulesModelMapperException | RulesServiceException | RulesFaultException | MessageException e) {
            log.error(" Error when fetching tickets by movements {}", e.getMessage());
            errorEvent.fire(message);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void countTicketsByMovementsEvent(@Observes @CountTicketsByMovementsEvent EventMessage message) {
        log.info(" Count tickets by movements..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.COUNT_TICKETS_BY_MOVEMENTS) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        " Error, count tickets by movements invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            CountTicketsByMovementsRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), CountTicketsByMovementsRequest.class);
            long response = rulesService.countTicketsByMovements(request.getMovementGuids());
            auditProducer.sendResponseMessageToSender(message.getJmsMessage(), RulesModuleResponseMapper.mapToCountTicketListByMovementsResponse(response));
        } catch (RulesModelMapperException | RulesServiceException | RulesFaultException | MessageException e) {
            log.error(" Error when fetching ticket count by movements {}", e.getMessage());
            errorEvent.fire(message);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getTicketsAndRulesByMovementsEvent(@Observes @GetTicketsAndRulesByMovementsEvent EventMessage message) {
        log.info(" Fetch tickets and rules by movements..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.GET_TICKETS_AND_RULES_BY_MOVEMENTS) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        " Error, Get Tickets And Rules By Movements invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            GetTicketsAndRulesByMovementsRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), GetTicketsAndRulesByMovementsRequest.class);
            GetTicketsAndRulesByMovementsResponse response = rulesService.getTicketsAndRulesByMovements(request.getMovementGuids());
            auditProducer.sendResponseMessageToSender(message.getJmsMessage(), RulesModuleResponseMapper.getTicketsAndRulesByMovementsResponse(response.getTicketsAndRules()));
        } catch (RulesModelMapperException | RulesServiceException | MessageException e) {
            log.error(" Error when fetching tickets and rules by movements {}", e.getMessage());
            errorEvent.fire(message);
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setFLUXFAReportMessageReceived(@Observes @SetFLUXFAReportMessageReceivedEvent EventMessage message) {
        try {
            SetFLUXFAReportMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXFAReportMessageRequest.class);
            faReportRulesMessageServiceBean.evaluateIncomingFLUXFAReport(request);
        } catch (RulesModelMarshallException e) {
            log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            log.error(" Error when sending FLUXFAReportMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFaReportMessageReceived(@Observes @SendFaReportEvent EventMessage message) {
        try {
            SetFLUXFAReportMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXFAReportMessageRequest.class);
            faReportRulesMessageServiceBean.evaluateOutgoingFaReport(request);
        } catch (RulesModelMarshallException e) {
            log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            log.error(" Error when sending FLUXFAReportMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setFaQueryMessageReceived(@Observes @SetFluxFaQueryMessageReceivedEvent EventMessage message) {
        try {
            SetFaQueryMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFaQueryMessageRequest.class);
            faQueryRulesMessageServiceBean.evaluateIncomingFAQuery(request);
        } catch (RulesModelMarshallException e) {
            log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            log.error(" Error when sending FLUXFAQueryMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFaQueryMessageReceived(@Observes @SendFaQueryEvent EventMessage message) {
        try {
            SetFaQueryMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFaQueryMessageRequest.class);
            faQueryRulesMessageServiceBean.evaluateOutgoingFAQuery(request);
        } catch (RulesModelMarshallException e) {
            log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            log.error(" Error when sending FLUXFAQueryMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setFluxFaResponseMessageReceived(@Observes @RcvFluxResponseEvent EventMessage message) {
        try {
            SetFluxFaResponseMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFluxFaResponseMessageRequest.class);
            faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(request);
        } catch (RulesModelMarshallException e) {
            log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            log.error(" Error when sending FLUXResponseMessage to rules {}", e);
        }
    }

    public void setFLUXMDRSyncRequestMessageReceivedEvent(@Observes @SetFLUXMDRSyncMessageReceivedEvent EventMessage message){
    	 try {
	         RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
	         log.debug("RulesBaseRequest Marshalling was successful. Method : "+baseRequest.getMethod());
	         SetFLUXMDRSyncMessageRulesRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXMDRSyncMessageRulesRequest.class);
	         log.debug("SetFLUXMDRSyncMessageRequest Marshall was successful");
             mdrRulesMessageServiceBean.mapAndSendFLUXMdrRequestToExchange(request.getRequest(), request.getFr());
    	 } catch (RulesModelMarshallException e) {
             log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
         }
    }


    public void getFLUXMDRSyncResponseMessageReceivedEvent(@Observes @GetFLUXMDRSyncMessageResponseEvent EventMessage message){
        try {
            SetFLUXMDRSyncMessageRulesResponse request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXMDRSyncMessageRulesResponse.class);
            mdrRulesMessageServiceBean.mapAndSendFLUXMdrResponseToMdrModule(request.getRequest());
        } catch (RulesModelMarshallException e) {
            log.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        }
    }

    @Override
    public void receiveSalesQueryEvent(@Observes @ReceiveSalesQueryEvent EventMessage message) {
        log.info(" Received ReceiveSalesQueryEvent..");
        try {
            ReceiveSalesQueryRequest receiveSalesQueryRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), ReceiveSalesQueryRequest.class);
            salesRulesMessageServiceBean.receiveSalesQueryRequest(receiveSalesQueryRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException(" Couldn't read ReceiveSalesQueryRequest.", e);
        }
    }

    @Override
    public void receiveSalesReportEvent(@Observes @ReceiveSalesReportEvent EventMessage message) {
        log.info(" Received ReceiveSalesReportEvent..");
        try {
            ReceiveSalesReportRequest receiveSalesReportRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), ReceiveSalesReportRequest.class);
            salesRulesMessageServiceBean.receiveSalesReportRequest(receiveSalesReportRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException(" Couldn't read ReceiveSalesReportRequest.", e);
        }
    }

    @Override
    public void receiveSalesResponseEvent(@Observes @ReceiveSalesResponseEvent EventMessage message) {
        log.info(" Received ReceiveSalesResponseEvent..");
        try {
            ReceiveSalesResponseRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), ReceiveSalesResponseRequest.class);
            salesRulesMessageServiceBean.receiveSalesResponseRequest(rulesRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException(" Couldn't read ReceiveSalesResponseRequest.", e);
        }
    }

    @Override
    public void sendSalesReportEvent(@Observes @SendSalesReportEvent EventMessage message) {
        log.info(" Received SendSalesReportEvent..");
        try {
            SendSalesReportRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), SendSalesReportRequest.class);
            salesRulesMessageServiceBean.sendSalesReportRequest(rulesRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException(" Couldn't read SendSalesReportRequest.", e);
        }
    }

    @Override
    public void sendSalesResponseEvent(@Observes @SendSalesResponseEvent EventMessage message) {
        log.info(" Received SendSalesResponseEvent..");
        try {
            SendSalesResponseRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), SendSalesResponseRequest.class);
            salesRulesMessageServiceBean.sendSalesResponseRequest(rulesRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException(" Couldn't read SendSalesResponseRequest.", e);
        }
    }

    @Override
    public void getValidationResultsByRawGuid(@Observes @GetValidationResultsByRawGuid EventMessage message) {
        try {
            TextMessage jmsRequestMessage = message.getJmsMessage();
            GetValidationsByRawMsgGuidRequest rulesRequest = JAXBMarshaller.unmarshallTextMessage(jmsRequestMessage, SendSalesResponseRequest.class);
            String validationsForRawMessageGuid = getValidationResultService.getValidationsForRawMessageUUID(rulesRequest.getGuid(), rulesRequest.getType(), rulesRequest.getDf());
            auditProducer.sendResponseMessageToSender(jmsRequestMessage, validationsForRawMessageGuid);
        } catch (RulesModelMarshallException | MessageException e) {
            log.error(" Error while trying to send Response to a GetValidationResultsByRawGuid to the Requestor Module..", e);
        }
    }

    @SuppressWarnings("unused")
	private void sendAuditMessage(AuditObjectTypeEnum type, AuditOperationEnum operation, String affectedObject, String comment, String username) {
        try {
            String message = AuditLogMapper.mapToAuditLog(type.getValue(), operation.getValue(), affectedObject, comment, username);
            auditProducer.sendModuleMessage(message, rulesConsumer.getDestination());
        }
        catch (AuditModelMarshallException | MessageException e) {
            log.error(" Error when sending message to Audit {}", e.getMessage());
        }
    }

}