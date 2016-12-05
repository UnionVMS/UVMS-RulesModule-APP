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

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.RecipientInfoType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.EmailType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.exchange.service.v1.ServiceResponseType;
import eu.europa.ec.fisheries.schema.exchange.service.v1.StatusType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmItemType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateAlarmReportResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateTicketResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetCustomRuleListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.dto.CustomRuleListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketEvent;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.user.model.mapper.UserModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.user.module.FindOrganisationsResponse;
import eu.europa.ec.fisheries.wsdl.user.module.GetContactDetailResponse;
import eu.europa.ec.fisheries.wsdl.user.types.EndPoint;
import eu.europa.ec.fisheries.wsdl.user.types.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
public class ValidationServiceBean implements ValidationService {

    private final static Logger LOG = LoggerFactory.getLogger(ValidationServiceBean.class);

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;

    @Inject
    @TicketEvent
    private Event<NotificationMessage> ticketEvent;

    @Inject
    @AlarmReportEvent
    private Event<NotificationMessage> alarmReportEvent;

    @Inject
    @AlarmReportCountEvent
    private Event<NotificationMessage> alarmReportCountEvent;

    @Inject
    @TicketCountEvent
    private Event<NotificationMessage> ticketCountEvent;

    @EJB(lookup = ServiceConstants.DB_ACCESS_RULES_DOMAIN_MODEL)
    private RulesDomainModel rulesDomainModel;

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws RulesServiceException
     */
    @Override
    public List<CustomRuleType> getCustomRulesByUser(String userName) throws RulesServiceException, RulesFaultException {
        LOG.info("Get all custom rules invoked in service layer");
        try {
            List<CustomRuleType> customRulesByUser = rulesDomainModel.getCustomRulesByUser(userName);
            return customRulesByUser;
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
    public List<CustomRuleType> getRunnableCustomRules() throws RulesServiceException, RulesFaultException {
        LOG.info("Get all valid custom rules invoked in service layer");
        try {
            List<CustomRuleType> runnableCustomRuleList = rulesDomainModel.getRunnableCustomRuleList();
            return runnableCustomRuleList;
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
    public List<SanityRuleType> getSanityRules() throws RulesServiceException, RulesFaultException {
        LOG.info("Get all sanity rules invoked in service layer");
        try {
            List<SanityRuleType> sanityRuleList = rulesDomainModel.getSanityRuleList();
            return sanityRuleList;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public GetCustomRuleListByQueryResponse getCustomRulesByQuery(CustomRuleQuery query) throws RulesServiceException, RulesFaultException {
        LOG.info("Get custom rules by query invoked in service layer");
        try {
            CustomRuleListResponseDto customRuleListByQuery = rulesDomainModel.getCustomRuleListByQuery(query);
            GetCustomRuleListByQueryResponse response = new GetCustomRuleListByQueryResponse();
            response.setTotalNumberOfPages(customRuleListByQuery.getTotalNumberOfPages());
            response.setCurrentPage(customRuleListByQuery.getCurrentPage());
            response.getCustomRules().addAll(customRuleListByQuery.getCustomRuleList());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by rule engine
    @Override
    public void customRuleTriggered(String ruleName, String ruleGuid, MovementFact fact, String actions) {
        LOG.info("Performing actions on triggered user rules");

        Date auditTimestamp = new Date();

        // Update last update
        updateLastTriggered(ruleGuid);
        auditTimestamp = auditLog("Time to update last triggered:", auditTimestamp);

        // Always create a ticket
        createTicket(ruleName, ruleGuid, fact);
        auditTimestamp = auditLog("Time to create ticket:", auditTimestamp);

        sendMailToSubscribers(ruleGuid, ruleName, fact);
        auditTimestamp = auditLog("Time to send email to subscribers:", auditTimestamp);

        // Actions list format:
        // ACTION,VALUE;ACTION,VALUE;
        // N.B! The .drl rule file gives the string "null" when (for instance)
        // value is null.
        String[] parsedActionKeyValueList = actions.split(";");
        for (String keyValue : parsedActionKeyValueList) {
            String[] keyValueList = keyValue.split(",");
            String action = keyValueList[0];
            String value = "";
            if (keyValueList.length == 2) {
                value = keyValueList[1];
            }
            switch (ActionType.valueOf(action)) {
                case EMAIL:
                    // Value=address.
                    sendToEmail(value, ruleName, fact);
                    auditTimestamp = auditLog("Time to send (action) email:", auditTimestamp);
                    break;
                case SEND_TO_FLUX:
                    sendToEndpointFlux(ruleName, fact, value);
                    auditTimestamp = auditLog("Time to send to endpoint:", auditTimestamp);
                    break;
                case SEND_TO_NAF:
                    sendToEndpointNaf(ruleName, fact, value);
                    auditTimestamp = auditLog("Time to send to endpoint:", auditTimestamp);
                    break;

                /*
                case MANUAL_POLL:
                    LOG.info("NOT IMPLEMENTED!");
                    break;

                case ON_HOLD:
                    LOG.info("NOT IMPLEMENTED!");
                    break;
                case TOP_BAR_NOTIFICATION:
                    LOG.info("NOT IMPLEMENTED!");
                    break;
                case SMS:
                    LOG.info("NOT IMPLEMENTED!");
                    break;
                    */
                default:
                    LOG.info("The action '{}' is not defined", action);
                    break;
            }
        }
    }

    private void sendMailToSubscribers(String ruleGuid, String ruleName, MovementFact fact) {
        CustomRuleType customRuleType = null;
        try {
            // Get email subscribers
            customRuleType = rulesDomainModel.getByGuid(ruleGuid);
        } catch (RulesModelException e) {
            LOG.error("[ Failed to fetch rule when sending email to subscribers! ] {}", e.getMessage());
        }

        List<SubscriptionType> subscriptions = customRuleType.getSubscriptions();
        if (subscriptions != null) {
            for (SubscriptionType subscription : subscriptions) {
                if (SubscriptionTypeType.EMAIL.equals(subscription.getType())) {
                    try {
                        // Find current email address
                        String userRequest = UserModuleRequestMapper.mapToGetContactDetailsRequest(subscription.getOwner());
                        String userMessageId = producer.sendDataSourceMessage(userRequest, DataSourceQueue.USER);
                        TextMessage userMessage = consumer.getMessage(userMessageId, TextMessage.class);
                        GetContactDetailResponse userResponse = JAXBMarshaller.unmarshallTextMessage(userMessage, GetContactDetailResponse.class);
                        String emailAddress = userResponse.getContactDetails().getEMail();
                        sendToEmail(emailAddress, ruleName, fact);
                    } catch (Exception e) {
                        // If a mail attempt fails, proceed with the rest
                        LOG.error("Could not send email to user '{}'", subscription.getOwner());
                    }
                }
            }
        }
    }

    private void updateLastTriggered(String ruleGuid) {
        try {
            rulesDomainModel.updateLastTriggeredCustomRule(ruleGuid);
        } catch (RulesModelException e) {
            LOG.warn("[ Failed to update last triggered date for rule {} ]", ruleGuid);
        }
    }
    
    private void sendToEndpoint(String ruleName, MovementFact fact, String endpoint, PluginType pluginType) {
        LOG.info("Sending to endpoint '{}'", endpoint);

        try {
            MovementType exchangeMovement = fact.getExchangeMovement();

            //XMLGregorianCalendar date = RulesUtil.dateToXmlGregorian(new Date());

            String userRequest = UserModuleRequestMapper.mapToFindOrganisationsRequest(endpoint);
            String userMessageId = producer.sendDataSourceMessage(userRequest, DataSourceQueue.USER);
            TextMessage userMessage = consumer.getMessage(userMessageId, TextMessage.class);
            FindOrganisationsResponse userResponse = JAXBMarshaller.unmarshallTextMessage(userMessage, FindOrganisationsResponse.class);

            List<RecipientInfoType> recipientInfoList = new ArrayList<>();

            List<Organisation> organisations = userResponse.getOrganisation();
            for (Organisation organisation : organisations) {
                List<EndPoint> endPoints = organisation.getEndPoints();
                for (EndPoint endPoint : endPoints) {
                    RecipientInfoType recipientInfo = new RecipientInfoType();
                    recipientInfo.setKey(endPoint.getName());
                    recipientInfo.setValue(endPoint.getUri());
                    recipientInfoList.add(recipientInfo);
                }
            }
            
            List<ServiceResponseType> pluginList = getPluginList(pluginType);
            if (pluginList != null && !pluginList.isEmpty()) {
                for (ServiceResponseType service : pluginList) {
                    if (StatusType.STOPPED.equals(service.getStatus())) {
                        LOG.info("Service {} was Stopped, trying the next one, if possible.", service.getName());
                        continue;
                    }
                    String exchangeRequest = ExchangeModuleRequestMapper.createSendReportToPlugin(service.getServiceClassName(), pluginType, new Date(), ruleName, endpoint, exchangeMovement, recipientInfoList, fact.getAssetName(), fact.getIrcs(), fact.getMmsiNo(), fact.getExternalMarking(), fact.getFlagState());
                    String messageId = producer.sendDataSourceMessage(exchangeRequest, DataSourceQueue.EXCHANGE);
                    TextMessage response = consumer.getMessage(messageId, TextMessage.class);

                    sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_ACTION, AuditOperationEnum.SEND_TO_ENDPOINT, null, endpoint, "UVMS");
                    // TODO: Do something with the response??? Or don't send response from Exchange
                    return;
                }
            }

            LOG.info("[ No plugin of the correct type found. Nothing was sent ]");
        } catch (ExchangeModelMapperException | MessageException | ModelMarshallException | RulesModelMarshallException e) {
            LOG.error("[ Failed to send to endpoint! ] {}", e.getMessage());
        }
        
    }

    private List<ServiceResponseType> getPluginList(PluginType pluginType) throws ExchangeModelMapperException, MessageException {
        ArrayList<PluginType> types = new ArrayList<>();
        types.add(pluginType);
        String serviceListRequest = ExchangeModuleRequestMapper.createGetServiceListRequest(types);
        String serviceListRequestId = producer.sendDataSourceMessage(serviceListRequest, DataSourceQueue.EXCHANGE);
        TextMessage serviceListResponse = consumer.getMessage(serviceListRequestId, TextMessage.class);
        return ExchangeDataSourceResponseMapper.mapToServiceTypeListFromModuleResponse(serviceListResponse, serviceListRequestId);
    }

    private void sendToEndpointFlux(String ruleName, MovementFact fact, String endpoint) {
        sendToEndpoint(ruleName, fact, endpoint, PluginType.FLUX);
    }

    private void sendToEndpointNaf(String ruleName, MovementFact fact, String endpoint) {
        sendToEndpoint(ruleName, fact, endpoint, PluginType.NAF);
    }

    private void sendToEmail(String emailAddress, String ruleName, MovementFact fact) {
        LOG.info("Sending email to '{}'", emailAddress);

        EmailType email = new EmailType();

        email.setSubject(buildSubject(ruleName));
        email.setBody(buildBody(ruleName, fact));
        email.setTo(emailAddress);

        try {
            List<ServiceResponseType> pluginList = getPluginList(PluginType.EMAIL);
            if (pluginList != null && !pluginList.isEmpty()) {
                for (ServiceResponseType service : pluginList) {
                    if (StatusType.STOPPED.equals(service.getStatus())) {
                        LOG.info("Service {} was Stopped, trying the next one, if possible.", service.getName());
                        continue;
                    }
                    String request = ExchangeModuleRequestMapper.createSetCommandSendEmailRequest(service.getServiceClassName(), email, ruleName);
                    producer.sendDataSourceMessage(request, DataSourceQueue.EXCHANGE);

                    sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_ACTION, AuditOperationEnum.SEND_EMAIL, null, emailAddress, "UVMS");
                    return;
                }
            }
            LOG.info("[ No plugin of the correct type found. Nothing was sent ]");
        } catch (ExchangeModelMapperException | MessageException e) {
            LOG.error("[ Failed to send email! ] {}", e.getMessage());
        }
    }

    private String buildSubject(String ruleName) {
        StringBuilder subjectBuilder = new StringBuilder();
        subjectBuilder.append("Rule '")
                .append(ruleName)
                .append("' has been triggered.");
        return subjectBuilder.toString();
    }

    private String buildBody(String ruleName, MovementFact fact) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>")
                .append(buildSubject(ruleName))
                .append("<br><br>")
                .append(buildAssetBodyPart(fact))
                .append(buildPositionBodyPart(fact))
                .append("</html>");

        return sb.toString();
    }

    private String buildAssetBodyPart(MovementFact fact) {
        StringBuilder assetBuilder = new StringBuilder();
        assetBuilder.append("<b>Asset:</b>")
                .append("<br>&nbsp;&nbsp;")
                .append("Name: ")
                .append(fact.getAssetName())
                .append("<br>&nbsp;&nbsp;")
                .append("IRCS: ")
                .append(fact.getIrcs())
                .append("<br>&nbsp;&nbsp;")
                .append("CFR: ")
                .append(fact.getCfr())
                .append("<br>");

        return assetBuilder.toString();
    }

    private String buildPositionBodyPart(MovementFact fact) {
        StringBuilder positionBuilder = new StringBuilder();
        positionBuilder.append("<b>Position report:</b>")
                .append("<br>&nbsp;&nbsp;")
                .append("Report timestamp: ")
                .append(fact.getPositionTime())
                .append("<br>&nbsp;&nbsp;")
                .append("Longitude: ")
                .append(fact.getLongitude())
                .append("<br>&nbsp;&nbsp;")
                .append("Latitude: ")
                .append(fact.getLatitude())
                .append("<br>&nbsp;&nbsp;")
                .append("Status code: ")
                .append(fact.getStatusCode())
                .append("<br>&nbsp;&nbsp;")
                .append("Reported speed: ")
                .append(fact.getReportedSpeed())
                .append("<br>&nbsp;&nbsp;")
                .append("Reported course: ")
                .append(fact.getReportedCourse())
                .append("<br>&nbsp;&nbsp;")
                .append("Calculated speed: ")
                .append(fact.getCalculatedSpeed())
                .append("<br>&nbsp;&nbsp;")
                .append("Calculated course: ")
                .append(fact.getCalculatedCourse())
                .append("<br>&nbsp;&nbsp;")
                .append("Com channel type: ")
                .append(fact.getComChannelType())
                .append("<br>&nbsp;&nbsp;")
                .append("Segment type: ")
                .append(fact.getSegmentType())
                .append("<br>&nbsp;&nbsp;")
                .append("Source: ")
                .append(fact.getSource())
                .append("<br>&nbsp;&nbsp;")
                .append("Movement type: ")
                .append(fact.getMovementType())
                .append("<br>&nbsp;&nbsp;")
                .append("Activity type: ")
                .append(fact.getActivityMessageType())
                .append("<br>&nbsp;&nbsp;")
                .append("Closest port: ")
                .append(fact.getClosestPortCode())
                .append("<br>&nbsp;&nbsp;")
                .append("Closest country: ")
                .append(fact.getClosestCountryCode())
                .append("<br>&nbsp;&nbsp;");

        positionBuilder.append("Areas:");
        for (int i = 0; i < fact.getAreaCodes().size(); i++) {
            positionBuilder.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append(fact.getAreaCodes().get(i))
                    .append(" (")
                    .append(fact.getAreaTypes().get(i))
                    .append(")");
        }

        return positionBuilder.toString();
    }

    private void createTicket(String ruleName, String ruleGuid, MovementFact fact) {
        LOG.info("Create ticket invoked in service layer");
        try {
            TicketType ticket = new TicketType();

            ticket.setAssetGuid(fact.getAssetGuid());
            ticket.setMobileTerminalGuid(fact.getMobileTerminalGuid());
            ticket.setChannelGuid(fact.getChannelGuid());
            ticket.setOpenDate(RulesUtil.dateToString(new Date()));
            ticket.setRuleName(ruleName);
            ticket.setRuleGuid(ruleGuid);
            ticket.setStatus(TicketStatusType.OPEN);
            ticket.setUpdatedBy("UVMS");
            ticket.setMovementGuid(fact.getMovementGuid());
            ticket.setGuid(UUID.randomUUID().toString());

            for (int i = 0; i < fact.getAreaTypes().size(); i++) {
                if ("EEZ".equals(fact.getAreaTypes().get(i))) {
                    ticket.setRecipient(fact.getAreaCodes().get(i));
                }
            }

            TicketType createdTicket = rulesDomainModel.createTicket(ticket);
            String request = RulesDataSourceRequestMapper.mapCreateTicket(ticket);

            ticketEvent.fire(new NotificationMessage("guid", createdTicket.getGuid()));

            // Notify long-polling clients of the change (no vlaue since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));

            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.CREATE, createdTicket.getGuid(), null, ticket.getUpdatedBy());
        } catch (RulesModelMapperException | RulesModelException e) {
            LOG.error("[ Failed to create ticket! ] {}", e.getMessage());
        }
    }

    // Triggered by rule engine
    @Override
    public void createAlarmReport(String ruleName, RawMovementFact fact) {
        LOG.info("Create alarm invoked in validation service");
        try {
            // TODO: Decide who sets the guid, Rules or Exchange
            if (fact.getRawMovementType().getGuid() == null) {
                fact.getRawMovementType().setGuid(UUID.randomUUID().toString());
            }

            AlarmReportType alarmReport = new AlarmReportType();
            alarmReport.setOpenDate(RulesUtil.dateToString(new Date()));
            alarmReport.setStatus(AlarmStatusType.OPEN);
            alarmReport.setRawMovement(fact.getRawMovementType());
            alarmReport.setUpdatedBy("UVMS");
            alarmReport.setPluginType(fact.getPluginType());
            alarmReport.setAssetGuid(fact.getAssetGuid());
            alarmReport.setInactivatePosition(false);

            // TODO: Add sender, recipient and assetGuid

            // Alarm item
            List<AlarmItemType> alarmItems = new ArrayList<>();
            AlarmItemType alarmItem = new AlarmItemType();
            alarmItem.setGuid(UUID.randomUUID().toString());
            alarmItem.setRuleName(ruleName);
            alarmItem.setRuleGuid(ruleName);
            alarmItems.add(alarmItem);
            alarmReport.getAlarmItem().addAll(alarmItems);

            AlarmReportType createdAlarmReport = rulesDomainModel.createAlarmReport(alarmReport);


            // Notify long-polling clients of the new alarm report
            alarmReportEvent.fire(new NotificationMessage("guid", createdAlarmReport.getGuid()));

            // Notify long-polling clients of the change (no vlaue since FE will need to fetch it)
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", null));

            sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.CREATE, createdAlarmReport.getGuid(), null, alarmReport.getUpdatedBy());
        } catch (RulesModelException e) {
            LOG.error("[ Failed to create alarm! ] {}", e.getMessage());
        }
    }

    @Override
    public long getNumberOfOpenAlarmReports() throws RulesServiceException, RulesFaultException {
        try {
            long numberOfOpenAlarms = rulesDomainModel.getNumberOfOpenAlarms();
            return numberOfOpenAlarms;
        } catch (RulesModelException e) {
            LOG.error("[ Error when getting number of open alarms ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
    }

    @Override
    public long getNumberOfOpenTickets(String userName) throws RulesServiceException, RulesFaultException {
        try {
            long numberOfOpenTickets = rulesDomainModel.getNumberOfOpenTickets(userName);
            return numberOfOpenTickets;
        } catch (RulesModelException e) {
            LOG.error("[ Error when getting number of open tickets ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
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

    private Date auditLog(String msg, Date lastTimestamp) {
        Date newTimestamp = new Date();
        long duration = newTimestamp.getTime() - lastTimestamp.getTime();
        LOG.info("--> AUDIT - {} {}ms", msg, duration);
        return newTimestamp;
    }

}