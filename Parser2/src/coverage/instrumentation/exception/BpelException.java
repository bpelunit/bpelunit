package coverage.instrumentation.exception;

public class BpelException extends Exception {
	
	public static final String NO_VALIDE_BPEL="No valide BPEL";
	public BpelException(String message) {
		super(message);
	}
}
