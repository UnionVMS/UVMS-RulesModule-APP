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

import eu.europa.ec.fisheries.schema.config.module.v1.SettingsListResponse;
import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefTypeType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.SetReportMovementType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.*;
import eu.europa.ec.fisheries.schema.movement.module.v1.CreateMovementResponse;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.AvailabilityType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscritionOperationType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.*;
import eu.europa.ec.fisheries.schema.rules.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelValidationException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalUnmarshallException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementDuplicateException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementFaultException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.bean.RulesDomainModelBean;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.business.*;
import eu.europa.ec.fisheries.uvms.rules.service.event.*;
import eu.europa.ec.fisheries.uvms.rules.service.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.*;
import eu.europa.ec.fisheries.uvms.user.model.mapper.UserModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.*;
import eu.europa.ec.fisheries.wsdl.user.module.GetContactDetailResponse;
import eu.europa.ec.fisheries.wsdl.user.module.GetUserContextResponse;
import eu.europa.ec.fisheries.wsdl.user.types.Feature;
import eu.europa.ec.fisheries.wsdl.user.types.UserContext;
import eu.europa.ec.fisheries.wsdl.user.types.UserContextId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;


@Stateless
public class RulesServiceBean implements RulesService {

    static final double VICINITY_RADIUS = 0.05;
    static final long TWENTYFOUR_HOURS_IN_MILLISEC = 86400000;
    private final static Logger LOG = LoggerFactory.getLogger(RulesServiceBean.class);

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;
    @EJB
    RulesValidator rulesValidator;
    @EJB
    ValidationService validationService;

    @Inject
    @AlarmReportEvent
    private Event<NotificationMessage> alarmReportEvent;
    @Inject
    @TicketEvent
    private Event<NotificationMessage> ticketEvent;
    @Inject
    @TicketUpdateEvent
    private Event<NotificationMessage> ticketUpdateEvent;
    @Inject
    @AlarmReportCountEvent
    private Event<NotificationMessage> alarmReportCountEvent;
    @Inject
    @TicketCountEvent
    private Event<NotificationMessage> ticketCountEvent;
    @EJB
    private RulesDomainModelBean rulesDomainModel;

