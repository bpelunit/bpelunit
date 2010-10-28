package net.bpelunit.framework.coverage.exceptions;

@SuppressWarnings("serial")
public class ArchiveFileException extends CoverageMeasurementException {

	public ArchiveFileException(String message) {
		super(message);
	}

	public ArchiveFileException(String message, Throwable e) {
		super(message, e);
	}
}
