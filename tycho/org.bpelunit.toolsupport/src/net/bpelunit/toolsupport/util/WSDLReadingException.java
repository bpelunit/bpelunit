/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.util;

/**
 * 
 * This exception is thrown when a WSDL read error occurs in the UI.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class WSDLReadingException extends Exception {

	private static final long serialVersionUID= -6318176656279235135L;

	public WSDLReadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public WSDLReadingException(String message) {
		super(message);
	}

}
