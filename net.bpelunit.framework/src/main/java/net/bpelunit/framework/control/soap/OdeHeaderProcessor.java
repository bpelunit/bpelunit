/**
 * 
 */
package net.bpelunit.framework.control.soap;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.model.test.activity.ActivityContext;

/**
 * @author dluebke
 * 
 *         <pre>
 * &lt;soapenv:Header&gt;
 *   &lt;addr:To xmlns:addr=&quot;http://www.w3.org/2005/08/addressing&quot;&gt;http://localhost:8080/ode/processes/ecrProcessResponse&lt;/addr:To&gt;
 *   &lt;addr:Action xmlns:addr=&quot;http://www.w3.org/2005/08/addressing&quot;&gt;&lt;/addr:Action&gt;
 *   &lt;addr:ReplyTo xmlns:addr=&quot;http://www.w3.org/2005/08/addressing&quot;&gt;
 *     &lt;addr:Address&gt;http://www.w3.org/2005/08/addressing/none&lt;/addr:Address&gt;
 *   &lt;/addr:ReplyTo&gt;
 *   &lt;addr:MessageID xmlns:addr=&quot;http://www.w3.org/2005/08/addressing&quot;&gt;urn:uuid:C152A226C259571A59189545542137&lt;/addr:MessageID&gt;
 *   &lt;intalio:callback xmlns:intalio=&quot;http://www.intalio.com/type/session&quot;&gt;
 *     &lt;addr:Address xmlns:addr=&quot;http://www.w3.org/2005/08/addressing&quot;&gt;http://localhost:8080/ode/processes/ecrProcess&lt;/addr:Address&gt;
 *     &lt;intalio:session&gt;hqejbhcnphr2kwi1bv24xp&lt;/intalio:session&gt;
 *   &lt;/intalio:callback&gt;
 * &lt;/soapenv:Header&gt;
 * </pre>
 */
public class OdeHeaderProcessor implements IHeaderProcessor {

	private static final String KEY_ODE_SENT = "ODE-header-sent";
	private static final String KEY_ODE_RECEIVED = "ODE-header-received";
	private static final String KEY_ODE_SESSION_ID = "ODE-session-id";
	private static final String KEY_ODE_RECEIVED_ADDRESS = "ode-address-received";

	private static final String ODE_SESSION = generateId();

	private static final QName ELEMENT_ODE_CALLBACK = new QName(
			"http://www.intalio.com/type/session", "callback");
	private static final QName ELEMENT_ODE_SESSION = new QName(
			"http://www.intalio.com/type/session", "session");
	private static final QName ELEMENT_WSA_ADDRESS = new QName(
			"http://www.w3.org/2005/08/addressing", "Address");

