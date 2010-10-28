package net.bpelunit.framework.coverage.exceptions;

import net.bpelunit.framework.coverage.exceptions.CoverageMeasurementException;

/**
 * Bpel Exception
 * @author Alex Salnikow, Ronald Becher
 */
@SuppressWarnings("serial")
public class BpelException extends CoverageMeasurementException {

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
