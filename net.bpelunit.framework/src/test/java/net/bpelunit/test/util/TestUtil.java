/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.result.XMLResultProducer;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.Partner;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import net.bpelunit.framework.xml.result.XMLTestResultDocument;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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

	public static Element readLiteralData(String fileName) throws Exception {
		String str = IOUtils.toString(TestUtil.class.getResourceAsStream(fileName));
		return parse(str);
	}

	public static SOAPOperationCallIdentifier getCall(String path, String wsdl, String operationName) throws SpecificationException {
		String abspath = FileUtils.toFile(TestUtil.class.getResource(path)).getAbsolutePath() + File.separator;
		Partner p= new Partner("MyPartner", abspath, wsdl, "", "");
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
		final XMLTestResultDocument xmlRDoc = XMLResultProducer.getXMLResults(getResults(bpts));

		// Discard data packages with SOAP envelopes: we are only interested
		// in the literal XML data. The SOAP envelopes change from run to run,
		// due to the UUIDs in the Message IDs.
		XmlObject[] envelopes = xmlRDoc.selectPath(String.format(
			"declare namespace soap='%s' $this//soap:Envelope",
			BPELUnitConstants.SOAP_1_1_NAMESPACE));
		for (XmlObject envelope : envelopes) {
			envelope.set(XmlObject.Factory.newInstance());
		}

		return xmlRDoc;
	}

	public static TestSuite getResults(File bpts)
			throws ConfigurationException, DeploymentException, SpecificationException {
		TestTestRunner runner = new TestTestRunner(bpts.getParent(), bpts.getName());
		runner.testRun();
		assertTrue("Test suites should pass all tests", runner.getTestSuite().getStatus().isPassed());
		return runner.getTestSuite();
	}
}
