/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.alarms;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.module.v1.CreateAlarmsReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.CreateTicketRequest;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMarshallException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.ReportingProducerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesAuditProducerBean;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.movement.communication.MovementSender;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static eu.europa.ec.fisheries.uvms.rules.service.bean.alarms.RawMovementTypeMapper.enrichRawMovement;

@Stateless
@LocalBean
@Slf4j
public class AlarmsServiceBean {

    @Inject
    @AlarmReportEvent
    private Event<NotificationMessage> alarmReportEvent;

    @Inject
    @AlarmReportCountEvent
    private Event<NotificationMessage> alarmReportCountEvent;

    @EJB
    private RulesAuditProducerBean auditProducer;

    @EJB
    private ReportingProducerBean reportingProducer;

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesDomainModel rulesDomainModel;

    @Inject
    private MovementSender movementSender;

    public void createTickets(CreateTicketRequest request) throws RulesServiceException {
        List<TicketType> ticketTypes = request.getTickets();
        for (TicketType ticketType : ticketTypes) {
            try {
                TicketType ticket = rulesDomainModel.createTicket(ticketType);
                sendAlarmTicketUpdateToReporting(ticket);
            } catch (RulesModelException e) {
                throw new RulesServiceException("Error creating ticket",e);
            }
        }
    }

    public List<AlarmReportType> createAlarmReport(CreateAlarmsReportRequest request) throws RulesServiceException {
        validateAndEnrichAlarmReportsWithMovementInfo(request.getAlarm());
        List<AlarmReportType> reportTypeList = new ArrayList<>();
        for (AlarmReportType alarmReport : request.getAlarm()) {
            reportTypeList.add(createAlarmReport(alarmReport));
        }
        return reportTypeList;
    }

    private AlarmReportType createAlarmReport(AlarmReportType alarmReport) {
        AlarmReportType createdAlarmReport = null;
        try {
            alarmReport.setStatus(AlarmStatusType.OPEN);
            alarmReport.setUpdatedBy(alarmReport.getUpdatedBy());
            alarmReport.setPluginType(alarmReport.getPluginType());
            alarmReport.setInactivatePosition(false);

            createdAlarmReport = rulesDomainModel.createAlarmReport(alarmReport);
            // Notify long-polling clients of the new alarm report
            alarmReportEvent.fire(new NotificationMessage("guid", createdAlarmReport.getGuid()));
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", null));
            sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.CREATE, createdAlarmReport.getGuid(), null, alarmReport.getUpdatedBy());
        } catch (RulesModelException e) {
            log.error("[ Failed to create alarm! ] {}", e.getMessage());
        }
        return createdAlarmReport;
    }

    private void sendAuditMessage(AuditObjectTypeEnum type, AuditOperationEnum operation, String affectedObject, String comment, String username) {
        try {
            String message = AuditLogMapper.mapToAuditLog(type.getValue(), operation.getValue(), affectedObject, comment, username);
            auditProducer.sendModuleMessage(message, consumer.getDestination());
        }
        catch (AuditModelMarshallException | MessageException e) {
            log.error("[ Error when sending message to Audit. ] {}", e.getMessage());
        }
    }

    private void validateAndEnrichAlarmReportsWithMovementInfo(List<AlarmReportType> alarmReports){
        Map<String,RawMovementType> existingGuidListMap = alarmReports.stream()
                .map(this::validateAndGetRowMovement)
                .filter(not(RawMovementType::isGuidGenerated))
                .collect(Collectors.toMap(RawMovementType::getGuid, Function.identity()));
        if(existingGuidListMap.isEmpty()) return;

        List<MovementBaseType> movementBaseTypes = movementSender.findRawMovements(new ArrayList<>(existingGuidListMap.keySet()));
        if(movementBaseTypes == null || movementBaseTypes.isEmpty()) return;

        movementBaseTypes.forEach(mb -> enrichRawMovement(existingGuidListMap.get(mb.getGuid()),mb));
    }

    private RawMovementType validateAndGetRowMovement(AlarmReportType alarmReportType){
        validate(alarmReportType);
        return alarmReportType.getRawMovement();
    }

    private void validate(AlarmReportType alarmReportType) throws RulesServiceException  {
        if(alarmReportType == null) throw new InputArgumentException("AlarmReportType cannot be null");
        if(alarmReportType.getAlarmItem().isEmpty()) throw new InputArgumentException("AlarmItem list was empty");
    }

    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    private void sendAlarmTicketUpdateToReporting(TicketType ticket) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("mainTopic", "reporting");
            params.put("subTopic", "alarm");
            AlarmTicket alarm = new AlarmTicket();
            alarm.setTicketType(ticket);
            reportingProducer.sendMessageToSpecificQueueSameTx(JAXBMarshaller.marshallJaxBObjectToString(alarm), reportingProducer.getDestination(), null, params);
        } catch (MessageException | AssetModelMarshallException e) {
            log.error("Could not send asset update to reporting", e);
        }
    }
}
