package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselIdType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementMetaDataAreaType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ConditionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CriteriaType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;

public class RulesUtil {
    final static Logger LOG = LoggerFactory.getLogger(RulesUtil.class);

    public static List<CustomRuleDto> parseRules(List<CustomRuleType> rawRules) {
        List<CustomRuleDto> rules = new ArrayList<CustomRuleDto>();

        for (CustomRuleType rawRule : rawRules) {
            CustomRuleDto rulesDto = new CustomRuleDto();

            rulesDto.setRuleName(rawRule.getName());
            rulesDto.setRuleGuid(rawRule.getGuid());

            List<CustomRuleSegmentType> segments = rawRule.getDefinitions();
            StringBuilder sb = new StringBuilder();

            for (CustomRuleSegmentType segment : segments) {
                sb.append(segment.getStartOperator());

                // Criteria
                if (segment.getCriteria() != null) {
                    switch (segment.getCriteria()) {
                        case AREA:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            break;
                        case ASSET_GROUP:
                            // TODO: Implement
                            break;
                        default:
                            break;
                    }
                }
                // All subcriteria
                if (segment.getSubCriteria() != null) {
                    switch (segment.getSubCriteria()) {
                        // ACTIVITY
                        case ACTIVITY_CALLBACK:
                            sb.append("activityCallback");
                            break;
                        case ACTIVITY_MESSAGE_ID:
                            sb.append("activityMessageId");
                            break;
                        case ACTIVITY_MESSAGE_TYPE:
                            sb.append("activityMessageType");
                            break;

                        // AREA
                        case AREA_CODE:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("areaCodes");
                            break;
                        case AREA_NAME:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("areaNames");
                            break;
                        case AREA_TYPE:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("areaTypes");
                            break;
                        case AREA_ID:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("areaRemoteIds");
                            break;

                        // ASSET
                        case ASSET_ID_GEAR_TYPE:
                            sb.append("assetIdGearType");
                            break;
                        case EXTERNAL_MARKING:
                            sb.append("externalMarking");
                            break;
                        case FLAG_STATE:
                            sb.append("flagState");
                            break;
                        case VESSEL_CFR:
                            sb.append("vesselCfr");
                            break;
                        case VESSEL_IRCS:
                            sb.append("vesselIrcs");
                            break;
                        case VESSEL_NAME:
                            sb.append("vesselName");
                            break;

                        // MOBILE_TERMINAL
                        case COMCHANNEL_TYPE:
                            sb.append("comChannelType");
                            break;
                        case MT_TYPE:
                            sb.append("mobileTerminalType");
                            break;
                        case MT_DNID:
                            sb.append("mobileTerminalDnid");
                            break;
                        case MT_MEMBER_ID:
                            sb.append("mobileTerminalMemberNumber");
                            break;
                        case MT_SERIAL_NO:
                            sb.append("mobileTerminalSerialNumber");
                            break;

                        // POSITION
                        case ALTITUDE:
                            sb.append("altitude");
                            break;
                        case LATITUDE:
                            sb.append("latitude");
                            break;
                        case LONGITUDE:
                            sb.append("longitude");
                            break;
                        case CALCULATED_COURSE:
                            sb.append("calculatedCourse");
                            break;
                        case CALCULATED_SPEED:
                            sb.append("calculatedSpeed");
                            break;
                        case MOVEMENT_TYPE:
                            sb.append("movementType");
                            break;
                        case POSITION_REPORT_TIME:
                            sb.append("positionTime");
                            break;
                        case REPORTED_COURSE:
                            sb.append("reportedCourse");
                            break;
                        case REPORTED_SPEED:
                            sb.append("reportedSpeed");
                            break;
                        case SEGMENT_TYPE:
                            sb.append("segmentType");
                            break;
                        case SOURCE:
                            sb.append("source");
                            break;
                        case STATUS_CODE:
                            sb.append("statusCode");
                            break;
                        case VICINITY_OF:
                            // TODO: Implement
                            break;
                        case CLOSEST_COUNTRY_CODE:
                            sb.append("closestCountryCode");
                            break;
                        case CLOSEST_PORT_CODE:
                            sb.append("closestPortCode");
                            break;
                        default:
                            break;
                    }
                }
                switch (segment.getCondition()) {
                case EQ:
                    // Different EQ if a list
                    if (segment.getCriteria().equals(CriteriaType.AREA)) {
                        sb.append(".contains(");
                    } else {
                        sb.append(" == ");
                    }
                    break;
                case NE:
                    // Different NE if a list
                    if (segment.getCriteria().equals(CriteriaType.AREA)) {
                        sb.append(".contains(");
                    } else {
                        sb.append(" != ");
                    }
                    break;
                case GT:
                    sb.append(" > ");
                    break;
                case GE:
                    sb.append(" >= ");
                    break;
                case LT:
                    sb.append(" < ");
                    break;
                case LE:
                    sb.append(" <= ");
                    break;
                default: // undefined
                    break;

                }
                sb.append("\"");
                sb.append(segment.getValue());
                sb.append("\"");

                // If list, end "contains" with parenthesis
                if (segment.getCriteria().equals(CriteriaType.AREA)
                        && (segment.getCondition().equals(ConditionType.EQ) || segment.getCondition().equals(ConditionType.NE))) {
                    sb.append(")");
                }

                sb.append(segment.getEndOperator());
                switch (segment.getLogicBoolOperator()) {
                case AND:
                    sb.append(" && ");
                    break;
                case OR:
                    sb.append(" || ");
                    break;
                case NONE:
                    break;
                default: // undefined
                    break;
                }
            }

            // Add time intervals
            List<CustomRuleIntervalType> intervals = rawRule.getTimeIntervals();

            for (int i = 0; i < intervals.size(); i++) {
                // If first
                if (i == 0) {
                    sb.append(" && (");
                }

                sb.append(createInterval(intervals.get(i)));

                // If last
                if (i == intervals.size() - 1) {
                    sb.append(")");
                } else {
                    sb.append(" || ");
                }
            }

            rulesDto.setExpression(sb.toString());

            // Format as of now: "ACTION,VALUE;ACTION,VALUE;ACTION,VALUE;"
            List<CustomRuleActionType> actions = rawRule.getActions();
            sb = new StringBuilder();
            for (CustomRuleActionType action : actions) {
                sb.append(action.getAction());
                sb.append(",");
                sb.append(action.getValue());
                sb.append(";");
            }

            rulesDto.setAction(sb.toString());

            rules.add(rulesDto);
        }

        return rules;
    }

