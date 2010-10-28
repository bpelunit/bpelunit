/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ext;

import javax.xml.soap.SOAPMessage;

/**
 * The send package exists solely for communication with the header processor, allowing it to
 * retrieve the sending SOAP message for extensions and changing the target URL of this send
 * activity if appropriate.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendPackage {

	private String fTargetURL;
	private SOAPMessage fSoapMessage;

	/**
	 * Creates the send package
	 * 
	 * @param targetURL current target URL
	 * @param soapMessage live copy of outgoing SOAP message
	 */
	public SendPackage(String targetURL, SOAPMessage soapMessage) {
		fTargetURL= targetURL;
		fSoapMessage= soapMessage;
	}

	/**
	 * Returns the outgoing SOAP Message. This is a live message, not a copy. Any changes made to it
	 * will be reflected in the send activity
	 * 
	 * @return send SOAP Message
	 */
	public SOAPMessage getSoapMessage() {
		return fSoapMessage;
	}

	/**
	 * Returns the current target URL
	 * 
	 * @return
	 */
	public String getTargetURL() {
		return fTargetURL;
	}

	/**
	 * Sets the current target URL
	 * 
	 * @param targetURL
	 */
	public void setTargetURL(String targetURL) {
		fTargetURL= targetURL;
	}

}
