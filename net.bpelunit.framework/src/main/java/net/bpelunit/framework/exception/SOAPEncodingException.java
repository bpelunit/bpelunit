/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

import javax.xml.soap.SOAPException;

/**
 * An exception while constructing or deconstructing a SOAP Message.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SOAPEncodingException extends BPELUnitException {

	private static final long serialVersionUID= 3619498013207248294L;

	public SOAPEncodingException(String message) {
		super(message);
	}

	public SOAPEncodingException(String message, SOAPException e) {
		super(message, e);
	}

}
