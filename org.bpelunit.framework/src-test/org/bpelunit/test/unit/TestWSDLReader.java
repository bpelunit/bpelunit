/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import org.junit.Test;

/**
 * 
 * Tests the WSDL reader and SOAP operation identifier
 * 
 * @version $Id: TestWSDLReader.java,v 1.4 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestWSDLReader extends SimpleTest {

	// Helpers

	private String getEncoding(String name) throws SpecificationException {
		Partner p= new Partner("MyPartner", "resources/wsdlreader/", name, "");

		// get a document/literal operation
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation2= p.getOperation(service, "MyPartnerSOAP", "NewOperation", SOAPOperationDirectionIdentifier.INPUT);
		return operation2.getEncodingStyle();
	}

	// Helpers

	private SOAPOperationCallIdentifier getOp(String name) throws SpecificationException {
		Partner p= new Partner("MyPartner", "resources/wsdlreader/", name, "");

		// get a document/literal operation
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation2= p.getOperation(service, "MyPartnerSOAP", "NewOperation", SOAPOperationDirectionIdentifier.INPUT);
		return operation2;
	}

	// Test cases

	@Test
	public void testGetDocumentLiteral1() throws SpecificationException {

		// Test "document" at binding level, "literal" at body level.
		String encodingStyle= getEncoding("MyPartner1.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetDocumentLiteral2() throws SpecificationException {

		// Test "document" at operation level, "literal" at body level.
		String encodingStyle= getEncoding("MyPartner2.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetDocumentLiteral5() throws SpecificationException {

		// Test no specs whatsoever (not legal!)
		String encodingStyle= getEncoding("MyPartner5.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetDocumentLiteral6() throws SpecificationException {

		// Test no style, but literal encoding in output element.
		String encodingStyle= getEncoding("MyPartner6.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetRPCLiteral() throws SpecificationException {

		// Test "rpc" at binding level, "literal" at body level.
		String encodingStyle= getEncoding("MyPartner3.wsdl");
		assertEquals("rpc/literal", encodingStyle);
	}

	@Test
	public void testGetRPCEncoded() throws SpecificationException {

		// Test "rpc" at binding level, "encoded" at body level.
		String encodingStyle= getEncoding("MyPartner4.wsdl");
		assertEquals("rpc/encoded", encodingStyle);
	}

	@Test
	public void testItems() throws SpecificationException {

		SOAPOperationCallIdentifier c= getOp("MyPartner6.wsdl");
		assertEquals("http://www.example.org/", c.getTargetURL());
		assertEquals("http://www.example.org/MyPartner/", c.getTargetNamespace());
		assertEquals("http://www.example.org/MyPartner/NewOperation", c.getSOAPHTTPAction());

	}

}
