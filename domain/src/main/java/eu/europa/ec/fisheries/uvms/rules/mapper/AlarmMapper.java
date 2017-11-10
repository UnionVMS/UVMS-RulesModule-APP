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
package eu.europa.ec.fisheries.uvms.rules.mapper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmItemType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdType;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementComChannelType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.Activity;
import eu.europa.ec.fisheries.uvms.rules.entity.AlarmItem;
import eu.europa.ec.fisheries.uvms.rules.entity.AlarmReport;
import eu.europa.ec.fisheries.uvms.rules.entity.Asset;
import eu.europa.ec.fisheries.uvms.rules.entity.MobileTerminal;
import eu.europa.ec.fisheries.uvms.rules.entity.MobileTerminalId;
import eu.europa.ec.fisheries.uvms.rules.entity.Position;
import eu.europa.ec.fisheries.uvms.rules.entity.RawMovement;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocalBean
@Stateless
public class AlarmMapper {

    private final static Logger LOG = LoggerFactory.getLogger(AlarmMapper.class);

    private static AlarmReportType toAlarmReportType(AlarmReportType alarmReportType, AlarmReport alarmReportEntity) throws DaoMappingException {
        if (alarmReportEntity == null) {
            return null;
        }

        try {
            // Base
            alarmReportType.setGuid(alarmReportEntity.getGuid());
            alarmReportType.setAssetGuid(alarmReportEntity.getAssetGuid());
            alarmReportType.setOpenDate(DateUtils.dateToString(alarmReportEntity.getCreatedDate()));
            if (alarmReportEntity.getStatus() != null) {
                alarmReportType.setStatus(AlarmStatusType.valueOf(alarmReportEntity.getStatus()));
            }
            alarmReportType.setUpdated(DateUtils.dateToString(alarmReportEntity.getUpdated()));
            alarmReportType.setUpdatedBy(alarmReportEntity.getUpdatedBy());
            alarmReportType.setRecipient(alarmReportEntity.getRecipient());
            alarmReportType.setPluginType(alarmReportEntity.getPluginType());

            // Raw movement
            if (alarmReportEntity.getRawMovement() != null) {
                RawMovementType rawMovement = new RawMovementType();
                rawMovement.setGuid(alarmReportEntity.getRawMovement().getGuid());
                rawMovement.setConnectId(alarmReportEntity.getRawMovement().getConnectId());
                if (alarmReportEntity.getRawMovement().getComChannelType() != null) {
                    rawMovement.setComChannelType(MovementComChannelType.valueOf(alarmReportEntity.getRawMovement().getComChannelType()));
                }
                if (alarmReportEntity.getRawMovement().getMovementType() != null) {
                    rawMovement.setMovementType(MovementTypeType.valueOf(alarmReportEntity.getRawMovement().getMovementType()));
                }
                rawMovement.setReportedSpeed(alarmReportEntity.getRawMovement().getReportedSpeed());
                rawMovement.setReportedCourse(alarmReportEntity.getRawMovement().getReportedCourse());
                if (alarmReportEntity.getRawMovement().getSource() != null) {
                    rawMovement.setSource(MovementSourceType.valueOf(alarmReportEntity.getRawMovement().getSource()));
                }
                rawMovement.setStatus(alarmReportEntity.getRawMovement().getStatus());
                if (alarmReportEntity.getRawMovement().getPositionTime() != null) {
                    rawMovement.setPositionTime(alarmReportEntity.getRawMovement().getPositionTime());
                }

                rawMovement.setAssetName(alarmReportEntity.getRawMovement().getAssetName());
                rawMovement.setFlagState(alarmReportEntity.getRawMovement().getFlagState());
                rawMovement.setExternalMarking(alarmReportEntity.getRawMovement().getExternalMarking());

                // Raw movement - activity
                if (alarmReportEntity.getRawMovement().getActivity() != null) {
                    MovementActivityType activity = new MovementActivityType();
                    activity.setCallback(alarmReportEntity.getRawMovement().getActivity().getCallback());
                    activity.setMessageId(alarmReportEntity.getRawMovement().getActivity().getMessageId());
                    if (alarmReportEntity.getRawMovement().getActivity().getMessageType() != null) {
                        activity.setMessageType(MovementActivityTypeType.valueOf(alarmReportEntity.getRawMovement().getActivity().getMessageType()));
                    }
                    rawMovement.setActivity(activity);
                } else {
                    LOG.warn("Activity missing for Raw Movement Entity '{}'", alarmReportEntity.getRawMovement().getGuid());
                }

                // Raw movement - asset
                if (alarmReportEntity.getRawMovement().getAsset() != null) {
                    Asset asset = alarmReportEntity.getRawMovement().getAsset();
                    if (asset != null) {
                        AssetId assetId = new AssetId();
                        if (asset.getAssetType() != null) {
                            assetId.setAssetType(AssetType.valueOf(asset.getAssetType()));
                        }
                        List<AssetIdList> assetIdLists = new ArrayList<>();

                        List<eu.europa.ec.fisheries.uvms.rules.entity.AssetIdList> assetIdListArray = alarmReportEntity.getRawMovement().getAsset()
                                .getAssetIdList();
                        for (eu.europa.ec.fisheries.uvms.rules.entity.AssetIdList assetIdList : assetIdListArray) {
                            AssetIdList assetIdListRaw = new AssetIdList();
                            assetIdListRaw.setValue(assetIdList.getValue());
                            if (assetIdList.getType() != null) {
                                assetIdListRaw.setIdType(AssetIdType.valueOf(assetIdList.getType()));
                            }
                            assetIdLists.add(assetIdListRaw);
                        }
                        assetId.getAssetIdList().addAll(assetIdLists);
                        rawMovement.setAssetId(assetId);
                    }
                } else {
                    LOG.warn("AssetId missing for Raw Movement Entity '{}'", alarmReportEntity.getRawMovement().getGuid());
                }

                // Raw movement - position
                if (alarmReportEntity.getRawMovement().getPosition() != null) {
                    MovementPoint position = new MovementPoint();
                    position.setAltitude(alarmReportEntity.getRawMovement().getPosition().getAltitude());
                    position.setLatitude(alarmReportEntity.getRawMovement().getPosition().getLatitude());
                    position.setLongitude(alarmReportEntity.getRawMovement().getPosition().getLongitude());
                    rawMovement.setPosition(position);
                } else {
                    LOG.warn("Position missing for Raw Movement Entity '{}'", alarmReportEntity.getRawMovement().getGuid());
                }

                // Raw movement - mobile terminal
                if (alarmReportEntity.getRawMovement().getMobileTerminal() != null) {
                    MobileTerminalType mobileTerminalIdRaw = new MobileTerminalType();
                    mobileTerminalIdRaw.setConnectId(alarmReportEntity.getRawMovement().getMobileTerminal().getConnectId());
                    mobileTerminalIdRaw.setGuid(alarmReportEntity.getRawMovement().getMobileTerminal().getGuid());

                    ArrayList<IdList> idLists = new ArrayList<IdList>();
                    List<MobileTerminalId> mobileTerminalIds = alarmReportEntity.getRawMovement().getMobileTerminal().getMobileTerminalId();
                    for (MobileTerminalId mobileTerminalId : mobileTerminalIds) {
                        IdList idList = new IdList();
                        if (mobileTerminalId.getType() != null) {
                            idList.setType(IdType.valueOf(mobileTerminalId.getType()));
                        }
                        idList.setValue(mobileTerminalId.getValue());
                        idLists.add(idList);
                    }
                    mobileTerminalIdRaw.getMobileTerminalIdList().addAll(idLists);
                    rawMovement.setMobileTerminal(mobileTerminalIdRaw);
                } else {
                    LOG.warn("Mobile Terminal missing for Raw Movement Entity '{}'", alarmReportEntity.getRawMovement().getGuid());
                }

                // Add Movement
                alarmReportType.setRawMovement(rawMovement);
            } else {
                LOG.warn("Raw Movement missing for Alarm Report Entity '{}'", alarmReportEntity.getGuid());
            }

            // Alarm items
            List<AlarmItem> alarmItems = alarmReportEntity.getAlarmItemList();
            for (AlarmItem alarmItem : alarmItems) {
                AlarmItemType alarmItemType = new AlarmItemType();
                alarmItemType.setGuid(alarmItem.getGuid());
                alarmItemType.setRuleGuid(alarmItem.getRuleGuid());
                alarmItemType.setRuleName(alarmItem.getRuleName());

                alarmReportType.getAlarmItem().add(alarmItemType);
            }

            return alarmReportType;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to model. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to model. ]", e);
        }
    }

    public static AlarmReport toAlarmReportEntity(AlarmReport alarmReportEntity, AlarmReportType alarmReportType) throws DaoMappingException {
        try {
            // Base
            alarmReportEntity.setAssetGuid(alarmReportType.getAssetGuid());
            alarmReportEntity.setCreatedDate(DateUtils.stringToDate(alarmReportType.getOpenDate()));
            if (alarmReportType.getStatus() != null) {
                alarmReportEntity.setStatus(alarmReportType.getStatus().name());
            }
            alarmReportEntity.setUpdated(DateUtils.stringToDate(alarmReportType.getUpdated()));
            alarmReportEntity.setUpdatedBy(alarmReportType.getUpdatedBy());
            alarmReportEntity.setUpdated(new Date());
            alarmReportEntity.setUpdatedBy(alarmReportType.getUpdatedBy());
            alarmReportEntity.setRecipient(alarmReportType.getRecipient());
            alarmReportEntity.setPluginType(alarmReportType.getPluginType());

            return alarmReportEntity;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to entity. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to entity. ]", e);
        }
    }

    public static AlarmItem toAlarmItemEntity(AlarmItemType alarmItemType) throws DaoMappingException {
        try {
            AlarmItem alarmItem = new AlarmItem();
            alarmItem.setRuleGuid(alarmItemType.getRuleGuid());
            alarmItem.setRuleName(alarmItemType.getRuleName());
            alarmItem.setGuid(alarmItemType.getGuid());
            alarmItem.setUpdated(new Date());
            alarmItem.setUpdatedBy("UVMS");

            return alarmItem;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to entity. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to entity. ]", e);
        }
    }

    public static RawMovement toRawMovementEntity(RawMovementType rawMovementType) {
        // Raw movement
        RawMovement rawMovementEntity = null;

        if (rawMovementType != null) {
            rawMovementEntity = new RawMovement();

            if (rawMovementType.getPositionTime() != null) {
                rawMovementEntity.setPositionTime(rawMovementType.getPositionTime());
            }
            if (rawMovementType.getComChannelType() != null) {
                rawMovementEntity.setComChannelType(rawMovementType.getComChannelType().name());
            }
            rawMovementEntity.setConnectId(rawMovementType.getConnectId());
            rawMovementEntity.setGuid(rawMovementType.getGuid());
            if (rawMovementType.getMovementType() != null) {
                rawMovementEntity.setMovementType(rawMovementType.getMovementType().name());
            }
            rawMovementEntity.setReportedSpeed(rawMovementType.getReportedSpeed());
            rawMovementEntity.setReportedCourse(rawMovementType.getReportedCourse());
            if (rawMovementType.getSource() != null) {
                rawMovementEntity.setSource(rawMovementType.getSource().name());
            }
            rawMovementEntity.setStatus(rawMovementType.getStatus());
            rawMovementEntity.setUpdated(new Date());
            rawMovementEntity.setUpdatedBy("UVMS");

            rawMovementEntity.setAssetName(rawMovementType.getAssetName());
            rawMovementEntity.setFlagState(rawMovementType.getFlagState());
            rawMovementEntity.setExternalMarking(rawMovementType.getExternalMarking());

            // Raw movement - activity
            if (rawMovementType.getActivity() != null) {
                Activity activity = new Activity();
                activity.setCallback(rawMovementType.getActivity().getCallback());
                activity.setMessageId(rawMovementType.getActivity().getMessageId());
                if (rawMovementType.getActivity().getMessageType() != null) {
                    activity.setMessageType(rawMovementType.getActivity().getMessageType().name());
                }
                rawMovementEntity.setActivity(activity);
                activity.setRawMovement(rawMovementEntity);
            } else {
                LOG.warn("Activity missing for Raw Movement Type '{}'", rawMovementType.getGuid());
            }

            // Raw movement - asset
            if (rawMovementType.getAssetId() != null) {
                Asset asset = new Asset();
                if (rawMovementType.getAssetId().getAssetType() != null) {
                    asset.setAssetType(rawMovementType.getAssetId().getAssetType().name());
                }
                rawMovementEntity.setAsset(asset);
                asset.setRawMovement(rawMovementEntity);

                if (rawMovementType.getAssetId().getAssetIdList() != null) {
                    List<AssetIdList> assetIdList = rawMovementType.getAssetId().getAssetIdList();
                    ArrayList<eu.europa.ec.fisheries.uvms.rules.entity.AssetIdList> assetIdListsEntityList = new ArrayList<eu.europa.ec.fisheries.uvms.rules.entity.AssetIdList>();
                    for (AssetIdList assetIdListRaw : assetIdList) {
                        eu.europa.ec.fisheries.uvms.rules.entity.AssetIdList assetIdListEntity = new eu.europa.ec.fisheries.uvms.rules.entity.AssetIdList();
                        assetIdListEntity.setValue(assetIdListRaw.getValue());
                        assetIdListEntity.setAsset(asset);
                        if (assetIdListRaw.getIdType() != null) {
                            assetIdListEntity.setType(assetIdListRaw.getIdType().name());
                        }
                        assetIdListsEntityList.add(assetIdListEntity);
                    }
                    asset.setAssetIdList(assetIdListsEntityList);
                }
            } else {
                LOG.warn("AssetId missing for Raw Movement Type '{}'", rawMovementType.getGuid());
            }

            // Raw movement - mobile terminal
            if (rawMovementType.getMobileTerminal() != null) {
                MobileTerminal mobileTerminal = new MobileTerminal();
                List<MobileTerminalId> mobileTerminalIds = new ArrayList<>();

                if (rawMovementType.getMobileTerminal().getGuid() != null) {
                    mobileTerminal.setGuid(rawMovementType.getMobileTerminal().getGuid());
                }
                if (rawMovementType.getMobileTerminal().getConnectId() != null) {
                    mobileTerminal.setConnectId(rawMovementType.getMobileTerminal().getConnectId());
                }

                List<IdList> mobileTerminalIdList = rawMovementType.getMobileTerminal().getMobileTerminalIdList();
                for (IdList mobileTerminalIdType : mobileTerminalIdList) {
                    MobileTerminalId mobileTerminalId = new MobileTerminalId();
                    if (mobileTerminalIdType.getType() != null) {
                        mobileTerminalId.setType(mobileTerminalIdType.getType().name());
                    }
                    mobileTerminalId.setValue(mobileTerminalIdType.getValue());
                    mobileTerminalId.setMobileTerminal(mobileTerminal);
                    mobileTerminalIds.add(mobileTerminalId);
                }
                mobileTerminal.setMobileTerminalId(mobileTerminalIds);

                // Add mobile terminal
                rawMovementEntity.setMobileTerminal(mobileTerminal);
                mobileTerminal.setRawMovement(rawMovementEntity);
            } else {
                LOG.warn("Mobile Terminal missing for Raw Movement Type'{}'", rawMovementType.getGuid());
            }

            // Raw movement - position
            if (rawMovementType.getPosition() != null) {
                Position position = new Position();
                position.setAltitude(rawMovementType.getPosition().getAltitude());
                position.setLatitude(rawMovementType.getPosition().getLatitude());
                position.setLongitude(rawMovementType.getPosition().getLongitude());
                rawMovementEntity.setPosition(position);
                position.setRawMovement(rawMovementEntity);
            }
        }

        return rawMovementEntity;
    }

    public static AlarmReport toAlarmReportEntity(AlarmReportType alarmReportType) throws DaoMappingException {
        AlarmReport alarmReportEntity = new AlarmReport();
        return toAlarmReportEntity(alarmReportEntity, alarmReportType);
    }

    public static AlarmReportType toAlarmReportType(AlarmReport alarmReportEntity) throws DaoMappingException {
        AlarmReportType alarmReportType = new AlarmReportType();
        return toAlarmReportType(alarmReportType, alarmReportEntity);
    }

    public static XMLGregorianCalendar dateToXmlGregorian(Date timestamp) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        XMLGregorianCalendar xmlCalendar = null;
        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        return xmlCalendar;
    }

}