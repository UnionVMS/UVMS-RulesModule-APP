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

import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementActivityTypeType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;

/**
 * Created by osdjup on 2016-05-18.
 */
public class MovementBaseTypeMapper {
    public static MovementBaseType mapRawMovementFact(RawMovementType rawMovement) {
        MovementBaseType movement = new MovementBaseType();
        movement.setActivity(mapMovementActivityType(rawMovement.getActivity()));
        movement.setAssetId(mapAssetId(rawMovement.getAssetId()));
        movement.setConnectId(rawMovement.getConnectId());
        movement.setGuid(rawMovement.getGuid());
        movement.setInternalReferenceNumber(rawMovement.getInternalReferenceNumber());
        movement.setMovementType(mapMovementTypeType(rawMovement.getMovementType()));
        movement.setPosition(mapMovementPoint(rawMovement.getPosition()));
        movement.setPositionTime(rawMovement.getPositionTime());
        movement.setReportedCourse(rawMovement.getReportedCourse());
        movement.setReportedSpeed(rawMovement.getReportedSpeed());
        movement.setSource(mapSourceType(rawMovement.getSource()));
        movement.setStatus(rawMovement.getStatus());
        movement.setTripNumber(rawMovement.getTripNumber());

        return movement;
    }

    private static MovementActivityType mapMovementActivityType(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityType rawActivityType) {
        if (rawActivityType == null) {
            return null;
        }
        MovementActivityType activityType = new MovementActivityType();
        activityType.setCallback(rawActivityType.getCallback());
        activityType.setMessageId(rawActivityType.getMessageId());
        activityType.setMessageType(mapMovementActivityTypeType(rawActivityType.getMessageType()));

        return activityType;
    }

    private static MovementActivityTypeType mapMovementActivityTypeType(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityTypeType rawActivityTypeType) {
        if (rawActivityTypeType == null) {
            return null;
        }
        try {
            return MovementActivityTypeType.valueOf(rawActivityTypeType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static AssetId mapAssetId(eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId rawAssetId) {
        if (rawAssetId == null) {
            return null;
        }
        AssetId assetId = new AssetId();
        try {
            if (rawAssetId.getAssetType() != null) {
                assetId.setAssetType(AssetType.valueOf(rawAssetId.getAssetType().value()));
            }
            for (AssetIdList assetIdList : rawAssetId.getAssetIdList()) {
                assetId.setValue(assetIdList.getValue());
                assetId.setIdType(AssetIdType.valueOf(assetIdList.getIdType().value()));
                if (assetIdList.getIdType().equals(eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.GUID)) {
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        return assetId;
    }

    private static MovementTypeType mapMovementTypeType(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementTypeType rawMovementTypeType) {
        if (rawMovementTypeType == null) {
            return null;
        }
        try {
            return MovementTypeType.valueOf(rawMovementTypeType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static MovementPoint mapMovementPoint(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementPoint rawMovementPoint) {
        if (rawMovementPoint == null) {
            return null;
        }
        MovementPoint movementPoint = new MovementPoint();
        movementPoint.setAltitude(rawMovementPoint.getAltitude());
        movementPoint.setLatitude(rawMovementPoint.getLatitude());
        movementPoint.setLongitude(rawMovementPoint.getLongitude());

        return movementPoint;
    }

    private static MovementSourceType mapSourceType(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementSourceType rawMovementSourceType) {
        if (rawMovementSourceType == null) {
            return null;
        }
        try {
            return MovementSourceType.valueOf(rawMovementSourceType.value());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}