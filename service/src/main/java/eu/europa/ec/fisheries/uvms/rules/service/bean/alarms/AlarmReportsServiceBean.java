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
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.module.v1.CreateAlarmsReportRequest;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesAuditProducerBean;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
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

@Stateless
@LocalBean
@Slf4j
public class AlarmReportsServiceBean {

    @Inject
    @AlarmReportEvent
    private Event<NotificationMessage> alarmReportEvent;

    @Inject
    @AlarmReportCountEvent
    private Event<NotificationMessage> alarmReportCountEvent;

    @EJB
    private RulesAuditProducerBean auditProducer;

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesDomainModel rulesDomainModel;

    public void createAlarmReport(CreateAlarmsReportRequest request) throws RulesServiceException {
        for (AlarmReportType alarmReport : request.getAlarm()) {
            validate(alarmReport);
            createAlarmReport(alarmReport);
        }
    }

    private void validate(AlarmReportType alarmReportType) throws RulesServiceException  {
        if(alarmReportType == null) throw new InputArgumentException("AlarmReportType cannot be null");
        if(alarmReportType.getAlarmItem().isEmpty()) throw new InputArgumentException("AlarmItem list was empty");
    }

    private void createAlarmReport(AlarmReportType alarmReport) {
        try {
            alarmReport.setStatus(AlarmStatusType.OPEN);
//            alarmReport.setUpdatedBy("UVMS");
//            alarmReport.setPluginType(fact.getPluginType());
            alarmReport.setInactivatePosition(false);
            AlarmReportType createdAlarmReport = rulesDomainModel.createAlarmReport(alarmReport);
            // Notify long-polling clients of the new alarm report
            alarmReportEvent.fire(new NotificationMessage("guid", createdAlarmReport.getGuid()));
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", null));
            sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.CREATE, createdAlarmReport.getGuid(), null, alarmReport.getUpdatedBy());
        } catch (RulesModelException e) {
            log.error("[ Failed to create alarm! ] {}", e.getMessage());
        }
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
}
