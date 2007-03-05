package exception;

/**
 * 
 * @author Alex Salnikow
 */
public class BpelException extends Exception {

	public static final String NO_VALIDE_BPEL = "No valide BPEL";

	public static final String MISSING_REQUIRED_ACTIVITY = "Missing required activity";

	public BpelException(String message) {
		super(message);
	}
}
