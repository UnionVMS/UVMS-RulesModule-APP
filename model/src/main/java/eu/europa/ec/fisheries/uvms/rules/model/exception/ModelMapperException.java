package eu.europa.ec.fisheries.uvms.rules.model.exception;

public class ModelMapperException extends Exception {
    private static final long serialVersionUID = 1L;

    public ModelMapperException() {
    }

    public ModelMapperException(String message) {
        super(message);
    }

    public ModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }

}
