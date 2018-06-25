package eu.europa.ec.fisheries.uvms.rules.domain;
import javax.ejb.Local;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.CustomRuleListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;

@Local
public interface RulesDomainModel {

    List<TemplateRuleMapDto> getAllFactTemplatesAndRules() throws RulesModelException;

    CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesModelException;

    CustomRuleType updateCustomRule(CustomRuleType model) throws RulesModelException;

    AlarmReportType setAlarmStatus(AlarmReportType alarm) throws RulesModelException;

    List<CustomRuleType> getRunnableCustomRuleList() throws RulesModelException;

    List<SanityRuleType> getSanityRuleList() throws RulesModelException;

    List<CustomRuleType> getCustomRulesByUser(String updatedBy) throws RulesModelException;

    CustomRuleListResponseDto getCustomRuleListByQuery(CustomRuleQuery query) throws RulesModelException;

    AlarmReportType createAlarmReport(AlarmReportType alarm) throws RulesModelException;

    AlarmListResponseDto getAlarmListByQuery(AlarmQuery query) throws RulesModelException;

    TicketListResponseDto getTicketListByQuery(String loggedInUser, TicketQuery query) throws RulesModelException;

    TicketType createTicket(TicketType ticket) throws RulesModelException;

    CustomRuleType updateCustomRuleSubscription(UpdateSubscriptionType updateSubscriptionType) throws RulesModelException;

    CustomRuleType deleteCustomRule(String guid) throws RulesModelException;

    CustomRuleType updateLastTriggeredCustomRule(String ruleGuid) throws RulesModelException;

    TicketType setTicketStatus(TicketType ticket) throws RulesModelException;

    TicketType updateTicketCount(TicketType ticket) throws RulesModelException;

    List<TicketType> updateTicketStatusByQuery(String userName, TicketQuery query, TicketStatusType status) throws RulesModelException;

    CustomRuleType getByGuid(String guid) throws RulesModelException;

    TicketListResponseDto getTicketListByMovements(List<String> movements) throws RulesModelException;

    long countTicketListByMovements(List<String> movements) throws RulesModelException;

    List<PreviousReportType> getPreviousReports() throws RulesModelException;

    PreviousReportType getPreviousReportByAssetGuid(String assetGuid) throws RulesModelException;

    TicketType getTicketByAssetGuid(String assetGuid, String ruleGuid) throws RulesModelException;

    TicketType getTicketByGuid(String guid) throws RulesModelException;

    AlarmReportType getAlarmReportByAssetAndRule(String assetGuid, String ruleGuid) throws RulesModelException;

    void upsertPreviousReport(PreviousReportType previousReport) throws RulesModelException;

    AlarmReportType getAlarmReportByGuid(String guid) throws RulesModelException;

    /**
     * @return number of open alarms
     * @throws RulesModelException
     */
    long getNumberOfOpenAlarms() throws RulesModelException;

    /**
     * @return number of open tickets
     * @throws RulesModelException
     */
    long getNumberOfOpenTickets(String userName) throws RulesModelException;

    long getNumberOfAssetsNotSending() throws RulesModelException;

    List<TicketAndRuleType> getTicketsAndRulesByMovements(List<String> movementGuids) throws RulesModelException;

    void updateFailedRules(List<String> failedRules) throws RulesModelException;

    void saveValidationMessages(RawMessageType rawMessageType) throws RulesModelException;

    List<ValidationMessageType> getValidationMessagesById(List<String> ids) throws RulesModelException;

    List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode) throws RulesModelException;

    List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode, boolean mandatory) throws RulesModelException;

    List<String> getAllFishingGearTypeCodes() throws RulesModelException;
}