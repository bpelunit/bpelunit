/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.exception;

import javax.xml.soap.SOAPException;

/**
 * A problem while processing SOAP headers.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class HeaderProcessingException extends BPELUnitException {

	private static final long serialVersionUID= -4639185955223357969L;

	public HeaderProcessingException(String message) {
		super(message);
	}

	public HeaderProcessingException(String string, SOAPException e) {
		super(string, e);
	}

}
