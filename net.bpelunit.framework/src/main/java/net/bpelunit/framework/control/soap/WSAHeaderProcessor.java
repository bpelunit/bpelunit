/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.soap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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
 * This class implements the WS-Adressing asynchronous call handling, as specified in the WS-A
 * 2003/03 standard.
 * 
 * @author Philip Mayer, Antonio Garcia-Dominguez
 */
public class WSAHeaderProcessor implements IHeaderProcessor {

	static final String WSA_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";

	static final String WSA_TAG_RELATES_TO = "RelatesTo";
	static final String WSA_TAG_ADDRESS = "Address";
	static final String WSA_TAG_REPLY_TO = "ReplyTo";
	static final String WSA_TAG_MESSAGE_ID = "MessageID";

	static final String WSA_RECEIVED = "WSA-Received";
	static final String WSA_RECEIVED_ADDRESS = "WSA-Received-Address";
	static final String WSA_RECEIVED_ID = "WSA-Received-ID";
	static final String WSA_SENT = "WSA-Sent";
	static final String WSA_SENT_ID = "WSA-Sent-ID";

	// Prefix for all message IDs produced by BPELUnit
	static final String WSA_MESSAGE_ID_PREFIX = "http://www.bpelunit.net/wsa/messageId/";

	private Map<String, String> fProperties = new HashMap<String, String>();

	public void processReceive(ActivityContext context, SOAPMessage message) throws HeaderProcessingException {
		if (context.getUserData(WSA_SENT).equals("true")) {
			/* 
			 * This is part two of a send-receive. We store the From and RelatesTo ID,
			 * to test whether this reply was intended for our request or not.
			 */
			SOAPHeader header = getHeader(message, "Incoming");

			String relatesToID = getRelatesToID(header);
			context.setUserData(WSA_RECEIVED_ID, relatesToID);
			context.setUserData(MSG_RECEIVED_ID, relatesToID);
		} else {
			/* 
			 * This is part one of a receive-send. We store it so we can later
			 * produce the right WS-A headers for the reply.
			 */
			SOAPHeader header = getHeader(message, "Incoming");
			context.setUserData(WSA_RECEIVED, "true");

			String messageID = getMessageID(header);
			context.setUserData(WSA_RECEIVED_ID, messageID);
			context.setUserData(MSG_RECEIVED_ID, messageID);
			context.setUserData(WSA_RECEIVED_ADDRESS, getEndpointURL(header, WSA_TAG_REPLY_TO));
		}
	}

	public void processSend(ActivityContext context, SendPackage sendSpec) throws HeaderProcessingException {
		if (context.getUserData(WSA_RECEIVED).equals("true")) {
			/* 
			 * This is part two of a receive-send: WS-A requires BPELUnit
			 * to add the RelatesTo header and send the message to the address
			 * in the To header previously received.
			 */
			SOAPHeader header = getHeader(sendSpec.getSoapMessage(), "Outgoing");

			String messageID = getRequiredUserData(context,
					WSA_RECEIVED_ID, "Message ID from presumed receive was empty.");
			if (!"".equals(messageID)) {
				addRelatesToHeader(header, messageID);
			}

			String targetURL = getRequiredUserData(context,
					WSA_RECEIVED_ADDRESS, "Target URL from presumed receive was empty.");
			sendSpec.setTargetURL(targetURL);
		}
		else {
			/* 
			 * This is part one of a send-receive. We add the ReplyTo and MessageID
			 * headers and store the ID sent to the BPEL process. We need this ID to
			 * drop responses to other messages later. The message ID to be used can
			 * be set as a property in the BPTS: if not set in the BPTS, BPELUnit
			 * will generate an universally unique ID. 
			 */
			SOAPHeader header = getHeader(sendSpec.getSoapMessage(), "Outgoing");
			String id = addMessageID(header);
			addReplyTo(header, context);
			context.setUserData(WSA_SENT, "true");
			context.setUserData(WSA_SENT_ID, id);
			context.setUserData(MSG_SENT_ID, id);
		}
	}

