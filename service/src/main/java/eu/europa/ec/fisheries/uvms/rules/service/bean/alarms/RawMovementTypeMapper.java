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

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;

public class RawMovementTypeMapper {
    public static void enrichRawMovement(RawMovementType rawMovementType, MovementBaseType movementBaseType) {
        if(movementBaseType.getActivity() != null){
            rawMovementType.setActivity(toActivityType(movementBaseType.getActivity()));
        }
        rawMovementType.setPosition(toPoint(movementBaseType.getPosition()));
        rawMovementType.setPositionTime(movementBaseType.getPositionTime());
        rawMovementType.setReportedCourse(movementBaseType.getReportedCourse());
        rawMovementType.setReportedSpeed(movementBaseType.getReportedSpeed());
        if(movementBaseType.getSource() != null){
            rawMovementType.setSource(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementSourceType.valueOf(movementBaseType.getSource().name()));
        }
        if(movementBaseType.getMovementType() != null){
            rawMovementType.setMovementType(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementTypeType.valueOf(movementBaseType.getMovementType().name()));
        }
        rawMovementType.setTripNumber(movementBaseType.getTripNumber());
        rawMovementType.setStatus(movementBaseType.getStatus());
    }

    static eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityType toActivityType(eu.europa.ec.fisheries.schema.movement.v1.MovementActivityType movementActivityType){
        eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityType activityType = new eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityType();
        activityType.setCallback(movementActivityType.getCallback());
        activityType.setMessageId(movementActivityType.getMessageId());
        if(movementActivityType.getMessageType() != null){
            activityType.setMessageType(eu.europa.ec.fisheries.schema.rules.movement.v1.MovementActivityTypeType.valueOf(movementActivityType.getMessageType().name()));
        }
        return activityType;
    }
    static eu.europa.ec.fisheries.schema.rules.movement.v1.MovementPoint toPoint(eu.europa.ec.fisheries.schema.movement.v1.MovementPoint movementPoint){
        eu.europa.ec.fisheries.schema.rules.movement.v1.MovementPoint point = new eu.europa.ec.fisheries.schema.rules.movement.v1.MovementPoint();
        point.setAltitude(movementPoint.getAltitude());
        point.setLatitude(movementPoint.getLatitude());
        point.setLongitude(movementPoint.getLongitude());
        return point;
    }

}
