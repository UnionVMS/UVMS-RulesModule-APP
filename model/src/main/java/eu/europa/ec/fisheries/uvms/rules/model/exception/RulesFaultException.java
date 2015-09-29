package eu.europa.ec.fisheries.uvms.rules.model.exception;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;

public class RulesFaultException extends RulesModelException {
    private static final long serialVersionUID = 1L;
    private RulesFault fault;

    public RulesFaultException(String message, RulesFault fault) {
        super(message);
        this.fault = fault;
    }

    public RulesFault getRulesFault() {
        return fault;
    }
}
