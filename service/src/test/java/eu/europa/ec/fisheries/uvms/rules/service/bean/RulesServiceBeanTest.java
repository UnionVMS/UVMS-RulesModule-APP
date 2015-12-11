package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmReportByVesselGuidResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketByVesselGuidResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.enterprise.event.Event;
import javax.jms.TextMessage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({RulesDataSourceRequestMapper.class, RulesDataSourceResponseMapper.class})
public class RulesServiceBeanTest {

    @Mock
    RulesMessageProducer mockProducer;
    @Mock
    RulesResponseConsumer mockConsumer;
    @Mock
    Event<NotificationMessage> ticketEvent;
    @Mock
    Event<NotificationMessage> ticketCountEvent;
    @Mock
    ValidationServiceBean mockValidationServiceBean;

    @InjectMocks
    RulesServiceBean rulesServiceBean;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    TextMessage response;

    @Ignore
    @Test
    public void testCreateCustomRule() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        CustomRuleType customRule = new CustomRuleType();
        String request = "request";
        when(RulesDataSourceRequestMapper.mapCreateCustomRule(customRule)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        CustomRuleType result = new CustomRuleType();
        when(RulesDataSourceResponseMapper.mapToCreateCustomRuleFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.createCustomRule(customRule);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapCreateCustomRule(customRule);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToCreateCustomRuleFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testGetCustomRuleByGuid() throws Exception {
        // Setup
        String guid = "guid";
        String request = "request";
        mockStatic(RulesDataSourceRequestMapper.class);
        when(RulesDataSourceRequestMapper.mapGetCustomRule(guid)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        CustomRuleType result = new CustomRuleType();
        mockStatic(RulesDataSourceResponseMapper.class);
        when(RulesDataSourceResponseMapper.getCustomRuleResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getCustomRuleByGuid(guid);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapGetCustomRule(guid);

        verifyStatic();
        RulesDataSourceResponseMapper.getCustomRuleResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testUpdateCustomRule() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        CustomRuleType customRule = new CustomRuleType();
        String request = "request";
        when(RulesDataSourceRequestMapper.mapUpdateCustomRule(customRule)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        CustomRuleType result = new CustomRuleType();
        when(RulesDataSourceResponseMapper.mapToUpdateCustomRuleFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.updateCustomRule(customRule);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapUpdateCustomRule(customRule);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToUpdateCustomRuleFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testGetAlarmList() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        AlarmQuery query = new AlarmQuery();
        String request = "request";
        when(RulesDataSourceRequestMapper.mapAlarmList(query)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        GetAlarmListByQueryResponse result = new GetAlarmListByQueryResponse();
        when(RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getAlarmList(query);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapAlarmList(query);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test (expected = RulesServiceException.class)
    public void testGetAlarmListThrowsExceptionWhenResponseIsNull() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        AlarmQuery query = new AlarmQuery();
        String request = "request";
        when(RulesDataSourceRequestMapper.mapAlarmList(query)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = null;
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        GetAlarmListByQueryResponse result = new GetAlarmListByQueryResponse();
        when(RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getAlarmList(query);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapAlarmList(query);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testGetTicketList() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        TicketQuery query = new TicketQuery();
        String loggedInUser = "loggedInUser";
        String request = "request";
        when(RulesDataSourceRequestMapper.mapTicketList(loggedInUser, query)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        GetTicketListByQueryResponse result = new GetTicketListByQueryResponse();
        when(RulesDataSourceResponseMapper.mapToTicketListFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getTicketList(loggedInUser, query);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapTicketList(loggedInUser, query);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToTicketListFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test (expected = RulesServiceException.class)
    public void testGetTicketListThrowsExceptionWhenResponseIsNull() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        TicketQuery query = new TicketQuery();
        String loggedInUser = "loggedInUser";
        String request = "request";
        when(RulesDataSourceRequestMapper.mapTicketList(loggedInUser, query)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = null;
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        GetTicketListByQueryResponse result = new GetTicketListByQueryResponse();
        when(RulesDataSourceResponseMapper.mapToTicketListFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getTicketList(loggedInUser, query);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapTicketList(loggedInUser, query);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToTicketListFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Ignore // Mocking events not working
    @Test
    public void testUpdateTicketStatus() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        TicketType ticket = new TicketType();
        String request = "request";
        when(RulesDataSourceRequestMapper.mapUpdateTicketStatus(ticket)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        TicketType result = new TicketType();
        when(RulesDataSourceResponseMapper.mapToSetTicketStatusFromResponse(response, messageId)).thenReturn(result);

//        ticketEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));

        // Act
        rulesServiceBean.updateTicketStatus(ticket);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapUpdateTicketStatus(ticket);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToSetTicketStatusFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
//        verify(ticketEvent, times(1)).fire(any(NotificationMessage.class));
//        ticketEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
    }

    @Ignore // Mocking events not working
    @Test
    public void testUpdateAlarmStatus() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        AlarmReportType alarm = new AlarmReportType();
        String request = "request";
        when(RulesDataSourceRequestMapper.mapUpdateAlarmStatus(alarm)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        AlarmReportType result = new AlarmReportType();
        when(RulesDataSourceResponseMapper.mapToSetAlarmStatusFromResponse(response, messageId)).thenReturn(result);

//        alarmReportEvent.fire(new NotificationMessage("guid", result.getGuid()));

        // Act
        rulesServiceBean.updateAlarmStatus(alarm);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapUpdateAlarmStatus(alarm);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToSetAlarmStatusFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testGetPreviousMovementReports() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetPreviousReports()).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        List<PreviousReportType> result = new ArrayList<>();
        when(RulesDataSourceResponseMapper.mapToGetPreviousReportsResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getPreviousMovementReports();

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapGetPreviousReports();

        verifyStatic();
        RulesDataSourceResponseMapper.mapToGetPreviousReportsResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testTimerRuleTriggeredTicketAlreadyCreated() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        PreviousReportFact fact = new PreviousReportFact();
        fact.setVesselGuid("vessetGuid");
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetTicketByVesselGuid(fact.getVesselGuid())).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

//        mockStatic(RulesDataSourceResponseMapper.class);
//        GetTicketByVesselGuidResponse ticketResponse = new GetTicketByVesselGuidResponse();
//        ticketResponse.setTicket(new TicketType());
//        when(RulesDataSourceResponseMapper.mapToGetTicketByVesselGuidFromResponse(response, messageId)).thenReturn(ticketResponse);

        mockStatic(RulesDataSourceResponseMapper.class);
        GetAlarmReportByVesselGuidResponse alarmReportResponse = new GetAlarmReportByVesselGuidResponse();
        alarmReportResponse.setAlarm(new AlarmReportType());
        when(RulesDataSourceResponseMapper.mapToGetAlarmReportByVesselGuidFromResponse(response, messageId)).thenReturn(alarmReportResponse);

        // Act
        String ruleName = "ruleName";
        String ruleGuid = "ruleGuid";
        rulesServiceBean.timerRuleTriggered(ruleName, ruleGuid, fact);

        // Verify
        verifyStatic();
//        RulesDataSourceRequestMapper.mapGetTicketByVesselGuid(fact.getVesselGuid());
        RulesDataSourceRequestMapper.mapGetAlarmReportByVesselGuid(fact.getVesselGuid());

        verifyStatic();
//        RulesDataSourceResponseMapper.mapToGetTicketByVesselGuidFromResponse(response, messageId);
        RulesDataSourceResponseMapper.mapToGetAlarmReportByVesselGuidFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Ignore // Mocking events not working
    @Test
    public void testTimerRuleTriggeredNoTicketCreated() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        PreviousReportFact fact = new PreviousReportFact();
        fact.setVesselGuid("vesselGuid");
        fact.setMovementGuid("movementGuid");
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetTicketByVesselGuid(fact.getVesselGuid())).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        TicketType result = new TicketType();

        GetTicketByVesselGuidResponse ticketResponse = new GetTicketByVesselGuidResponse();
        ticketResponse.setTicket(null);
        when(RulesDataSourceResponseMapper.mapToGetTicketByVesselGuidFromResponse(response, messageId)).thenReturn(ticketResponse);

        Long ticketCount = 5L;
        when(mockValidationServiceBean.getNumberOfOpenTickets()).thenReturn(ticketCount);

//        ticketCountEvent.fire(new NotificationMessage("ticketCount", ticketCount));

        // Act
        String ruleName = "ruleName";
        String ruleGuid = "ruleGuid";
        rulesServiceBean.timerRuleTriggered(ruleName, ruleGuid, fact);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapGetTicketByVesselGuid(fact.getVesselGuid());

        verifyStatic();
        RulesDataSourceResponseMapper.mapToGetTicketByVesselGuidFromResponse(response, messageId);

        verify(mockProducer, times(2)).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer, times(2)).getMessage(messageId, TextMessage.class);

        // A ticket is created. Capture to verify data
        verifyStatic();
        ArgumentCaptor<TicketType> argument = ArgumentCaptor.forClass(TicketType.class);
        RulesDataSourceRequestMapper.mapCreateTicket(argument.capture());
        TicketType ticket = argument.getValue();
        assertEquals(ruleName, ticket.getRuleName());
        assertEquals(ruleGuid, ticket.getRuleGuid());
        assertEquals(TicketStatusType.OPEN, ticket.getStatus());
        assertEquals(fact.getMovementGuid(), ticket.getMovementGuid());
        assertEquals(fact.getVesselGuid(), ticket.getVesselGuid());

    }

    @Test
    public void testGetAlarmReportByGuid() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        String guid = "guid";
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetAlarmByGuid(guid)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        AlarmReportType result = new AlarmReportType();
        when(RulesDataSourceResponseMapper.mapSingleAlarmFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getAlarmReportByGuid(guid);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapGetAlarmByGuid(guid);

        verifyStatic();
        RulesDataSourceResponseMapper.mapSingleAlarmFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    @Test
    public void testGetTicketByGuid() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        String guid = "guid";
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetTicketByGuid(guid)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        TicketType result = new TicketType();
        when(RulesDataSourceResponseMapper.mapSingleTicketFromResponse(response, messageId)).thenReturn(result);

        // Act
        rulesServiceBean.getTicketByGuid(guid);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapGetTicketByGuid(guid);

        verifyStatic();
        RulesDataSourceResponseMapper.mapSingleTicketFromResponse(response, messageId);

        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer).getMessage(messageId, TextMessage.class);
    }

    public void testReprocessAlarm() throws Exception {

    }

    public void testSetMovementReportReceived() throws Exception {

    }

}