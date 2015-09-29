package eu.europa.ec.fisheries.uvms.rules.service.exception;

public class RulesServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    public RulesServiceException() {
    }

    public RulesServiceException(String message) {
        super(message);
    }

    public RulesServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
