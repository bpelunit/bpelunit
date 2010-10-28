/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

/**
 * 
 * Thrown if no partner was found for an incoming message.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class PartnerNotFoundException extends BPELUnitException {

	private static final long serialVersionUID= 2468349014691707384L;

	public PartnerNotFoundException(String message, Throwable e) {
		super(message, e);
	}

	public PartnerNotFoundException(String message) {
		super(message);
	}

}
