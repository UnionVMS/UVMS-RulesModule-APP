package eu.europa.ec.fisheries.uvms.rules.exception;

/**
 * RuntimeException wrapper for the {@link DaoMappingException}
 */
public class DaoMappingRuntimeException extends RuntimeException {
	public DaoMappingRuntimeException(String s, DaoMappingException throwable) {
		super(s, throwable);
	}

	public DaoMappingException unwrap() {
		return (DaoMappingException) getCause();
	}
}
