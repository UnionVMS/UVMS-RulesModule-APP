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
import java.util.UUID;

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

public class RawMovementFactMapper {
    public static RawMovementFact mapRawMovementFact(RawMovementType rawMovement, MobileTerminalType mobileTerminal, Asset asset, String pluginType) {
        RawMovementFact fact = new RawMovementFact();
        fact.setRawMovementType(rawMovement);
        fact.setOk(true);
        fact.setPluginType(pluginType);

        // Base
        if (rawMovement.getComChannelType() != null) {
            fact.setComChannelType(rawMovement.getComChannelType().name());
        }
        fact.setMovementGuid(UUID.randomUUID().toString());
        if (rawMovement.getMovementType() != null) {
            fact.setMovementType(rawMovement.getMovementType().name());
        }
        if (rawMovement.getPositionTime() != null) {
            fact.setPositionTime(rawMovement.getPositionTime());
        }
        fact.setReportedCourse(rawMovement.getReportedCourse());
        fact.setReportedSpeed(rawMovement.getReportedSpeed());
        if (rawMovement.getSource() != null) {
            fact.setSource(rawMovement.getSource().name());
        }
        fact.setStatusCode(rawMovement.getStatus());

        fact.setAssetName(rawMovement.getAssetName());
        fact.setFlagState(rawMovement.getFlagState());
        fact.setExternalMarking(rawMovement.getExternalMarking());

        // Activity
        if (rawMovement.getActivity() != null) {
            fact.setActivityCallback(rawMovement.getActivity().getCallback());
            fact.setActivityMessageId(rawMovement.getActivity().getMessageId());
            if (rawMovement.getActivity().getMessageType() != null) {
                fact.setActivityMessageType(rawMovement.getActivity().getMessageType().name());
            }
        }

        // Position
        if (rawMovement.getPosition() != null) {
            fact.setAltitude(rawMovement.getPosition().getAltitude());
            fact.setLatitude(rawMovement.getPosition().getLatitude());
            fact.setLongitude(rawMovement.getPosition().getLongitude());
        }

        if (rawMovement.getAssetId() != null) {
            List<AssetIdList> assetIds = rawMovement.getAssetId().getAssetIdList();
            for (AssetIdList assetId : assetIds) {
                switch (assetId.getIdType()) {
                    case CFR:
                        fact.setCfr(assetId.getValue());
                        break;
                    case IRCS:
                        fact.setIrcs(assetId.getValue());
                        break;
                    case ID:
                    case IMO:
                    case MMSI:
                    case GUID:
                }
            }
        }

        if (rawMovement.getMobileTerminal() != null) {
            List<IdList> mobileTerminalIds = rawMovement.getMobileTerminal().getMobileTerminalIdList();
            for (IdList mobileTerminalId : mobileTerminalIds) {
                switch(mobileTerminalId.getType()) {
                    case DNID:
                        fact.setMobileTerminalDnid(mobileTerminalId.getValue());
                        break;
                    case MEMBER_NUMBER:
                        fact.setMobileTerminalMemberNumber(mobileTerminalId.getValue());
                        break;
                    case SERIAL_NUMBER:
                        fact.setMobileTerminalSerialNumber(mobileTerminalId.getValue());
                        break;
                    case LES:
                        break;
                }
            }
        }

        // From Mobile Terminal
        if (mobileTerminal != null) {
            fact.setMobileTerminalConnectId(mobileTerminal.getConnectId());
            fact.setMobileTerminalType(mobileTerminal.getType());
        }

        // From Asset
        if (asset != null) {
            fact.setAssetGuid(asset.getAssetId().getGuid());
            fact.setAssetName(asset.getName());
        }

        return fact;
    }

}