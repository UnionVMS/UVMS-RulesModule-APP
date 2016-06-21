package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.module.v1.CreateMovementResponse;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefTypeType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.SetReportMovementType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.*;
import eu.europa.ec.fisheries.schema.movement.search.v1.*;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.*;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.*;
import eu.europa.ec.fisheries.schema.rules.search.v1.ListPagination;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelValidationException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementDuplicateException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementFaultException;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.AvailabilityType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscritionOperationType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.*;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketByAssetAndRuleResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalUnmarshallException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.business.*;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketUpdateEvent;
import eu.europa.ec.fisheries.uvms.rules.service.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.*;
import eu.europa.ec.fisheries.uvms.user.model.mapper.UserModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.*;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetIdType;
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
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.concurrent.*;

@Stateless
public class RulesServiceBean implements RulesService {

    private final static Logger LOG = LoggerFactory.getLogger(RulesServiceBean.class);

    static final double VICINITY_RADIUS = 0.05;

    @EJB
    ParameterService parameterService;

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;

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
    RulesValidator rulesValidator;

    @EJB
    ValidationService validationService;

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
     *
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
            if(customRule.getAvailability().equals(AvailabilityType.GLOBAL)){
                UserContext userContext = getFullUserContext(customRule.getUpdatedBy(), applicationName);
                if(!hasFeature(userContext, featureName)){
                    throw new AccessDeniedException("Forbidden access");
                }
            }

            String request = RulesDataSourceRequestMapper.mapCreateCustomRule(customRule);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // TODO: Rewrite so rules are loaded when changed
            rulesValidator.updateCustomRules();

