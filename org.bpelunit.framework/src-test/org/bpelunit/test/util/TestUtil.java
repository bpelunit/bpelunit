/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.test.util;

import java.io.File;
import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.xmlbeans.XmlOptions;
import org.bpelunit.framework.control.result.XMLResultProducer;
import org.bpelunit.framework.control.soap.NamespaceContextImpl;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.test.TestSuite;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import org.bpelunit.framework.xml.result.XMLTestResultDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 * Some Test Utilities.
 * 
 * @version $Id: TestUtil.java,v 1.2 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestUtil {

	public static Element parse(String toEncode) throws Exception {
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder= factory.newDocumentBuilder();
		Document document= builder.parse(new InputSource(new StringReader("<dummy>" + toEncode + "</dummy>")));
		// should be one element
		Element dummy= (Element) document.getChildNodes().item(0);
		return dummy;
	}

	public static Element readLiteralData(String path, String fileName) throws Exception {
		File f= new File(path + fileName);
		String str= FileUtils.readFileToString(f, null);
		return parse(str);
	}

	public static SOAPOperationCallIdentifier getCall(String path, String wsdl, String operationName) throws SpecificationException {
		Partner p= new Partner("MyPartner", path, wsdl, "");
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation= p.getOperation(service, "MyPartnerSOAP", operationName, SOAPOperationDirectionIdentifier.INPUT);
		return operation;
	}

	public static Node getNode(Element literalData, NamespaceContextImpl context, String string) throws XPathExpressionException {
		XPath xpath= XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(context);
		return (Node) xpath.evaluate(string, literalData, XPathConstants.NODE);
	}

	public static void assertSameAndSuccessfulResults(String msg, File bptsOriginal, File bptsNew) throws Exception {
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		final String xmlTextA = getXMLResults(bptsOriginal).xmlText(options);
		final String xmlTextB = getXMLResults(bptsNew).xmlText(options);
		assertEquals(msg, xmlTextA, xmlTextB);
	}

	public static void assertDifferentResults(String msg, File bptsOriginal, File bptsNew) throws Exception {
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		final String xmlTextA = getXMLResults(bptsOriginal).xmlText(options);
		final String xmlTextB = getXMLResults(bptsNew).xmlText(options);
		assertFalse(msg, xmlTextA.equals(xmlTextB));
	}

	public static XMLTestResultDocument getXMLResults(File bpts) throws Exception {
		return XMLResultProducer.getXMLResults(getResults(bpts));
	}

	public static TestSuite getResults(File bpts)
			throws ConfigurationException, SpecificationException {
		TestTestRunner runner = new TestTestRunner(bpts.getParent(), bpts.getName());
		runner.testRun();
		assertTrue("Test suites should pass all tests", runner.getTestSuite().getStatus().isPassed());
		return runner.getTestSuite();
	}
}
