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
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmItemType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.rules.module.v1.CreateAlarmsReportRequest;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.commons.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesAuditProducerBean;
import eu.europa.ec.fisheries.uvms.rules.movement.communication.MovementSender;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link AlarmsServiceBeanTest}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AlarmsServiceBeanTest {

    private static final String DELTA_GUID = "97a40d34-45ea-11e7-bec7-4c32759615eb";
    @Mock
    private RulesDomainModel rulesDomainModel;
    @Mock
    private Event<NotificationMessage> alarmReportEvent;
    @Mock
    private Event<NotificationMessage> alarmReportCountEvent;
    @Mock
    private RulesAuditProducerBean auditProducer;
    @Mock
    private RulesResponseConsumer consumer;
    @Mock
    private MovementSender movementSender;

    @InjectMocks
    private AlarmsServiceBean sut;

    @Test(expected = RulesServiceException.class)
    public void testCreateAlarmReportWithoutAlarmItemAndExpectServiceException(){
        CreateAlarmsReportRequest request = request(
                                                report( DELTA_GUID ,false));
        sut.createAlarmReport(request);
    }

    @Test
    @SneakyThrows
    public void testCreateAlarmReportWithEnrichedDataOfMovementModule(){
        CreateAlarmsReportRequest request = request(
                report( DELTA_GUID,false ,item("test")));
        List<MovementBaseType> movementBaseTypes = new ArrayList<>();
        MovementBaseType responseItem = responseItem(DELTA_GUID);
        movementBaseTypes.add(responseItem);

        when(movementSender.findRawMovements(any())).thenReturn(movementBaseTypes);
        when(rulesDomainModel.createAlarmReport(any(AlarmReportType.class))).thenAnswer(returnsFirstArg());
        AlarmReportType createdReport = sut.createAlarmReport(request).get(0);
        assertNotNull(createdReport.getRawMovement().getPosition());
        assertEquals(createdReport.getRawMovement().getMovementType().name(),responseItem.getMovementType().name());
    }

    @Test
    @SneakyThrows
    public void testCreateAlarmReportWithoutEnrichedDataOfMovementModule(){
        CreateAlarmsReportRequest request = request(
                report( UUID.randomUUID().toString(),true ,item("test")));
        List<MovementBaseType> movementBaseTypes = new ArrayList<>();
        MovementBaseType responseItem = responseItem(DELTA_GUID);
        movementBaseTypes.add(responseItem);

        when(movementSender.findRawMovements(any())).thenReturn(movementBaseTypes);
        when(rulesDomainModel.createAlarmReport(any(AlarmReportType.class))).thenAnswer(returnsFirstArg());
        AlarmReportType createdReport = sut.createAlarmReport(request).get(0);
        assertNull(createdReport.getRawMovement().getPosition());
    }

    private static CreateAlarmsReportRequest request(AlarmReportType... reports){
        CreateAlarmsReportRequest request = new CreateAlarmsReportRequest();
        for(AlarmReportType report : reports)
            request.getAlarm().add(report);
        return request;
    }

    private MovementBaseType responseItem(String guid){
        MovementBaseType movementBaseType = new MovementBaseType();
        movementBaseType.setGuid(guid);
        movementBaseType.setDuplicate(false);
        movementBaseType.setReportedCourse(180.0);
        movementBaseType.setReportedSpeed(14.0);
        MovementPoint movementPoint = new MovementPoint();
        movementPoint.setLongitude(41.17);
        movementPoint.setLatitude(18.10);
        movementBaseType.setPosition(movementPoint);
        movementBaseType.setMovementType(MovementTypeType.POS);
        movementBaseType.setPositionTime(new Date());
        return movementBaseType;
    }

    private static AlarmReportType report(String assetGuid,boolean guidGenerated,AlarmItemType... items){
        AlarmReportType alarmReportType = new AlarmReportType();
        for(AlarmItemType item : items)
            alarmReportType.getAlarmItem().add(item);
        alarmReportType.setAssetGuid(assetGuid);
        alarmReportType.setRawMovement(raw( rowData(assetGuid,guidGenerated,null,"XR003","SVK","SVK123456789","IRCS3")));
        alarmReportType.setStatus(AlarmStatusType.OPEN);
        alarmReportType.setGuid(UUID.randomUUID().toString());
        return alarmReportType;
    }

    private static RawMovementType raw(SubscriptionRowData rowData){
        RawMovementType raw = new RawMovementType();
        raw.setFlagState(rowData.flagState);
        raw.setGuid(rowData.guid);
        raw.setGuidGenerated(rowData.guidGenerated);
        raw.setConnectId(rowData.connectId);
        raw.setExternalMarking(rowData.extMark);
        raw.setFlagState(rowData.extMark);
        return raw;
    }

    private static AlarmItemType item(String ruleName){
        AlarmItemType item = new AlarmItemType();
        item.setRuleName(ruleName);
        item.setGuid(UUID.randomUUID().toString());
        return item;
    }

    private static SubscriptionRowData rowData(String guid,boolean guidGenerated,String connectId,String extMark,String flagState,String cfr,String ircs){
        SubscriptionRowData rawData = new SubscriptionRowData();
        AssetId assetId = new AssetId();
        assetId.setAssetType(AssetType.VESSEL);
        addVesselIdIfNotNull(assetId, AssetIdType.CFR, cfr);
        addVesselIdIfNotNull(assetId, AssetIdType.IRCS, ircs);
        rawData.assetId = assetId;
        rawData.guid = guid;
        rawData.guidGenerated = guidGenerated;
        rawData.connectId = connectId;
        rawData.extMark = extMark;
        rawData.flagState = flagState;
        return rawData;
    }

    private static void addVesselIdIfNotNull(AssetId assetId, AssetIdType assetIdType, String value) {
        if (value != null) {
            AssetIdList assetIdList = new AssetIdList();
            assetIdList.setIdType(assetIdType);
            assetIdList.setValue(value);
            assetId.getAssetIdList().add(assetIdList);
        }
    }

    private static class SubscriptionRowData{
        AssetId assetId;
        String guid;
        boolean guidGenerated;
        String connectId;
        String extMark;
        String flagState;
    }
}
