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

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementMetaDataAreaType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

public class MovementFactMapper {
    public static MovementFact mapMovementFact(MovementType movement, MobileTerminalType mobileTerminal, Asset asset, String comChannelType, List<AssetGroup> assetGroups, Long timeDiffInSeconds, Integer numberOfReportsLast24Hours, String channelGuid, List<String> vicinityOf) throws RulesServiceException {
        if (movement == null) {
            throw new RulesServiceException("Movement was null, asset: " + asset + ", mobileTerminal: " + mobileTerminal);
        }
        MovementFact fact = new MovementFact();

        fact.setChannelGuid(channelGuid);

        fact.setMovementMovement(movement);
        fact.setMovementGuid(movement.getGuid());

        // ROOT
        for (AssetGroup assetGroup : assetGroups) {
            fact.getAssetGroups().add(assetGroup.getGuid());
        }

        // ACTIVITY
        if (movement.getActivity() != null) {
            fact.setActivityCallback(movement.getActivity().getCallback());
            fact.setActivityMessageId(movement.getActivity().getMessageId());
            if (movement.getActivity().getMessageType() != null) {
                fact.setActivityMessageType(movement.getActivity().getMessageType().name());
            }
        }

        // AREA
        if (movement.getMetaData() != null) {
            List<MovementMetaDataAreaType> areas = movement.getMetaData().getAreas();
            for (MovementMetaDataAreaType area : areas) {
                if (MovementTypeType.POS.equals(area.getTransitionType()) || MovementTypeType.ENT.equals(area.getTransitionType())) {
                    fact.getAreaCodes().add(area.getCode());
                    fact.getAreaTypes().add(area.getAreaType());
                }
                if (MovementTypeType.ENT.equals(area.getTransitionType())) {
                    fact.getEntAreaCodes().add(area.getName());
                    fact.getAreaCodes().add(area.getCode());
                    fact.getEntAreaTypes().add(area.getAreaType());
                }
                if (MovementTypeType.EXI.equals(area.getTransitionType())) {
                    fact.getExtAreaCodes().add(area.getName());
                    fact.getAreaCodes().add(area.getCode());
                    fact.getExtAreaTypes().add(area.getAreaType());
                }
            }
        }

        // ASSET
        if (asset != null) {
            fact.setAssetIdGearType(asset.getGearType());
            fact.setExternalMarking(asset.getExternalMarking());
            fact.setFlagState(asset.getCountryCode());
            fact.setCfr(asset.getCfr());
            fact.setIrcs(asset.getIrcs());
            fact.setAssetName(asset.getName());
            fact.setAssetGuid(asset.getAssetId().getGuid());
            fact.setAssetStatus(asset.isActive() ? "ACTIVE":"INACTIVE");
            fact.setMmsiNo(asset.getMmsiNo());
        }

        // MOBILE_TERMINAL
        if (mobileTerminal != null) {
            fact.setMobileTerminalGuid(mobileTerminal.getMobileTerminalId().getGuid());
            fact.setComChannelType(comChannelType);
            fact.setMobileTerminalType(mobileTerminal.getType());
            List<ComChannelType> channels = mobileTerminal.getChannels();
            for (ComChannelType channel : channels) {
                List<ComChannelAttribute> chanAttributes = channel.getAttributes();
                for (ComChannelAttribute chanAttribute : chanAttributes) {
                    if (chanAttribute.getType().equals("DNID")) {
                        fact.setMobileTerminalDnid(chanAttribute.getValue());
                    }
                    if (chanAttribute.getType().equals("MEMBER_NUMBER")) {
                        fact.setMobileTerminalMemberNumber(chanAttribute.getValue());
                    }
                }
            }
            List<MobileTerminalAttribute> attributes = mobileTerminal.getAttributes();
            for (MobileTerminalAttribute attribute : attributes) {
                if (attribute.getType().equals("SERIAL_NUMBER")) {
                    fact.setMobileTerminalSerialNumber(attribute.getValue());
                }
            }
            fact.setMobileTerminalStatus(mobileTerminal.isInactive() ? "INACTIVE":"ACTIVE");
        }

        // POSITION
        if (movement.getPosition() != null) {
            fact.setAltitude(movement.getPosition().getAltitude());
            fact.setLatitude(movement.getPosition().getLatitude());
            fact.setLongitude(movement.getPosition().getLongitude());
        }
        fact.setCalculatedCourse(movement.getCalculatedCourse());
        fact.setCalculatedSpeed(movement.getCalculatedSpeed());
        if (movement.getMovementType() != null) {
            fact.setMovementType(movement.getMovementType().name());
        }
        if (movement.getPositionTime() != null) {
            fact.setPositionTime(movement.getPositionTime());
        }
        fact.setReportedCourse(movement.getReportedCourse());
        fact.setReportedSpeed(movement.getReportedSpeed());
        if (movement.getMetaData() != null) {
            if (movement.getMetaData().getFromSegmentType() != null) {
                fact.setSegmentType(movement.getMetaData().getFromSegmentType().name());
            }
            if (movement.getMetaData().getClosestCountry() != null) {
                fact.setClosestCountryCode(movement.getMetaData().getClosestCountry().getCode());
            }
            if (movement.getMetaData().getClosestPort() != null) {
                fact.setClosestPortCode(movement.getMetaData().getClosestPort().getCode());
            }
        }
        if (movement.getSource() != null) {
            fact.setSource(movement.getSource().name());
        }
        fact.setStatusCode(movement.getStatus());
        // TODO
        fact.setVicinityOf(vicinityOf);

        // REPORT
        fact.setTimeDiffPositionReport(timeDiffInSeconds);
        fact.setSumPositionReport(numberOfReportsLast24Hours);

        return fact;
    }

}