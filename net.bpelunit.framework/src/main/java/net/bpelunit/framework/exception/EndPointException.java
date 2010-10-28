/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package net.bpelunit.framework.exception;

public class EndPointException extends BPELUnitException {

	private static final long serialVersionUID = -3914618981294748737L;

	public EndPointException(String message) {
		super(message);
	}

	public EndPointException(String string, Exception e) {
		super(string, e);
	}

}
