package net.bpelunit.toolsupport.util.schema;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception {

	public InvalidInputException(String message) {
		super(message);
	}

	public InvalidInputException(Throwable e) {
		super(e);
	}

}
