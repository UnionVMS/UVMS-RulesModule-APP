package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmItemType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesMapper;

@Stateless
public class RulesServiceBean implements RulesService {

    final static Logger LOG = LoggerFactory.getLogger(RulesServiceBean.class);

    @EJB
    ParameterService parameterService;

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;

    /**
     * {@inheritDoc}
     *
     * @param customRule
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesServiceException {
        LOG.info("Create invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapCreateCustomRule(customRule);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToCreateCustomRuleFromResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

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

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws RulesServiceException
     */
    @Override
    public GetAlarmListByQueryResponse getAlarmList(AlarmQuery query) throws RulesServiceException {
        LOG.info("Get alarm list invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapAlarmList(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            if (response == null) {
                LOG.error("[ Error when getting list, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }
            return RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public GetTicketListByQueryResponse getTicketList(TicketQuery query) throws RulesServiceException {
        LOG.info("Get ticket list invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapTicketList(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            if (response == null) {
                LOG.error("[ Error when getting list, response from JMS Queue is null ]");
                throw new RulesServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }
            return RulesDataSourceResponseMapper.mapToTicketListFromResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException {
        LOG.info("Update ticket status invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateTicketStatus(ticket);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToSetTicketStatusFromResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    @Override
    public AlarmReportType updateAlarmStatus(AlarmReportType alarm) throws RulesServiceException {
        LOG.info("Update alarm status invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateAlarmStatus(alarm);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            AlarmReportType result = RulesDataSourceResponseMapper.mapToSetAlarmStatusFromResponse(response);

            // If accepted, send movement to Movement Module
            if (result.getStatus() == AlarmStatusType.CLOSED) {
                MovementBaseType movementBaseType = RulesMapper.getInstance().getMapper()
                        .map(result.getRawMovement(), MovementBaseType.class);
                String movement = MovementModuleRequestMapper.mapToCreateMovementRequest(movementBaseType);
                messageId = producer.sendDataSourceMessage(movement, DataSourceQueue.MOVEMENT);
                response = consumer.getMessage(messageId, TextMessage.class);
            }

            return result;
        } catch (RulesModelMapperException | MessageException | ModelMarshallException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    // Triggered by rule engine, no response expected
    @Override
    public void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException {
        LOG.info("Create alarm invoked in service layer");
        try {
            // TODO: Decide who sets the guid, Rules or Exchange
            if (fact.getRawMovementType().getGuid() == null) {
                fact.getRawMovementType().setGuid(UUID.randomUUID().toString());
            }

            AlarmReportType alarmReport = new AlarmReportType();
            alarmReport.setGuid(UUID.randomUUID().toString());
            alarmReport.setOpenDate(RulesUtil.dateToString(new Date()));
            alarmReport.setStatus(AlarmStatusType.OPEN);
            alarmReport.setRawMovement(fact.getRawMovementType());

            // Alarm item
            List<AlarmItemType> alarmItems = new ArrayList<AlarmItemType>();
            AlarmItemType alarmItem = new AlarmItemType();
            alarmItem.setGuid(UUID.randomUUID().toString());
            alarmItem.setRuleName(ruleName);
            alarmItems.add(alarmItem);
            alarmReport.getAlarmItem().addAll(alarmItems);

            String request = RulesDataSourceRequestMapper.mapCreateAlarmReport(alarmReport);
            producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    // Triggered by rule engine, no response expected
    @Override
    public void customRuleTriggered(String ruleName, MovementFact fact, String actions) throws RulesServiceException {
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
                LOG.info("Performing action '{}' with value '{}'", action, value);
                break;
            case ON_HOLD:
                LOG.info("Performing action '{}' with value '{}'", action, value);
                break;
            case TICKET:
                LOG.info("Performing action '{}' with value '{}'", action, value);
                createTicket(ruleName, fact);
                break;
            case MANUAL_POLL:
                LOG.info("Performing action '{}' with value '{}'", action, value);
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
            case ALARM:
            default:
                // Unreachable, ActionType.valueOf(action) would fail before
                LOG.info("The action '{}' is not defined", action);
                break;
            }
        }

    }

    private void createTicket(String ruleName, MovementFact fact) throws RulesServiceException {
        LOG.info("Create ticket invoked in service layer");
        try {
            TicketType ticket = new TicketType();

            ticket.setVesselGuid(fact.getVesselGuid());
            ticket.setOpenDate(RulesUtil.dateToString(new Date()));
            ticket.setRuleName(ruleName);
            ticket.setStatus(TicketStatusType.OPEN);

            MovementType m = new MovementType();
            m.setLatitude(fact.getLatitude());
            m.setLongitude(fact.getLongitude());
            m.setTimestamp(RulesUtil.dateToString(fact.getPositionTime()));
            ticket.setMovement(m);

            String request = RulesDataSourceRequestMapper.mapCreateTicket(ticket);
            producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType getById(Long id) throws RulesServiceException {
        LOG.info("Update invoked in service layer");
        throw new RulesServiceException("Update not implemented in service layer");

    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType getByGuid(String guid) throws RulesServiceException, RulesModelMapperException, RulesFaultException {
        LOG.info("Get by id invoked in service layer");
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
     * @param data
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType updateCustomRule(CustomRuleType customRule) throws RulesServiceException {
        LOG.info("Update custom rule invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapUpdateCustomRule(customRule);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToUpdateCustomRuleFromResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    // Triggered by RulesTimerBean
    @Override
    public List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException {
        LOG.info("Get previous movement reports invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapGetPreviousReport();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToGetPreviousReportResponse(response);
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }
    }

    // Triggered by timer rule
    @Override
    public void timerRuleTriggered(String ruleName, PreviousReportFact fact) throws RulesServiceException {
        LOG.info("Timer rule triggered invoked in service layer");
        try {
            // Check if ticket already is created for this vessel
            String getTicketRequest = RulesDataSourceRequestMapper.mapGetTicketByVesselGuid(fact.getVesselGuid());
            String messageId = producer.sendDataSourceMessage(getTicketRequest, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);

            boolean noTicketCreated = RulesDataSourceResponseMapper.mapToGetTicketByVesselGuidFromResponse(response).getTicket() == null;

            if (noTicketCreated) {
                TicketType ticket = new TicketType();

                ticket.setVesselGuid(fact.getVesselGuid());
                ticket.setOpenDate(RulesUtil.dateToString(new Date()));
                ticket.setRuleName(ruleName);
                ticket.setStatus(TicketStatusType.OPEN);

                String createTicketRequest = RulesDataSourceRequestMapper.mapCreateTicket(ticket);
                producer.sendDataSourceMessage(createTicketRequest, DataSourceQueue.INTERNAL);
            }
        } catch (RulesModelMapperException | MessageException ex) {
            throw new RulesServiceException(ex.getMessage());
        }

    }

}
