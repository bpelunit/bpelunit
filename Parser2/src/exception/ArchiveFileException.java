package exception;

public class ArchiveFileException extends Exception {
	public ArchiveFileException(String message) {
		super(message);
	}

	public ArchiveFileException(String message, Throwable e) {
		super(message, e);
	}
}
