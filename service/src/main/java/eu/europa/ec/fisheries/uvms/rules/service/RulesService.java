package eu.europa.ec.fisheries.uvms.rules.service;

import java.util.List;

import javax.ejb.Local;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
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
    public CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesServiceException;

    /**
     * Lists (all) custom rules
     *
     * @return
     * @throws RulesServiceException
     */
    public List<CustomRuleType> getCustomRuleList() throws RulesServiceException;

    /**
     * Lists alarms by query
     *
     * @return
     * @throws RulesServiceException
     */
    public GetAlarmListByQueryResponse getAlarmList(AlarmQuery query) throws RulesServiceException;

    /**
     * Lists tickets by query
     *
     * @return
     * @throws RulesServiceException
     */
    public GetTicketListByQueryResponse getTicketList(TicketQuery query) throws RulesServiceException;

    /**
     * Update a ticket status
     *
     * @param ticket
     * @throws RulesServiceException
     */
    public TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException;

    /**
     * Get an object by id
     *
     * @param id
     * @return
     * @throws RulesServiceException
     */
    public CustomRuleType getById(Long id) throws RulesServiceException;

    /**
     * Update an object
     *
     * @param data
     * @throws RulesServiceException
     */
    public CustomRuleType updateCustomRule(CustomRuleType customRuleType) throws RulesServiceException;

    /**
     * Creates an error report
     *
     * @param comment
     *            note on the error occured
     * @param guid
     *            the offending guid
     * @throws RulesServiceException
     */
    public void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException;

    /**
     * Entry point of action performed as a result of a custom rule triggered
     *
     * @param f
     *            the fact that triggered the rule
     * @param action
     *            the action(s) to be performed
     */
    public void customRuleTriggered(String ruleName, MovementFact f, String action) throws RulesServiceException;

    /**
     * Get a custom rule by guid
     *
     * @param guid
     * @return
     * @throws RulesServiceException, RulesModelMapperException, RulesFaultException
     */
    public CustomRuleType getByGuid(String guid) throws RulesServiceException, RulesModelMapperException, RulesFaultException;

    public AlarmReportType updateAlarmStatus(AlarmReportType ticket) throws RulesServiceException;

    public List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException;

    public void timerRuleTriggered(String ruleName, PreviousReportFact fact) throws RulesServiceException;

}
