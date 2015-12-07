package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.RecipientInfoType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.EmailType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmItemType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateAlarmReportResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateTicketResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetCustomRuleListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketEvent;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
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
import java.util.*;

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
            String request = RulesDataSourceRequestMapper.mapGetCustomRulesByUser(userName);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToGetCustomRulesFromResponse(response, messageId);
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
    public List<CustomRuleType> getRunnableCustomRules() throws RulesServiceException, RulesFaultException {
        LOG.info("Get all valid custom rules invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapGetRunnableCustomRules();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToGetRunnableCustomRulesFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    @Override
    public GetCustomRuleListByQueryResponse getCustomRulesByQuery(CustomRuleQuery query) throws RulesServiceException, RulesFaultException {
        LOG.info("Get custom rules by query invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapCustomRuleListByQuery(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapToCustomRuleListByQueryFromResponse(response, messageId);
        } catch (RulesModelMapperException | MessageException | JMSException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by rule engine
    @Override
    public void customRuleTriggered(String ruleName, String ruleGuid, MovementFact fact, String actions) {
        LOG.info("Performing actions on triggered user rules");

        // Update last update
        updateLastTriggered(ruleGuid);

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
                    // todo: What will the mail contain? Value=address.
                    // Is it enough with a notification, or what's in the fact?
                    // Or should I enrich the rules, and this method,
                    // to receive an additional text?
                    sendToEmail(value, ruleName, fact);
                    break;
                case SEND_TO_ENDPOINT:
                    sendToEndpoint(ruleName, fact, value);
                    break;
                case TICKET:
                    createTicket(ruleName, ruleGuid, fact, value);
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

    private void updateLastTriggered(String ruleGuid) {
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateCustomRuleLastTriggered(ruleGuid);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
        } catch (RulesModelMapperException | MessageException e) {
            LOG.warn("[ Failed to update last triggered date for rule {} ]", ruleGuid);
        }

    }

// TODO: This is unused and should probably be deleted
/*
    @Override
    public void sendToEndpoint(eu.europa.ec.fisheries.schema.movement.v1.MovementType createdMovement, String countryCode) throws MessageException, ExchangeModelMapperException {
        if (createdMovement.getMetaData() != null) {
            List<MovementMetaDataAreaType> areas = createdMovement.getMetaData().getAreas();
            MovementType exchangeMovement = RulesDozerMapper.getInstance().getMapper().map(createdMovement, MovementType.class);

            for (MovementMetaDataAreaType area : areas) {
                String ruleName = "Automatic Forwarding Rule";

                XMLGregorianCalendar date = null;
                try {
                    GregorianCalendar c = new GregorianCalendar();
                    c.setTime(new Date());
                    date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }

                if ("EEZ".equals(area.getAreaType()) || "RFMO".equals(area.getAreaType())) {
                    String destination = area.getCode();

                    // Make sure you don't send to flag state since it already has it (it this one that we are forwarding here)
                    if (!countryCode.equals(destination)) {
                        LOG.info("Forwarding movement '{}' to {}", exchangeMovement.getGuid(), destination);
                        String request = ExchangeModuleRequestMapper.createSendReportToPlugin(null, PluginType.FLUX, date, ruleName, destination, exchangeMovement);
                        String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.EXCHANGE);
                        TextMessage response = consumer.getMessage(messageId, TextMessage.class);

                        // TODO: Do something with the response
                    }
                }
            }
        }
    }
*/

    private void sendToEndpoint(String ruleName, MovementFact fact, String endpoint) {
        LOG.info("Sending to endpoint '{}'", endpoint);

        try {
            MovementType exchangeMovement = fact.getExchangeMovement();

            XMLGregorianCalendar date = RulesUtil.dateToXmlGregorian(new Date());

            // TODO: Get this from user module
            RecipientInfoType recipientInfo = new RecipientInfoType();
            recipientInfo.setKey("dummyKey");
            recipientInfo.setValue("dummyValue");
            List<RecipientInfoType> recipientInfoList = new ArrayList<>();
            recipientInfoList.add(recipientInfo);

            String request = ExchangeModuleRequestMapper.createSendReportToPlugin(null, PluginType.FLUX, date, ruleName, endpoint, exchangeMovement, recipientInfoList, fact.getVesselName(), fact.getVesselIrcs());
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.EXCHANGE);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // TODO: Do something with the response

        } catch (ExchangeModelMapperException | MessageException | DatatypeConfigurationException e) {
            LOG.error("[ Failed to send to endpoint! {} ]", e.getMessage());
        }

    }

    private void sendToEmail(String emailAddress, String ruleName, MovementFact fact) {
        // TODO: Decide on what message to send

        LOG.info("Sending email to '{}'", emailAddress);

        EmailType email = new EmailType();


        String subject = buildSubject(ruleName);
        email.setSubject(subject);

        String assetString = buildAsset(fact);
        String positionString = buildPosition(fact);
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder
                .append(subject)
                .append("\n")
                .append(assetString)
                .append("\n")
                .append(positionString);
        email.setBody(bodyBuilder.toString());

        email.setTo(emailAddress);

        // TODO: Hard coded...
        String pluginName = "eu.europa.ec.fisheries.uvms.plugins.sweagencyemail";
        try {
            String request = ExchangeModuleRequestMapper.createSetCommandSendEmailRequest(pluginName, email);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.EXCHANGE);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // TODO: Do something with the response
//            xxx = ExchangeModuleResponseMapper.mapSetCommandSendEmailResponse(response);

//            ExchangeModuleResponseMapper.mapSetCommandResponse(response);
//            för att få ut AcknowledgeType för hur det gick med Email-et.
// Metoden kastar ExchangeValidationException (som är en ExchangeModelMapperException) - som containar "ExchangeFault.code - och ExchangeFault.message" som message om det är ett Fault, ger AcknowledgeType.OK om det gick bra AcknowledgeType.NOK om pluginen inte är startad

        } catch (ExchangeModelMapperException | MessageException e) {
            LOG.error("[ Failed to send email! {} ]", e.getMessage());
        }
    }

    private String buildAsset(MovementFact fact) {
        StringBuilder assetBuilder = new StringBuilder();
        assetBuilder.append("Asset:")
                .append("\n\tName: ")
                .append(fact.getVesselName())
                .append("\n\tIRCS: ")
                .append(fact.getVesselIrcs())
                .append("\n\tCFR: ")
                .append(fact.getVesselCfr());

        return assetBuilder.toString();
    }

    private String buildPosition(MovementFact fact) {
        StringBuilder positionBuilder = new StringBuilder();
        positionBuilder.append("Position report:")
                .append("\n\tReport timestamp: ")
                .append(fact.getPositionTime())
                .append("\n\tLongitude: ")
                .append(fact.getLongitude())
                .append("\n\tLatitude: ")
                .append(fact.getLatitude())
                .append("\n\tStatus code: ")
                .append(fact.getStatusCode())
                .append("\n\tReported speed: ")
                .append(fact.getReportedSpeed())
                .append("\n\tReported course: ")
                .append(fact.getReportedCourse())
                .append("\n\tCalculated speed: ")
                .append(fact.getCalculatedSpeed())
                .append("\n\tCom channel type: ")
                .append(fact.getComChannelType())
                .append("\n\tSegment type: ")
                .append(fact.getSegmentType())
                .append("\n\tSource: ")
                .append(fact.getSource())
                .append("\n\tMovement type: ")
                .append(fact.getMovementType())
                .append("\n\tActivity type: ")
                .append(fact.getActivityMessageType())
                .append("\n\tClosest port: ")
                .append(fact.getClosestPortCode())
                .append("\n\tClosest country: ")
                .append(fact.getClosestCountryCode());

        positionBuilder.append("\n\tAreas:");
        for (int i = 0; i < fact.getAreaCodes().size(); i++) {
            positionBuilder.append("\n\t\t")
                    .append(fact.getAreaCodes().get(i))
                    .append(" (")
                    .append(fact.getAreaTypes().get(i))
                    .append(")");
        }

        return positionBuilder.toString();
    }

    private String buildSubject(String ruleName) {
        StringBuilder subjectBuilder = new StringBuilder();
        subjectBuilder.append("Rule '")
                .append(ruleName)
                .append("' has been triggered.");
        return subjectBuilder.toString();
    }

    private void createTicket(String ruleName, String ruleGuid, MovementFact fact, String user) {
        LOG.info("Create ticket invoked in service layer");
        try {
            TicketType ticket = new TicketType();

            ticket.setVesselGuid(fact.getVesselGuid());
            ticket.setOpenDate(RulesUtil.dateToString(new Date()));
            ticket.setRuleName(ruleName);
            ticket.setRuleGuid(ruleGuid);
            ticket.setStatus(TicketStatusType.OPEN);
            ticket.setUpdatedBy(user);
            ticket.setMovementGuid(fact.getMovementGuid());
            ticket.setGuid(UUID.randomUUID().toString());

            for (int i = 0; i < fact.getAreaTypes().size(); i++) {
                if ("EEZ".equals(fact.getAreaTypes().get(i))) {
                    ticket.setRecipient(fact.getAreaCodes().get(i));
                }
            }

            String request = RulesDataSourceRequestMapper.mapCreateTicket(ticket);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // Notify long-polling clients of the new ticket
            CreateTicketResponse createTicketResponse = JAXBMarshaller.unmarshallTextMessage(response, CreateTicketResponse.class);
            ticketEvent.fire(new NotificationMessage("guid", createTicketResponse.getTicket().getGuid()));

            long ticketCount = this.getNumberOfOpenTickets();
            // Notify long-polling clients of the change
            ticketCountEvent.fire(new NotificationMessage("ticketCount", ticketCount));

        } catch (RulesModelMapperException | MessageException | RulesServiceException | RulesFaultException e) {
            LOG.error("[ Failed to create ticket! {} ]", e.getMessage());
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
            alarmReport.setVesselGuid(fact.getVesselGuid());
            alarmReport.setInactivatePosition(false);

            // TODO: Add sender, recipient and assetGuid

            // Alarm item
            List<AlarmItemType> alarmItems = new ArrayList<>();
            AlarmItemType alarmItem = new AlarmItemType();
            alarmItem.setGuid(UUID.randomUUID().toString());
            alarmItem.setRuleGuid("sanity rule - " + ruleName);
            alarmItem.setRuleName(ruleName);
            alarmItems.add(alarmItem);
            alarmReport.getAlarmItem().addAll(alarmItems);

            String request = RulesDataSourceRequestMapper.mapCreateAlarmReport(alarmReport);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            // Notify long-polling clients of the new alarm report
            CreateAlarmReportResponse createAlarmResponse = JAXBMarshaller.unmarshallTextMessage(response, CreateAlarmReportResponse.class);
            alarmReportEvent.fire(new NotificationMessage("guid", createAlarmResponse.getAlarm().getGuid()));

            long alarmCount = this.getNumberOfOpenAlarmReports();
            // Notify long-polling clients of the change
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", alarmCount));

        } catch (RulesModelMapperException | MessageException | RulesServiceException | RulesFaultException e) {
            LOG.error("[ Failed to create alarm! {} ]", e.getMessage());
        }
    }

    @Override
    public long getNumberOfOpenAlarmReports() throws RulesServiceException, RulesFaultException {
        try {
            String request = RulesDataSourceRequestMapper.getNumberOfOpenAlarmReports();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapGetNumberOfOpenAlarmReportsFromResponse(response, messageId);
        } catch (MessageException | JMSException | RulesModelMapperException e) {
            LOG.error("[ Error when getting number of open alarms ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
    }

    @Override
    public long getNumberOfOpenTickets() throws RulesServiceException, RulesFaultException {
        try {
            String request = RulesDataSourceRequestMapper.getNumberOfOpenTickets();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            return RulesDataSourceResponseMapper.mapGetNumberOfOpenTicketsFromResponse(response, messageId);
        } catch (MessageException | JMSException | RulesModelMapperException e) {
            LOG.error("[ Error when getting number of open tickets ] {}", e.getMessage());
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
    }

}
