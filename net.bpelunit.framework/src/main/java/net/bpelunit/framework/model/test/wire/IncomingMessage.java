/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.wire;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.io.IOUtils;

/**
 * An IncomingMessage object is a plain, on-the-wire representation of an
 * "incoming message" from the frameworks point of view, be it a HTTP Response
 * (in which case it includes a HTTP Code) or a HTTP Request (made by an entity
 * external to the framework).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class IncomingMessage {

	/**
	 * If this message is a HTTP response, this is the HTTP code of the
	 * response.
	 */
	private int fCode;

	private SOAPMessage message;

	public void setStatusCode(int code) {
		fCode = code;
	}

	public int getReturnCode() {
		return fCode;
	}

	public void setMessage(InputStream in) {
		try {
			message = MessageFactory.newInstance().createMessage(null, in);
		} catch (IOException e) {
			message = null;
		} catch (SOAPException e) {
			message = null;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public void setMessage(byte[] body) {
		setMessage(new ByteArrayInputStream(body));
	}

	public String getMessageAsString() {
		if (message == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			message.writeTo(out);
			return out.toString(); // TODO Character set
		} catch (SOAPException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public SOAPMessage getMessage() {
		return message;
	}
}
