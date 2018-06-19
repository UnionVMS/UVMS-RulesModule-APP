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
package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.schema.rules.module.v1.CreateCustomRuleResponse;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.source.v1.*;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.CustomRuleListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

public class RulesDataSourceResponseMapper {

    private final static Logger LOG = LoggerFactory.getLogger(RulesDataSourceResponseMapper.class);

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

    public static long mapGetNumberOfOpenAlarmReportsFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetNumberOfOpenAlarmsResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetNumberOfOpenAlarmsResponse.class);
        return response.getResponse();
    }

    public static long mapGetNumberOfOpenTicketsFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetNumberOfOpenTicketsResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetNumberOfOpenTicketsResponse.class);
        return response.getResponse();
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

    public static String deleteCustomRuleResponse(CustomRuleType customRule) throws RulesModelMapperException {
        DeleteCustomRuleResponse response = new DeleteCustomRuleResponse();
        response.setCustomRule(customRule);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static CustomRuleType mapToUpdateCustomRuleFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        UpdateCustomRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, UpdateCustomRuleResponse.class);
        return response.getCustomRule();
    }

    public static CustomRuleType mapToDeleteCustomRuleFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        DeleteCustomRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, DeleteCustomRuleResponse.class);
        return response.getCustomRule();
    }

    public static TicketType mapToSetTicketStatusFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        SetTicketStatusResponse response = JAXBMarshaller.unmarshallTextMessage(message, SetTicketStatusResponse.class);
        return response.getTicket();
    }

    public static TicketType mapToUpdateTicketCountFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        UpdateTicketCountResponse response = JAXBMarshaller.unmarshallTextMessage(message, UpdateTicketCountResponse.class);
        return response.getTicket();
    }

    public static List<TicketType> mapToUpdateTicketStatusByQueryFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        UpdateTicketStatusByQueryResponse response = JAXBMarshaller.unmarshallTextMessage(message, UpdateTicketStatusByQueryResponse.class);
        return response.getTickets();
    }

    public static List<CustomRuleType> mapToGetCustomRulesFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetCustomRulesByUserResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetCustomRulesByUserResponse.class);
        return response.getCustomRules();
    }

    public static List<CustomRuleType> mapToGetRunnableCustomRulesFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetRunnableCustomRulesResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetRunnableCustomRulesResponse.class);
        return response.getCustomRules();
    }

    public static List<SanityRuleType> mapToGetSanityRulesFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetSanityRulesResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetSanityRulesResponse.class);
        return response.getSanityRules();
    }

    public static GetCustomRuleListByQueryResponse mapToCustomRuleListByQueryFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetCustomRuleListByQueryResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetCustomRuleListByQueryResponse.class);
        return response;
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

    public static String getCustomRulesByUserResponse(List<CustomRuleType> customRules) throws RulesModelMapperException {
        GetCustomRulesByUserResponse response = new GetCustomRulesByUserResponse();
        response.getCustomRules().addAll(customRules);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String getRunnableCustomRulesResponse(List<CustomRuleType> customRules) throws RulesModelMapperException {
        GetRunnableCustomRulesResponse response = new GetRunnableCustomRulesResponse();
        response.getCustomRules().addAll(customRules);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String getSanityRulesResponse(List<SanityRuleType> sanityRules) throws RulesModelMapperException {
        GetSanityRulesResponse response = new GetSanityRulesResponse();
        response.getSanityRules().addAll(sanityRules);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String getCustomRuleListByQueryResponse(CustomRuleListResponseDto responseDto) throws RulesModelMapperException {
        GetCustomRuleListByQueryResponse response = new GetCustomRuleListByQueryResponse();
        response.getCustomRules().addAll(responseDto.getCustomRuleList());
        response.setCurrentPage(responseDto.getCurrentPage());
        response.setTotalNumberOfPages(responseDto.getTotalNumberOfPages());
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

    public static GetTicketListByMovementsResponse mapToTicketsByMovementsFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetTicketListByMovementsResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetTicketListByMovementsResponse.class);
        return response;
    }

    public static long mapToCountTicketsByMovementsFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        CountTicketListByMovementsResponse response = JAXBMarshaller.unmarshallTextMessage(message, CountTicketListByMovementsResponse.class);
        return response.getCount();
    }

    public static String createTicketsByMovementsResponse(TicketListResponseDto responseDto) throws RulesModelMapperException {
        GetTicketListByMovementsResponse response = new GetTicketListByMovementsResponse();
        response.getTickets().addAll(responseDto.getTicketList());
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String createCountTicketsByMovementsResponse(long count) throws RulesModelMapperException {
        CountTicketListByMovementsResponse response = new CountTicketListByMovementsResponse();
        response.setCount(count);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String setTicketStatusResponse(TicketType ticket) throws RulesModelMapperException {
        SetTicketStatusResponse response = new SetTicketStatusResponse();
        response.setTicket(ticket);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String updateTicketCountResponse(TicketType ticket) throws RulesModelMapperException {
        UpdateTicketCountResponse response = new UpdateTicketCountResponse();
        response.setTicket(ticket);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String updateTicketStatusByQueryResponse(List<TicketType> tickets) throws RulesModelMapperException {
        UpdateTicketStatusByQueryResponse response = new UpdateTicketStatusByQueryResponse();
        response.getTickets().addAll(tickets);
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

    public static PreviousReportType mapToGetPreviousReportByAssetGuidResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetPreviousReportByAssetGuidResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetPreviousReportByAssetGuidResponse.class);
        return response.getPreviousReport();
    }

    public static String mapToGetPreviousReportResponse(List<PreviousReportType> previousReports) throws RulesModelMapperException {
        GetPreviousReportsResponse response = new GetPreviousReportsResponse();
        response.getPreviousReports().addAll(previousReports);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String mapToGetPreviousReportByAssetGuidResponse(PreviousReportType previousReport) throws RulesModelMapperException {
        GetPreviousReportByAssetGuidResponse response = new GetPreviousReportByAssetGuidResponse();
        response.setPreviousReport(previousReport);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static GetTicketByAssetAndRuleResponse mapToGetTicketByAssetGuidFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetTicketByAssetAndRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetTicketByAssetAndRuleResponse.class);
        return response;
    }

    public static GetAlarmReportByAssetAndRuleResponse mapToGetAlarmReportByAssetAndRuleFromResponse(TextMessage message, String correlationId) throws RulesModelMapperException, RulesFaultException, JMSException {
        validateResponse(message, correlationId);
        GetAlarmReportByAssetAndRuleResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetAlarmReportByAssetAndRuleResponse.class);
        return response;
    }

    public static String getTicketByAssetAndRuleResponse(TicketType ticketType) throws RulesModelMapperException {
        GetTicketByAssetAndRuleResponse response = new GetTicketByAssetAndRuleResponse();
        response.setTicket(ticketType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String getAlarmReportByAssetAndRuleResponse(AlarmReportType alarmReportType) throws RulesModelMapperException {
        GetAlarmReportByAssetAndRuleResponse response = new GetAlarmReportByAssetAndRuleResponse();
        response.setAlarm(alarmReportType);
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

    public static String toNumberOfOpenAlarmsResponse(long alarmCount) throws RulesModelMarshallException {
        GetNumberOfOpenAlarmsResponse response = new GetNumberOfOpenAlarmsResponse();
        response.setResponse(alarmCount);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String toNumberOfOpenTicketsResponse(long ticketCount) throws RulesModelMarshallException {
        GetNumberOfOpenTicketsResponse response = new GetNumberOfOpenTicketsResponse();
        response.setResponse(ticketCount);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static long mapToGetNumberOfAssetsNotSendingFromResponse(TextMessage message, String correlationId) throws JMSException, RulesFaultException, RulesModelMapperException {
        validateResponse(message, correlationId);
        GetNumberOfAssetsNotSendingResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetNumberOfAssetsNotSendingResponse.class);
        return response.getResponse();
    }

    public static String toNumberOfAssetsNotSendingResponse(long count) throws RulesModelMarshallException {
        GetNumberOfAssetsNotSendingResponse response = new GetNumberOfAssetsNotSendingResponse();
        response.setResponse(count);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String getTicketsAndRulesByMovementsResponse(List<TicketAndRuleType> ticketAndRuleType) throws RulesModelMapperException {
        GetTicketsAndRulesByMovementsResponse response = new GetTicketsAndRulesByMovementsResponse();
        response.getTicketsAndRules().addAll(ticketAndRuleType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String mapToGetTicketsAndRulesByMovementsResponse(List<TicketAndRuleType> ticketAndRuleTypes) throws RulesModelMarshallException {
        GetTicketsAndRulesByMovementsResponse response = new GetTicketsAndRulesByMovementsResponse();
        response.getTicketsAndRules().addAll(ticketAndRuleTypes);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

}