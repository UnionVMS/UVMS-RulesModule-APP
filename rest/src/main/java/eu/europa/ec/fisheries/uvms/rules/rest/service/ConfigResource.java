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
package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.MainCriteria;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.SubCriteria;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("/config")
@Stateless
@RequiresFeature(UnionVMSFeature.viewAlarmRules)
public class ConfigResource {

    private final static Logger LOG = LoggerFactory.getLogger(ConfigResource.class);

    /**
     *
     * @responseMessage 200 Get all config for custom rules
     * @responseMessage 500 No config fetched
     *
     * @summary Get a map of all config for populating drop downs in custom rule
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "/")
    public ResponseDto getConfig() {
        try {
            Map map = new HashMap();

            Map<String, HashMap<String, ArrayList<String>>> crit = getCriterias();
            Map act = getActions();
            LogicOperatorType[] log = getLogicOperatorType();
            AvailabilityType[] availability = getAvailability();
            MobileTerminalStatus[] mobileTerminalStatuses = getMobileTerminalStatuses();
            AssetStatus[] assetStatuses = getAssetStatuses();

            map.put("CRITERIA", crit);
            map.put("ACTIONS", act);
            map.put("LOGIC_OPERATORS", log);
            map.put("AVAILABILITY", availability);
            map.put("MOBILETERMINAL_STATUSES", mobileTerminalStatuses);
            map.put("ASSET_STATUSES", assetStatuses);

            return new ResponseDto(map, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting config. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

    private Map<String, HashMap<String, ArrayList<String>>> getCriterias() {
        Map<String, HashMap<String, ArrayList<String>>> map = new HashMap<String, HashMap<String, ArrayList<String>>>();

        MainCriteria[] mainCriterias = MainCriteria.values();
        for (int i = 0; i < mainCriterias.length; i++) {

            HashMap<String, ArrayList<String>> subResult = new HashMap<String, ArrayList<String>>();

            String mainCrit = mainCriterias[i].toString();

            SubCriteria[] subCriterias = SubCriteria.values();
            for (int j = 0; j < subCriterias.length; j++) {
                if (subCriterias[j].getMainCriteria().equals(mainCriterias[i])) {
                    getConditionsByCriteria(subCriterias[j]);
                    subResult.put(subCriterias[j].toString(), getConditionsByCriteria(subCriterias[j]));
                }
                if (!mainCriterias[i].equals(MainCriteria.ROOT)) {
                    map.put(mainCrit, subResult);
                }
            }
        }
        return map;
    }

    private LogicOperatorType[] getLogicOperatorType() {
        return LogicOperatorType.values();
    }

    private AvailabilityType[] getAvailability() {
        return AvailabilityType.values();
    }

    private MobileTerminalStatus[] getMobileTerminalStatuses() {
        return MobileTerminalStatus.values();
    }

    private AssetStatus[] getAssetStatuses() {
        return AssetStatus.values();
    }


    private Map getActions() {
        Map map = new HashMap();
        ActionType[] actionTypes = ActionType.values();
        for (int i = 0; i < actionTypes.length; i++) {

            boolean needValue = false;
            ActionType actionType = actionTypes[i];
            switch (actionType) {
                case SEND_TO_FLUX:
                    needValue = true;
                    break;
                case SEND_TO_NAF:
                    needValue = true;
                    break;
                case EMAIL:
                    needValue = true;
                    break;
//                case TICKET:
//                    needValue = false;
//                    break;
//                case MANUAL_POLL:
//                    break;
//                case ON_HOLD:
//                    break;
//                case TOP_BAR_NOTIFICATION:
//                    break;
//                case SMS:
//                    needValue = true;
//                    break;
            }
            map.put(actionType, needValue);
        }
        return map;
    }

    private ArrayList<String> getConditionsByCriteria(SubCriteria subCriteria) {
        ArrayList<String> conditions = new ArrayList<>();
        switch (subCriteria) {
            case ACTIVITY_CALLBACK:
            case ACTIVITY_MESSAGE_ID:
            case ACTIVITY_MESSAGE_TYPE:
            case AREA_CODE:
            case AREA_TYPE:
            case ASSET_ID_GEAR_TYPE:
            case EXTERNAL_MARKING:
            case ASSET_NAME:
            case COMCHANNEL_TYPE:
            case MT_TYPE:
            case FLAG_STATE:
            case MOVEMENT_TYPE:
            case SEGMENT_TYPE:
            case SOURCE:
            case CLOSEST_COUNTRY_CODE:
            case CLOSEST_PORT_CODE:
            case ASSET_GROUP:
            case ASSET_STATUS:
            case MT_STATUS:
            case AREA_CODE_ENT:
            case AREA_TYPE_ENT:
            case AREA_CODE_EXT:
            case AREA_TYPE_EXT:
            case VICINITY_OF:
                conditions.add(ConditionType.EQ.name());
                conditions.add(ConditionType.NE.name());
                break;

            case ASSET_CFR:
            case ASSET_IRCS:
            case MT_DNID:
            case MT_MEMBER_ID:
            case MT_SERIAL_NO:
            case ALTITUDE:
            case LATITUDE:
            case LONGITUDE:
            case POSITION_REPORT_TIME:
            case STATUS_CODE:
            case REPORTED_COURSE:
            case REPORTED_SPEED:
            case CALCULATED_COURSE:
            case CALCULATED_SPEED:
            case TIME_DIFF_POSITION_REPORT:
            case SUM_POSITION_REPORT:
            default:
                conditions.add(ConditionType.EQ.name());
                conditions.add(ConditionType.NE.name());
                conditions.add(ConditionType.LT.name());
                conditions.add(ConditionType.GT.name());
                conditions.add(ConditionType.LE.name());
                conditions.add(ConditionType.GE.name());
                break;
        }

        return conditions;
    }

    /**
     *
     * @responseMessage 200 Alarm statuses fetched
     * @responseMessage 500 No config fetched
     *
     * @summary Get alarm statuses
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "/alarmstatus")
    public ResponseDto getAlarmStatuses() {
        try {
            return new ResponseDto(AlarmStatusType.values(), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting alarm statuses. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 Ticket statuses fetched
     * @responseMessage 500 No config fetched
     *
     * @summary Get ticket statuses
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "/ticketstatus")
    public ResponseDto getTicketStatuses() {
        try {
            return new ResponseDto(TicketStatusType.values(), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting ticket statuses. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

}