    private String getOrganisationName(String userName) throws eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException, MessageException, RulesModelMarshallException {
        String userRequest = UserModuleRequestMapper.mapToGetContactDetailsRequest(userName);
        String userMessageId = producer.sendDataSourceMessage(userRequest, DataSourceQueue.USER);
        TextMessage userMessage = consumer.getMessage(userMessageId, TextMessage.class);
        GetContactDetailResponse userResponse = JAXBMarshaller.unmarshallTextMessage(userMessage, GetContactDetailResponse.class);

        if (userResponse != null && userResponse.getContactDetails() != null) {
            return userResponse.getContactDetails().getOrganisationName();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param customRule
     * @throws RulesServiceException
     * @throws RulesFaultException
     */
    @Override
    public CustomRuleType createCustomRule(CustomRuleType customRule, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException {
        LOG.info("Create invoked in service layer");
        try {
            // Get organisation of user
            String organisationName = getOrganisationName(customRule.getUpdatedBy());
            if (organisationName != null) {
                customRule.setOrganisation(organisationName);
            } else {
                LOG.warn("User {} is not connected to any organisation!", customRule.getUpdatedBy());
            }
            if (customRule.getAvailability().equals(AvailabilityType.GLOBAL)) {
                UserContext userContext = getFullUserContext(customRule.getUpdatedBy(), applicationName);
                if (!hasFeature(userContext, featureName)) {
                    throw new AccessDeniedException("Forbidden access");
                }
            }
            CustomRuleType createdRule = rulesDomainModel.createCustomRule(customRule);
            // TODO: Rewrite so rules are loaded when changed
            rulesValidator.updateCustomRules();
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.CREATE, createdRule.getGuid(), null, customRule.getUpdatedBy());
            return createdRule;

        } catch (RulesModelMapperException | MessageException e) {
            throw new RulesServiceException(e.getMessage());
        } catch (eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
            throw new RulesServiceException(e.getMessage());
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param guid
     * @return
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType getCustomRuleByGuid(String guid) throws RulesServiceException, RulesModelMapperException, RulesFaultException {
        LOG.info("Get Custom Rule by guid invoked in service layer");
        try {
            CustomRuleType customRule = rulesDomainModel.getByGuid(guid);
            return customRule;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param oldCustomRule
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType updateCustomRule(CustomRuleType oldCustomRule, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException {
        LOG.info("Update custom rule invoked in service layer");
        try {
            // Get organisation of user
            String organisationName = getOrganisationName(oldCustomRule.getUpdatedBy());
            if (organisationName != null) {
                oldCustomRule.setOrganisation(organisationName);
            } else {
                LOG.warn("User {} is not connected to any organisation!", oldCustomRule.getUpdatedBy());
            }

            if (oldCustomRule.getAvailability().equals(AvailabilityType.GLOBAL)) {
                UserContext userContext = getFullUserContext(oldCustomRule.getUpdatedBy(), applicationName);
                if (!hasFeature(userContext, featureName)) {
                    throw new AccessDeniedException("Forbidden access");
                }
            }

            CustomRuleType customRule = rulesDomainModel.updateCustomRule(oldCustomRule);
            rulesValidator.updateCustomRules();
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.UPDATE, customRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            return customRule;
        } catch (RulesModelMapperException | MessageException | eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
            throw new RulesServiceException(e.getMessage());
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param oldCustomRule
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType updateCustomRule(CustomRuleType oldCustomRule) throws RulesServiceException, RulesFaultException {
        LOG.info("Update custom rule invoked in service layer by timer");
        try {
            CustomRuleType updatedCustomRule = rulesDomainModel.updateCustomRule(oldCustomRule);
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.UPDATE, updatedCustomRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            return updatedCustomRule;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param updateSubscriptionType
     */
    @Override
    public CustomRuleType updateSubscription(UpdateSubscriptionType updateSubscriptionType, String username) throws RulesServiceException, RulesFaultException {
        LOG.info("Update subscription invoked in service layer");
        try {
            boolean validRequest = updateSubscriptionType.getSubscription().getType() != null && updateSubscriptionType.getSubscription().getOwner() != null;
            if (!validRequest) {
                throw new RulesServiceException("Not a valid subscription!");
            }

            CustomRuleType updateCustomRule = rulesDomainModel.updateCustomRuleSubscription(updateSubscriptionType);

            if (SubscritionOperationType.ADD.equals(updateSubscriptionType.getOperation())) {
                // TODO: Don't log rule guid, log subscription guid?
                sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_SUBSCRIPTION, AuditOperationEnum.CREATE, updateSubscriptionType.getRuleGuid(), updateSubscriptionType.getSubscription().getOwner() + "/" + updateSubscriptionType.getSubscription().getType(), username);
            } else if (SubscritionOperationType.REMOVE.equals(updateSubscriptionType.getOperation())) {
                // TODO: Don't log rule guid, log subscription guid?
                sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_SUBSCRIPTION, AuditOperationEnum.DELETE, updateSubscriptionType.getRuleGuid(), updateSubscriptionType.getSubscription().getOwner() + "/" + updateSubscriptionType.getSubscription().getType(), username);
            }
            return updateCustomRule;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param guid
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType deleteCustomRule(String guid, String username, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException {
        LOG.info("Deleting custom rule by guid: {}.", guid);
        if (guid == null) {
            throw new InputArgumentException("No custom rule to remove");
        }

        try {
            CustomRuleType customRuleFromDb = getCustomRuleByGuid(guid);
            if (customRuleFromDb.getAvailability().equals(AvailabilityType.GLOBAL)) {
                UserContext userContext = getFullUserContext(username, applicationName);
                if (!hasFeature(userContext, featureName)) {
                    throw new AccessDeniedException("Forbidden access");
                }
            }

            CustomRuleType deletedRule = rulesDomainModel.deleteCustomRule(guid);
            rulesValidator.updateCustomRules();
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.DELETE, deletedRule.getGuid(), null, username);
            return deletedRule;
        } catch (RulesModelMapperException e) {
            throw new RulesServiceException(e.getMessage());
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws RulesServiceException
     */
    @Override
    public GetAlarmListByQueryResponse getAlarmList(AlarmQuery query) throws RulesServiceException, RulesFaultException {
        LOG.info("Get alarm list invoked in service layer");
        try {
            AlarmListResponseDto alarmList = rulesDomainModel.getAlarmListByQuery(query);
            GetAlarmListByQueryResponse response = new GetAlarmListByQueryResponse();
            response.getAlarms().addAll(alarmList.getAlarmList());
            response.setTotalNumberOfPages(alarmList.getTotalNumberOfPages());
            response.setCurrentPage(alarmList.getCurrentPage());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public GetTicketListByQueryResponse getTicketList(String loggedInUser, TicketQuery query) throws RulesServiceException, RulesFaultException {
        LOG.info("Get ticket list invoked in service layer");
        try {
            TicketListResponseDto ticketList = rulesDomainModel.getTicketListByQuery(loggedInUser, query);
            GetTicketListByQueryResponse response = new GetTicketListByQueryResponse();
            response.setCurrentPage(ticketList.getCurrentPage());
            response.setTotalNumberOfPages(ticketList.getTotalNumberOfPages());
            response.getTickets().addAll(ticketList.getTicketList());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public GetTicketListByMovementsResponse getTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException {
        LOG.info("Get tickets by movements invoked in service layer");
        try {
            TicketListResponseDto ticketListByMovements = rulesDomainModel.getTicketListByMovements(movements);
            GetTicketListByMovementsResponse response = new GetTicketListByMovementsResponse();
            response.getTickets().addAll(ticketListByMovements.getTicketList());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public GetTicketsAndRulesByMovementsResponse getTicketsAndRulesByMovements(List<String> movements) throws RulesServiceException {
        LOG.info("Get tickets and rules by movements invoked in service layer");
        try {
            List<TicketAndRuleType> ticketsAndRulesByMovements = rulesDomainModel.getTicketsAndRulesByMovements(movements);
            GetTicketsAndRulesByMovementsResponse response = new GetTicketsAndRulesByMovementsResponse();
            response.getTicketsAndRules().addAll(ticketsAndRulesByMovements);
            return response;
        } catch (RulesFaultException ex) {
            throw new RulesServiceException(ex.getMessage());
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public long countTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException {
        LOG.info("Get number of tickets by movements invoked in service layer");
        try {
            long countTicketListByMovements = rulesDomainModel.countTicketListByMovements(movements);
            return countTicketListByMovements;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException, RulesFaultException {
        LOG.info("Update ticket status invoked in service layer");
        try {
            TicketType updatedTicket = rulesDomainModel.setTicketStatus(ticket);
            // Notify long-polling clients of the update
            ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), ticket.getComment(), ticket.getUpdatedBy());
            return updatedTicket;


        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public List<TicketType> updateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesServiceException, RulesFaultException {
        LOG.info("Update all ticket status invoked in service layer");
        try {
            List<TicketType> updatedTickets = rulesDomainModel.updateTicketStatusByQuery(loggedInUser, query, status);
            // Notify long-polling clients of the update
            for (TicketType updatedTicket : updatedTickets) {
                ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
                sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), null, loggedInUser);
            }
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
            return updatedTickets;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public long getNumberOfAssetsNotSending() throws RulesServiceException, RulesFaultException {
        try {
            long numberOfAssetsNotSending = rulesDomainModel.getNumberOfAssetsNotSending();
            return numberOfAssetsNotSending;
        } catch (RulesModelException e) {
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
    }

    @Override
    public AlarmReportType updateAlarmStatus(AlarmReportType alarm) throws RulesServiceException, RulesFaultException {
        LOG.info("Update alarm status invoked in service layer");
        try {
            AlarmReportType updatedAlarm = rulesDomainModel.setAlarmStatus(alarm);
            // Notify long-polling clients of the change
            alarmReportEvent.fire(new NotificationMessage("guid", updatedAlarm.getGuid()));
            // Notify long-polling clients of the change (no vlaue since FE will need to fetch it)
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", null));
            sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.UPDATE, updatedAlarm.getGuid(), null, alarm.getUpdatedBy());
            return updatedAlarm;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by RulesTimerBean
    @Override
    public List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException, RulesFaultException {
        LOG.info("Get previous movement reports invoked in service layer");
        try {
            List<PreviousReportType> previousReports = rulesDomainModel.getPreviousReports();
            return previousReports;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by timer rule
    @Override
    public void timerRuleTriggered(String ruleName, PreviousReportFact fact) throws RulesServiceException, RulesFaultException {
        LOG.info("Timer rule triggered invoked in service layer");
        try {
            // Check if ticket already is created for this asset
            TicketType ticket = rulesDomainModel.getTicketByAssetGuid(fact.getAssetGuid(), ruleName);
            if (ticket == null) {
                createAssetNotSendingTicket(ruleName, fact);
            } else if (ticket.getTicketCount() != null) {
                ticket.setTicketCount(ticket.getTicketCount() + 1);
                updateTicketCount(ticket);
            } else {
                ticket.setTicketCount(2L);
                updateTicketCount(ticket);
            }
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    private void createAssetNotSendingTicket(String ruleName, PreviousReportFact fact) throws RulesModelException {
        TicketType ticket = new TicketType();

        ticket.setAssetGuid(fact.getAssetGuid());
        ticket.setOpenDate(RulesUtil.dateToString(new Date()));
        ticket.setRuleName(ruleName);
        ticket.setRuleGuid(ruleName);
        ticket.setUpdatedBy("UVMS");
        ticket.setStatus(TicketStatusType.OPEN);
        ticket.setMovementGuid(fact.getMovementGuid());
        ticket.setGuid(UUID.randomUUID().toString());
        TicketType createdTicket = rulesDomainModel.createTicket(ticket);
        sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.CREATE, createdTicket.getGuid(), null, ticket.getUpdatedBy());
        // Notify long-polling clients of the change
        ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
    }

    @Override
    public TicketType updateTicketCount(TicketType ticket) throws RulesServiceException {
        LOG.info("Update ticket count invoked in service layer");
        try {
            TicketType updatedTicket = rulesDomainModel.updateTicketCount(ticket);
            // Notify long-polling clients of the update
            ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), null, ticket.getUpdatedBy());
            return updatedTicket;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesServiceException, RulesFaultException {
        try {
            AlarmReportType alarmReport = rulesDomainModel.getAlarmReportByGuid(guid);
            return alarmReport;
        } catch (RulesModelException e) {
            throw new RulesServiceException("[ Error when getting alarm by GUID. ]");
        }
    }

    @Override
    public TicketType getTicketByGuid(String guid) throws RulesServiceException, RulesFaultException {
        try {
            TicketType ticket = rulesDomainModel.getTicketByGuid(guid);
            return ticket;
        } catch (RulesModelException e) {
            throw new RulesServiceException("[ Error when getting ticket by GUID. ]");
        }
    }

    @Override
    public String reprocessAlarm(List<String> alarmGuids, String username) throws RulesServiceException {
        LOG.info("Reprocess alarms invoked in service layer");
        try {
            AlarmQuery query = mapToOpenAlarmQuery(alarmGuids);
            AlarmListResponseDto alarms = rulesDomainModel.getAlarmListByQuery(query);

            for (AlarmReportType alarm : alarms.getAlarmList()) {
                // Cannot reprocess without a movement (i.e. "Asset not sending" alarm)
                if (alarm.getRawMovement() == null) {
                    continue;
                }

                // Mark the alarm as REPROCESSED before reprocessing. That will create a new alarm (if still wrong) with the items remaining.
                alarm.setStatus(AlarmStatusType.REPROCESSED);
                alarm = updateAlarmStatus(alarm);
                sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.UPDATE, alarm.getGuid(), null, username);
                RawMovementType rawMovementType = alarm.getRawMovement();
                // TODO: Use better type (some variation of PluginType...)
                String pluginType = alarm.getPluginType();
                setMovementReportReceived(rawMovementType, pluginType, username);
            }
//            return RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId);
            // TODO: Better
            return "OK";
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    private AlarmQuery mapToOpenAlarmQuery(List<String> alarmGuids) {
        AlarmQuery query = new AlarmQuery();
        ListPagination pagination = new ListPagination();
        pagination.setListSize(alarmGuids.size());
        pagination.setPage(1);
        query.setPagination(pagination);

        for (String alarmGuid : alarmGuids) {
            AlarmListCriteria criteria = new AlarmListCriteria();
            criteria.setKey(AlarmSearchKey.ALARM_GUID);
            criteria.setValue(alarmGuid);
            query.getAlarmSearchCriteria().add(criteria);
        }

        // We only want open alarms
        AlarmListCriteria openCrit = new AlarmListCriteria();
        openCrit.setKey(AlarmSearchKey.STATUS);
        openCrit.setValue(AlarmStatusType.OPEN.name());
        query.getAlarmSearchCriteria().add(openCrit);
        query.setDynamic(true);
        return query;
    }

    @Override
    public void setMovementReportReceived(final RawMovementType rawMovement, String pluginType, String username) throws RulesServiceException {
        try {
            Date auditTimestamp = new Date();
            Date auditTotalTimestamp = new Date();

            Asset asset = null;

            // Get Mobile Terminal if it exists
            MobileTerminalType mobileTerminal = getMobileTerminalByRawMovement(rawMovement);
            auditTimestamp = auditLog("Time to fetch from Mobile Terminal Module:", auditTimestamp);

            // Get Asset
            if (mobileTerminal != null) {
                String connectId = mobileTerminal.getConnectId();
                if (connectId != null) {
                    asset = getAssetByConnectId(connectId);
                }
            } else {
                asset = getAssetByCfrIrcs(rawMovement.getAssetId());
                if (isPluginTypeWithoutMobileTerminal(rawMovement.getPluginType()) && asset != null) {
                    mobileTerminal = findMobileTerminalByAsset(asset.getAssetId().getGuid());
                    rawMovement.setMobileTerminal(MobileTerminalMapper.mapMobileTerminal(mobileTerminal));
                }
            }
            if (rawMovement.getAssetId() == null && asset != null) {
                AssetId assetId = AssetAssetIdMapper.mapAssetToAssetId(asset);
                rawMovement.setAssetId(assetId);
            }
            auditTimestamp = auditLog("Time to fetch from Asset Module:", auditTimestamp);

            RawMovementFact rawMovementFact = RawMovementFactMapper.mapRawMovementFact(rawMovement, mobileTerminal, asset, pluginType);
            LOG.debug("rawMovementFact:{}", rawMovementFact);

            rulesValidator.evaluate(rawMovementFact);
            auditTimestamp = auditLog("Time to validate sanity:", auditTimestamp);

            if (rawMovementFact.isOk()) {
                MovementFact movementFact = collectMovementData(mobileTerminal, asset, rawMovement, username);

                LOG.info("Validating movement from Movement Module");
                rulesValidator.evaluate(movementFact);

                auditLog("Rules total time:", auditTotalTimestamp);

                // Tell Exchange that a movement was persisted in Movement
                sendBackToExchange(movementFact.getMovementGuid(), rawMovement, MovementRefTypeType.MOVEMENT, username);
            } else {
                // Tell Exchange that the report caused an alarm
                sendBackToExchange(null, rawMovement, MovementRefTypeType.ALARM, username);
            }
        } catch (MessageException | MobileTerminalModelMapperException | MobileTerminalUnmarshallException | JMSException | AssetModelMapperException | RulesModelMapperException | InterruptedException | ExecutionException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    private boolean isPluginTypeWithoutMobileTerminal(String pluginType) {
        if (pluginType == null) {
            return true;
        }
        try {
            PluginType type = PluginType.valueOf(pluginType);
            switch (type) {
                case MANUAL:
                case NAF:
                case OTHER:
                    return true;
                default:
                    return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private MovementFact collectMovementData(MobileTerminalType mobileTerminal, Asset asset, final RawMovementType rawMovement, final String username) throws MessageException, RulesModelMapperException, ExecutionException, InterruptedException, RulesServiceException {
        int threadNum = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        Integer numberOfReportsLast24Hours = null;
        final String assetGuid;
        final String assetFlagState;
        if (asset != null && asset.getAssetId() != null) {
            assetGuid = asset.getAssetId().getGuid();
            assetFlagState = asset.getCountryCode();
            LOG.warn("[ Asset was null for {} ]", rawMovement.getAssetId());
        } else {
            assetGuid = null;
            assetFlagState = null;
        }

        final Date positionTime = rawMovement.getPositionTime();

        FutureTask<Long> timeDiffAndPersistMovementTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() {
                return timeDiffAndPersistMovement(rawMovement.getSource(), assetGuid, assetFlagState, positionTime);
            }
        });
        executor.execute(timeDiffAndPersistMovementTask);

        FutureTask<Integer> numberOfReportsLast24HoursTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() {
                return numberOfReportsLast24Hours(assetGuid, positionTime);
            }
        });
        executor.execute(numberOfReportsLast24HoursTask);

        FutureTask<MovementType> sendToMovementTask = new FutureTask<>(new Callable<MovementType>() {
            @Override
            public MovementType call() {
                return sendToMovement(assetGuid, rawMovement, username);
            }
        });
        executor.execute(sendToMovementTask);

        FutureTask<List<AssetGroup>> assetGroupTask = new FutureTask<>(new Callable<List<AssetGroup>>() {
            @Override
            public List<AssetGroup> call() {
                return getAssetGroup(assetGuid);
            }
        });
        executor.execute(assetGroupTask);

        FutureTask<List<String>> vicinityOfTask = new FutureTask<>(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return getVicinityOf(rawMovement);
            }
        });
        executor.execute(vicinityOfTask);

        // Get channel guid
        String channelGuid = "";
        if (mobileTerminal != null) {
            channelGuid = getChannelGuid(mobileTerminal, rawMovement);
        }

        // Get channel type
        String comChannelType = null;
        if (rawMovement.getComChannelType() != null) {
            comChannelType = rawMovement.getComChannelType().name();
        }

        // Get data from parallel tasks
        try {
            Date auditParallelTimestamp = new Date();
            Long timeDiffInSeconds = timeDiffAndPersistMovementTask.get();
            List<AssetGroup> assetGroups = assetGroupTask.get();
            numberOfReportsLast24Hours = numberOfReportsLast24HoursTask.get();
            MovementType createdMovement = sendToMovementTask.get();
            List<String> vicinityOf = vicinityOfTask.get();
            auditLog("Total time for parallel tasks:", auditParallelTimestamp);

            MovementFact movementFact = MovementFactMapper.mapMovementFact(createdMovement, mobileTerminal, asset, comChannelType, assetGroups, timeDiffInSeconds, numberOfReportsLast24Hours, channelGuid, vicinityOf);
            LOG.debug("movementFact:{}", movementFact);

            executor.shutdown();
            return movementFact;
        } catch (RulesServiceException | NullPointerException e) {
            executor.shutdown();
            throw new RulesServiceException("Error likely caused by a duplicate movement.", e);
        }
    }

    private Long timeDiffAndPersistMovement(MovementSourceType movementSource, String assetGuid, String assetFlagState, Date positionTime) {
        Date auditTimestamp = new Date();

        // This needs to be done before persisting last report
        Long timeDiffInSeconds = null;
        Long timeDiff = timeDiffFromLastCommunication(assetGuid, positionTime);
        timeDiffInSeconds = timeDiff != null ? timeDiff / 1000 : null;
        auditTimestamp = auditLog("Time to fetch time difference to previous report:", auditTimestamp);

        // We only persist our own last communications that were not from AIS.
        if (isLocalFlagstate(assetFlagState) && !movementSource.equals(MovementSourceType.AIS)) {
            persistLastCommunication(assetGuid, positionTime);
        }
        auditLog("Time to persist the position time:", auditTimestamp);

        return timeDiffInSeconds;
    }

    private boolean isLocalFlagstate(String assetFlagState) {
        if (assetFlagState == null) {
            return false;
        }
        TextMessage response;
        try {
            String settingsRequest = ModuleRequestMapper.toListSettingsRequest("asset");
            String messageId = producer.sendDataSourceMessage(settingsRequest, DataSourceQueue.CONFIG);
            response = consumer.getMessage(messageId, TextMessage.class);
            SettingsListResponse settings = eu.europa.ec.fisheries.uvms.config.model.mapper.JAXBMarshaller.unmarshallTextMessage(response, SettingsListResponse.class);
            for (SettingType setting : settings.getSettings()) {
                if (setting.getKey().equals("asset.default.flagstate")) {
                    return assetFlagState.equalsIgnoreCase(setting.getValue());
                }
            }
        } catch (eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException | MessageException e) {
            return false;
        }
        return false;
    }

    private Long timeDiffFromLastCommunication(String assetGuid, Date thisTime) {
        LOG.info("Fetching time difference to previous movement report");
        Long timeDiff = null;
        try {
            PreviousReportType previousReport = rulesDomainModel.getPreviousReportByAssetGuid(assetGuid);
            Date previousTime = previousReport.getPositionTime();
            timeDiff = thisTime.getTime() - previousTime.getTime();
        } catch (Exception e) {
            // If something goes wrong, continue with the other validation
            LOG.warn("[ Error when fetching time difference of previous movement reports ]");
        }
        return timeDiff;
    }

    private void persistLastCommunication(String assetGuid, Date positionTime) {
        PreviousReportType thisReport = new PreviousReportType();
        thisReport.setPositionTime(positionTime);
        thisReport.setAssetGuid(assetGuid);
        String upsertPreviousReportequest = null;
        try {
            rulesDomainModel.upsertPreviousReport(thisReport);
        } catch (RulesModelException e) {
            LOG.error("[ Error persisting report. ] {}", e.getMessage());
        }
    }

    private Integer numberOfReportsLast24Hours(String assetGuid, Date thisTime) {
        LOG.info("Fetching number of reports last 24 hours");
        Date auditTimestamp = new Date();
        Integer numberOfMovements = null;
        MovementQuery query = new MovementQuery();

        // Range
        RangeCriteria dateRangeCriteria = new RangeCriteria();
        dateRangeCriteria.setKey(RangeKeyType.DATE);
        Date twentyFourHoursAgo = new Date(thisTime.getTime() - TWENTYFOUR_HOURS_IN_MILLISEC);
        dateRangeCriteria.setFrom(twentyFourHoursAgo.toString());
        //String to = RulesUtil.xmlGregorianToString(thisTime);
        dateRangeCriteria.setTo(thisTime.toString());
        query.getMovementRangeSearchCriteria().add(dateRangeCriteria);

        // Id
        eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria idCriteria = new eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria();
        idCriteria.setKey(eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey.CONNECT_ID);
        idCriteria.setValue(assetGuid);
        query.getMovementSearchCriteria().add(idCriteria);

        try {
            String request = MovementModuleRequestMapper.mapToGetMovementMapByQueryRequest(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.MOVEMENT);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            List<MovementMapResponseType> result = MovementModuleResponseMapper.mapToMovementMapResponse(response);

            List<MovementType> movements;

            if (result == null || result.isEmpty()) {
                LOG.warn("[ Error when fetching sum of previous movement reports: No result found");
                return null;
            } else if (result.size() != 1) {
                LOG.warn("[ Error when fetching sum of previous movement reports: Duplicate assets found ({})", result.size());
                return null;
            } else if (!assetGuid.equals(result.get(0).getKey())) {
                LOG.warn("[ Error when fetching sum of previous movement reports: Wrong asset found ({})", result.get(0).getKey());
                return null;
            } else {
                movements = result.get(0).getMovements();
            }

            numberOfMovements = movements != null ? movements.size() : 0;
        } catch (Exception e) {
            // If something goes wrong, continue with the other validation
            LOG.warn("[ Error when fetching sum of previous movement reports:{} ]", e.getMessage());
        }

        auditLog("Time to fetch number of reports last 24 hours:", auditTimestamp);

        return numberOfMovements;
    }

    private MovementType sendToMovement(String assetGuid, RawMovementType rawMovement, String username) {
        LOG.info("Send the validated raw position to Movement");

        Date auditTimestamp = new Date();

        MovementType createdMovement = null;
        try {
            MovementBaseType movementBaseType = MovementBaseTypeMapper.mapRawMovementFact(rawMovement);
            movementBaseType.setConnectId(assetGuid);
            String createMovementRequest = MovementModuleRequestMapper.mapToCreateMovementRequest(movementBaseType, username);
            String messageId = producer.sendDataSourceMessage(createMovementRequest, DataSourceQueue.MOVEMENT);
            TextMessage movementResponse = consumer.getMessage(messageId, TextMessage.class);

            CreateMovementResponse createMovementResponse = MovementModuleResponseMapper.mapToCreateMovementResponseFromMovementResponse(movementResponse);
            createdMovement = createMovementResponse.getMovement();
        } catch (JMSException | MovementFaultException | ModelMapperException | MessageException e) {
            LOG.error("[ Error when getting movement from Movement , movementResponse from JMS Queue is null ]");
        } catch (MovementDuplicateException e) {
            LOG.error("[ Error when getting movement from Movement, tried to create duplicate movement ]");
        }

        auditLog("Time to get movement from Movement Module:", auditTimestamp);

        return createdMovement;
    }

    private List<String> getVicinityOf(RawMovementType rawMovement) {
        long start = System.currentTimeMillis();
        List<String> vicinityOf = new ArrayList<>();
        /*
        try {
            MovementQuery query = new MovementQuery();
            query.setExcludeFirstAndLastSegment(true);

            RangeCriteria time = new RangeCriteria();
            //GregorianCalendar from = rawMovement.getPositionTime().toGregorianCalendar();
            //from.add(Calendar.HOUR_OF_DAY, -1);
            Date fromDate = new Date(rawMovement.getPositionTime().getTime() - TWENTYFOUR_HOURS_IN_MILLISEC);
            time.setKey(RangeKeyType.DATE);
            time.setFrom(fromDate.toString());
            time.setTo(rawMovement.getPositionTime().toString());
            query.getMovementRangeSearchCriteria().add(time);

            eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination pagination = new eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination();
            pagination.setListSize(BigInteger.valueOf(1000L));
            pagination.setPage(BigInteger.ONE);
            query.setPagination(pagination);

            String request = MovementModuleRequestMapper.mapToGetMovementListByQueryRequest(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.MOVEMENT);
            TextMessage movementResponse = consumer.getMessage(messageId, TextMessage.class);
            List<MovementType> movements = MovementModuleResponseMapper.mapToMovementListResponse(movementResponse);
            double centerX = rawMovement.getPosition().getLongitude();
            double centerY = rawMovement.getPosition().getLatitude();
            List<String> guidList = new ArrayList<>();
            for (MovementType movement : movements) {
                if (guidList.contains(movement.getConnectId())) {
                    continue;
                }
                double x = movement.getPosition().getLongitude();
                double y = movement.getPosition().getLatitude();
                double distance = Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
                if (distance < VICINITY_RADIUS) {
                    guidList.add(movement.getConnectId());
                    Asset asset = getAssetByConnectId(movement.getConnectId());
                    vicinityOf.add(asset.getIrcs());
                }
                //(x - center_x)^2 + (y - center_y)^2 < radius^2
            }
        } catch (AssetModelMapperException | JMSException | MessageException | ModelMapperException | MovementFaultException | MovementDuplicateException e) {
            LOG.warn("Could not fetch movements for vicinity of.");
        }

        LOG.debug("[ Get nearby vessels: {} ms ]", (System.currentTimeMillis() - start));
        */
        return vicinityOf;
    }

    private List<AssetGroup> getAssetGroup(String assetGuid) {
        LOG.info("Fetch asset groups from Asset");

        Date auditTimestamp = new Date();

        TextMessage getAssetResponse = null;
        String getAssetMessageId = null;
        List<AssetGroup> assetGroups = null;
        try {
            String getAssetRequest = AssetModuleRequestMapper.createAssetGroupListByAssetGuidRequest(assetGuid);
            getAssetMessageId = producer.sendDataSourceMessage(getAssetRequest, DataSourceQueue.ASSET);
            getAssetResponse = consumer.getMessage(getAssetMessageId, TextMessage.class);

            assetGroups = AssetModuleResponseMapper.mapToAssetGroupListFromResponse(getAssetResponse, getAssetMessageId);
        } catch (AssetModelMapperException | MessageException e) {
            LOG.warn("[ Failed while fetching asset groups ]", e.getMessage());
        }

        auditLog("Time to get asset groups:", auditTimestamp);

        return assetGroups;
    }

    private void sendBackToExchange(String guid, RawMovementType rawMovement, MovementRefTypeType status, String username) throws RulesModelMarshallException, MessageException {
        LOG.info("Sending back processed movement to exchange");

        // Map response
        MovementRefType movementRef = new MovementRefType();
        movementRef.setMovementRefGuid(guid);
        movementRef.setType(status);
        movementRef.setAckResponseMessageID(rawMovement.getAckResponseMessageID());

        // Map movement
        SetReportMovementType setReportMovementType = ExchangeMovementMapper.mapExchangeMovement(rawMovement);

        try {
            String exchangeResponseText = ExchangeMovementMapper.mapToProcessedMovementResponse(setReportMovementType, movementRef, username);
//            String exchangeResponseText = eu.europa.ec.fisheries.uvms.exchange.model.mapper.JAXBMarshaller.marshallJaxBObjectToString(ExchangeModuleRequestMapper.mapToProcessedMovementResopnse(setReportMovementType, movementRef));
            producer.sendDataSourceMessage(exchangeResponseText, DataSourceQueue.EXCHANGE);
        } catch (ExchangeModelMapperException e) {
            e.printStackTrace();
        }
    }

    private Asset getAssetByConnectId(String connectId) throws AssetModelMapperException, MessageException {
        LOG.info("Fetch asset by connectId '{}'", connectId);

        AssetListQuery query = new AssetListQuery();
        AssetListCriteria criteria = new AssetListCriteria();
        AssetListCriteriaPair criteriaPair = new AssetListCriteriaPair();
        criteriaPair.setKey(ConfigSearchField.GUID);
        criteriaPair.setValue(connectId);
        criteria.getCriterias().add(criteriaPair);
        criteria.setIsDynamic(true);

        query.setAssetSearchCriteria(criteria);

        AssetListPagination pagination = new AssetListPagination();
        // To leave room to find erroneous results - it must be only one in the list
        pagination.setListSize(2);
        pagination.setPage(1);
        query.setPagination(pagination);

        String getAssetRequest = AssetModuleRequestMapper.createAssetListModuleRequest(query);
        String getAssetMessageId = producer.sendDataSourceMessage(getAssetRequest, DataSourceQueue.ASSET);
        TextMessage getAssetResponse = consumer.getMessage(getAssetMessageId, TextMessage.class);

        List<Asset> resultList = AssetModuleResponseMapper.mapToAssetListFromResponse(getAssetResponse, getAssetMessageId);

        return resultList.size() != 1 ? null : resultList.get(0);
    }

    private Asset getAssetByCfrIrcs(AssetId assetId) {
        LOG.info("Fetch asset by assetId");

        Asset asset = null;
        try {
            // If no asset information exists, don't look for one
            if (assetId == null || assetId.getAssetIdList() == null) {
                LOG.warn("No asset information exists!");
                return null;
            }

            List<AssetIdList> ids = assetId.getAssetIdList();

            String cfr = null;
            String ircs = null;
            String mmsi = null;

            // Get possible search parameters
            for (AssetIdList id : ids) {
                if (eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.CFR.equals(id.getIdType())) {
                    cfr = id.getValue();
                }
                if (eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.IRCS.equals(id.getIdType())) {
                    ircs = id.getValue();
                }
                if (eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.MMSI.equals(id.getIdType())) {
                    mmsi = id.getValue();
                }

            }

            if (ircs != null && cfr != null && mmsi != null) {
                try {
                    asset = getAsset(AssetIdType.CFR, cfr);
                    // If the asset matches on ircs as well we have a winner
                    if (asset != null && asset.getIrcs().equals(ircs)) {
                        return asset;
                    }
                    // If asset is null, try fetching by IRCS (cfr will fail for SE national db)
                    if (asset == null) {
                        asset = getAsset(AssetIdType.IRCS, ircs);
                        // If asset is still null, try mmsi (this should be the case for movement coming from AIS)
                        if (asset == null) {
                            return getAsset(AssetIdType.MMSI, mmsi);
                        }
                    }
                } catch (AssetModelValidationException e) {
                    return getAsset(AssetIdType.IRCS, ircs);
                }
            } else if (cfr != null) {
                return getAsset(AssetIdType.CFR, cfr);
            } else if (ircs != null) {
                return getAsset(AssetIdType.IRCS, ircs);
            } else if (mmsi != null) {
                return getAsset(AssetIdType.MMSI, mmsi);
            }

        } catch (Exception e) {
            // Log and continue validation
            LOG.warn("Could not find asset!");
        }
        return null;
    }

    private Asset getAsset(AssetIdType type, String value) throws AssetModelMapperException, MessageException {
        String getAssetListRequest = AssetModuleRequestMapper.createGetAssetModuleRequest(value, type);
        String getAssetMessageId = producer.sendDataSourceMessage(getAssetListRequest, DataSourceQueue.ASSET);
        TextMessage getAssetResponse = consumer.getMessage(getAssetMessageId, TextMessage.class);

        return AssetModuleResponseMapper.mapToAssetFromResponse(getAssetResponse, getAssetMessageId);
    }

    private MobileTerminalType getMobileTerminalByRawMovement(RawMovementType rawMovement) throws MessageException, MobileTerminalModelMapperException, MobileTerminalUnmarshallException, JMSException {
        LOG.info("Fetch mobile terminal");
        MobileTerminalListQuery query = new MobileTerminalListQuery();

        // If no mobile terminal information exists, don't look for one
        if (rawMovement.getMobileTerminal() == null || rawMovement.getMobileTerminal().getMobileTerminalIdList() == null) {
            return null;
        }

        List<IdList> ids = rawMovement.getMobileTerminal().getMobileTerminalIdList();

        MobileTerminalSearchCriteria criteria = new MobileTerminalSearchCriteria();
        for (IdList id : ids) {
            eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListCriteria crit = new eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListCriteria();
            switch (id.getType()) {
                case DNID:
                    if (id.getValue() != null) {
                        crit.setKey(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey.DNID);
                        crit.setValue(id.getValue());
                        criteria.getCriterias().add(crit);
                    }
                    break;
                case MEMBER_NUMBER:
                    if (id.getValue() != null) {
                        crit.setKey(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey.MEMBER_NUMBER);
                        crit.setValue(id.getValue());
                        criteria.getCriterias().add(crit);
                    }
                    break;
                case SERIAL_NUMBER:
                    if (id.getValue() != null) {
                        crit.setKey(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey.SERIAL_NUMBER);
                        crit.setValue(id.getValue());
                        criteria.getCriterias().add(crit);
                    }
                    break;
                case LES:
                default:
                    LOG.error("[ Unhandled Mobile Terminal id: {} ]", id.getType());
                    break;
            }
        }

        // If no valid criterias, don't look for a mobile terminal
        if (criteria.getCriterias().isEmpty()) {
            return null;
        }

        // If we know the transponder type from the source, use it in the search criteria
        eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListCriteria transponderTypeCrit = new eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListCriteria();
        transponderTypeCrit.setKey(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey.TRANSPONDER_TYPE);
        transponderTypeCrit.setValue(rawMovement.getSource().name());
        criteria.getCriterias().add(transponderTypeCrit);

        query.setMobileTerminalSearchCriteria(criteria);
        eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListPagination pagination = new eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListPagination();
        // To leave room to find erroneous results - it must be only one in the list
        pagination.setListSize(2);
        pagination.setPage(1);
        query.setPagination(pagination);

        String getMobileTerminalListRequest = MobileTerminalModuleRequestMapper.createMobileTerminalListRequest(query);
        String getMobileTerminalMessageId = producer.sendDataSourceMessage(getMobileTerminalListRequest, DataSourceQueue.MOBILE_TERMINAL);
        TextMessage getMobileTerminalResponse = consumer.getMessage(getMobileTerminalMessageId, TextMessage.class);

        List<MobileTerminalType> resultList = MobileTerminalModuleResponseMapper.mapToMobileTerminalListResponse(getMobileTerminalResponse);

        MobileTerminalType mobileTerminal = resultList.size() != 1 ? null : resultList.get(0);

        return mobileTerminal;
    }

    // TODO: Implement for IRIDIUM as well (if needed)
    private String getChannelGuid(MobileTerminalType mobileTerminal, RawMovementType rawMovement) {
        String dnid = "";
        String memberNumber = "";
        String channelGuid = "";

        List<IdList> ids = rawMovement.getMobileTerminal().getMobileTerminalIdList();

        for (IdList id : ids) {
            switch (id.getType()) {
                case DNID:
                    if (id.getValue() != null) {
                        dnid = id.getValue();
                    }
                    break;
                case MEMBER_NUMBER:
                    if (id.getValue() != null) {
                        memberNumber = id.getValue();
                    }
                    break;
                case SERIAL_NUMBER:
                    // IRIDIUM
                case LES:
                default:
                    LOG.error("[ Unhandled Mobile Terminal id: {} ]", id.getType());
                    break;
            }
        }

        // Get the channel guid
        boolean correctDnid = false;
        boolean correctMemberNumber = false;
        List<ComChannelType> channels = mobileTerminal.getChannels();
        for (ComChannelType channel : channels) {

            List<ComChannelAttribute> attributes = channel.getAttributes();

            for (ComChannelAttribute attribute : attributes) {
                String type = attribute.getType();
                String value = attribute.getValue();

                if ("DNID".equals(type)) {
                    correctDnid = value.equals(dnid);
                }
                if ("MEMBER_NUMBER".equals(type)) {
                    correctMemberNumber = value.equals(memberNumber);
                }
            }

            if (correctDnid && correctMemberNumber) {
                channelGuid = channel.getGuid();
            }
        }

        return channelGuid;
    }

    private MobileTerminalType findMobileTerminalByAsset(String assetGuid) throws MessageException, MobileTerminalModelMapperException, MobileTerminalUnmarshallException, JMSException {
        MobileTerminalListQuery query = new MobileTerminalListQuery();
        eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListPagination pagination = new eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListPagination();
        pagination.setListSize(2);
        pagination.setPage(1);
        MobileTerminalSearchCriteria criteria = new MobileTerminalSearchCriteria();
        ListCriteria guidCriteria = new ListCriteria();
        guidCriteria.setKey(SearchKey.CONNECT_ID);
        guidCriteria.setValue(assetGuid);
        criteria.getCriterias().add(guidCriteria);
        query.setMobileTerminalSearchCriteria(criteria);
        query.setPagination(pagination);

        String request = MobileTerminalModuleRequestMapper.createMobileTerminalListRequest(query);
        String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.MOBILE_TERMINAL);

        TextMessage getMobileTerminalResponse = consumer.getMessage(messageId, TextMessage.class);

        List<MobileTerminalType> resultList = MobileTerminalModuleResponseMapper.mapToMobileTerminalListResponse(getMobileTerminalResponse);

        MobileTerminalType mobileTerminal = null;

        for (MobileTerminalType mobileTerminalType : resultList) {
            if (mobileTerminalType.getConnectId() != null) {
                mobileTerminal = mobileTerminalType;
                break;
            }
        }

        return mobileTerminal;
    }

    private Date auditLog(String msg, Date lastTimestamp) {
        Date newTimestamp = new Date();
        long duration = newTimestamp.getTime() - lastTimestamp.getTime();
        LOG.info("--> AUDIT - {} {}ms", msg, duration);
        return newTimestamp;
    }

    private void sendAuditMessage(AuditObjectTypeEnum type, AuditOperationEnum operation, String affectedObject, String comment, String username) {
        try {
            String message = AuditLogMapper.mapToAuditLog(type.getValue(), operation.getValue(), affectedObject, comment, username);
            producer.sendDataSourceMessage(message, DataSourceQueue.AUDIT);
        } catch (AuditModelMarshallException | MessageException e) {
            LOG.error("[ Error when sending message to Audit. ] {}", e.getMessage());
        }
    }

    private UserContext getFullUserContext(String remoteUser, String applicationName) throws RulesServiceException, RulesModelMarshallException {
        LOG.debug("Request getFullUserContext({}, {})", remoteUser, applicationName);
        UserContext userContext = null;
        UserContextId contextId = new UserContextId();
        contextId.setApplicationName(applicationName);
        contextId.setUserName(remoteUser);
        String userRequest;
        try {
            userRequest = UserModuleRequestMapper.mapToGetUserContextRequest(contextId);
            String messageId = producer.sendDataSourceMessage(userRequest, DataSourceQueue.USER);
            LOG.debug("JMS message with ID: {} is sent to USM.", messageId);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response != null) {
                GetUserContextResponse userContextResponse = JAXBMarshaller.unmarshallTextMessage(response, GetUserContextResponse.class);
                LOG.debug("Response concerning message with ID: {} is received.", messageId);
                userContext = userContextResponse.getContext();
            } else {
                LOG.error("Error occurred while receiving JMS response for message ID: {}", messageId);
                throw new RulesServiceException("Unable to receive a response from USM.");
            }
        } catch (eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
            throw new RulesModelMarshallException("Unexpected exception while trying to get user context.", e);
        } catch (MessageException e) {
            LOG.error("Unable to receive a response from USM.");
            throw new RulesServiceException("Unable to receive a response from USM.");
        }
        return userContext;
    }

    private boolean hasFeature(UserContext userContext, String featureName) {
        for (eu.europa.ec.fisheries.wsdl.user.types.Context c : userContext.getContextSet().getContexts()) {
            for (Feature f : c.getRole().getFeature()) {
                if (featureName.equals(f.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
