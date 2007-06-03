package org.bpelunit.framework.coverage.exceptions;

public class CoverageMeasurmentException extends Exception {

	private Throwable fOriginalException;

	public CoverageMeasurmentException(String message) {
		super(message);

		fOriginalException = null;
	}

	public CoverageMeasurmentException(String message, Throwable e) {
		super(message, e);

		fOriginalException = e;
	}

	@Override
	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.getMessage() + "\n");
		if (fOriginalException != null) {
			buffer.append("Original Exception Message: "
					+ fOriginalException.getMessage());
		}
		return buffer.toString();
	}
}
