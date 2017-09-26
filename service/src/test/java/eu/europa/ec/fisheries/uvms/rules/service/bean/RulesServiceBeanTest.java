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
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import javax.enterprise.event.Event;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketByAssetAndRuleResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
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
    Event<NotificationMessage> ticketUpdateEvent;
    @Mock
    Event<NotificationMessage> ticketCountEvent;
    @Mock
    ValidationServiceBean mockValidationServiceBean;
    @Mock
    Event<NotificationMessage> alarmReportCountEvent;

    @InjectMocks
    RulesServiceBean rulesServiceBean;
    @Mock
    TextMessage response;
    @Mock
    private RulesDomainModelBean rulesDomainModel;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

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
        rulesServiceBean.createCustomRule(customRule, "manageGlobalAlarmsRules", "Union-VMS");

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
        CustomRuleType mockCustomRule = mock(CustomRuleType.class);
        when(rulesDomainModel.getByGuid(guid)).thenReturn(mockCustomRule);
        rulesServiceBean.getCustomRuleByGuid(guid);

    }

    @Ignore
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
        rulesServiceBean.updateCustomRule(customRule, "manageGlobalAlarmsRules", "Union-VMS");

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
        AlarmListResponseDto responseDto = mock(AlarmListResponseDto.class);
        when(rulesDomainModel.getAlarmListByQuery(query)).thenReturn(responseDto);
        rulesServiceBean.getAlarmList(query);
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
        TicketListResponseDto responseDto = mock(TicketListResponseDto.class);
        when(rulesDomainModel.getTicketListByQuery(loggedInUser, query)).thenReturn(responseDto);
        rulesServiceBean.getTicketList(loggedInUser, query);

        verifyStatic();
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

        verify(mockProducer, times(2)).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer, times(2)).getMessage(messageId, TextMessage.class);
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
        List<PreviousReportType> previousReportTypes = new ArrayList<>();
        when(rulesDomainModel.getPreviousReports()).thenReturn(previousReportTypes);
        rulesServiceBean.getPreviousMovementReports();
    }

    @Test
    public void testTimerRuleTriggeredTicketAlreadyCreated() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        PreviousReportFact fact = new PreviousReportFact();
        fact.setAssetGuid("assetGuid");
        String ruleName = ServiceConstants.ASSET_NOT_SENDING_RULE;
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetTicketByAssetAndRule(fact.getAssetGuid(), ruleName)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        GetTicketByAssetAndRuleResponse ticketResponse = new GetTicketByAssetAndRuleResponse();
        ticketResponse.setTicket(new TicketType());
        when(RulesDataSourceResponseMapper.mapToGetTicketByAssetGuidFromResponse(response, messageId)).thenReturn(ticketResponse);

        when(RulesDataSourceResponseMapper.mapToUpdateTicketCountFromResponse(response, messageId)).thenReturn(new TicketType());

        // Act
        TicketType mock = mock(TicketType.class);
        when(rulesDomainModel.getTicketByAssetGuid(fact.getAssetGuid(), ruleName)).thenReturn(mock);
        when(rulesDomainModel.updateTicketCount(mock)).thenReturn(mock);
        rulesServiceBean.timerRuleTriggered(ruleName, fact);
    }

