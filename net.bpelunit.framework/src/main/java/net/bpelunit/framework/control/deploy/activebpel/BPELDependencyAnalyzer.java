/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.deploy.activebpel;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.model.bpel.BPELConstants;

/**
 * Extracts the files that a BPEL process depends upon.
 */
public class BPELDependencyAnalyzer {

	private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	static {
		dbf.setNamespaceAware(true);
	}

	private File fBPEL;
	private Set<File> dependencies;
	private Document docBPEL;

	public BPELDependencyAnalyzer(File fBPEL) throws SAXException, IOException, ParserConfigurationException {
		this.fBPEL = fBPEL;
		docBPEL = dbf.newDocumentBuilder().parse(fBPEL);
	}

	private void addDependencies(File baseFile, Document document,
			Set<File> deps) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		final NodeList importedPaths = (NodeList) evaluateExpression(
				document,
				"//bpel:import/@location | //xsd:import/@schemaLocation | //wsdl:import/@location",
				XPathConstants.NODESET);

		for (int iPath = 0; iPath < importedPaths.getLength(); ++iPath) {
			final Attr importAttr = (Attr) importedPaths.item(iPath);
			final String importedPath = importAttr.getValue();

			File importedFile = new File(importedPath);
			if (!importedFile.isAbsolute()) {
				importedFile = new File(baseFile.getParentFile(), importedPath);
			}
			importedFile = importedFile.getCanonicalFile();
			if (!deps.add(importedFile)) {
				// Skip files that have been visited before
				continue;
			}
	
			final Document importedDoc = dbf.newDocumentBuilder().parse(importedFile);
			addDependencies(importedFile, importedDoc, deps);
		}
	}

	private Object evaluateExpression(Node context, String query, QName returnType) throws XPathExpressionException {
		final XPath xpath = XPathFactory.newInstance().newXPath();
		final NamespaceContextImpl nsContext = new NamespaceContextImpl();
		nsContext.setNamespace(BPELConstants.BPEL2_NS_PREFIX, BPELConstants.BPEL2_NAMESPACE);
		nsContext.setNamespace(BPELConstants.WSDL_NS_PREFIX, BPELConstants.WSDL_NAMESPACE);
		nsContext.setNamespace("xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI);
		xpath.setNamespaceContext(nsContext);
		return xpath.evaluate(query, context, returnType);
	}

	/**
	 * Returns an unmodifiable set with all the files required to run this
	 * composition, excluding the <code>.bpel</code> file itself. The files are
	 * returned in canonical form. Results of the first call are cached for
	 * subsequent calls.
	 * 
	 * This function requires parsing the imported dependencies as XML, but it
	 * does not parse the resulting DOM trees as proper WSDL or XML Schema
	 * documents. Instead, it simply runs a few XPath queries on them.
	 *
	 * Dependencies on external XSLT stylesheets through 'bpel:doXslTransform'
	 * in a &lt;from&gt; XPath expression are also detected, so long as the
	 * first argument of the 'bpel:doXslTransform' call is a string literal
	 * of the form "project:/file.xsl". Any other form is not supported.
	 */
	protected synchronized Set<File> getDependencies()
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		if (dependencies == null) {
			dependencies = new HashSet<File>();
			addDependencies(fBPEL, docBPEL, dependencies);
		}
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * Returns all the dependencies which belong to the namespace URI
	 * <code>nsURI</code>.
	 * 
	 * @param nsURI
	 *            Namespace URI to which the dependencies should belong.
	 * @return Set of files which belong to the namespace URI <code>nsURI</code>.
	 */
	public Set<File> getDependenciesWithURI(final String nsURI)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		final Set<File> wsdlDependencies = new HashSet<File>();
		for (File f : getDependencies()) {
			final Document doc = dbf.newDocumentBuilder().parse(f);
			final String docNSURI = doc.getDocumentElement().getNamespaceURI();
			if (nsURI.equals(docNSURI)) {
				wsdlDependencies.add(f);
			}
		}
		return wsdlDependencies;
	}

	/**
	 * Returns the WSDL files among all the dependencies of this BPEL composition.
	 * @see #getDependenciesWithURI(String)
	 */
	public Set<File> getWSDLDependencies() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		return getDependenciesWithURI(BPELConstants.WSDL_NAMESPACE);
	}

	/**
	 * Returns the XML Schema files among all the dependencies of this BPEL composition.
	 * @see #getDependenciesWithURI(String)
	 */
	public Set<File> getXSDDependencies() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		return getDependenciesWithURI(BPELConstants.XSD_NAMESPACE);
	}

	/**
	 * Returns the XSLT files among all the dependencies of this BPEL composition.
	 * @see #getDependenciesWithURI(String)
	 */
	public Set<File> getXSLTDependencies() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		return getDependenciesWithURI(BPELConstants.XSLT_NAMESPACE);
	}
	
}
