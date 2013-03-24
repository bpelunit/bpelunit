/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.SpecificationLoader;
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
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * 
 * Some Test Utilities.
 * 
 * @author Philip Mayer, Antonio García-Domínguez
 */
public class TestUtil {

	private static final class IgnorePrefixesDifferenceListener implements DifferenceListener {
		@Override
		public int differenceFound(Difference diff) {
			if (diff.getId() == DifferenceConstants.NAMESPACE_PREFIX_ID) {
				return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
			}
			return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
		}

		@Override
		public void skippedComparison(Node arg0, Node arg1) {
			// do nothing
		}
	}

	// This is an utility class with no non-static methods, so the constructor should be private
	private TestUtil() {}

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

	public static SOAPOperationCallIdentifier getCall(String path, String wsdl, String operationName, SOAPOperationDirectionIdentifier direction) throws SpecificationException {
		String abspath = FileUtils.toFile(TestUtil.class.getResource(path)).getAbsolutePath() + File.separator;
		
		Definition d = SpecificationLoader.loadWsdlDefinition(abspath, wsdl, "TEST");
		
		Partner p= new Partner("MyPartner", d, null, "");
		QName service= new QName("http://www.example.org/MyPartner/", "MyPartner");
		SOAPOperationCallIdentifier operation= p.getOperation(service, "MyPartnerSOAP", operationName, direction);
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
		assertEqualXML(xmlTextA, xmlTextB);
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

	public static void assertEqualXML(String expected, String obtained) throws Exception {
        XMLUnit.setNormalizeWhitespace(true);

        final Diff diff = new Diff(expected, obtained);
        diff.overrideDifferenceListener(new IgnorePrefixesDifferenceListener());
        final DetailedDiff myDiff = new DetailedDiff(diff);
        for (Object o : myDiff.getAllDifferences()) {
                System.err.println(o);
        }
        assertTrue(myDiff.getAllDifferences().isEmpty());
	}
}
