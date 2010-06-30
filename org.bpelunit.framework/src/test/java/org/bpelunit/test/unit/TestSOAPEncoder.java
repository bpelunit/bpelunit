/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.soap.DocumentLiteralEncoder;
import org.bpelunit.framework.control.soap.NamespaceContextImpl;
import org.bpelunit.framework.control.soap.RPCLiteralEncoder;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.exception.SOAPEncodingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.test.util.StringOutputStream;
import org.bpelunit.test.util.TestUtil;
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

	private static final String PATH_TO_FILES= "/soapencoder/";

	private String toString(SOAPMessage message) throws SOAPException, IOException {
		StringOutputStream sos= new StringOutputStream();
		message.writeTo(sos);
		String s= sos.getString();
		return s;
	}

	// ********************* Test Cases ************************

	@Test
	public void testEncodeDocLit() throws SpecificationException, Exception {

		// Create a document/literal message

		Element literal= TestUtil.readLiteralData(PATH_TO_FILES + "doclit1.xmlfrag");
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, "MyPartner.wsdl", "NewOperation");

		ISOAPEncoder encoder= new DocumentLiteralEncoder();
		SOAPMessage message= encoder.construct(operation, literal,
				BPELUnitConstants.SOAP_FAULT_CODE_CLIENT,
				BPELUnitConstants.SOAP_FAULT_DESCRIPTION);

		String messageAsString= toString(message);

		assertEquals(
				nl("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><tns:about xmlns:tns=\"http://xxx\">\n"
						+ "  <tns:me name=\"Phil\"/>\n" + "</tns:about></SOAP-ENV:Body></SOAP-ENV:Envelope>"), nl(messageAsString));
	}

	private String nl(String messageAsString) {
		return StringUtils.remove(messageAsString, '\r');
	}

	/* Reuse the same test body for both RPC encoding tests (wrapped/unwrapped) */
	private void testEncodeRPCLit(final String fragmentPath) throws Exception,
			SpecificationException, SOAPEncodingException, SOAPException,
			IOException {
		// Create a RPC Literal message
		Element literal= TestUtil.readLiteralData(PATH_TO_FILES + fragmentPath);
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, "MyPartner.wsdl", "NewOperation");
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

	@Test
	public void testEncodeRPCLitUnwrapped() throws SpecificationException, Exception {
		testEncodeRPCLit("rpclit1.xmlfrag");
	}

	@Test
	public void testEncodeRPCLitWrapped() throws SpecificationException, Exception {
		testEncodeRPCLit("rpclit3.xmlfrag");
	}

	@Test
	public void testDecodeDocLit() throws SpecificationException, Exception {

		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage rcvMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "doclit2.xmlfrag"));
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, "MyPartner.wsdl", "NewOperation");

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
	public void testDecodeRPCLit() throws SpecificationException, Exception {

		MessageFactory factory= MessageFactory.newInstance();
		SOAPMessage rcvMessage= factory.createMessage(null, this.getClass().getResourceAsStream(PATH_TO_FILES + "rpclit2.xmlfrag"));
		SOAPOperationCallIdentifier operation= TestUtil.getCall(PATH_TO_FILES, "MyPartner.wsdl", "NewOperation");

		ISOAPEncoder encoder= new RPCLiteralEncoder();
		Element parent= encoder.deconstruct(operation, rcvMessage);

		NamespaceContextImpl nsi= new NamespaceContextImpl();
		nsi.setNamespace("tns", "http://xxx");

		Node node= TestUtil.getNode(parent, nsi, "someFirstPart");
		assertNotNull(node);

		Node node2= TestUtil.getNode(parent, nsi, "someFirstPart/tns:me");
		assertNotNull(node2);
	}
}
