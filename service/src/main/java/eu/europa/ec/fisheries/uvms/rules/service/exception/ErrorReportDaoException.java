package eu.europa.ec.fisheries.uvms.rules.service.exception;

public class ErrorReportDaoException extends Exception {
    private static final long serialVersionUID = 1L;

    public ErrorReportDaoException() {
    }

    public ErrorReportDaoException(String message) {
        super(message);
    }

    public ErrorReportDaoException(String message, Throwable cause) {
        super(message, cause);
    }

}
