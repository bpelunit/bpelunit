package org.bpelunit.framework.coverage.exceptions;

@SuppressWarnings("serial")
public class CoverageMeasurementException extends Exception {

	private Throwable fOriginalException;

	public CoverageMeasurementException(String message) {
		super(message);

		fOriginalException = null;
	}

	public CoverageMeasurementException(String message, Throwable e) {
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
