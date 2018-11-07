package eu.europa.ec.fisheries.uvms.rules.service.exception;

public class MdrLoadingException extends Exception {

    private static final long serialVersionUID = 1L;

    public MdrLoadingException() {
    }

    public MdrLoadingException(String message) {
        super(message);
    }

    public MdrLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

}
