package eu.europa.ec.fisheries.uvms.rules.service.exception;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
