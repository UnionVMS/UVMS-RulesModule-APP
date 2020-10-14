package eu.europa.ec.fisheries.uvms.rules.exception;

/**
 * RuntimeException wrapper for a {@link DaoException}.
 */
public class DaoRuntimeException extends RuntimeException {
	public DaoRuntimeException(String s, DaoException throwable) {
		super(s, throwable);
	}

	public DaoException unwrap() {
		return (DaoException) getCause();
	}
}
