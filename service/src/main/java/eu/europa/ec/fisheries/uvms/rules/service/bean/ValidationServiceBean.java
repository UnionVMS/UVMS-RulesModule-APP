package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.EmailType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmItemType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateAlarmReportResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateTicketResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketEvent;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
public class ValidationServiceBean implements ValidationService {

    final static Logger LOG = LoggerFactory.getLogger(ValidationServiceBean.class);

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

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws RulesServiceException
     */
    @Override
    public List<CustomRuleType> getCustomRuleList() throws RulesServiceException {
        LOG.info("Get custom rule list invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapCustomRuleList();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToCustomRuleListFromResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    // Triggered by rule engine
    public void customRuleTriggered(String ruleName, String ruleGuid, MovementFact fact, String actions) throws RulesServiceException {
        LOG.info("Creating custom event. NOT FULLY IMPLEMENTED");

        // For now the actions are described as a comma separated list. Parse
        // out the action, switch on it, and log the action and the
        // corresponding value
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
                    // todo: What will the mail contain? Value=address.
                    // Is it enough with a notification, or what's in the fact?
                    // Or should I enrich the rules, and this method,
                    // to receive an additional text?
                    LOG.info("Sending email to '{}'", value);
                    sendToEmail(value, ruleName);
                    break;
                case ON_HOLD:
                    LOG.info("Performing action '{}' with value '{}'", action, value);
                    break;
                case TICKET:
                    LOG.info("Performing action '{}' with value '{}'", action, value);
                    createTicket(ruleName, ruleGuid, fact);
                    break;
                case MANUAL_POLL:
                    LOG.info("Performing action '{}' with value '{}'", action, value);
                    sendManualPoll(value);
                    break;
                case SEND_TO_ENDPOINT:
                    LOG.info("Performing action '{}' with value '{}'", action, value);
                    break;
                case SMS:
                    LOG.info("Performing action '{}' with value '{}'", action, value);
                    break;
                case TOP_BAR_NOTIFICATION:
                    LOG.info("Performing action '{}' with value '{}'", action, value);
                    break;
                default:
                    LOG.info("The action '{}' is not defined", action);
                    break;
            }
        }
    }


    private void sendManualPoll(String value) {
        // todo: value is probably not used...
        // But we still need plugin name, so perhaps we can use this here, but populate automatically. We'll see...

        String pluginName = "";

        //      String sendMovementToPluginRequest = ExchangeModuleRequestMapper.createSetCommandSendPollRequest(pluginName, PluginType.SATELLITE_RECEIVER);

//        String getVesselMessageId = producer.sendDataSourceMessage(getVesselRequest, DataSourceQueue.VESSEL);
//        TextMessage getVesselResponse = consumer.getMessage(getVesselMessageId, TextMessage.class);

    }

    private void sendToEmail(String emailAddress, String ruleName) {
        // TODO: Decide on what message to send

        EmailType email = new EmailType();
        String body = "A rule has been triggered in UVMS: '" + ruleName + "'";
        email.setBody(body);
        email.setFrom("No Reply");
        email.setSubject("You've got mail!");
        email.setTo(emailAddress);

        try {
            ExchangeModuleRequestMapper.createSetCommandSendEmailRequest("pluginName", email);
        } catch (ExchangeModelMapperException e) {
            LOG.error("[ Failed to send email! ]");
        }
    }

    private void createTicket(String ruleName, String ruleGuid, MovementFact fact) throws RulesServiceException {
        LOG.info("Create ticket invoked in service layer");
        try {
            TicketType ticket = new TicketType();

            ticket.setVesselGuid(fact.getVesselGuid());
            ticket.setOpenDate(RulesUtil.dateToString(new Date()));
            ticket.setRuleName(ruleName);
            ticket.setRuleGuid(ruleGuid);
            ticket.setStatus(TicketStatusType.OPEN);
            ticket.setUpdatedBy("UVMS");
            ticket.setMovementGuid(fact.getMovementGuid());
            ticket.setGuid(UUID.randomUUID().toString());

            String request = RulesDataSourceRequestMapper.mapCreateTicket(ticket);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // Notify long-polling clients of the new ticket
            CreateTicketResponse createTicketResponse = JAXBMarshaller.unmarshallTextMessage(response, CreateTicketResponse.class);
            ticketEvent.fire(new NotificationMessage("guid", createTicketResponse.getTicket().getGuid()));

        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    // Triggered by rule engine
    @Override
    public void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException {
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

            // TODO: Add sender, recipient and assetGuid

            // Alarm item
            List<AlarmItemType> alarmItems = new ArrayList<AlarmItemType>();
            AlarmItemType alarmItem = new AlarmItemType();
            alarmItem.setGuid(UUID.randomUUID().toString());
            alarmItem.setRuleGuid(ruleName);
            alarmItems.add(alarmItem);
            alarmReport.getAlarmItem().addAll(alarmItems);

            String request = RulesDataSourceRequestMapper.mapCreateAlarmReport(alarmReport);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // Notify long-polling clients of the new alarm report
            CreateAlarmReportResponse createAlarmResponse = JAXBMarshaller.unmarshallTextMessage(response, CreateAlarmReportResponse.class);
            alarmReportEvent.fire(new NotificationMessage("guid", createAlarmResponse.getAlarm().getGuid()));

        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

}
