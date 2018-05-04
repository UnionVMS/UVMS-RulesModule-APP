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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.module.v1.CountTicketsByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetCustomRuleRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetValidationsByRawMsgGuidRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.PingResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXMDRSyncMessageRulesRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXMDRSyncMessageRulesResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.event.CountTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetCustomRuleReceivedEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetFLUXMDRSyncMessageResponseEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsAndRulesByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetTicketsByMovementsEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.GetValidationResultsByRawGuid;
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
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
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
import eu.europa.ec.fisheries.uvms.rules.service.RulesMessageService;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceTechnicalException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Slf4j
public class RulesEventServiceBean implements EventService {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEventServiceBean.class);

    private static final String ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST = "[ERROR] Error when un marshalling RulesBaseRequest {}";

    @Inject
    @ErrorEvent
    private Event<EventMessage> errorEvent;

    @EJB
    private RulesMessageProducer rulesProducer;

    @EJB
    private RulesService rulesService;

    @EJB
    private RulesMessageService messageService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void pingReceived(@Observes @PingReceivedEvent EventMessage eventMessage) {
        try {
            PingResponse pingResponse = new PingResponse();
            pingResponse.setResponse("pong");
            String pingResponseText = JAXBMarshaller.marshallJaxBObjectToString(pingResponse);
            rulesProducer.sendModuleResponseMessage(eventMessage.getJmsMessage(), pingResponseText);
        } catch (RulesModelMarshallException | MessageException e) {
            LOG.error("[ERROR] Error when responding to ping {}", e.getMessage());
            errorEvent.fire(eventMessage);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setMovementReportReceived(@Observes @SetMovementReportReceivedEvent EventMessage message) {
        LOG.info("[INFO] Validating movement from Received from Exchange Module..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.SET_MOVEMENT_REPORT) {
                LOG.error("[ERROR] Error, Set Movement Report invoked but it is not the intended method, caller is trying : " + baseRequest.getMethod().name());
            }
            SetMovementReportRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetMovementReportRequest.class);
            rulesService.setMovementReportReceived(request.getRequest(), request.getType().name(), baseRequest.getUsername());
        } catch (RulesModelMapperException | RulesServiceException e) {
            LOG.error("[ERROR] Error when creating movement {}", e.getMessage());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getCustomRule(@Observes @GetCustomRuleReceivedEvent EventMessage message) {
        LOG.info("[INFO] Get custom rule by guid..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.GET_CUSTOM_RULE) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        "[ERROR] Error, Get Custom Rule invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            GetCustomRuleRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), GetCustomRuleRequest.class);
            CustomRuleType response = rulesService.getCustomRuleByGuid(request.getGuid());
            rulesProducer.sendModuleResponseMessage(message.getJmsMessage(), RulesModuleResponseMapper.mapToGetCustomRuleResponse(response));
        } catch (RulesModelMapperException | RulesServiceException | RulesFaultException | MessageException e) {
            LOG.error("[ERROR] Error when fetching rule by guid {}", e.getMessage());
            errorEvent.fire(message);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getTicketsByMovements(@Observes @GetTicketsByMovementsEvent EventMessage message) {
        LOG.info("[INFO] Fetch tickets by movements..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.GET_TICKETS_BY_MOVEMENTS) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        "[ERROR] Error, Get Tickets By Movements invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            GetTicketsByMovementsRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), GetTicketsByMovementsRequest.class);
            GetTicketListByMovementsResponse response = rulesService.getTicketsByMovements(request.getMovementGuids());
            String responseString = RulesModuleResponseMapper.mapToGetTicketListByMovementsResponse(response.getTickets());
            rulesProducer.sendModuleResponseMessage(message.getJmsMessage(), responseString);
        } catch (RulesModelMapperException | RulesServiceException | RulesFaultException | MessageException e) {
            LOG.error("[ERROR] Error when fetching tickets by movements {}", e.getMessage());
            errorEvent.fire(message);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void countTicketsByMovementsEvent(@Observes @CountTicketsByMovementsEvent EventMessage message) {
        LOG.info("[INFO] Count tickets by movements..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.COUNT_TICKETS_BY_MOVEMENTS) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        "[ERROR] Error, count tickets by movements invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            CountTicketsByMovementsRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), CountTicketsByMovementsRequest.class);
            long response = rulesService.countTicketsByMovements(request.getMovementGuids());
            rulesProducer.sendModuleResponseMessage(message.getJmsMessage(), RulesModuleResponseMapper.mapToCountTicketListByMovementsResponse(response));
        } catch (RulesModelMapperException | RulesServiceException | RulesFaultException | MessageException e) {
            LOG.error("[ERROR] Error when fetching ticket count by movements {}", e.getMessage());
            errorEvent.fire(message);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getTicketsAndRulesByMovementsEvent(@Observes @GetTicketsAndRulesByMovementsEvent EventMessage message) {
        LOG.info("[INFO] Fetch tickets and rules by movements..");
        try {
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            if (baseRequest.getMethod() != RulesModuleMethod.GET_TICKETS_AND_RULES_BY_MOVEMENTS) {
                errorEvent.fire(new EventMessage(message.getJmsMessage(), ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE,
                        "[ERROR] Error, Get Tickets And Rules By Movements invoked but it is not the intended method, caller is trying: "
                                + baseRequest.getMethod().name())));
            }
            GetTicketsAndRulesByMovementsRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), GetTicketsAndRulesByMovementsRequest.class);
            GetTicketsAndRulesByMovementsResponse response = rulesService.getTicketsAndRulesByMovements(request.getMovementGuids());
            rulesProducer.sendModuleResponseMessage(message.getJmsMessage(), RulesModuleResponseMapper.getTicketsAndRulesByMovementsResponse(response.getTicketsAndRules()));
        } catch (RulesModelMapperException | RulesServiceException | MessageException e) {
            LOG.error("[ERROR] Error when fetching tickets and rules by movements {}", e.getMessage());
            errorEvent.fire(message);
        }
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setFLUXFAReportMessageReceived(@Observes @SetFLUXFAReportMessageReceivedEvent EventMessage message) {
        try {
            LOG.info("\n\n[INFO] Got SetFLUXFAReportMessageReceived (@SetFLUXFAReportMessageReceivedEvent) inside rules");
            SetFLUXFAReportMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXFAReportMessageRequest.class);
            messageService.evaluateReceiveFLUXFAReportRequest(request);
        } catch (RulesModelMarshallException e) {
            LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            LOG.error("[ERROR] Error when sending FLUXFAReportMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFaReportMessageReceived(@Observes @SendFaReportEvent EventMessage message) {
        try {
            LOG.info("\n\n[INFO] Got SetFLUXFAReportMessageRequest (@SendFaReportEvent) inside rules..");
            SetFLUXFAReportMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXFAReportMessageRequest.class);
            messageService.evaluateSendFaReportMessage(request);
        } catch (RulesModelMarshallException e) {
            LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            LOG.error("[ERROR] Error when sending FLUXFAReportMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setFaQueryMessageReceived(@Observes @SetFluxFaQueryMessageReceivedEvent EventMessage message) {
        try {
            LOG.info("\n\n[INFO] Got SetFluxFaQueryMessageReceivedEvent (@SetFluxFaQueryMessageReceivedEvent) inside rules..");
            SetFaQueryMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFaQueryMessageRequest.class);
            messageService.evaluateReceiveFaQueryRequest(request);
        } catch (RulesModelMarshallException e) {
            LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            LOG.error("[ERROR] Error when sending FLUXFAQueryMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFaQueryMessageReceived(@Observes @SendFaQueryEvent EventMessage message) {
        try {
            LOG.info("\n\n[INFO] Got SetFluxFaQueryMessageReceivedEvent (@SendFaQueryEvent) inside rules..");
            SetFaQueryMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFaQueryMessageRequest.class);
            messageService.evaluateSendFaQueryRequest(request);
        } catch (RulesModelMarshallException e) {
            LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            LOG.error("[ERROR] Error when sending FLUXFAQueryMessage to rules {}", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setFluxFaResponseMessageReceived(@Observes @RcvFluxResponseEvent EventMessage message) {
        try {
            LOG.info("\n\n[INFO] Got SetFluxFaResponseMessageRequest (@RcvFluxResponseEvent) inside rules..");
            SetFluxFaResponseMessageRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFluxFaResponseMessageRequest.class);
            messageService.evaluateSetFluxFaResponseRequest(request);
        } catch (RulesModelMarshallException e) {
            LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        } catch (RulesServiceException e) {
            LOG.error("[ERROR] Error when sending FLUXResponseMessage to rules {}", e);
        }
    }

    public void setFLUXMDRSyncRequestMessageReceivedEvent(@Observes @SetFLUXMDRSyncMessageReceivedEvent EventMessage message){
    	 try {
	    	 LOG.debug("[INFO] @SetFLUXMDRSyncMessageReceivedEvent recieved inside Rules Module.");
	         RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
	         LOG.debug("[INFO]RulesBaseRequest Marshalling was successful. Method : "+baseRequest.getMethod());
	         SetFLUXMDRSyncMessageRulesRequest request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXMDRSyncMessageRulesRequest.class);
	         LOG.debug("[INFO]SetFLUXMDRSyncMessageRequest Marshall was successful");
             messageService.mapAndSendFLUXMdrRequestToExchange(request.getRequest());
    	 } catch (RulesModelMarshallException e) {
             LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
         }
    }


    public void getFLUXMDRSyncResponseMessageReceivedEvent(@Observes @GetFLUXMDRSyncMessageResponseEvent EventMessage message){
        try {
            LOG.debug("[INFO] @SetFLUXMDRSyncMessageReceivedEvent recieved inside Rules Module.");
            RulesBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), RulesBaseRequest.class);
            LOG.debug("[INFO] RulesBaseRequest Marshalling was successful. Method : "+baseRequest.getMethod());
            SetFLUXMDRSyncMessageRulesResponse request = JAXBMarshaller.unmarshallTextMessage(message.getJmsMessage(), SetFLUXMDRSyncMessageRulesResponse.class);
            LOG.debug("[INFO] SetFLUXMDRSyncMessageRequest Marshall was successful");
            messageService.mapAndSendFLUXMdrResponseToMdrModule(request.getRequest());
        } catch (RulesModelMarshallException e) {
            LOG.error(ERROR_WHEN_UN_MARSHALLING_RULES_BASE_REQUEST, e);
        }
    }

    @Override
    public void receiveSalesQueryEvent(@Observes @ReceiveSalesQueryEvent EventMessage message) {
        LOG.info("[INFO] Received ReceiveSalesQueryEvent..");
        try {
            ReceiveSalesQueryRequest receiveSalesQueryRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), ReceiveSalesQueryRequest.class);
            messageService.receiveSalesQueryRequest(receiveSalesQueryRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException("[ERROR] Couldn't read ReceiveSalesQueryRequest.", e);
        }
    }

    @Override
    public void receiveSalesReportEvent(@Observes @ReceiveSalesReportEvent EventMessage message) {
        LOG.info("[INFO] Received ReceiveSalesReportEvent..");
        try {
            ReceiveSalesReportRequest receiveSalesReportRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), ReceiveSalesReportRequest.class);
            messageService.receiveSalesReportRequest(receiveSalesReportRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException("[ERROR] Couldn't read ReceiveSalesReportRequest.", e);
        }
    }

    @Override
    public void receiveSalesResponseEvent(@Observes @ReceiveSalesResponseEvent EventMessage message) {
        LOG.info("[INFO] Received ReceiveSalesResponseEvent..");
        try {
            ReceiveSalesResponseRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), ReceiveSalesResponseRequest.class);
            messageService.receiveSalesResponseRequest(rulesRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException("[ERROR] Couldn't read ReceiveSalesResponseRequest.", e);
        }
    }

    @Override
    public void sendSalesReportEvent(@Observes @SendSalesReportEvent EventMessage message) {
        LOG.info("[INFO] Received SendSalesReportEvent..");
        try {
            SendSalesReportRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), SendSalesReportRequest.class);
            messageService.sendSalesReportRequest(rulesRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException("[ERROR] Couldn't read SendSalesReportRequest.", e);
        }
    }

    @Override
    public void sendSalesResponseEvent(@Observes @SendSalesResponseEvent EventMessage message) {
        LOG.info("[INFO] Received SendSalesResponseEvent..");
        try {
            SendSalesResponseRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(message.getJmsMessage().getText(), SendSalesResponseRequest.class);
            messageService.sendSalesResponseRequest(rulesRequest);
        } catch (JMSException | SalesMarshallException e) {
            throw new RulesServiceTechnicalException("[ERROR] Couldn't read SendSalesResponseRequest.", e);
        }
    }

    @Override
    public void getValidationResultsByRawGuid(@Observes @GetValidationResultsByRawGuid EventMessage message) {
        try {
            TextMessage jmsRequestMessage = message.getJmsMessage();
            GetValidationsByRawMsgGuidRequest rulesRequest = JAXBMarshaller.unmarshallTextMessage(jmsRequestMessage, SendSalesResponseRequest.class);
            String validationsForRawMessageGuid = messageService.getValidationsForRawMessageGuid(rulesRequest.getGuid(), rulesRequest.getType());
            rulesProducer.sendModuleResponseMessage(jmsRequestMessage, validationsForRawMessageGuid);
        } catch (RulesModelMarshallException | MessageException e) {
            log.error("[ERROR] Error while trying to send Response to a GetValidationResultsByRawGuid to the Requestor Module..", e);
        }
    }

    @SuppressWarnings("unused")
	private void sendAuditMessage(AuditObjectTypeEnum type, AuditOperationEnum operation, String affectedObject, String comment, String username) {
        try {
            String message = AuditLogMapper.mapToAuditLog(type.getValue(), operation.getValue(), affectedObject, comment, username);
            rulesProducer.sendDataSourceMessage(message, DataSourceQueue.AUDIT);
        }
        catch (AuditModelMarshallException | MessageException e) {
            LOG.error("[ERROR] Error when sending message to Audit {}", e.getMessage());
        }
    }

}