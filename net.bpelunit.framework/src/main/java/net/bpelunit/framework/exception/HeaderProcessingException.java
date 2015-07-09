/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

/**
 * A problem while processing SOAP headers.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class HeaderProcessingException extends BPELUnitException {

	private static final long serialVersionUID = -4639185955223357969L;

	public HeaderProcessingException(String message) {
		super(message);
	}

	public HeaderProcessingException(String string, Exception e) {
		super(string, e);
	}

}
