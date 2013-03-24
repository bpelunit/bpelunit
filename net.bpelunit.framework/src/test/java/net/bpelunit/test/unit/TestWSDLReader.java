/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

import net.bpelunit.framework.SpecificationLoader;
import net.bpelunit.framework.model.Partner;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

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

	private static final String ABS_PATH = TestWSDLReader.class.getResource("/wsdlreader").getPath() + "/";
	
	// Helpers

	private String getEncoding(String name) throws Exception {
		
		Definition d = SpecificationLoader.loadWsdlDefinition(ABS_PATH, name, "TEST");
		
		Partner p= new Partner("MyPartner", d, null, "");

		// get a document/literal operation
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation= p.getOperation(service, "MyPartnerSOAP", "NewOperation", SOAPOperationDirectionIdentifier.INPUT);
		return operation.getEncodingStyle();
	}

	// Helpers

	private SOAPOperationCallIdentifier getOp(String name) throws Exception {
		
		Definition d = SpecificationLoader.loadWsdlDefinition(ABS_PATH, name, "TEST");
		
		Partner p= new Partner("MyPartner", d, null, "");

		// get a document/literal operation
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation= p.getOperation(service, "MyPartnerSOAP", "NewOperation", SOAPOperationDirectionIdentifier.INPUT);
		return operation;
	}

	// Test cases

	@Test
	public void testGetDocumentLiteral1() throws Exception {

		// Test "document" at binding level, "literal" at body level.
		String encodingStyle= getEncoding("MyPartner1.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetDocumentLiteral2() throws Exception {

		// Test "document" at operation level, "literal" at body level.
		String encodingStyle= getEncoding("MyPartner2.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetDocumentLiteral5() throws Exception {

		// Test no specs whatsoever (not legal!)
		String encodingStyle= getEncoding("MyPartner5.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetDocumentLiteral6() throws Exception {

		// Test no style, but literal encoding in output element.
		String encodingStyle= getEncoding("MyPartner6.wsdl");
		assertEquals("document/literal", encodingStyle);
	}

	@Test
	public void testGetRPCLiteral() throws Exception {

		// Test "rpc" at binding level, "literal" at body level.
		String encodingStyle= getEncoding("MyPartner3.wsdl");
		assertEquals("rpc/literal", encodingStyle);
	}

	@Test
	public void testGetRPCEncoded() throws Exception {

		// Test "rpc" at binding level, "encoded" at body level.
		String encodingStyle= getEncoding("MyPartner4.wsdl");
		assertEquals("rpc/encoded", encodingStyle);
	}

	@Test
	public void testItems() throws Exception {

		SOAPOperationCallIdentifier c= getOp("MyPartner6.wsdl");
		assertEquals("http://www.example.org/", c.getTargetURL());
		assertEquals("http://www.example.org/MyPartner/", c.getTargetNamespace());
		assertEquals("http://www.example.org/MyPartner/NewOperation", c.getSOAPHTTPAction());

	}

	@Test
	public void testTwoWSDLsSpecified() throws Exception {
		
		Definition d1 = SpecificationLoader.loadWsdlDefinition(ABS_PATH, "MyPartner1.wsdl", "TEST");
		Definition d2 = SpecificationLoader.loadWsdlDefinition(ABS_PATH, "Callback.wsdl", "TEST");
		
		Partner p = new Partner("Partner", d1, d2, "");
		
		// Fetch service in WSDL 1
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation = p.getOperation(service, "MyPartnerSOAP", "NewOperation", SOAPOperationDirectionIdentifier.INPUT);
		assertNotNull(operation);
		
		// Fetch service in WSDL 2
		service= new QName("http://www.example.org/Callback/", "Callback");
		operation = p.getOperation(service, "CallbackSOAP", "NewOperation", SOAPOperationDirectionIdentifier.INPUT);
		assertNotNull(operation);
	}
	
}