//    @Test
//    public void testTimerRuleTriggeredAlarmReportAlreadyCreated() throws Exception {
//        // Setup
//        mockStatic(RulesDataSourceRequestMapper.class);
//        PreviousReportFact fact = new PreviousReportFact();
//        fact.setAssetGuid("assetGuid");
//        String ruleName = ServiceConstants.ASSET_NOT_SENDING_RULE;
//        String request = "request";
//        when(RulesDataSourceRequestMapper.mapGetAlarmReportByAssetAndRule(fact.getAssetGuid(), ruleName)).thenReturn(request);
//
//        String messageId = "messageId";
//        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);
//
//        TextMessage response = mock(TextMessage.class);
//        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);
//
//        mockStatic(RulesDataSourceResponseMapper.class);
//        GetAlarmReportByAssetAndRuleResponse alarmReportResponse = new GetAlarmReportByAssetAndRuleResponse();
//        alarmReportResponse.setAlarm(new AlarmReportType());
//        when(RulesDataSourceResponseMapper.mapToGetAlarmReportByAssetAndRuleFromResponse(response, messageId)).thenReturn(alarmReportResponse);
//
//        // Act
//        rulesServiceBean.timerRuleTriggered(ruleName, fact);
//
//        // Verify
//        verifyStatic();
//        RulesDataSourceRequestMapper.mapGetAlarmReportByAssetAndRule(fact.getAssetGuid(), ruleName);
//
//        verifyStatic();
//        RulesDataSourceResponseMapper.mapToGetAlarmReportByAssetAndRuleFromResponse(response, messageId);
//
//        verify(mockProducer).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
//        verify(mockConsumer).getMessage(messageId, TextMessage.class);
//    }

    @Ignore // Mocking events not working
    @Test
    public void testTimerRuleTriggeredNoTicketCreated() throws Exception {
        // Setup
        mockStatic(RulesDataSourceRequestMapper.class);
        PreviousReportFact fact = new PreviousReportFact();
        fact.setAssetGuid("assetGuid");
        fact.setMovementGuid("movementGuid");
        String ruleName = ServiceConstants.ASSET_NOT_SENDING_RULE;
        String request = "request";
        when(RulesDataSourceRequestMapper.mapGetTicketByAssetAndRule(fact.getAssetGuid(), ruleName)).thenReturn(request);

        String messageId = "messageId";
        when(mockProducer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL))).thenReturn(messageId);

        TextMessage response = mock(TextMessage.class);
        when(mockConsumer.getMessage(messageId, TextMessage.class)).thenReturn(response);

        mockStatic(RulesDataSourceResponseMapper.class);
        TicketType result = new TicketType();

        GetTicketByAssetAndRuleResponse ticketResponse = new GetTicketByAssetAndRuleResponse();
        ticketResponse.setTicket(null);
        when(RulesDataSourceResponseMapper.mapToGetTicketByAssetGuidFromResponse(response, messageId)).thenReturn(ticketResponse);

        Long ticketCount = 5L;
        when(mockValidationServiceBean.getNumberOfOpenTickets("userName")).thenReturn(ticketCount);

//        ticketCountEvent.fire(new NotificationMessage("ticketCount", ticketCount));

        // Act
        rulesServiceBean.timerRuleTriggered(ruleName, fact);

        // Verify
        verifyStatic();
        RulesDataSourceRequestMapper.mapGetTicketByAssetAndRule(fact.getAssetGuid(), ruleName);

        verifyStatic();
        RulesDataSourceResponseMapper.mapToGetTicketByAssetGuidFromResponse(response, messageId);

        verify(mockProducer, times(2)).sendDataSourceMessage(anyString(), eq(DataSourceQueue.INTERNAL));
        verify(mockConsumer, times(2)).getMessage(messageId, TextMessage.class);

        // A ticket is created. Capture to verify data
        verifyStatic();
        ArgumentCaptor<TicketType> argument = ArgumentCaptor.forClass(TicketType.class);
        RulesDataSourceRequestMapper.mapCreateTicket(argument.capture());
        TicketType ticket = argument.getValue();
        assertEquals(ruleName, ticket.getRuleName());
        assertEquals(ruleName, ticket.getRuleGuid());
        assertEquals(TicketStatusType.OPEN, ticket.getStatus());
        assertEquals(fact.getMovementGuid(), ticket.getMovementGuid());
        assertEquals(fact.getAssetGuid(), ticket.getAssetGuid());
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
        AlarmReportType mock = mock(AlarmReportType.class);
        when(rulesDomainModel.getAlarmReportByGuid(guid)).thenReturn(mock);
        rulesServiceBean.getAlarmReportByGuid(guid);

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
        TicketType mock = mock(TicketType.class);
        when(rulesDomainModel.getTicketByGuid(guid)).thenReturn(mock);
        rulesServiceBean.getTicketByGuid(guid);

    }

    public void testReprocessAlarm() throws Exception {

    }

    public void testSetMovementReportReceived() throws Exception {

    }

}