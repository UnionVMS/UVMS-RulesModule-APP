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

            List<CustomRuleSegmentType> segments = rawRule.getDefinitions();
            StringBuilder sb = new StringBuilder();

            for (CustomRuleSegmentType segment : segments) {
                sb.append(segment.getStartOperator());

                // All criteria without subcriteria
                switch (segment.getCriteria()) {
//                case VESSEL:
//                    sb.append("vessel");
//                    break;
//                case MOBILE_TERMINAL:
//                    sb.append("mobileTerminal");
//                    break;
                case AREA:
                    // If list and NE
                    if (segment.getCondition().equals(ConditionType.NE)) {
                        sb.append("!");
                    }
//                    sb.append("area");
                    break;
                case EXTERNAL_MARKING:
                    sb.append("externalMarking");
                    break;
                case FLAG_STATE:
                    sb.append("flagState");
                    break;
                case POSITION_REPORT_TIME:
                    sb.append("positionTime");
                    break;
                case STATUS_CODE:
                    sb.append("statusCode");
                    break;
                case CALCULATED_SPEED:
                    sb.append("calculatedSpeed");
                    break;
//                case ACTIVITY:
//                    sb.append("activity");
//                    break;
                case ALTITUDE:
                    sb.append("altitude");
                    break;
//                case ASSET_ID:
//                    sb.append("assetId");
//                    break;
                case CALCULATED_COURSE:
                    sb.append("calculatedCourse");
                    break;
//                case CLOSEST_COUNTRY:
//                    sb.append("closestCountry");
//                    break;
//                case CLOSEST_PORT:
//                    sb.append("closestPort");
//                    break;
                case COMCHANNEL_TYPE:
                    sb.append("comChannelType");
                    break;
                case CONNECT_ID:
                    sb.append("connectId");
                    break;
                case LATITUDE:
                    sb.append("latitude");
                    break;
                case LONGITUDE:
                    sb.append("longitude");
                    break;
                case MOVEMENT_GUID:
                    sb.append("movementGuid");
                    break;
                case MOVEMENT_TYPE:
                    sb.append("movementType");
                    break;
                case REPORTED_COURSE:
                    sb.append("reportedCourse");
                    break;
                case REPORTED_SPEED:
                    sb.append("reportedSpeed");
                    break;
                case SEGMENT_TYPE:
                    sb.append("fromSegmentType");
                    break;
                case SOURCE:
                    sb.append("source");
                    break;
                case WKT:
                    sb.append("wkt");
                    break;

                case VECINITY_OF:
                    break;
                case ASSET_GROUP:
                    break;
                default:
                    break;
                }
                // All subcriteria
                switch (segment.getSubCriteria()) {
                case VESSEL_CFR:
                    sb.append("vesselCfr");
                    break;
                case VESSEL_IRCS:
                    sb.append("vesselIrcs");
                    break;
                case VESSEL_NAME:
                    sb.append("vesselName");
                    break;
                case MT_MEMBER_ID:
                    sb.append("mobileTerminalMemberNumber");
                    break;
                case MT_SERIAL_NO:
                    sb.append("mobileTerminalSerialNumber");
                    break;
                case MT_DNID:
                    sb.append("mobileTerminalDnid");
                    break;
                case AREA_TYPE:
                    sb.append("areaTypes");
                    break;
                case AREA_CODE:
                    sb.append("areaCodes");
                    break;
                case AREA_ID:
                    sb.append("areaRemoteIds");
                    break;
                case ACTIVITY_CALLBACK:
                    sb.append("activityCallback");
                    break;
                case ACTIVITY_MESSAGE_ID:
                    sb.append("activityMessageId");
                    break;
                case ACTIVITY_MESSAGE_TYPE:
                    sb.append("activityMessageType");
                    break;
                case ASSET_ID_ASSET_TYPE:
                    sb.append("assetIdAssetType");
                    break;
                case ASSET_ID_TYPE:
                    sb.append("assetIdType");
                    break;
                case ASSET_ID_VALUE:
                    sb.append("assetIdValue");
                    break;
                case COUNTRY_CODE:
                    sb.append("closestCountryCode");
                    break;
                case COUNTRY_DISTANCE:
                    sb.append("closestCountryDistance");
                    break;
                case COUNTRY_REMOTE_ID:
                    sb.append("closestCountryRemoteId");
                    break;
                case PORT_CODE:
                    sb.append("closestPortCode");
                    break;
                case PORT_DISTANCE:
                    sb.append("closestPortDistance");
                    break;
                case PORT_REMOTE_ID:
                    sb.append("closestPortRemoteId");
                    break;
                default:
                    break;
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

    public static MovementFact mapMovementFact(MovementType movement, String externalMarking, String flagState, String mobileTerminalDnid,
                                               String mobileTerminalMemberNumber, String mobileTerminalSerialNumber, String vesselName, String vesselGuid) {
        MovementFact fact = new MovementFact();

        // Base
        fact.setCalculatedCourse(movement.getCalculatedCourse());
        fact.setCalculatedSpeed(movement.getCalculatedSpeed());
        if (movement.getComChannelType() != null) {
            fact.setComChannelType(movement.getComChannelType().name());
        }
        fact.setConnectId(movement.getConnectId());
        fact.setExternalMarking(externalMarking);
        fact.setFlagState(flagState);
        fact.setMobileTerminalDnid(mobileTerminalDnid);
        fact.setMobileTerminalMemberNumber(mobileTerminalMemberNumber);
        fact.setMobileTerminalSerialNumber(mobileTerminalSerialNumber);
        fact.setMovementGuid(movement.getGuid());
        if (movement.getMovementType() != null) {
            fact.setMovementType(movement.getMovementType().name());
        }
        if (movement.getPositionTime() != null) {
            fact.setPositionTime(movement.getPositionTime().toGregorianCalendar().getTime());
        }
        fact.setReportedCourse(movement.getReportedCourse());
        fact.setReportedSpeed(movement.getReportedSpeed());
        if (movement.getSource() != null) {
            fact.setSource(movement.getSource().name());
        }
        fact.setStatusCode(movement.getStatus());
        fact.setVesselGuid(vesselGuid);
        fact.setVesselName(vesselName);
        fact.setWkt(movement.getWkt());

        // Activity
        if (movement.getActivity() != null) {
            fact.setActivityCallback(movement.getActivity().getCallback());
            fact.setActivityMessageId(movement.getActivity().getMessageId());
            if (movement.getActivity().getMessageType() != null) {
                fact.setActivityMessageType(movement.getActivity().getMessageType().name());
            }
        }

        // AssetId
        if (movement.getAssetId() != null) {
            fact.setAssetIdAssetType(movement.getAssetId().getAssetType().name());
            if (movement.getAssetId().getIdType() != null) {
                fact.setAssetIdType(movement.getAssetId().getIdType().name());
            }
            fact.setAssetIdValue(movement.getAssetId().getValue());
            if (movement.getAssetId().getAssetType() == AssetType.VESSEL && movement.getAssetId().getIdType() == AssetIdType.CFR) {
                fact.setVesselCfr(movement.getAssetId().getValue());
            }
            if (movement.getAssetId().getAssetType() == AssetType.VESSEL && movement.getAssetId().getIdType() == AssetIdType.IRCS) {
                fact.setVesselIrcs(movement.getAssetId().getValue());
            }
        }

        // Position
        if (movement.getPosition() != null) {
            fact.setAltitude(movement.getPosition().getAltitude());
            fact.setLatitude(movement.getPosition().getLatitude());
            fact.setLongitude(movement.getPosition().getLongitude());
        }

        // Meta data
        if (movement.getMetaData() != null) {
            // Meta data base
            // fact.setPreviousMovementId(movement.getMetaData().getPreviousMovementId());
            if (movement.getMetaData().getFromSegmentType() != null) {
                fact.setFromSegmentType(movement.getMetaData().getFromSegmentType().name());
            }

            // Areas
            List<MovementMetaDataAreaType> areas = movement.getMetaData().getAreas();
            for (MovementMetaDataAreaType area : areas) {
                fact.getAreaCodes().add(area.getCode());
                fact.getAreaRemoteIds().add(area.getRemoteId());
                fact.getAreaTypes().add(area.getAreaType());
            }

            // Country
            if (movement.getMetaData().getClosestCountry() != null) {
                fact.setClosestCountryCode(movement.getMetaData().getClosestCountry().getCode());
                fact.setClosestCountryDistance(movement.getMetaData().getClosestCountry().getDistance());
                fact.setClosestCountryRemoteId(movement.getMetaData().getClosestCountry().getRemoteId());
            }

            // Port
            if (movement.getMetaData().getClosestPort() != null) {
                fact.setClosestPortCode(movement.getMetaData().getClosestPort().getCode());
                fact.setClosestPortDistance(movement.getMetaData().getClosestPort().getDistance());
                fact.setClosestPortRemoteId(movement.getMetaData().getClosestPort().getRemoteId());
            }

        }

        return fact;
    }

    public static RawMovementFact mapRawMovementFact(RawMovementType rawMovement, MobileTerminalType mobileTerminal, String pluginType) {
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

        // AssetId
//        // TODO: Fix better test data!!!
//        List<AssetIdList> assetIdList = rawMovement.getAssetId().getAssetIdList();
//        for (AssetIdList idList : assetIdList) {
//            fact.setAssetIdAssetType(rawMovement.getAssetId().getAssetType().name());
//            if (idList.getIdType().equals(AssetIdType.CFR) && rawMovement.getAssetId().getAssetType().equals(AssetType.VESSEL) ) {
//                fact.setVesselCfr(idList.getValue());
//            }
//            if (idList.getIdType().equals(AssetIdType.IRCS) && rawMovement.getAssetId().getAssetType().equals(AssetType.VESSEL) ) {
//                fact.setVesselIrcs(idList.getValue());
//            }
//            // TODO: If we want more, add the valid combinations here
//        }


        // Mobile Terminal
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
            fact.setConnectId(mobileTerminal.getConnectId());
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
