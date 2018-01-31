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
package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import java.util.List;

import eu.europa.ec.fisheries.schema.exchange.module.v1.ExchangeModuleMethod;
import eu.europa.ec.fisheries.schema.exchange.module.v1.ProcessedMovementResponse;
import eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdType;
import eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementComChannelType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementMetaData;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.SetReportMovementType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.JAXBMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeMovementMapper {
    private final static Logger LOG = LoggerFactory.getLogger(ExchangeMovementMapper.class);

    /**
     * Maps only what Exchange needs to complete it's work in the callback
     *
     * @param rawMovement
     * @return the necessary parts of the original SetMovement request
     */
    public static SetReportMovementType mapExchangeMovement(RawMovementType rawMovement) {
        LOG.debug("Mapping response movement to Exchange");

        SetReportMovementType setReportMovementType = new SetReportMovementType();
        setReportMovementType.setTimestamp(rawMovement.getDateRecieved());
        setReportMovementType.setPluginName(rawMovement.getPluginName());
        if (rawMovement.getPluginType() != null) {
            setReportMovementType.setPluginType(PluginType.valueOf(rawMovement.getPluginType()));
        }
        MovementBaseType movement = new MovementBaseType();
        if (rawMovement.getSource() != null) {
            movement.setSource(MovementSourceType.fromValue(rawMovement.getSource().value()));
        }

        // MobileTerminalId
        if (rawMovement.getMobileTerminal() != null) {
            MobileTerminalId mobileTerminalId = new MobileTerminalId();
            mobileTerminalId.setGuid(rawMovement.getMobileTerminal().getGuid());
            mobileTerminalId.setConnectId(rawMovement.getMobileTerminal().getConnectId());
            List<IdList> mtIds = rawMovement.getMobileTerminal().getMobileTerminalIdList();
            for (IdList mtId : mtIds) {
                eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdList idList = new eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdList();
                idList.setType(IdType.fromValue(mtId.getType().value()));
                idList.setValue(mtId.getValue());

                mobileTerminalId.getMobileTerminalIdList().add(idList);
            }
            movement.setMobileTerminalId(mobileTerminalId);
        }

        // AssetId
        if (rawMovement.getAssetId() != null) {
            eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetId assetId = new eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetId();
            if (rawMovement.getAssetId().getAssetType() != null) {
                assetId.setAssetType(eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetType.fromValue(rawMovement.getAssetId().getAssetType().value()));
            }
            List<AssetIdList> aIds = rawMovement.getAssetId().getAssetIdList();
            for (AssetIdList aId : aIds) {
                eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList idList = new eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList();
                idList.setIdType(eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdType.fromValue(aId.getIdType().value()));
                idList.setValue(aId.getValue());

                assetId.getAssetIdList().add(idList);
            }
            movement.setAssetId(assetId);
        }

        setFlagState(rawMovement, movement);
        setReportMovementType.setMovement(movement);

        return setReportMovementType;
    }

    // Will be recipient in Exchange log
    private static void setFlagState(RawMovementType rawMovement, MovementBaseType movement){
        if (rawMovement.getPluginType()!=null && (rawMovement.getPluginType().equals(PluginType.FLUX.value())|| rawMovement.getPluginType().equals(PluginType.NAF.value()))) {
            movement.setFlagState(rawMovement.getFlagState());
        }
    }

    public static String mapToProcessedMovementResponse(SetReportMovementType orgRequest, MovementRefType movementRef, String username) throws ExchangeModelMapperException {
        ProcessedMovementResponse response = new ProcessedMovementResponse();
        response.setMethod(ExchangeModuleMethod.PROCESSED_MOVEMENT);
        response.setOrgRequest(orgRequest);
        response.setMovementRefType(movementRef);
        response.setUsername(username);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static MovementType mapToExchangeMovementType(eu.europa.ec.fisheries.schema.movement.v1.MovementType movementMovement) {
        MovementType movement = new MovementType();
        movement.setGuid(movementMovement.getGuid());
        movement.setConnectId(movementMovement.getConnectId());
        movement.setTripNumber(movementMovement.getTripNumber());
        movement.setCalculatedCourse(movementMovement.getCalculatedCourse());
        movement.setCalculatedSpeed(movementMovement.getCalculatedSpeed());
        movement.setMetaData(mapMetaData(movementMovement.getMetaData()));
        movement.setWkt(movementMovement.getWkt());
        movement.setActivity(mapActivity(movementMovement.getActivity()));
        movement.setAssetId(mapAssetId(movementMovement.getAssetId()));
        movement.setComChannelType(mapComChannelType(movementMovement.getComChannelType()));
        movement.setConnectId(movementMovement.getConnectId());
        movement.setInternalReferenceNumber(movementMovement.getInternalReferenceNumber());
        movement.setMovementType(mapMovementType(movementMovement.getMovementType()));
        movement.setPosition(mapPosition(movementMovement.getPosition()));
        movement.setPositionTime(movementMovement.getPositionTime());
        movement.setSource(mapSource(movementMovement.getSource()));
        movement.setStatus(movementMovement.getStatus());
        movement.setTripNumber(movementMovement.getTripNumber());

        return movement;
    }

    private static MovementSourceType mapSource(eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType movementSourceType) {
        if (movementSourceType == null) {
            return null;
        }
        try {
            return MovementSourceType.valueOf(movementSourceType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static MovementPoint mapPosition(eu.europa.ec.fisheries.schema.movement.v1.MovementPoint movementPoint) {
        if (movementPoint == null) {
            return null;
        }
        MovementPoint point = new MovementPoint();
        point.setAltitude(movementPoint.getAltitude());
        point.setLatitude(movementPoint.getLatitude());
        point.setLongitude(movementPoint.getLongitude());

        return point;
    }

    private static MovementTypeType mapMovementType(eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType movementTypeType) {
        if (movementTypeType == null) {
            return null;
        }
        try {
            return MovementTypeType.valueOf(movementTypeType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static MovementComChannelType mapComChannelType(eu.europa.ec.fisheries.schema.movement.v1.MovementComChannelType movementComChannelType) {
        if (movementComChannelType == null) {
            return null;
        }
        try {
            return MovementComChannelType.valueOf(movementComChannelType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static AssetId mapAssetId(eu.europa.ec.fisheries.schema.movement.asset.v1.AssetId assetId) {
        if (assetId == null) {
            return null;
        }
        try {
            AssetId exAssetId = new AssetId();
            eu.europa.ec.fisheries.schema.movement.asset.v1.AssetIdType type = assetId.getIdType();
            eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList idList = new eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList();
            idList.setIdType(AssetIdType.valueOf(assetId.getIdType().value()));
            idList.setValue(assetId.getValue());

            exAssetId.setAssetType(AssetType.valueOf(assetId.getAssetType().value()));
            exAssetId.getAssetIdList().add(idList);
            return exAssetId;
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    private static MovementMetaData mapMetaData(eu.europa.ec.fisheries.schema.movement.v1.MovementMetaData movementMetaData) {
        if (movementMetaData == null) {
            return null;
        }
        MovementMetaData metaData = new MovementMetaData();
        if (movementMetaData.getClosestCountry() != null) {
            metaData.setClosestCountryCoast(movementMetaData.getClosestCountry().getCode());
            metaData.setDistanceToCountryCoast(movementMetaData.getClosestCountry().getDistance());
        }
        if (movementMetaData.getClosestPort() != null) {
            metaData.setClosestPort(movementMetaData.getClosestPort().getCode());
            metaData.setDistanceToClosestPort(movementMetaData.getClosestPort().getDistance());
        }


        return metaData;
    }

    private static MovementActivityType mapActivity(eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType activityType) {
        if (activityType == null) {
            return null;
        }
        MovementActivityType activity = new MovementActivityType();
        activity.setMessageId(activityType.getMessageId());
        activity.setCallback(activityType.getCallback());
        activity.setMessageType(mapActivityType(activityType.getMessageType()));

        return activity;
    }

    private static MovementActivityTypeType mapActivityType(eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType activityTypeType) {
        if (activityTypeType == null) {
            return null;
        }
        try {
            return MovementActivityTypeType.valueOf(activityTypeType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}