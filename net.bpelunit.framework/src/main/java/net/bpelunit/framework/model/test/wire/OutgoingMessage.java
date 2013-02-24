/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.wire;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.io.IOUtils;

import net.bpelunit.framework.control.run.BlackBoard;
import net.bpelunit.framework.control.run.BlackBoardKey;

/**
 * An OutgoingMessage object is a plain, on-the-wire representation of an
 * "outgoing message" from the frameworks point of view, be it a response to a
 * HTTP request from an external entitiy (in which case it will contain a code,
 * but no target URL and no SOAP Action) or a full request (in which case it
 * will contain target URL and SOAP Action, but no code).
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke
 * 
 */
public class OutgoingMessage implements BlackBoardKey {

	public OutgoingMessage() {
	}
	
	private Map<String, String> protocolOptions = new HashMap<String, String>();

	/**
	 * If this message represents a full initial request, this is the target
	 * URL.
	 */
	private String targetURL;

	/**
	 * If this message represents a full initial request, this is the SOAP
	 * Action for the HTTP Header.
	 */
	private String soapAction;

	/**
	 * If this message represents a reply in a HTTP operation, this is the HTTP
	 * result code.
	 */
	private int code;

	
	/**
	 * The body of the HTTP message, i.e. the SOAP message
	 */
	private SOAPMessage message;

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	public String getTargetURL() {
		return targetURL;
	}

	public void setSOAPAction(String action) {
		this.soapAction = action;
	}

	public String getSOAPHTTPAction() {
		return soapAction;
	}

	public void setBody(SOAPMessage message) {
		this.message = message;
	}

	public SOAPMessage getMessage() {
		return message;
	}

	public String getMessageAsString() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			message.writeTo(out);
			return out.toString();
		} catch (SOAPException e) {
			return "";
		} catch (IOException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public void addProtocolOption(String name, String value) {
		protocolOptions.put(name, value);
	}
	
	public String getProtocolOption(String name) {
		return protocolOptions.get(name);
	}
	
	public String[] getProtocolOptionNames() {
		return protocolOptions.keySet().toArray(new String[protocolOptions.size()]);
	}

	@Override
	public boolean canStillProvideValue(BlackBoard<?, ?> blackboard) {
		return true;
	}
}
