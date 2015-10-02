package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateAlarmReportRequest;
import eu.europa.ec.fisheries.schema.rules.source.v1.CreateCustomRuleRequest;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryRequest;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetCustomRuleListRequest;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryRequest;
import eu.europa.ec.fisheries.schema.rules.source.v1.RulesDataSourceMethod;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;

public class RulesDataSourceRequestMapper {

    final static Logger LOG = LoggerFactory.getLogger(RulesDataSourceRequestMapper.class);

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

    public static String mapCreateAlarmReport(String comment, String guid) throws RulesModelMapperException {
        CreateAlarmReportRequest request = new CreateAlarmReportRequest();
        request.setMethod(RulesDataSourceMethod.CREATE_ALARM_REPORT);
        request.setComment(comment);
        request.setOffendingGuid(guid);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapAlarmList(AlarmQuery query) throws RulesModelMapperException {
        GetAlarmListByQueryRequest request = new GetAlarmListByQueryRequest();
        request.setMethod(RulesDataSourceMethod.LIST_ALARMS);
        request.setQuery(query);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String mapTicketList(TicketQuery query) throws RulesModelMapperException {
        GetTicketListByQueryRequest request = new GetTicketListByQueryRequest();
        request.setMethod(RulesDataSourceMethod.LIST_TICKETS);
        request.setQuery(query);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

}
