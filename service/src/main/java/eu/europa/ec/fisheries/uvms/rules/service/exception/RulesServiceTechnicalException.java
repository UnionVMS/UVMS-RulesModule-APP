package eu.europa.ec.fisheries.uvms.rules.service.exception;

/**
 * Exception that expresses that something went wrong on a pure technical side.
 * This exception should not be used for functional errors.
 *
 * Since this is a RuntimeException, a transaction rollback will occur.
 * This means, if applicable to the situation:
 *  - database changes will be rollbacked
 *  - the JMS will try to redeliver the message, a new attempt to process will occur.
 */
public class RulesServiceTechnicalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RulesServiceTechnicalException() {
    }

    public RulesServiceTechnicalException(String message) {
        super(message);
    }

    public RulesServiceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

}