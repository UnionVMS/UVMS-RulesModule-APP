package eu.europa.ec.fisheries.uvms.rules.service;

import java.util.List;

import javax.ejb.Local;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Local
public interface RulesService {

    /**
     * Creates a new custom rule
     *
     * @param customRule
     *            the rule to be added
     * @return
     * @throws RulesServiceException
     */
    CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesServiceException;

    /**
     * Lists (all) custom rules
     *
     * @return
     * @throws RulesServiceException
     */
//    List<CustomRuleType> getCustomRuleList() throws RulesServiceException;

    /**
     * Lists alarms by query
     *
     * @return
     * @throws RulesServiceException
     */
    GetAlarmListByQueryResponse getAlarmList(AlarmQuery query) throws RulesServiceException;

    /**
     * Lists tickets by query
     *
     * @return
     * @throws RulesServiceException
     */
    GetTicketListByQueryResponse getTicketList(TicketQuery query) throws RulesServiceException;

    /**
     * Update a ticket status
     *
     * @param ticket
     * @throws RulesServiceException
     */
    TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException;

    /**
     * Get an object by id
     *
     * @param id
     * @return
     * @throws RulesServiceException
     */
    CustomRuleType getById(Long id) throws RulesServiceException;

    /**
     * Update an object
     *
     * @param customRuleType
     * @throws RulesServiceException
     */
    CustomRuleType updateCustomRule(CustomRuleType customRuleType) throws RulesServiceException;

    /**
     * Creates an error report
     *
     * @param ruleName
     * @param fact
     * @throws RulesServiceException
     */
//    void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException;

    /**
     * Entry point of action performed as a result of a custom rule triggered
     *
     * @param f
     *            the fact that triggered the rule
     * @param action
     *            the action(s) to be performed
     */
//    void customRuleTriggered(String ruleName, String ruleGuid, MovementFact f, String action) throws RulesServiceException;

    /**
     * Get a custom rule by guid
     *
     * @param guid
     * @return
     * @throws RulesServiceException, RulesModelMapperException, RulesFaultException
     */
    CustomRuleType getByGuid(String guid) throws RulesServiceException, RulesModelMapperException, RulesFaultException;

    AlarmReportType updateAlarmStatus(AlarmReportType ticket) throws RulesServiceException;

    List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException;

    void timerRuleTriggered(String ruleName, String ruleGuid, PreviousReportFact fact) throws RulesServiceException;

    String reprocessAlarm(List<String> alarms) throws RulesServiceException;

    MovementRefType setMovementReportReceived(RawMovementType rawMovementType, String pluginType) throws RulesServiceException;

    /**
     * @param guid the GUID of an alarm
     * @return an alarm
     * @throws RulesServiceException if unsuccessful
     */
    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesServiceException;

    /**
     * @param guid the GUID of a ticket
     * @return a ticket
     * @throws RulesServiceException if unsuccessful
     */
    public TicketType getTicketByGuid(String guid) throws RulesServiceException;

}
