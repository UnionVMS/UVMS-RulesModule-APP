package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.schema.rules.source.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.module.v1.CreateCustomRuleResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetCustomRuleListResponse;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetPreviousReportsResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;

public class RulesDataSourceResponseMapper {

    final static Logger LOG = LoggerFactory.getLogger(RulesDataSourceResponseMapper.class);

    /**
     * Validates a response
     *
     * @param response
     * @param correlationId
     * @throws RulesModelMapperException
     * @throws JMSException
     */
    private static void validateResponse(TextMessage response, String correlationId) throws RulesModelMapperException, JMSException, RulesFaultException {

        if (response == null) {
            LOG.error("[ Error when validating response in ResponseMapper: Response is Null ]");
            throw new RulesModelMapperException("[ Error when validating response in ResponseMapper: Response is Null ]");
        }

        if (response.getJMSCorrelationID() == null) {
            LOG.error("[ No corelationId in response.] Expected was: {} ", correlationId);
            throw new RulesModelMapperException("[ No corelationId in response (Null) ] . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            LOG.error("[ Wrong corelationId in response. Expected was {0} But actual was: {1} ]", correlationId, response.getJMSCorrelationID());
            throw new RulesModelMapperException("[ Wrong corelationId in response. ] Expected was: " + correlationId + "But actual was: "
                    + response.getJMSCorrelationID());
        }

        try {
            RulesFault rulesFault = JAXBMarshaller.unmarshallTextMessage(response, RulesFault.class);
            throw  new RulesFaultException(response.getText(), rulesFault);
        } catch (RulesModelMarshallException e) {
            // Do nothing because the response message is not a error message. Can continue to parse the response message to a GetCustomRuleResponse
        }

    }

    public static CustomRuleType mapToCreateCustomRuleFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        CreateCustomRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, CreateCustomRuleResponse.class);
        return response.getCustomRule();
    }

    public static TicketType mapSingleTicketFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        SingleTicketResponse singleTicketResponse = JAXBMarshaller.unmarshallTextMessage(message, SingleTicketResponse.class);
        return singleTicketResponse.getTicket();
    }

    public static AlarmReportType mapSingleAlarmFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        SingleAlarmResponse singleTicketResponse = JAXBMarshaller.unmarshallTextMessage(message, SingleAlarmResponse.class);
        return singleTicketResponse.getAlarm();
    }

    public static String createCustomRuleResponse(CustomRuleType customRule) throws RulesModelMapperException {
        CreateCustomRuleResponse response = new CreateCustomRuleResponse();
        response.setCustomRule(customRule);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String updateCustomRuleResponse(CustomRuleType customRule) throws RulesModelMapperException {
        UpdateCustomRuleResponse response = new UpdateCustomRuleResponse();
        response.setCustomRule(customRule);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static CustomRuleType mapToUpdateCustomRuleFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        UpdateCustomRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, UpdateCustomRuleResponse.class);
        return response.getCustomRule();
    }

    public static TicketType mapToSetTicketStatusFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        SetTicketStatusResponse response = JAXBMarshaller.unmarshallTextMessage(message, SetTicketStatusResponse.class);
        return response.getTicket();
    }

    public static List<CustomRuleType> mapToCustomRuleListFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetCustomRuleListResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetCustomRuleListResponse.class);
        return response.getCustomRules();
    }
    
    public static CustomRuleType getCustomRuleResponse(TextMessage message, String correlationId) throws RulesModelMapperException, JMSException, RulesFaultException {
        validateResponse(message, correlationId);
        GetCustomRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetCustomRuleResponse.class);
        return response.getCustomRule();
    }

    public static String getCustomRuleResponse(CustomRuleType customRuleType) throws RulesModelMarshallException{
        GetCustomRuleResponse response = new GetCustomRuleResponse(); 
        response.setCustomRule(customRuleType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }
    
    public static String getCustomRuleListResponse(List<CustomRuleType> customRules) throws RulesModelMapperException {
        GetCustomRuleListResponse response = new GetCustomRuleListResponse();
        response.getCustomRules().addAll(customRules);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static GetAlarmListByQueryResponse mapToAlarmListFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetAlarmListByQueryResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetAlarmListByQueryResponse.class);
        return response;
    }

    public static String createAlarmListResponse(AlarmListResponseDto responseDto) throws RulesModelMapperException {
        GetAlarmListByQueryResponse response = new GetAlarmListByQueryResponse();
        response.getAlarms().addAll(responseDto.getAlarmList());
        response.setCurrentPage(responseDto.getCurrentPage());
        response.setTotalNumberOfPages(responseDto.getTotalNumberOfPages());
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static GetTicketListByQueryResponse mapToTicketListFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetTicketListByQueryResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetTicketListByQueryResponse.class);
        return response;
    }

    public static String createTicketListResponse(TicketListResponseDto responseDto) throws RulesModelMapperException {
        GetTicketListByQueryResponse response = new GetTicketListByQueryResponse();
        response.getTickets().addAll(responseDto.getTicketList());
        response.setCurrentPage(responseDto.getCurrentPage());
        response.setTotalNumberOfPages(responseDto.getTotalNumberOfPages());
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String setTicketStatusResponse(TicketType ticket) throws RulesModelMapperException {
        SetTicketStatusResponse response = new SetTicketStatusResponse();
        response.setTicket(ticket);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static AlarmReportType mapToSetAlarmStatusFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        SetAlarmStatusResponse response = JAXBMarshaller.unmarshallTextMessage(message, SetAlarmStatusResponse.class);
        return response.getAlarm();
    }

    public static String setAlarmStatusResponse(AlarmReportType alarm) throws RulesModelMapperException {
        SetAlarmStatusResponse response = new SetAlarmStatusResponse();
        response.setAlarm(alarm);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static List<PreviousReportType> mapToGetPreviousReportsResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetPreviousReportsResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetPreviousReportsResponse.class);
        return response.getPreviousReports();
    }

    public static PreviousReportType mapToGetPreviousReportByVesselGuidResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetPreviousReportByVesselGuidResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetPreviousReportByVesselGuidResponse.class);
        return response.getPreviousReport();
    }

    public static String mapToGetPreviousReportResponse(List<PreviousReportType> previousReports) throws RulesModelMapperException {
        GetPreviousReportsResponse response = new GetPreviousReportsResponse();
        response.getPreviousReports().addAll(previousReports);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String mapToGetPreviousReportByVesselGuidResponse(PreviousReportType previousReport) throws RulesModelMapperException {
        GetPreviousReportByVesselGuidResponse response = new GetPreviousReportByVesselGuidResponse();
        response.setPreviousReport(previousReport);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static GetTicketByVesselGuidResponse mapToGetTicketByVesselGuidFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetTicketByVesselGuidResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetTicketByVesselGuidResponse.class);
        return response;
    }

    public static String getTicketByVesselGuidResponse(TicketType ticketType) throws RulesModelMapperException {
        GetTicketByVesselGuidResponse response = new GetTicketByVesselGuidResponse();
        response.setTicket(ticketType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String mapToCreateTicketResponse(TicketType ticket) throws RulesModelMarshallException {
        CreateTicketResponse response = new CreateTicketResponse();
        response.setTicket(ticket);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String toCreateAlarmReportResponse(AlarmReportType createdReport) throws RulesModelMarshallException {
        CreateAlarmReportResponse response = new CreateAlarmReportResponse();
        response.setAlarm(createdReport);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String toSingleAlarmReportResponse(AlarmReportType alarmReport) throws RulesModelMarshallException {
        SingleAlarmResponse response = new SingleAlarmResponse();
        response.setAlarm(alarmReport);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String toSingleTicketResponse(TicketType ticket) throws RulesModelMarshallException {
        SingleTicketResponse response = new SingleTicketResponse();
        response.setTicket(ticket);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

}
