package exception;

public class ArchiveFileException extends CoverageMeasurmentException {
	public ArchiveFileException(String message) {
		super(message);
	}

	public ArchiveFileException(String message, Throwable e) {
		super(message, e);
	}
}
