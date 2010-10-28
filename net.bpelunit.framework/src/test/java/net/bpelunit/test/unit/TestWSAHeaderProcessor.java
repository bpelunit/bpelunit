/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.soap.WSAHeaderProcessor;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.test.util.TestUtil;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * Tests the WSA Header Processor
 * 
 * @version $Id: TestWSAHeaderProcessor.java,v 1.5 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestWSAHeaderProcessor extends SimpleTest {

	private static final String PATH_TO_FILES= "/wsahp/";

	@Test
	public void testWSAHeadersReceiveSend() throws Exception {

		ActivityContext dummyContext= new ActivityContext("http://simulated.url");

		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage rcvMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "incomingSOAP.xml"));

		IHeaderProcessor proc= new WSAHeaderProcessor();

		proc.processReceive(dummyContext, rcvMessage);

		assertEquals("true", dummyContext.getUserData("WSA-Received"));
		assertEquals("kjasdhfjsdbksjdgdsrhfdgjkdfjk", dummyContext.getUserData("WSA-Received-ID"));
		assertEquals("http://return.to.me/", dummyContext.getUserData("WSA-Received-Address"));

		SOAPMessage sendMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "outgoingSOAP.xml"));

		SendPackage p= new SendPackage("http://target.url", sendMessage);
		proc.processSend(dummyContext, p);

		assertEquals("http://return.to.me/", p.getTargetURL());
		Element header= p.getSoapMessage().getSOAPHeader();

		NamespaceContextImpl ns= new NamespaceContextImpl();
		ns.setNamespace("wsa", "http://schemas.xmlsoap.org/ws/2003/03/addressing");

		Node msgId= TestUtil.getNode(header, ns, "wsa:RelatesTo");
		assertEquals("kjasdhfjsdbksjdgdsrhfdgjkdfjk", msgId.getTextContent());
	}

	@Test
	public void testWSAHeadersSendReceive() throws Exception {

		ActivityContext dummyContext= new ActivityContext("http://simulated.url");
		IHeaderProcessor proc= new WSAHeaderProcessor();

		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage sendMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "outgoingSOAP.xml"));

		SendPackage p= new SendPackage("http://target.url", sendMessage);
		proc.processSend(dummyContext, p);

		/*
		 * Processor needs to insert MessageID and Reply-To-Adress into the SOAP heaper.
		 */

		Element header= p.getSoapMessage().getSOAPHeader();
		NamespaceContextImpl ns= new NamespaceContextImpl();
		ns.setNamespace("wsa", "http://schemas.xmlsoap.org/ws/2003/03/addressing");
		Node msgId= TestUtil.getNode(header, ns, "wsa:MessageID");
		assertEquals("WSA-838848474774-3883873747", msgId.getTextContent());
		Node replyTo= TestUtil.getNode(header, ns, "wsa:ReplyTo/wsa:Address");
		assertEquals("http://simulated.url", replyTo.getTextContent());

		assertEquals("true", dummyContext.getUserData("WSA-Sent"));
		assertEquals("WSA-838848474774-3883873747", dummyContext.getUserData("WSA-Sent-ID"));

		// Receive something back (doesn't really matter actually)

		SOAPMessage rcvMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "incomingSOAP2.xml"));
		proc.processReceive(dummyContext, rcvMessage);

	}

}