	public void setProperty(String name, String stringValue) {
		fProperties.put(name, stringValue);
	}

	private String addMessageID(SOAPHeader header) throws HeaderProcessingException {
		try {
			String id = null;
			if (fProperties.containsKey(WSA_TAG_MESSAGE_ID)) {
				id = fProperties.get(WSA_TAG_MESSAGE_ID);
			} else {
				id = WSA_MESSAGE_ID_PREFIX + UUID.randomUUID().toString();
			}
			SOAPElement msgId = header.addChildElement(wsaQName(WSA_TAG_MESSAGE_ID));
			msgId.setTextContent(id);
			return id;
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
					"Could not add MessageID header to outgoing SOAP message.",
					e);
		}
	}

	private void addRelatesToHeader(SOAPHeader header, String messageID)
			throws HeaderProcessingException {
		SOAPElement msgId;
		try {
			msgId = header.addChildElement(wsaQName(WSA_TAG_RELATES_TO));
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
				"Could not add RelatesTo header to outgoing SOAP message.", e);
		}
		msgId.setTextContent(messageID);
	}

	private void addReplyTo(SOAPHeader header, ActivityContext context) throws HeaderProcessingException {
		try {
			SOAPElement replyTo = header.addChildElement(wsaQName(WSA_TAG_REPLY_TO));
			SOAPElement address = replyTo.addChildElement(wsaQName(WSA_TAG_ADDRESS));
			address.setTextContent(context.getPartnerURL());
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
					"Could not add ReplyTo header to outgoing SOAP message.", e);
		}
	}

	private String getEndpointURL(SOAPHeader header, String wsaTagName)
			throws HeaderProcessingException {
		String replyTo = null;
		for (Iterator<?> i = header.getChildElements(wsaQName(wsaTagName)); i.hasNext();) {
			SOAPElement soapElement = (SOAPElement) i.next();
			for (Iterator<?> j = soapElement.getChildElements(wsaQName(WSA_TAG_ADDRESS)); j.hasNext();) {
				SOAPElement soapElement2 = (SOAPElement) j.next();
				replyTo = soapElement2.getTextContent();
			}
		}
		if (replyTo == null) {
			throw new HeaderProcessingException(wsaTagName + " address not found in incoming message.");
		}
		return replyTo;
	}

	private SOAPHeader getHeader(SOAPMessage message, String msgDirection)
			throws HeaderProcessingException {
		SOAPHeader header;
		try {
			header = message.getSOAPHeader();
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
				msgDirection + " SOAP message did not have a SOAP Header.", e);
		}
		return header;
	}

	private String getMessageID(SOAPHeader header) {
		String messageID = "";
		for (Iterator<?> i = header.getChildElements(wsaQName(WSA_TAG_MESSAGE_ID)); i.hasNext();) {
			SOAPElement soapElement = (SOAPElement) i.next();
			messageID = soapElement.getTextContent();
		}
		return messageID;
	}

	private String getRelatesToID(SOAPHeader header)
			throws HeaderProcessingException {
		String relatesTo = null;
		for (Iterator<?> itRelatesTo = header.getChildElements(wsaQName(WSA_TAG_RELATES_TO)); itRelatesTo.hasNext();) {
			final SOAPElement soapE = (SOAPElement)itRelatesTo.next();
			relatesTo = soapE.getTextContent();
		}
		if (relatesTo == null) {
			throw new HeaderProcessingException("No RelatesTo header found in the incoming message");
		}
		return relatesTo;
	}

	private String getRequiredUserData(ActivityContext context, String name,
			String msgIfMissing) throws HeaderProcessingException {
		String targetURL = context.getUserData(name);
		if (targetURL == null) {
			throw new HeaderProcessingException(msgIfMissing);
		}
		return targetURL;
	}

	private QName wsaQName(String localPart) {
		return new QName(WSA_NAMESPACE, localPart);
	}

}
