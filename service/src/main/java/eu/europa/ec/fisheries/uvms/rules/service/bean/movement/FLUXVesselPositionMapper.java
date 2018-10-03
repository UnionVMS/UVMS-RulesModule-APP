/*
 * ﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 * © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.movement;

import eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.*;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselCountryType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselGeographicalCoordinateType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselPositionEventType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselTransportMeansType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.MeasureType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FLUXVesselPositionMapper {

    private static final String ASSET_EXT_MARKING_CODE = "EXT_MARKING";
    private static final String ASSET_IRCS_CODE = "IRCS";
    private static final String ASSET_UVI_CODE = "UVI";
    private static final String ASSET_CFR_CODE = "CFR";

    private static final String MOVEMENTTYPE_POS = "POS";
    private static final String MOVEMENTTYPE_EXI = "EXI";
    private static final String MOVEMENTTYPE_ENT = "ENT";
    private static final String MOVEMENTTYPE_MAN = "MAN";

    /**
     * This method can be removed when not needed as a historical record of what was happening in the plugin!
     *
     * @param fluxVesselPositionMessage
     * @param registerClassName
     * @return
     */
    public static List<SetReportMovementType> mapToReportMovementTypes(FLUXVesselPositionMessage fluxVesselPositionMessage, String registerClassName) {
        VesselTransportMeansType positionReport = fluxVesselPositionMessage.getVesselTransportMeans();
        List<SetReportMovementType> movementList = new ArrayList<>();
        for (VesselPositionEventType col : positionReport.getSpecifiedVesselPositionEvents()) {
            SetReportMovementType movementType = new SetReportMovementType();
            movementType.setMovement(mapResponse(col, positionReport));
            movementType.setPluginType(PluginType.FLUX);
            movementType.setPluginName(registerClassName);
            movementType.setTimestamp(DateUtils.getNowDateUTC());
            movementList.add(movementType);
        }
        return movementList;
    }


    /**
     * The transformations happening in the
     * 1. Plugin From FLUXVesselPositionMessage to List<SetReportMovementType>
     * and then in
     * 2. Exchange From List<SetReportMovementType> to List<RawMovementType>
     * Has now been compacted in this method.
     * In his method the first tranformation has been avoided since not needed anymore..
     *
     * @param fluxVesselPositionMessage
     * @param registerClassName
     * @return
     */
    public static List<RawMovementType> mapToRawMovementTypes(FLUXVesselPositionMessage fluxVesselPositionMessage, String registerClassName) {
        VesselTransportMeansType positionReport = fluxVesselPositionMessage.getVesselTransportMeans();
        List<RawMovementType> rowMovements = new ArrayList<>();
        for (VesselPositionEventType col : positionReport.getSpecifiedVesselPositionEvents()) {
            MovementBaseType baseMovement = mapResponse(col, positionReport);
            RawMovementType rawMovement = MovementMapper.getInstance().getMapper().map(baseMovement, RawMovementType.class);
            final eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId assetId = rawMovement.getAssetId();
            if (assetId != null && assetId.getAssetIdList() != null) {
                assetId.getAssetIdList().addAll(MovementMapper.mapAssetIdList(baseMovement.getAssetId().getAssetIdList()));
            }
            if (baseMovement.getMobileTerminalId() != null && baseMovement.getMobileTerminalId().getMobileTerminalIdList() != null) {
                rawMovement.getMobileTerminal().getMobileTerminalIdList().addAll(MovementMapper.mapMobileTerminalIdList(baseMovement.getMobileTerminalId().getMobileTerminalIdList()));
            }
            rawMovement.setPluginType(PluginType.FLUX.name());
            rawMovement.setPluginName(registerClassName);
            rawMovement.setDateRecieved(DateUtils.getNowDateUTC());
            rowMovements.add(rawMovement);
        }
        return rowMovements;
    }

    private static MovementBaseType mapResponse(VesselPositionEventType response, VesselTransportMeansType report) {
        MovementBaseType movement = new MovementBaseType();
        HashMap<String, String> extractAssetIds = extractAssetIds(report.getIDS());
        movement.setAssetId(mapToAssetId(extractAssetIds));
        movement.setExternalMarking(extractAssetIds.get(ASSET_EXT_MARKING_CODE));
        movement.setIrcs(extractAssetIds.get(ASSET_IRCS_CODE));
        movement.setMovementType(mapToMovementTypeFromPositionType(movement, response.getTypeCode()));
        setFlagState(movement, report.getRegistrationVesselCountry());
        movement.setPosition(mapToMovementPoint(response.getSpecifiedVesselGeographicalCoordinate()));
        if (response.getObtainedOccurrenceDateTime() != null) {
            movement.setPositionTime(XMLDateUtils.xmlGregorianCalendarToDate(response.getObtainedOccurrenceDateTime().getDateTime()));
        }
        setCourseAndSpeed(response, movement);
        movement.setComChannelType(MovementComChannelType.FLUX);
        movement.setSource(MovementSourceType.OTHER);
        return movement;
    }

    private static void setFlagState(MovementBaseType movement, VesselCountryType registrationVesselCountry) {
        if (registrationVesselCountry != null && registrationVesselCountry.getID() != null) {
            movement.setFlagState(registrationVesselCountry.getID().getValue());
        } else {
            movement.setFlagState(null);
            log.error("[ERROR] Couldn't set FlagState, VesselTransportMeansType.getRegistrationVesselCountry.ID!");
        }
    }

    private static void setCourseAndSpeed(VesselPositionEventType response, MovementBaseType movement) {
        final MeasureType courseValueMeasure = response.getCourseValueMeasure();
        if (courseValueMeasure != null) {
            if (courseValueMeasure.getValue() != null) {
                movement.setReportedCourse(courseValueMeasure.getValue().doubleValue());
            }
        }
        final MeasureType speedValueMeasure = response.getSpeedValueMeasure();
        if (speedValueMeasure != null) {
            if (speedValueMeasure.getValue() != null) {
                movement.setReportedSpeed(speedValueMeasure.getValue().doubleValue());
            }
        }
    }

    private static MovementTypeType mapToMovementTypeFromPositionType(MovementBaseType movement, CodeType vessPosTypeCode) {
        MovementTypeType movType;
        if (vessPosTypeCode != null) {
            switch (vessPosTypeCode.getValue()) {
                case MOVEMENTTYPE_POS:
                    movType = MovementTypeType.POS;
                    break;
                case MOVEMENTTYPE_EXI:
                    movType = MovementTypeType.EXI;
                    break;
                case MOVEMENTTYPE_ENT:
                    movType = MovementTypeType.ENT;
                    break;
                case MOVEMENTTYPE_MAN:
                    movType = MovementTypeType.MAN;
                    break;
                default:
                    movType = null;
                    log.error("[ERROR] Movement type couldn't be mapped", vessPosTypeCode.getValue());
            }
        } else {
            movType = MovementTypeType.POS;
            log.error("[ERROR] Couldn't map to movementType, vessPosTypeCode was null!");
        }
        return movType;
    }

    private static MovementPoint mapToMovementPoint(VesselGeographicalCoordinateType coordinate) {
        MovementPoint point = new MovementPoint();
        if (coordinate != null) {
            final MeasureType latitudeMeasure = coordinate.getLatitudeMeasure();
            if (latitudeMeasure != null) {
                if (latitudeMeasure.getValue() != null) {
                    point.setLatitude(latitudeMeasure.getValue().doubleValue());
                }
            }
            final MeasureType longitudeMeasure = coordinate.getLongitudeMeasure();
            if (longitudeMeasure != null) {
                if (longitudeMeasure.getValue() != null) {
                    point.setLongitude(longitudeMeasure.getValue().doubleValue());
                }
            }
            final MeasureType altitudeMeasure = coordinate.getAltitudeMeasure();
            if (altitudeMeasure != null) {
                if (altitudeMeasure.getValue() != null) {
                    point.setAltitude(altitudeMeasure.getValue().doubleValue());
                }
            }
        }
        return point;
    }

    private static HashMap<String, String> extractAssetIds(List<IDType> vesselIds) {
        HashMap<String, String> ids = new HashMap<>();
        if (CollectionUtils.isNotEmpty(vesselIds)) {
            for (IDType vesselId : vesselIds) {
                ids.put(vesselId.getSchemeID(), vesselId.getValue());
            }
        }
        return ids;
    }

    private static AssetId mapToAssetId(Map<String, String> vesselIds) {
        AssetId idType = new AssetId();
        List<AssetIdList> assetIdList = idType.getAssetIdList();
        if (MapUtils.isNotEmpty(vesselIds)) {
            for (Map.Entry<String, String> vesselId : vesselIds.entrySet()) {
                switch (vesselId.getKey()) {
                    case ASSET_IRCS_CODE:
                        assetIdList.add(mapToVesselId(AssetIdType.IRCS, vesselId.getValue()));
                        break;
                    case ASSET_CFR_CODE:
                        assetIdList.add(mapToVesselId(AssetIdType.CFR, vesselId.getValue()));
                        break;
                    case ASSET_UVI_CODE:
                        assetIdList.add(mapToVesselId(AssetIdType.IMO, vesselId.getValue()));
                        break;
                    default:
                        log.error("VesselId type not mapped {}", vesselId.getKey());
                }
            }
        }
        return idType;
    }

    private static AssetIdList mapToVesselId(AssetIdType assetIdType, String value) {
        AssetIdList assetId = new AssetIdList();
        assetId.setIdType(assetIdType);
        assetId.setValue(value);
        return assetId;
    }
}
