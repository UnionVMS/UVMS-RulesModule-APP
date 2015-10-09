package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

                switch (segment.getCriteria()) {
                case VESSEL:
                    sb.append("vessel");
                    break;
                case MOBILE_TERMINAL:
                    sb.append("mobileTerminal");
                    break;
                case GEO_AREA:
                    sb.append("geoArea");
                    break;
                default:
                    break;
                }
                switch (segment.getSubCriteria()) {
                case CFR:
                    sb.append("Cfr");
                    break;
                case IRCS:
                    sb.append("Ircs");
                    break;
                case NAME:
                    sb.append("Name");
                    break;
                case MEMBER_ID:
                    sb.append("MemberNumber");
                    break;
                case SERIAL_NO:
                    sb.append("SerialNumber");
                    break;
                case DNID:
                    sb.append("Dnid");
                    break;
                case AREA_ID:
                    sb.append("AreaId");
                    break;
                default:
                    break;
                }

                switch (segment.getCondition()) {
                case EQ:
                    sb.append(" == ");
                    break;
                case NE:
                    sb.append(" != ");
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
        sb.append("RulesUtil.stringToDate(\"");
        sb.append(interval.getStart());
        sb.append("\")");
        sb.append(" <= timestamp && timestamp <= ");
        // sb.append(" <= timestamp <= "); // test
        sb.append("RulesUtil.stringToDate(\"");
        sb.append(interval.getEnd());
        sb.append("\")");
        return sb.toString();
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

}
