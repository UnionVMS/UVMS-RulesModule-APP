package eu.europa.ec.fisheries.uvms.rules.model.exception;

public class RulesModelMapperException extends Exception {
    private static final long serialVersionUID = 1L;

    public RulesModelMapperException() {
    }

    public RulesModelMapperException(String message) {
        super(message);
    }

    public RulesModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }

}
