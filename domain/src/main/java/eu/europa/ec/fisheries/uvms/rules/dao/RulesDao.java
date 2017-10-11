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
package eu.europa.ec.fisheries.uvms.rules.dao;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import eu.europa.ec.fisheries.uvms.rules.entity.*;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.AlarmSearchValue;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.CustomRuleSearchValue;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.TicketSearchValue;

import javax.ejb.Local;
import java.util.List;

@Local
public interface RulesDao {

    /**
     * Create custom rule in database
     *
     * @param customRule
     *            the rule to be persisted
     * @return
     * @throws DaoException
     */
    CustomRule createCustomRule(CustomRule customRule) throws DaoException;


    /**
     * Update entity in database
     *
     * @param customRule
     *            the rule to be updated
     * @return
     * @throws DaoException
     */
    CustomRule updateCustomRule(CustomRule customRule) throws DaoException;

    List<CustomRule> getRunnableCustomRuleList() throws DaoException;

    List<SanityRule> getSanityRules() throws DaoException;

    /**
     * Get all custom rules
     *
     * @return
     * @throws DaoException
     */
    List<CustomRule> getCustomRulesByUser(String owner) throws DaoException;

    // Triggered by rules engine
    /**
     * Get all entities (FIND_ALL)
     *
     * @return AlarmReport
     * @throws DaoException
     */
    AlarmReport createAlarmReport(AlarmReport errorReport) throws DaoException;

    /**
     * Get all error reports on a specific offending guid
     *
     * @param guid
     *            the guid of the object that caused the error report, for
     *            instance a faulty position report
     * @return AlarmReport
     * @throws DaoException
     */
    AlarmReport getOpenAlarmReportByMovementGuid(String guid) throws DaoException;

    @SuppressWarnings("unchecked")
    Long getCustomRuleListSearchCount(String countSql, List<CustomRuleSearchValue> searchKeyValues) throws DaoException;

    @SuppressWarnings("unchecked")
    List<CustomRule> getCustomRuleListPaginated(Integer page, Integer listSize, String sql, List<CustomRuleSearchValue> searchKeyValues)
            throws DaoException;

    Long getAlarmListSearchCount(String countSql, List<AlarmSearchValue> searchKeyValues) throws DaoException;

    List<AlarmReport> getAlarmListPaginated(Integer page, Integer listSize, String sql, List<AlarmSearchValue> searchKeyValues)
            throws DaoException;

    Long getTicketListSearchCount(String countSql, List<TicketSearchValue> searchKeyValues) throws DaoException;

    List<Ticket> getTicketListPaginated(Integer page, Integer listSize, String sql, List<TicketSearchValue> searchKeyValues)
            throws DaoException;

    Ticket createTicket(Ticket ticket) throws DaoException;

    Ticket getTicketByGuid(String guid) throws DaoException;

    void removeSubscription(RuleSubscription entity) throws DaoException;

    void detachSubscription(RuleSubscription entity) throws DaoException;

    Ticket updateTicket(Ticket entity) throws DaoException;

    AlarmReport updateAlarm(AlarmReport entity) throws DaoException;

    List<Ticket> getTicketsByMovements(List<String> movements) throws DaoException;

    long countTicketListByMovements(List<String> movements) throws DaoException;

    List<String> getCustomRulesForTicketsByUser(String userName) throws DaoException;

    /**
     * @return number of open alarms
     * @throws DaoException
     */
    long getNumberOfOpenAlarms() throws DaoException;

    /**
     * @return number of open tickets
     * @throws DaoException
     */
    long getNumberOfOpenTickets(List<String> validRuleGuids) throws DaoException;

    AlarmReport getAlarmReportByGuid(String guid) throws DaoException;

    List<Ticket> getTicketList(String sql, List<TicketSearchValue> searchKeyValues) throws DaoException;

    List<PreviousReport> getPreviousReportList() throws DaoException;

    Ticket getTicketByAssetAndRule(String assetGuid, String ruleGuid) throws DaoException;

    CustomRule getCustomRuleByGuid(String id) throws DaoException;

    // Used by timer to prevent duplicate tickets for passing the reporting
    // deadline
    AlarmReport getAlarmReportByAssetAndRule(String assetGuid, String ruleGuid) throws DaoException;

    PreviousReport getPreviousReportByAssetGuid(String assetGuid);

    void updatePreviousReport(PreviousReport report) throws DaoException;

    long getNumberOfTicketsByRuleGuid(String ruleGuid) throws DaoException;

    List<Template> getAllFactTemplates() throws DaoException;

    void updatedFailedRules(List<String> brIds) throws DaoException;

    void saveValidationMessages(List<RawMessage> rawMessages) throws DaoException;

    List<ValidationMessage> getValidationMessagesById(List<String> ids) throws DaoException;

    RuleStatusType checkRuleStatus() throws DaoException;

    void createRuleStatus(RuleStatus ruleStatus) throws DaoException;

    void deleteRuleStatus() throws DaoException;

    List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode) throws DaoException;

    List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode, boolean onlyMandatory) throws DaoException;

    List<String> getAllFishingGearTypeCodes() throws DaoException;

    List<FishingGearTypeCharacteristic> getAllFishingGearTypeCharacteristics() throws DaoException;
}