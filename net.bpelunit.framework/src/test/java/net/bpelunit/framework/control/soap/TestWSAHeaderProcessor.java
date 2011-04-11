/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.soap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.test.unit.SimpleTest;
import net.bpelunit.test.util.TestUtil;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Tests the WSA Header Processor
 * 
 * @author Philip Mayer, Antonio Garcia-Dominguez 
 */
public class TestWSAHeaderProcessor extends SimpleTest {

	private static final String PATH_TO_FILES = "/wsahp/";

	@Test
	public void testWSAHeadersReceiveSend() throws Exception {

		ActivityContext dummyContext = new ActivityContext("http://simulated.url");

		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage rcvMessage = factory.createMessage(null,
			this.getClass().getResourceAsStream(PATH_TO_FILES + "incomingSOAP.xml"));

		IHeaderProcessor proc  = new WSAHeaderProcessor();
		proc.processReceive(dummyContext, rcvMessage);

		assertEquals("true",
			dummyContext.getUserData(WSAHeaderProcessor.WSA_RECEIVED));
		assertEquals("kjasdhfjsdbksjdgdsrhfdgjkdfjk",
			dummyContext.getUserData(WSAHeaderProcessor.WSA_RECEIVED_ID));
		assertEquals("http://return.to.me/",
			dummyContext.getUserData(WSAHeaderProcessor.WSA_RECEIVED_ADDRESS));

		SOAPMessage sendMessage = factory.createMessage(null,
			this.getClass().getResourceAsStream(PATH_TO_FILES + "outgoingSOAP.xml"));

		SendPackage p= new SendPackage("http://target.url", sendMessage);
		proc.processSend(dummyContext, p);

		assertEquals("http://return.to.me/", p.getTargetURL());
		Element header= p.getSoapMessage().getSOAPHeader();

		NamespaceContextImpl ns= new NamespaceContextImpl();
		ns.setNamespace("wsa", WSAHeaderProcessor.WSA_NAMESPACE);

		Node msgId= TestUtil.getNode(header, ns, "wsa:" + WSAHeaderProcessor.WSA_TAG_RELATES_TO);
		assertEquals("kjasdhfjsdbksjdgdsrhfdgjkdfjk", msgId.getTextContent());
	}

	@Test
	public void testWSAHeadersSendReceive() throws Exception {

		ActivityContext dummyContext= new ActivityContext("http://simulated.url");
		IHeaderProcessor proc= new WSAHeaderProcessor();

		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage sendMessage= factory.createMessage(null,
			this.getClass().getResourceAsStream(PATH_TO_FILES + "outgoingSOAP.xml"));

		SendPackage p= new SendPackage("http://target.url", sendMessage);
		proc.processSend(dummyContext, p);

		/*
		 * Processor needs to insert MessageID and Reply-To-Adress into the SOAP heaper.
		 */

		Element header = p.getSoapMessage().getSOAPHeader();
		NamespaceContextImpl ns = new NamespaceContextImpl();
		ns.setNamespace("wsa", WSAHeaderProcessor.WSA_NAMESPACE);
		Node msgId = TestUtil.getNode(header, ns, "wsa:" + WSAHeaderProcessor.WSA_TAG_MESSAGE_ID);
		assertTrue(msgId.getTextContent().startsWith(WSAHeaderProcessor.WSA_MESSAGE_ID_PREFIX));
		Node replyTo = TestUtil.getNode(header, ns,
			String.format("wsa:%s/wsa:%s",
					WSAHeaderProcessor.WSA_TAG_REPLY_TO,
					WSAHeaderProcessor.WSA_TAG_ADDRESS));
		assertEquals("http://simulated.url", replyTo.getTextContent());

		assertEquals("true", dummyContext.getUserData(WSAHeaderProcessor.WSA_SENT));
		assertTrue(dummyContext.getUserData(WSAHeaderProcessor.WSA_SENT_ID)
			.startsWith(WSAHeaderProcessor.WSA_MESSAGE_ID_PREFIX));

		// Receive something back
		SOAPMessage rcvMessage= factory.createMessage(null,
			this.getClass().getResourceAsStream(PATH_TO_FILES + "incomingSOAP2.xml"));
		proc.processReceive(dummyContext, rcvMessage);
		assertEquals("WSA-838848474774-3883873747",
			dummyContext.getUserData(WSAHeaderProcessor.WSA_RECEIVED_ID));
	}
}
