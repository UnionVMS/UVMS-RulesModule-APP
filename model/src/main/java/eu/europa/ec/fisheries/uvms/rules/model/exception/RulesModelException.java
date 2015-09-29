package eu.europa.ec.fisheries.uvms.rules.model.exception;

public class RulesModelException extends Exception {
    private static final long serialVersionUID = 1L;

    public RulesModelException() {
    }

    public RulesModelException(String message) {
        super(message);
    }

    public RulesModelException(String message, Throwable cause) {
        super(message, cause);
    }

}
