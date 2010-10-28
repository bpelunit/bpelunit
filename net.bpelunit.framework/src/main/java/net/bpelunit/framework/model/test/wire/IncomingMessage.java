/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.wire;

/**
 * An IncomingMessage object is a plain, on-the-wire representation of an "incoming message" from
 * the frameworks point of view, be it a HTTP Response (in which case it includes a HTTP Code) or a
 * HTTP Request (made by an entity external to the framework).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class IncomingMessage {

	/**
	 * If this message is a HTTP response, this is the HTTP code of the response.
	 */
	private int fCode;

	/**
	 * The body of the message
	 */
	private String fBody;


	public void setStatusCode(int code) {
		fCode= code;
	}

	public int getReturnCode() {
		return fCode;
	}

	public void setBody(String body) {
		fBody= body;
	}

	public String getBody() {
		return fBody;
	}

}
