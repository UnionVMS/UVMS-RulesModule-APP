package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.CustomRuleDto;

import java.util.ArrayList;
import java.util.List;

public class CustomRuleParser {
    public static List<CustomRuleDto> parseRules(List<CustomRuleType> rawRules) {
        List<CustomRuleDto> rules = new ArrayList<>();

        for (CustomRuleType rawRule : rawRules) {
            CustomRuleDto rulesDto = new CustomRuleDto();

            rulesDto.setRuleName(rawRule.getName());
            rulesDto.setRuleGuid(rawRule.getGuid());

            List<CustomRuleSegmentType> segments = rawRule.getDefinitions();
            StringBuilder sb = new StringBuilder();

            for (CustomRuleSegmentType segment : segments) {
                sb.append(segment.getStartOperator());

                // All subcriteria
                if (segment.getSubCriteria() != null) {
                    switch (segment.getSubCriteria()) {
                        case ASSET_GROUP:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("assetGroups");
                            break;

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
                        case AREA_TYPE:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("areaTypes");
                            break;
                        case AREA_MOVEMENT_TYPE:
                            // If list and NE
                            if (segment.getCondition().equals(ConditionType.NE)) {
                                sb.append("!");
                            }
                            sb.append("areaMovementType");
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
                        case VESSEL_STATUS:
                            sb.append("vesselStatus");
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
                        case MT_STATUS:
                            sb.append("mobileTerminalStatus");
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
//                        case VICINITY_OF:
//                            sb.append("vicinityOf");
//                            break;
                        case CLOSEST_COUNTRY_CODE:
                            sb.append("closestCountryCode");
                            break;
                        case CLOSEST_PORT_CODE:
                            sb.append("closestPortCode");
                            break;

                        // REPORT
                        case SUM_POSITION_REPORT:
                            sb.append("sumPositionReport");
                            break;
                        case TIME_DIFF_POSITION_REPORT:
                            sb.append("timeDiffPositionReport");
                            break;

                        default:
                            break;
                    }
                }
                switch (segment.getCondition()) {
                    case EQ:
                        // Different EQ if a list
                        if (isListCriteria(segment.getSubCriteria())) {
                            sb.append(".contains(");
                        } else {
                            sb.append(" == ");
                        }
                        break;
                    case NE:
                        // Different NE if a list
                        if (isListCriteria(segment.getSubCriteria())) {
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
                // Remove quotations (event though there shouldn't be any) from the value, since it totally messes up the rule engine
                String value = segment.getValue().replace("\"","");
                if (segment.getSubCriteria().equals(SubCriteriaType.POSITION_REPORT_TIME)) {
                    sb.append("RulesUtil.stringToDate(\"");
                    sb.append(value);
                    sb.append("\")");
                } else  {
                    sb.append("\"");
                    sb.append(value);
                    sb.append("\"");
                }

                // If list, end "contains" with parenthesis
                if ((segment.getCriteria().equals(CriteriaType.AREA) || segment.getCriteria().equals(CriteriaType.ASSET_GROUP))
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

            // Format: "ACTION,VALUE;ACTION,VALUE;ACTION,VALUE;"
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

    private static boolean isListCriteria(SubCriteriaType subcriteria) {
        return SubCriteriaType.AREA_CODE.equals(subcriteria) ||
                SubCriteriaType.AREA_TYPE.equals(subcriteria) ||
                SubCriteriaType.AREA_MOVEMENT_TYPE.equals(subcriteria) ||
                SubCriteriaType.ASSET_GROUP.equals(subcriteria);
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

}
