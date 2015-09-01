package eu.europa.ec.fisheries.uvms.rules.model.exception;

public class ModelMarshallException extends ModelMapperException {
    private static final long serialVersionUID = 1L;

    public ModelMarshallException(String message) {
        super(message);
    }

    public ModelMarshallException(String message, Throwable cause) {
        super(message, cause);
    }

}
