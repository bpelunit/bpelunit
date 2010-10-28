/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

/**
 * A problem while sending out a synchronous SOAP message, or while receiving the answer.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SynchronousSendException extends BPELUnitException {

	private static final long serialVersionUID= 5974034006907866587L;

	private Exception fException;

	public SynchronousSendException(Exception e) {
		this("Synchronous Sending Fault", e);
	}

	public SynchronousSendException(String message, Exception e) {
		super(message);
		fException= e;
	}

	@Override
	public String getMessage() {
		return fException.getMessage();
	}
}
