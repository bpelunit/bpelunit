/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.wire;

/**
 * An OutgoingMessage object is a plain, on-the-wire representation of an "outgoing message" from
 * the frameworks point of view, be it a response to a HTTP request from an external entitiy (in
 * which case it will contain a code, but no target URL and no SOAP Action) or a full request (in
 * which case it will contain target URL and SOAP Action, but no code).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class OutgoingMessage {

	/**
	 * If this message represents a full initial request, this is the target URL.
	 */
	private String fTargetURL;

	/**
	 * If this message represents a full initial request, this is the SOAP Action for the HTTP
	 * Header.
	 */
	private String fSOAPAction;

	/**
	 * If this message represents a reply in a HTTP operation, this is the HTTP result code.
	 */
	private int fCode;

	/**
	 * The body of the message
	 */
	private String fBody;


	public void setTargetURL(String targetURL) {
		fTargetURL= targetURL;
	}

	public String getTargetURL() {
		return fTargetURL;
	}

	public void setSOAPAction(String action) {
		fSOAPAction= action;
	}

	public String getSOAPHTTPAction() {
		return fSOAPAction;
	}

	public void setBody(String body) {
		fBody= body;
	}

	public String getBody() {
		return fBody;
	}

	public int getCode() {
		return fCode;
	}

	public void setCode(int code) {
		fCode= code;
	}

}
