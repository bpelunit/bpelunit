package net.bpelunit.toolsupport.util.schema;

@SuppressWarnings("serial")
public class NoSuchOperationException extends Exception {

	public NoSuchOperationException(String message) {
		super(message);
	}

	public NoSuchOperationException(Throwable cause) {
		super(cause);
	}

}