    private static String createInterval(CustomRuleIntervalType interval) {
        StringBuilder sb = new StringBuilder();
        if (interval.getStart() != null) {
            sb.append("RulesUtil.stringToDate(\"");
            sb.append(interval.getStart());
            sb.append("\")");
            sb.append(" <= positionTime");
        }

        if (interval.getStart() != null && interval.getEnd() != null) {
            sb.append(" && ");
        }

        if (interval.getEnd() != null) {
            sb.append("positionTime <= ");
            sb.append("RulesUtil.stringToDate(\"");
            sb.append(interval.getEnd());
            sb.append("\")");
        }
        return sb.toString();
    }

    public static MovementFact mapMovementFact(MovementType movement, MobileTerminalType mobileTerminal, Vessel vessel, String comChannelType) {
        MovementFact fact = new MovementFact();

        fact.setMovementMovement(movement);
        fact.setMovementGuid(movement.getGuid());

        // ROOT
        // TODO
//        fact.setAssetGroup(assetGroup);

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
                fact.getAreaCodes().add(area.getCode());
                fact.getAreaNames().add(area.getName());
                fact.getAreaTypes().add(area.getAreaType());
                fact.getAreaRemoteIds().add(area.getRemoteId());
            }
        }

        // ASSET
        if (vessel != null) {
            fact.setAssetIdGearType(vessel.getGearType());
            fact.setExternalMarking(vessel.getExternalMarking());
            fact.setFlagState(vessel.getCountryCode());
            fact.setVesselCfr(vessel.getCfr());
            fact.setVesselIrcs(vessel.getIrcs());
            fact.setVesselName(vessel.getName());
            fact.setVesselGuid(vessel.getVesselId().getGuid());
        }

        // MOBILE_TERMINAL
        fact.setComChannelType(comChannelType);
        if (mobileTerminal != null) {
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
            fact.setPositionTime(movement.getPositionTime().toGregorianCalendar().getTime());
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
//        fact.setVicinityOf(vicinityOf);


        return fact;
    }

    public static RawMovementFact mapRawMovementFact(RawMovementType rawMovement, MobileTerminalType mobileTerminal, Vessel vessel, String pluginType) {
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
            fact.setPositionTime(rawMovement.getPositionTime().toGregorianCalendar().getTime());
        }
        fact.setReportedCourse(rawMovement.getReportedCourse());
        fact.setReportedSpeed(rawMovement.getReportedSpeed());
        if (rawMovement.getSource() != null) {
            fact.setSource(rawMovement.getSource().name());
        }
        fact.setStatusCode(rawMovement.getStatus());

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

        if (vessel != null) {
            fact.setVesselGuid(vessel.getVesselId().getGuid());
            fact.setVesselCfr(vessel.getCfr());
            fact.setVesselIrcs(vessel.getIrcs());
        }

        // From Mobile Terminal
        if (mobileTerminal != null) {
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
            fact.setMobileTerminalConnectId(mobileTerminal.getConnectId());
            fact.setMobileTerminalType(mobileTerminal.getType());
        }

        return fact;
    }

    final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    public static Date stringToDate(String dateString) throws IllegalArgumentException {
        if (dateString != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT).withOffsetParsed();
            DateTime dateTime = formatter.withZoneUTC().parseDateTime(dateString);
            GregorianCalendar cal = dateTime.toGregorianCalendar();
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static String dateToString(Date date) {
        String dateString = null;
        if (date != null) {
            DateFormat df = new SimpleDateFormat(FORMAT);
            dateString = df.format(date);
        }
        return dateString;
    }

    public static DateTime nowUTC() {
        return new DateTime(DateTimeZone.UTC);
    }

}
