package eu.europa.ec.fisheries.uvms.rules.service;

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
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import javax.ejb.Local;
import java.util.List;

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
    CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesServiceException, RulesFaultException;

    /**
     * Lists (all) custom rules
     *
     * @return
     * @throws RulesServiceException
     */
//    List<CustomRuleType> getCustomRuleList() throws RulesServiceException;

    CustomRuleType deleteCustomRule(String guid) throws RulesServiceException, RulesFaultException;

    /**
     * Lists alarms by query
     *
     * @return
     * @throws RulesServiceException
     */
    GetAlarmListByQueryResponse getAlarmList(AlarmQuery query) throws RulesServiceException, RulesFaultException;

    /**
     * Lists tickets by query
     *
     * @return
     * @throws RulesServiceException
     */
    GetTicketListByQueryResponse getTicketList(TicketQuery query) throws RulesServiceException, RulesFaultException;

    /**
     * Update a ticket status
     *
     * @param ticket
     * @throws RulesServiceException
     */
    TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException, RulesFaultException;

    /**
     * Update an object
     *
     * @param customRuleType
     * @throws RulesServiceException
     */
    CustomRuleType updateCustomRule(CustomRuleType customRuleType) throws RulesServiceException, RulesFaultException;

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
    CustomRuleType getCustomRuleByGuid(String guid) throws RulesServiceException, RulesModelMapperException, RulesFaultException;

    AlarmReportType updateAlarmStatus(AlarmReportType ticket) throws RulesServiceException, RulesFaultException;

    List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException, RulesFaultException;

    void timerRuleTriggered(String ruleName, String ruleGuid, PreviousReportFact fact) throws RulesServiceException, RulesFaultException;

    String reprocessAlarm(List<String> alarms) throws RulesServiceException, RulesFaultException;

    MovementRefType setMovementReportReceived(RawMovementType rawMovementType, String pluginType) throws RulesServiceException;

    /**
     * @param guid the GUID of an alarm
     * @return an alarm
     * @throws RulesServiceException if unsuccessful
     */
    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesServiceException, RulesFaultException;

    /**
     * @param guid the GUID of a ticket
     * @return a ticket
     * @throws RulesServiceException if unsuccessful
     */
    public TicketType getTicketByGuid(String guid) throws RulesServiceException, RulesFaultException;

}
