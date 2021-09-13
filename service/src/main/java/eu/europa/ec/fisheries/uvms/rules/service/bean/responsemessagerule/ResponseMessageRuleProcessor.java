package eu.europa.ec.fisheries.uvms.rules.service.bean.responsemessagerule;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogResponseStatusEnum;
import eu.europa.ec.fisheries.uvms.rules.dto.ResponseMessageRuleDto;
import eu.europa.ec.fisheries.uvms.rules.entity.ResponseMessageRule;

import java.util.*;
import static java.util.stream.Collectors.*;

/**
 * Processor for {@link eu.europa.ec.fisheries.uvms.rules.entity.ResponseMessageRule}s
 * A rule has three parts: (1)dataflow, (2)messagetype and (3)receiver which can be case insensitive strings or the asterisk.
 *
 * When a response message arrives it is checked against the rules:
 * <ul>
 *     <li>If there at least one rule that matches all parts of the message then the status is {@link ExchangeLogResponseStatusEnum#ALLOWED}
 *     and the message can be forwarded to the receiver
 *     </li>
 *     <li>If no rules match then the status is {@link ExchangeLogResponseStatusEnum#BLOCKED} and the message is not forwarded to the receiver</li>
 * </ul>
 */
public class ResponseMessageRuleProcessor {

    private static final ResponseMessageRuleProcessor INSTANCE = new ResponseMessageRuleProcessor();
    private static final String ASTERISK = "*";
    private ResponseMessageRuleProcessor(){}

    private List<ResponseMessageRuleDto> rules;

    static ResponseMessageRuleProcessor getInstance() {
        return INSTANCE;
    }

    void setRules(List<ResponseMessageRule> rules) {
        this.rules = processRules(rules);
    }


    public ExchangeLogResponseStatusEnum process(final ResponseMessageRuleDto dto) {
        final String testDataFlow = dto.getDataFlow();
        final String testMessageType = dto.getMessageType();
        final String testReceiver = dto.getReceiver();

        for (ResponseMessageRuleDto rule : rules) {
            if (!matches(rule.getDataFlow(), testDataFlow))  continue;

            if (!matches(rule.getMessageType(), testMessageType)) continue;

            if (!matches(rule.getReceiver(), testReceiver)) continue;

            //If we are here, all parts of the rule have matched
            return  ExchangeLogResponseStatusEnum.BLOCKED;
        }

        return ExchangeLogResponseStatusEnum.ALLOWED;
    }


    /**
     * Transform the {@link ResponseMessageRule} to {@link ResponseMessageRuleDto}
     * @param rules
     * @return
     */
    private List<ResponseMessageRuleDto> processRules(List<ResponseMessageRule> rules) {
        return rules.stream().map(ResponseMessageRuleDto::new)
                .collect(collectingAndThen(toList(),Collections::unmodifiableList));
    }

    private boolean matches(String rule, String expression) {
        return rule.trim().equalsIgnoreCase(expression.trim()) || rule.trim().equals(ASTERISK) ;
    }

}
