package net.bpelunit.framework.coverage.exceptions;

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
		buffer.append(super.getMessage()).append("\n");
		if (fOriginalException != null) {
			buffer
				.append("Original Exception Message: ")
				.append(fOriginalException.getMessage());
		}
		return buffer.toString();
	}
}
