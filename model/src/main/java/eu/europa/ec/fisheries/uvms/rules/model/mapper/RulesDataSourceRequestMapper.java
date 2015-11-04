package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.source.v1.*;
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

    final static Logger LOG = LoggerFactory.getLogger(RulesDataSourceRequestMapper.class);

    // Custom rule
    public static String mapCreateCustomRule(CustomRuleType customRule) throws RulesModelMapperException {
        CreateCustomRuleRequest request = new CreateCustomRuleRequest();
        request.setCustomRule(customRule);
        request.setMethod(RulesDataSourceMethod.CREATE_CUSTOM_RULE);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapCustomRuleList() throws RulesModelMapperException {
        GetCustomRuleListRequest request = new GetCustomRuleListRequest();
        request.setMethod(RulesDataSourceMethod.LIST_CUSTOM_RULES);
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
    public static String mapTicketList(TicketQuery query) throws RulesModelMapperException {
        GetTicketListByQueryRequest request = new GetTicketListByQueryRequest();
        request.setMethod(RulesDataSourceMethod.LIST_TICKETS);
        request.setQuery(query);
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

    public static String mapUpdateAlarmStatus(AlarmReportType alarm) throws RulesModelMapperException {
        SetAlarmStatusRequest request = new SetAlarmStatusRequest();
        request.setAlarm(alarm);
        request.setMethod(RulesDataSourceMethod.SET_ALARM_STATUS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapGetTicketByVesselGuid(String vesselGuid) throws RulesModelMapperException {
        GetTicketByVesselGuidRequest request = new GetTicketByVesselGuidRequest();
        request.setVesselGuid(vesselGuid);
        request.setMethod(RulesDataSourceMethod.GET_TICKET_BY_VESSEL_GUID);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    // Previous report
    public static String mapGetPreviousReport() throws RulesModelMapperException {
        GetPreviousReportRequest request = new GetPreviousReportRequest();
        request.setMethod(RulesDataSourceMethod.GET_PREVIOUS_REPORTS);
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

    public static String mapReprocessAlarms(List<String> alarmGuids) throws RulesModelMapperException {
        ReprocessAlarmRequest request = new ReprocessAlarmRequest();
        request.getAlarmReportGuid().addAll(alarmGuids);
        request.setMethod(RulesDataSourceMethod.REPROCESS_ALARMS);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

}
