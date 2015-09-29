package eu.europa.ec.fisheries.uvms.rules.model.exception;

public class RulesModelMarshallException extends RulesModelMapperException {
    private static final long serialVersionUID = 1L;

    public RulesModelMarshallException(String message) {
        super(message);
    }

    public RulesModelMarshallException(String message, Throwable cause) {
        super(message, cause);
    }

}
