package eu.europa.ec.fisheries.uvms.rules.message.exception;

public class MessageException extends Exception {
    private static final long serialVersionUID = 1L;

    public MessageException() {
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
