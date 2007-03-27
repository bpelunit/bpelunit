package coverage.exception;


/**
 * 
 * @author Alex Salnikow
 */
public class BpelException extends CoverageMeasurmentException {

	public static final String NO_VALIDE_BPEL = "No valide BPEL";

	public static final String MISSING_REQUIRED_ACTIVITY = "Missing required activity";

	public static final String MISSING_REQUIRED_ELEMENT = "Missing required element";

	public BpelException(String message, Throwable e) {
		super(message,e);
	}
	
	public BpelException(String message) {
		super(message);
	}
}
