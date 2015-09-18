package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleType;

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
                sb.append(segment.getCriteria());
                if (segment.getSubCriteria() != null) {
                    sb.append("_");
                    sb.append(segment.getSubCriteria());
                }

                switch (segment.getCondition()) {
                case "EQ":
                    sb.append(" == ");
                    break;
                case "NE":
                    sb.append(" != ");
                    break;
                case "GT":
                    sb.append(" > ");
                    break;
                case "GE":
                    sb.append(" >= ");
                    break;
                case "LT":
                    sb.append(" < ");
                    break;
                case "LE":
                    sb.append(" <= ");
                    break;
                default: // undefined
                    break;

                }
                sb.append("\"");
                sb.append(segment.getValue());
                sb.append("\"");
                sb.append(segment.getEndOperator());
                // TODO: Better if this is empty instead of NONE
                switch (segment.getLogicBoolOperator()) {
                case "AND":
                    sb.append(" && ");
                    break;
                case "OR":
                    sb.append(" || ");
                    break;
                case "NONE":
                    break;
                default: // undefined
                    break;
                }

            }

            rulesDto.setExpression(sb.toString());

            // Format as of now: "ACTION,VALUE;ACTION,VALUE;ACTION,VALUE;"
            List<CustomRuleActionType> actions = rawRule.getNotifications();
            sb = new StringBuilder();
            for (CustomRuleActionType action : actions) {
                sb.append(action.getType());
                sb.append(",");
                sb.append(action.getText());
                sb.append(";");
            }

            rulesDto.setAction(sb.toString());

            rules.add(rulesDto);
        }

        return rules;
    }

}
