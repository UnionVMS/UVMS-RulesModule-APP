package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.RawPositionReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.RulesParameterService;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Stateless
public class RulesServiceBean implements RulesService {

    final static Logger LOG = LoggerFactory.getLogger(RulesServiceBean.class);

    @EJB
    RulesParameterService parameterService;

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
    public AlarmType updateAlarm(AlarmType alarm) throws RulesServiceException {
        LOG.info("Update alarm invoked in service layer - NOT IMPLEMENTED");
        return null;
    }

    @Override
    public TicketType updateTicket(TicketType ticket) throws RulesServiceException {
        LOG.info("Update ticket invoked in service layer - NOT IMPLEMENTED");
        return null;
    }

    // Triggered by rule engine, no response expected
    @Override
    public void createAlarmReport(String ruleName, RawFact fact) throws RulesServiceException {
        LOG.info("Create alarm invoked in service layer");
        try {
            AlarmType alarm = new AlarmType();
            alarm.setOpenDate(RulesUtil.dateToString(new Date()));
            alarm.setSender("DummyFlagState");
            alarm.setStatus(AlarmStatusType.OPEN);
            alarm.setRuleTriggered(ruleName);

            RawPositionReportType rawPosition = new RawPositionReportType();
            rawPosition.setLatitude(fact.getLatitude());
            rawPosition.setLongitude(fact.getLongitude());

            if (fact.getRawMovementType().getPositionTime() != null) {
                Date timestamp = fact.getRawMovementType().getPositionTime().toGregorianCalendar().getTime();
                rawPosition.setTimestamp(RulesUtil.dateToString(timestamp));
            }

            rawPosition.setGuid(fact.getRawMovementType().getGuid());
            alarm.setRawPositionReport(rawPosition);

            String request = RulesDataSourceRequestMapper.mapCreateAlarmReport(alarm);
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

            AssetIdType assetIdType = new AssetIdType();
            assetIdType.setType(fact.getMovementType().getAssetId().getIdType().name());
            assetIdType.setValue(fact.getMovementType().getAssetId().getValue());
            ticket.setAssetId(assetIdType);

            ticket.setOpenDate(RulesUtil.dateToString(new Date()));
            ticket.setRuleName(ruleName);
            ticket.setStatus(TicketStatusType.OPEN);

            MovementType m = new MovementType();
            m.setLatitude(fact.getMovementType().getPosition().getLatitude());
            m.setLongitude(fact.getMovementType().getPosition().getLongitude());
            m.setTimestamp(RulesUtil.dateToString(fact.getMovementType().getPositionTime().toGregorianCalendar().getTime()));
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
        LOG.info("Get by id invoked in service layer");
        throw new RulesServiceException("Get by id not implemented in service layer");
    }

    /**
     * {@inheritDoc}
     *
     * @param data
     * @throws RulesServiceException
     */
    @Override
    public CustomRuleType update(CustomRuleType customRuleType) throws RulesServiceException {
        LOG.info("Update invoked in service layer");
        throw new RulesServiceException("Update not implemented in service layer");
    }

}
