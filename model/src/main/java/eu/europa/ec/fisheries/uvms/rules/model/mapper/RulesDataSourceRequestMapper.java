package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.*;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;

import java.util.List;

public class RulesDataSourceRequestMapper {

    private final static Logger LOG = LoggerFactory.getLogger(RulesDataSourceRequestMapper.class);

    // Custom rule
    public static String mapCreateCustomRule(CustomRuleType customRule) throws RulesModelMapperException {
        CreateCustomRuleRequest request = new CreateCustomRuleRequest();
        request.setCustomRule(customRule);
        request.setMethod(RulesDataSourceMethod.CREATE_CUSTOM_RULE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetCustomRulesByUser(String userName) throws RulesModelMapperException {
        GetCustomRulesByUserRequest request = new GetCustomRulesByUserRequest();
        request.setUserName(userName);
        request.setMethod(RulesDataSourceMethod.LIST_CUSTOM_RULES_BY_USER);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetRunnableCustomRules() throws RulesModelMapperException {
        GetRunnableCustomRulesRequest request = new GetRunnableCustomRulesRequest();
        request.setMethod(RulesDataSourceMethod.GET_RUNNABLE_CUSTOM_RULES);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetSanityRules() throws RulesModelMapperException {
        GetRunnableCustomRulesRequest request = new GetRunnableCustomRulesRequest();
        request.setMethod(RulesDataSourceMethod.GET_SANITY_RULES);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapCustomRuleListByQuery(CustomRuleQuery query) throws RulesModelMapperException {
        GetCustomRuleListByQueryRequest request = new GetCustomRuleListByQueryRequest();
        request.setMethod(RulesDataSourceMethod.LIST_CUSTOM_RULES_BY_QUERY);
        request.setQuery(query);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetCustomRule(String guid) throws RulesModelMapperException {
        GetCustomRuleRequest request = new GetCustomRuleRequest();
        request.setMethod(RulesDataSourceMethod.GET_CUSTOM_RULE);
        request.setGuid(guid);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpdateCustomRule(CustomRuleType customRule) throws RulesModelMapperException {
        UpdateCustomRuleRequest request = new UpdateCustomRuleRequest();
        request.setCustomRule(customRule);
        request.setMethod(RulesDataSourceMethod.UPDATE_CUSTOM_RULE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapDeleteCustomRule(String guid) throws RulesModelMapperException {
        DeleteCustomRuleRequest request = new DeleteCustomRuleRequest();
        request.setGuid(guid);
        request.setMethod(RulesDataSourceMethod.DELETE_CUSTOM_RULE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpdateCustomRuleLastTriggered(String ruleGuid) throws RulesModelMapperException {
        UpdateCustomRuleLastTriggeredRequest request = new UpdateCustomRuleLastTriggeredRequest();
        request.setCustomRuleGuid(ruleGuid);
        request.setMethod(RulesDataSourceMethod.UPDATE_CUSTOM_RULE_LAST_TRIGGERED);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpdateSubscription(UpdateSubscriptionType updateSubscriptionType) throws RulesModelMapperException {
        UpdateCustomRuleSubscriptionRequest request = new UpdateCustomRuleSubscriptionRequest();
        request.setSubscription(updateSubscriptionType);
        request.setMethod(RulesDataSourceMethod.UPDATE_CUSTOM_RULE_SUBSCRIPTION);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    // Alarm
    public static String mapCreateAlarmReport(AlarmReportType alarm) throws RulesModelMapperException {
        CreateAlarmReportRequest request = new CreateAlarmReportRequest();
        request.setMethod(RulesDataSourceMethod.CREATE_ALARM_REPORT);
        request.setAlarm(alarm);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapAlarmList(AlarmQuery query) throws RulesModelMapperException {
        GetAlarmListByQueryRequest request = new GetAlarmListByQueryRequest();
        request.setMethod(RulesDataSourceMethod.LIST_ALARMS);
        request.setQuery(query);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    // Tickets
    public static String mapTicketList(String loggedInUser, TicketQuery query) throws RulesModelMapperException {
        GetTicketListByQueryRequest request = new GetTicketListByQueryRequest();
        request.setMethod(RulesDataSourceMethod.LIST_TICKETS);
        request.setLoggedInUser(loggedInUser);
        request.setQuery(query);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapTicketsByMovements(List<String> movements) throws RulesModelMapperException {
        GetTicketListByMovementsRequest request = new GetTicketListByMovementsRequest();
        request.setMethod(RulesDataSourceMethod.GET_TICKETS_BY_MOVEMENTS);
        request.getMovementGuids().addAll(movements);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapCountTicketsByMovements(List<String> movements) throws RulesModelMapperException {
        CountTicketListByMovementsRequest request = new CountTicketListByMovementsRequest();
        request.setMethod(RulesDataSourceMethod.COUNT_TICKETS_BY_MOVEMENTS);
        request.getMovementGuids().addAll(movements);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapCreateTicket(TicketType ticket) throws RulesModelMapperException {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setMethod(RulesDataSourceMethod.CREATE_TICKET);
        request.setTicket(ticket);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpdateTicketStatus(TicketType ticket) throws RulesModelMapperException {
        SetTicketStatusRequest request = new SetTicketStatusRequest();
        request.setTicket(ticket);
        request.setMethod(RulesDataSourceMethod.SET_TICKET_STATUS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpdateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesModelMapperException {
        UpdateTicketStatusByQueryRequest request = new UpdateTicketStatusByQueryRequest();
        request.setLoggedInUser(loggedInUser);
        request.setStatus(status);
        request.setQuery(query);
        request.setMethod(RulesDataSourceMethod.UPDATE_TICKET_STATUS_BY_QUERY);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpdateAlarmStatus(AlarmReportType alarm) throws RulesModelMapperException {
        SetAlarmStatusRequest request = new SetAlarmStatusRequest();
        request.setAlarm(alarm);
        request.setMethod(RulesDataSourceMethod.SET_ALARM_STATUS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetTicketByAssetAndRule(String assetGuid, String ruleGuid) throws RulesModelMapperException {
        GetTicketByAssetAndRuleRequest request = new GetTicketByAssetAndRuleRequest();
        request.setAssetGuid(assetGuid);
        request.setRuleGuid(ruleGuid);
        request.setMethod(RulesDataSourceMethod.GET_TICKET_BY_ASSET_AND_RULE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetAlarmReportByAssetAndRule(String assetGuid, String ruleGuid) throws RulesModelMapperException {
        GetAlarmReportByAssetAndRuleRequest request = new GetAlarmReportByAssetAndRuleRequest();
        request.setAssetGuid(assetGuid);
        request.setRuleGuid(ruleGuid);
        request.setMethod(RulesDataSourceMethod.GET_ALARM_REPORT_BY_ASSET_AND_RULE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    // Previous reports
    public static String mapGetPreviousReports() throws RulesModelMapperException {
        GetPreviousReportsRequest request = new GetPreviousReportsRequest();
        request.setMethod(RulesDataSourceMethod.GET_PREVIOUS_REPORTS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetPreviousReportByAssetGuid(String assetGuid) throws RulesModelMapperException {
        GetPreviousReportByAssetGuidRequest request = new GetPreviousReportByAssetGuidRequest();
        request.setMethod(RulesDataSourceMethod.GET_PREVIOUS_REPORT_BY_ASSET_GUID);
        request.setAssetGuid(assetGuid);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapUpsertPreviousReport(PreviousReportType report) throws RulesModelMapperException {
        UpsertPreviousReportRequest request = new UpsertPreviousReportRequest();
        request.setMethod(RulesDataSourceMethod.UPSERT_PREVIOUS_REPORT);
        request.setPreviousReport(report);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetTicketByGuid(String guid) throws RulesModelMarshallException {
        GetTicketRequest request = new GetTicketRequest();
        request.setGuid(guid);
        request.setMethod(RulesDataSourceMethod.GET_TICKET);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetAlarmByGuid(String guid) throws RulesModelMarshallException {
        GetAlarmRequest request = new GetAlarmRequest();
        request.setMethod(RulesDataSourceMethod.GET_ALARM);
        request.setGuid(guid);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String getNumberOfOpenAlarmReports() throws RulesModelMarshallException {
        GetNumberOfOpenAlarmsRequest request = new GetNumberOfOpenAlarmsRequest();
        request.setMethod(RulesDataSourceMethod.GET_NUMBER_OF_OPEN_ALARMS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String getNumberOfOpenTickets(String userName) throws RulesModelMarshallException {
        GetNumberOfOpenTicketsRequest request = new GetNumberOfOpenTicketsRequest();
        request.setUserName(userName);
        request.setMethod(RulesDataSourceMethod.GET_NUMBER_OF_OPEN_TICKETS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapReprocessAlarms(List<String> alarmGuids) throws RulesModelMapperException {
        ReprocessAlarmRequest request = new ReprocessAlarmRequest();
        request.getAlarmReportGuid().addAll(alarmGuids);
        request.setMethod(RulesDataSourceMethod.REPROCESS_ALARMS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

}
