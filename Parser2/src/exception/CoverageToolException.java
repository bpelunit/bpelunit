package exception;

public class CoverageToolException extends Exception {

	public CoverageToolException(String message) {
		super(message);
	}

	public CoverageToolException(String message, Throwable e) {
		super(message, e);
	}
}