	/**
	 * @see net.bpelunit.framework.control.ext.IHeaderProcessor#processReceive(net.bpelunit.framework.model.test.activity.ActivityContext,
	 *      javax.xml.soap.SOAPMessage)
	 */
	//@Override
	public void processReceive(ActivityContext context, SOAPMessage message)
			throws HeaderProcessingException {
		if (context.getUserData(KEY_ODE_SENT).equals("true")) {
			// this is part two -> nothing to do
			context.setUserData(KEY_ODE_SENT, null);
		} else {
			// this is (presumably) part one
			// get message ID and reply-To-Adress
			SOAPHeader header;
			try {
				header = message.getSOAPHeader();
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"Incoming SOAP message did not have a SOAP Header.", e);
			}

			SOAPElement callBackHeader = null;
			for (Iterator<?> i = header.getChildElements(ELEMENT_ODE_CALLBACK); i
					.hasNext();) {
				callBackHeader = (SOAPElement) i.next();
			}

			if (callBackHeader != null) {
				// we can only process headers if we got any :-)

				SOAPElement wsaAddress = null;
				for (Iterator<?> i = callBackHeader
						.getChildElements(ELEMENT_WSA_ADDRESS); i.hasNext();) {
					wsaAddress = (SOAPElement) i.next();
				}
				if (wsaAddress == null) {
					throw new HeaderProcessingException(
							"There was no WS-Addressing Address Element in the ODE/Intalio header.");
				}
				String replyTo = wsaAddress.getTextContent();

				SOAPElement odeSession = null;
				for (Iterator<?> i = callBackHeader
						.getChildElements(ELEMENT_ODE_SESSION); i.hasNext();) {
					odeSession = (SOAPElement) i.next();
				}
				if (odeSession == null) {
					throw new HeaderProcessingException(
							"There was no ODE Session in the ODE/Intalio header.");
				}
				String sessionId = wsaAddress.getTextContent();

				context.setUserData(KEY_ODE_RECEIVED, "true");
				context.setUserData(KEY_ODE_SESSION_ID, sessionId);
				context.setUserData(KEY_ODE_RECEIVED_ADDRESS, replyTo);
			}
		}
	}

	/**
	 * 
	 * @see net.bpelunit.framework.control.ext.IHeaderProcessor#processSend(net.bpelunit.framework.model.test.activity.ActivityContext,
	 *      net.bpelunit.framework.control.ext.SendPackage)
	 */
	//@Override
	public void processSend(ActivityContext context, SendPackage message)
			throws HeaderProcessingException {

		message.getSoapMessage();

		// Two options: Either this is part one in a send/receive, or part two
		// in a receive/send.
		if (context.getUserData(KEY_ODE_RECEIVED).equals("true")) {
			context.setUserData(KEY_ODE_RECEIVED, null);

			// we are sending back
			String targetURL = context.getUserData(KEY_ODE_RECEIVED_ADDRESS);
			String sessionId = context.getUserData(KEY_ODE_SESSION_ID);

			if (targetURL == null) {
				throw new HeaderProcessingException(
						"Target URL from presumed receive was empty.");
			}

			if (sessionId == null) {
				throw new HeaderProcessingException(
						"Session ID from presumed receive was empty.");
			}

			SOAPMessage msg = message.getSoapMessage();
			SOAPHeader header = null;
			try {
				header = msg.getSOAPHeader();
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"No SOAP header in outgoing SOAP message.", e);
			}

			if (!sessionId.equals("")) {
				try {
					SOAPElement sessionElement = header.addChildElement(
							ELEMENT_ODE_CALLBACK).addChildElement(ELEMENT_ODE_SESSION);
					sessionElement.setTextContent(sessionId);
				} catch (SOAPException e) {
					throw new HeaderProcessingException(
							"Could not add relatesTo child element to outgoing SOAP message.",
							e);
				}
			}
			message.setTargetURL(targetURL);
		} else {
			// we are first, so this is a send-receive activity
			// add reply-to and message ID to the SOAP message
			SOAPMessage msg = message.getSoapMessage();
			SOAPHeader header;
			try {
				header = msg.getSOAPHeader();
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"No SOAP header in outgoing SOAP message.", e);
			}

			try {

				SOAPElement odeHeaderElement = header
						.addChildElement(ELEMENT_ODE_CALLBACK);

				// Reply To
				SOAPElement address = odeHeaderElement
						.addChildElement(ELEMENT_WSA_ADDRESS);
				address.setTextContent(context.getPartnerURL());

				// Message ID
				String id = ODE_SESSION;
				SOAPElement sesionId = odeHeaderElement
						.addChildElement(ELEMENT_ODE_SESSION);
				sesionId.setTextContent(id);

				// store this
				context.setUserData(KEY_ODE_SENT, "true");
				context.setUserData(KEY_ODE_SESSION_ID, id);
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"Could not add MessageID, ReplyTo or Address child element to outgoing SOAP message.",
						e);
			}
		}

	}

	private static String generateId() {
		return "BPELUNIT-" + (Math.random() * Long.MAX_VALUE) + "-"
				+ (Math.random() * Long.MAX_VALUE);
	}

	/**
	 * This header processor does not have any properties
	 * 
	 * @see net.bpelunit.framework.control.ext.IHeaderProcessor#setProperty(java.lang.String,
	 *      java.lang.String)
	 */
	//@Override
	public void setProperty(String name, String value) {
		return; // so far we have no properties
	}

}
