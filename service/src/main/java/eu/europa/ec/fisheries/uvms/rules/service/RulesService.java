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
package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import javax.ejb.Local;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Local
public interface RulesService {

    /**
     * Creates a new custom rule
     *
     * @param customRule the rule to be added
     * @return
     * @throws RulesServiceException
     */
    CustomRuleType createCustomRule(CustomRuleType customRule, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException;

    CustomRuleType updateSubscription(UpdateSubscriptionType updateSubscriptionType, String username) throws RulesServiceException, RulesFaultException;

    /**
     * Lists (all) custom rules
     *
     * @return
     * @throws RulesServiceException
     */
//    List<CustomRuleType> getCustomRuleList() throws RulesServiceException;

    CustomRuleType deleteCustomRule(String guid, String username, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException;

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
    GetTicketListByQueryResponse getTicketList(String loggedInUser, TicketQuery query) throws RulesServiceException, RulesFaultException;

    GetTicketListByMovementsResponse getTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException;

    long countTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException;

    /**
     * Update a ticket status
     *
     * @param ticket
     * @throws RulesServiceException
     */
    TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException, RulesFaultException;

    /**
     * Update a ticket count, for Asset not sending tickets
     *
     * @param ticket
     * @throws RulesServiceException
     */
    TicketType updateTicketCount(TicketType ticket) throws RulesServiceException, RulesFaultException;

    /**
     * Update an object
     *
     * @param customRuleType
     * @throws RulesServiceException
     */
    CustomRuleType updateCustomRule(CustomRuleType customRuleType, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException;

    /**
     * Update an object
     *
     * @param customRuleType
     * @throws RulesServiceException
     */
    CustomRuleType updateCustomRule(CustomRuleType oldCustomRule) throws RulesServiceException, RulesFaultException;

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

    void timerRuleTriggered(String ruleName, PreviousReportFact fact) throws RulesServiceException, RulesFaultException;

    String reprocessAlarm(List<String> alarms, String username) throws RulesServiceException, RulesFaultException;

    void setMovementReportReceived(RawMovementType rawMovementType, String pluginType, String username, String jmsXGroupId) throws RulesServiceException;

    /**
     * @param guid the GUID of an alarm
     * @return an alarm
     * @throws RulesServiceException if unsuccessful
     */
    AlarmReportType getAlarmReportByGuid(String guid) throws RulesServiceException, RulesFaultException;

    /**
     * @param guid the GUID of a ticket
     * @return a ticket
     * @throws RulesServiceException if unsuccessful
     */
    TicketType getTicketByGuid(String guid) throws RulesServiceException, RulesFaultException;

    List<TicketType> updateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesServiceException, RulesFaultException;

    long getNumberOfAssetsNotSending() throws RulesServiceException, RulesFaultException;

    GetTicketsAndRulesByMovementsResponse getTicketsAndRulesByMovements(List<String> movements) throws RulesServiceException;
}