            CustomRuleType customRuleType = RulesDataSourceResponseMapper.mapToCreateCustomRuleFromResponse(response, messageId);
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.CREATE, customRuleType.getGuid(), null, customRule.getUpdatedBy());
            return customRuleType;

        } catch (RulesModelMapperException | JMSException | MessageException  e) {
            throw new RulesServiceException(e.getMessage());
        } catch (eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
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
            String request = RulesDataSourceRequestMapper.mapGetCustomRule(guid);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.getCustomRuleResponse(response, messageId);
        } catch (MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        } catch (JMSException e) {
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

            if(oldCustomRule.getAvailability().equals(AvailabilityType.GLOBAL)){
                UserContext userContext = getFullUserContext(oldCustomRule.getUpdatedBy(), applicationName);
                if(!hasFeature(userContext, featureName)){
                    throw new AccessDeniedException("Forbidden access");
                }
            }

            String request = RulesDataSourceRequestMapper.mapUpdateCustomRule(oldCustomRule);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            rulesValidator.updateCustomRules();

            CustomRuleType newCustomRule = RulesDataSourceResponseMapper.mapToUpdateCustomRuleFromResponse(response, messageId);
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.DELETE, oldCustomRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.CREATE, newCustomRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            return newCustomRule;
        } catch (RulesModelMapperException | MessageException | JMSException | eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
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
            String request = RulesDataSourceRequestMapper.mapUpdateCustomRule(oldCustomRule);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            CustomRuleType newCustomRule = RulesDataSourceResponseMapper.mapToUpdateCustomRuleFromResponse(response, messageId);
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.DELETE, oldCustomRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.CREATE, newCustomRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            return newCustomRule;
        } catch (RulesModelMapperException | MessageException | JMSException e) {
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

            String request = RulesDataSourceRequestMapper.mapUpdateSubscription(updateSubscriptionType);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (SubscritionOperationType.ADD.equals(updateSubscriptionType.getOperation()))  {
                // TODO: Don't log rule guid, log subscription guid?
                sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_SUBSCRIPTION, AuditOperationEnum.CREATE, updateSubscriptionType.getRuleGuid(), updateSubscriptionType.getSubscription().getOwner() + "/" + updateSubscriptionType.getSubscription().getType(), username);
            } else if (SubscritionOperationType.REMOVE.equals(updateSubscriptionType.getOperation())) {
                // TODO: Don't log rule guid, log subscription guid?
                sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_SUBSCRIPTION, AuditOperationEnum.DELETE, updateSubscriptionType.getRuleGuid(), updateSubscriptionType.getSubscription().getOwner() + "/" + updateSubscriptionType.getSubscription().getType(), username);
            }

            return RulesDataSourceResponseMapper.mapToUpdateCustomRuleFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException e) {
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
            if(customRuleFromDb.getAvailability().equals(AvailabilityType.GLOBAL)){
                UserContext userContext = getFullUserContext(username, applicationName);
                if(!hasFeature(userContext, featureName)){
                    throw new AccessDeniedException("Forbidden access");
                }
            }
            String request = RulesDataSourceRequestMapper.mapDeleteCustomRule(guid, username);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            rulesValidator.updateCustomRules();

            CustomRuleType customRuleType = RulesDataSourceResponseMapper.mapToDeleteCustomRuleFromResponse(response, messageId);
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.DELETE, customRuleType.getGuid(), null, username);
            return customRuleType;
        } catch (RulesModelMapperException | MessageException | JMSException e) {
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
            String request = RulesDataSourceRequestMapper.mapAlarmList(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response == null) {
                LOG.error("[ Error when getting list, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }

            return RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException  ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public GetTicketListByQueryResponse getTicketList(String loggedInUser, TicketQuery query) throws RulesServiceException, RulesFaultException {
        LOG.info("Get ticket list invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapTicketList(loggedInUser, query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response == null) {
                LOG.error("[ Error when getting list, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }

            return RulesDataSourceResponseMapper.mapToTicketListFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public GetTicketListByMovementsResponse getTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException {
        LOG.info("Get tickets by movements invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapTicketsByMovements(movements);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response == null) {
                LOG.error("[ Error when getting tickets by movements, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting tickets by movements, response from JMS Queue is null ]");
            }

            return RulesDataSourceResponseMapper.mapToTicketsByMovementsFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public GetTicketsAndRulesByMovementsResponse getTicketsAndRulesByMovements(List<String> movements) throws RulesServiceException {
        LOG.info("Get tickets and rules by movements invoked in service layer");

        try {
            String request = RulesDataSourceRequestMapper.mapTicketsAndRulesByMovements(movements);

            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response == null) {
                LOG.error("[ Error when getting tickets and rules by movements, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting tickets and rules by movements, response from JMS Queue is null ]");
            }

            return RulesModuleResponseMapper.mapToGetTicketsAndRulesByMovementsFromResponse(response);
        } catch (RulesModelMapperException | MessageException | JMSException | RulesFaultException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public long countTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException {
        LOG.info("Get number of tickets by movements invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapCountTicketsByMovements(movements);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapToCountTicketsByMovementsFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException, RulesFaultException {
        LOG.info("Update ticket status invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateTicketStatus(ticket);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            TicketType updatedTicket = RulesDataSourceResponseMapper.mapToSetTicketStatusFromResponse(response, messageId);

            // Notify long-polling clients of the update
            ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));

            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));

            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), ticket.getComment(), ticket.getUpdatedBy());

            return updatedTicket;

        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public List<TicketType> updateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesServiceException, RulesFaultException {
        LOG.info("Update all ticket status invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateTicketStatusByQuery(loggedInUser, query, status);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            List<TicketType> updatedTickets = RulesDataSourceResponseMapper.mapToUpdateTicketStatusByQueryFromResponse(response, messageId);

            // Notify long-polling clients of the update
            for (TicketType updatedTicket : updatedTickets) {
                ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
                sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), null, loggedInUser);
            }

            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));

            return updatedTickets;

        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public long getNumberOfAssetsNotSending() throws RulesServiceException, RulesFaultException {
        try {
            String request = RulesDataSourceRequestMapper.getNumberOfAssetsNotSending();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapToGetNumberOfAssetsNotSendingFromResponse(response, messageId);
        } catch (MessageException | JMSException | RulesModelMapperException e) {
            LOG.error("[ Error when getting number of open tickets ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
    }

    @Override
    public AlarmReportType updateAlarmStatus(AlarmReportType alarm) throws RulesServiceException, RulesFaultException {
        LOG.info("Update alarm status invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateAlarmStatus(alarm);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            AlarmReportType updatedAlarm = RulesDataSourceResponseMapper.mapToSetAlarmStatusFromResponse(response, messageId);

            // Notify long-polling clients of the change
            alarmReportEvent.fire(new NotificationMessage("guid", updatedAlarm.getGuid()));

            // Notify long-polling clients of the change (no vlaue since FE will need to fetch it)
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", null));

            sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.UPDATE, updatedAlarm.getGuid(), null, alarm.getUpdatedBy());

            return updatedAlarm;
        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by RulesTimerBean
    @Override
    public List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException, RulesFaultException {
        LOG.info("Get previous movement reports invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapGetPreviousReports();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapToGetPreviousReportsResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by timer rule
    @Override
    public void timerRuleTriggered(String ruleName, PreviousReportFact fact) throws RulesServiceException, RulesFaultException {
        LOG.info("Timer rule triggered invoked in service layer");
        try {
            // Check if ticket already is created for this asset
            String getTicketRequest = RulesDataSourceRequestMapper.mapGetTicketByAssetAndRule(fact.getAssetGuid(), ruleName);
            String messageIdTicket = producer.sendDataSourceMessage(getTicketRequest, DataSourceQueue.INTERNAL);
            TextMessage ticketResponse = consumer.getMessage(messageIdTicket, TextMessage.class);
            GetTicketByAssetAndRuleResponse ticketByAssetAndRuleResponse = RulesDataSourceResponseMapper.mapToGetTicketByAssetGuidFromResponse(ticketResponse, messageIdTicket);
            TicketType ticket = ticketByAssetAndRuleResponse.getTicket();

            if (ticket == null) {
                createAssetNotSendingTicket(ruleName, fact);
            } else if (ticket.getTicketCount() != null){
                ticket.setTicketCount(ticket.getTicketCount() + 1);
                updateTicketCount(ticket);
            } else {
                ticket.setTicketCount(2L);
                updateTicketCount(ticket);
            }
        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    private void createAssetNotSendingTicket(String ruleName, PreviousReportFact fact) throws RulesModelMapperException, MessageException, RulesFaultException, RulesServiceException, JMSException {
        TicketType ticket = new TicketType();

        ticket.setAssetGuid(fact.getAssetGuid());
        ticket.setOpenDate(RulesUtil.dateToString(new Date()));
        ticket.setRuleName(ruleName);
        ticket.setRuleGuid(ruleName);
        ticket.setUpdatedBy("UVMS");
        ticket.setStatus(TicketStatusType.OPEN);
        ticket.setMovementGuid(fact.getMovementGuid());
        ticket.setGuid(UUID.randomUUID().toString());

        String createTicketRequest = RulesDataSourceRequestMapper.mapCreateTicket(ticket);
        String ticketMessageId = producer.sendDataSourceMessage(createTicketRequest, DataSourceQueue.INTERNAL);
        TextMessage ticketResponse = consumer.getMessage(ticketMessageId, TextMessage.class);

        TicketType createdTicket = RulesDataSourceResponseMapper.mapSingleTicketFromResponse(ticketResponse, ticketMessageId);

        sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.CREATE, createdTicket.getGuid(), null, ticket.getUpdatedBy());

        // Notify long-polling clients of the change
        ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
    }

    @Override
    public TicketType updateTicketCount(TicketType ticket) throws RulesServiceException, RulesFaultException {
        LOG.info("Update ticket count invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateTicketCount(ticket);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            TicketType updatedTicket = RulesDataSourceResponseMapper.mapToUpdateTicketCountFromResponse(response, messageId);

            // Notify long-polling clients of the update
            ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));

            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));

            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), null, ticket.getUpdatedBy());

            return updatedTicket;

        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesServiceException, RulesFaultException {
        try {
            String getAlarmReportRequest = RulesDataSourceRequestMapper.mapGetAlarmByGuid(guid);
            String messageId = producer.sendDataSourceMessage(getAlarmReportRequest, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapSingleAlarmFromResponse(response, messageId);
        } catch (MessageException | JMSException | RulesModelMapperException e) {
            LOG.error("[ Error when getting alarm by GUID ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting alarm by GUID. ]");
        }
    }

    @Override
    public TicketType getTicketByGuid(String guid) throws RulesServiceException, RulesFaultException {
        try {
            String getTicketReportRequest = RulesDataSourceRequestMapper.mapGetTicketByGuid(guid);
            String messageId = producer.sendDataSourceMessage(getTicketReportRequest, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapSingleTicketFromResponse(response, messageId);
        } catch (MessageException | JMSException | RulesModelMapperException e) {
            LOG.error("[ Error when getting ticket by GUID ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting ticket by GUID. ]");
        }
    }

    @Override
    public String reprocessAlarm(List<String> alarmGuids, String username) throws RulesServiceException, RulesFaultException {
        LOG.info("Reprocess alarms invoked in service layer");
        try {
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

            String request = RulesDataSourceRequestMapper.mapAlarmList(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response == null) {
                LOG.error("[ Error when getting alarm list, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }

            List<AlarmReportType> alarms = RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId).getAlarms();

            for (AlarmReportType alarm : alarms) {
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
                //NHI
                setMovementReportReceived(rawMovementType, pluginType, username);
            }

//            return RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId);
            // TODO: Better
            return "OK";

        } catch (RulesModelMapperException | MessageException | JMSException  e) {
            throw new RulesServiceException(e.getMessage());
        }
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
        if (asset != null && asset.getAssetId() != null) {
             assetGuid = asset.getAssetId().getGuid();
            LOG.warn("[ Asset was null for {} ]", rawMovement.getAssetId());
        } else {
            assetGuid = null;
        }

        final XMLGregorianCalendar positionTime = rawMovement.getPositionTime();

        FutureTask<Long> timeDiffAndPersistMovementTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() {
                return timeDiffAndPersistMovement(assetGuid, positionTime);
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

    private Long timeDiffAndPersistMovement(String assetGuid, XMLGregorianCalendar positionTime) {
        Date auditTimestamp = new Date();

        // This needs to be done before persisting last report
        Long timeDiffInSeconds = null;
        Long timeDiff = timeDiffFromLastCommunication(assetGuid, positionTime);
        timeDiffInSeconds = timeDiff != null ? timeDiff / 1000 : null;
        auditTimestamp = auditLog("Time to fetch time difference to previous report:", auditTimestamp);

        persistLastCommunication(assetGuid, positionTime);
        auditLog("Time to persist the position time:", auditTimestamp);

        return timeDiffInSeconds;
    }

    private Long timeDiffFromLastCommunication(String assetGuid, XMLGregorianCalendar thisTime) {
        LOG.info("Fetching time difference to previous movement report");

        Long timeDiff = null;
        try {
            String request = RulesDataSourceRequestMapper.mapGetPreviousReportByAssetGuid(assetGuid);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            PreviousReportType previousReport = RulesDataSourceResponseMapper.mapToGetPreviousReportByAssetGuidResponse(response, messageId);

            XMLGregorianCalendar previousTime = previousReport.getPositionTime();

            timeDiff = thisTime.toGregorianCalendar().getTimeInMillis() - previousTime.toGregorianCalendar().getTimeInMillis();
        } catch (Exception e) {
            // If something goes wrong, continue with the other validation
            LOG.warn("[ Error when fetching time difference of previous movement reports ]");
        }
        return timeDiff;
    }

    private void persistLastCommunication(String assetGuid, XMLGregorianCalendar positionTime) {
        PreviousReportType thisReport = new PreviousReportType();

        thisReport.setPositionTime(positionTime);
        thisReport.setAssetGuid(assetGuid);

        String upsertPreviousReportequest = null;
        try {
            upsertPreviousReportequest = RulesDataSourceRequestMapper.mapUpsertPreviousReport(thisReport);
            producer.sendDataSourceMessage(upsertPreviousReportequest, DataSourceQueue.INTERNAL);
        } catch (RulesModelMapperException | MessageException e) {
            LOG.error("[ Error persisting report. ] {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private Integer numberOfReportsLast24Hours(String assetGuid, XMLGregorianCalendar thisTime) {
        LOG.info("Fetching number of reports last 24 hours");

        Date auditTimestamp = new Date();

        Integer numberOfMovements = null;
        MovementQuery query = new MovementQuery();

        // Range
        RangeCriteria dateRangeCriteria = new RangeCriteria();
        dateRangeCriteria.setKey(RangeKeyType.DATE);
        GregorianCalendar twentyFourHoursAgo = thisTime.toGregorianCalendar();
        twentyFourHoursAgo.add(GregorianCalendar.HOUR, -24);
        String from = RulesUtil.gregorianToString(twentyFourHoursAgo);
        dateRangeCriteria.setFrom(from);
        String to = RulesUtil.xmlGregorianToString(thisTime);
        dateRangeCriteria.setTo(to);
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
        try {
            MovementQuery query = new MovementQuery();
            query.setExcludeFirstAndLastSegment(true);

            RangeCriteria time = new RangeCriteria();
            GregorianCalendar from = rawMovement.getPositionTime().toGregorianCalendar();
            from.add(Calendar.HOUR_OF_DAY, -1);

            time.setKey(RangeKeyType.DATE);
            time.setFrom(RulesUtil.gregorianToString(from));
            time.setTo(RulesUtil.gregorianToString(rawMovement.getPositionTime().toGregorianCalendar()));
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
        switch(rawMovement.getSource()) {
            case INMARSAT_C:
                transponderTypeCrit.setKey(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey.TRANSPONDER_TYPE);
                transponderTypeCrit.setValue("INMARSAT_C");
                criteria.getCriterias().add(transponderTypeCrit);
                break;
            case IRIDIUM:
                transponderTypeCrit.setKey(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.SearchKey.TRANSPONDER_TYPE);
                transponderTypeCrit.setValue("IRIDIUM");
                criteria.getCriterias().add(transponderTypeCrit);
                break;
        }

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

        for(MobileTerminalType mobileTerminalType : resultList){
            if(mobileTerminalType.getConnectId()!=null){
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
        }
        catch (AuditModelMarshallException | MessageException e) {
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
