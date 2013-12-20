/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.soap.DocumentLiteralEncoder;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.soap.RPCLiteralEncoder;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import net.bpelunit.test.util.StringOutputStream;
import net.bpelunit.test.util.TestUtil;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * Tests the two SOAP encoders.
 * 
 * @version $Id: TestSOAPEncoder.java,v 1.5 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestSOAPEncoder extends SimpleTest {

	private static final String PARTNER_OPERATION = "NewOperation";
	private static final String PARTNER_WSDL = "MyPartner.wsdl";
	private static final String PATH_TO_FILES= "/soapencoder/";


	// ********************* Test Cases ************************

	@Test
	public void testEncodeDocLit() throws Exception {
		// Create a document/literal message
		Element literal= TestUtil.readLiteralData(PATH_TO_FILES + "doclit1.xmlfrag");
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, PARTNER_WSDL, PARTNER_OPERATION, SOAPOperationDirectionIdentifier.INPUT);

		ISOAPEncoder encoder= new DocumentLiteralEncoder();
		SOAPMessage message= encoder.construct(operation, literal,
				BPELUnitConstants.SOAP_FAULT_CODE_CLIENT,
				BPELUnitConstants.SOAP_FAULT_DESCRIPTION);

		String messageAsString= toString(message);

		assertEquals(
				nl("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><tns:about xmlns:tns=\"http://xxx\">\n"
						+ "  <tns:me name=\"Phil\"/>\n" + "</tns:about></SOAP-ENV:Body></SOAP-ENV:Envelope>"), nl(messageAsString));
	}

	@Test
	public void testEncodeRPCLitUnwrapped() throws Exception {
		testEncodeRPCLit("rpclit1.xmlfrag");
	}
	
	@Test
	public void testEncodeRPCLitUnwrappedWithPartsInNamespaces() throws Exception {
		Element literal = TestUtil.readLiteralData(PATH_TO_FILES + "rpclit4.xmlfrag");
		SOAPOperationCallIdentifier operation = TestUtil.getCall(PATH_TO_FILES, PARTNER_WSDL, PARTNER_OPERATION, SOAPOperationDirectionIdentifier.INPUT);
		ISOAPEncoder encoder = new RPCLiteralEncoder();
		SOAPMessage message = encoder.construct(operation, literal,
				BPELUnitConstants.SOAP_FAULT_CODE_CLIENT,
				BPELUnitConstants.SOAP_FAULT_DESCRIPTION);

		final SOAPBody soapBody = message.getSOAPBody();
		assertEquals(1, soapBody.getElementsByTagName("someFirstPart").getLength());
		assertEquals(1, soapBody.getElementsByTagName("someSecondPart").getLength()); 
	}

	@Test
	public void testEncodeRPCLitWrapped() throws Exception {
		testEncodeRPCLit("rpclit3.xmlfrag");
	}

	@Test
	public void testEncodeRPCLiteResponseUnwrapped() throws Exception {
		Element literal= TestUtil.readLiteralData(PATH_TO_FILES + "rpclit1.xmlfrag");
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, PARTNER_WSDL, PARTNER_OPERATION, SOAPOperationDirectionIdentifier.OUTPUT);
		ISOAPEncoder encoder= new RPCLiteralEncoder();
		SOAPMessage message= encoder.construct(operation, literal,
				BPELUnitConstants.SOAP_FAULT_CODE_CLIENT,
				BPELUnitConstants.SOAP_FAULT_DESCRIPTION);

		final Node firstChild = message.getSOAPBody().getFirstChild();
		assertEquals(XMLConstants.NULL_NS_URI, firstChild.getNamespaceURI());
		assertEquals(operation.getName() + "Response", firstChild.getLocalName());
	}
	
	@Test
	public void testDecodeDocLit() throws Exception {
		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage rcvMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "doclit2.xmlfrag"));
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, PARTNER_WSDL, PARTNER_OPERATION, SOAPOperationDirectionIdentifier.INPUT);

		ISOAPEncoder encoder= new DocumentLiteralEncoder();
		Element parent= encoder.deconstruct(operation, rcvMessage);

		NamespaceContextImpl nsi= new NamespaceContextImpl();
		nsi.setNamespace("tns", "http://xxx");

		Node node= TestUtil.getNode(parent, nsi, "tns:about");
		assertNotNull(node);

		Node node2= TestUtil.getNode(parent, nsi, "tns:about/tns:me");
		assertNotNull(node2);
	}

	@Test
	public void testDecodeRPCLit() throws Exception {
		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage rcvMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "rpclit2.xmlfrag"));
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, PARTNER_WSDL, PARTNER_OPERATION, SOAPOperationDirectionIdentifier.INPUT);

		ISOAPEncoder encoder= new RPCLiteralEncoder();
		Element parent= encoder.deconstruct(operation, rcvMessage);

		NamespaceContextImpl nsi= new NamespaceContextImpl();
		nsi.setNamespace("tns", "http://xxx");

		Node node= TestUtil.getNode(parent, nsi, "someFirstPart");
		assertNotNull(node);

		Node node2= TestUtil.getNode(parent, nsi, "someFirstPart/tns:me");
		assertNotNull(node2);
	}

	/* Reuse the same test body for both RPC encoding tests (wrapped/unwrapped) */
	private void testEncodeRPCLit(final String fragmentPath) throws Exception,
			SpecificationException, SOAPEncodingException, SOAPException,
			IOException {
		// Create a RPC Literal message
		Element literal= TestUtil.readLiteralData(PATH_TO_FILES + fragmentPath);
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, PARTNER_WSDL, PARTNER_OPERATION, SOAPOperationDirectionIdentifier.INPUT);
		ISOAPEncoder encoder= new RPCLiteralEncoder();
		SOAPMessage message= encoder.construct(operation, literal,
				BPELUnitConstants.SOAP_FAULT_CODE_CLIENT,
				BPELUnitConstants.SOAP_FAULT_DESCRIPTION);
		String messageAsString= toString(message);
		assertEquals(
				nl("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><rpcwrappernsprefix:NewOperation xmlns:rpcwrappernsprefix=\"http://test.com/NewOperation\"><someFirstPart>\n"
						+ "	<tns:me xmlns:tns=\"http://xxx\" name=\"Phil\"/>\n"
						+ "</someFirstPart><someSecondPart>directStringPart</someSecondPart></rpcwrappernsprefix:NewOperation></SOAP-ENV:Body></SOAP-ENV:Envelope>"),
				nl(messageAsString));
	}

	private String toString(SOAPMessage message) throws SOAPException, IOException {
		StringOutputStream sos = new StringOutputStream();
		message.writeTo(sos);
		return sos.getString();
	}

	private String nl(String messageAsString) {
		return StringUtils.remove(messageAsString, '\r');
	}
}
