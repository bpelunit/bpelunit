/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public final class XMLUtil {

	private XMLUtil() {
		// utility class
	}

	/**
	 * @param xmlAsString
	 *            document in string form, encoding specified in XML should be
	 *            UTF-8
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParserConfigurationException
	 */
	public static Document parseXML(String xmlAsString) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(new ByteArrayInputStream(xmlAsString
				.getBytes("UTF-8")));
	}

	public static Document parseXML(InputStream in) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(in);
	}

	public static void writeXML(Node xml, File file) throws IOException,
			TransformerException {
		writeXML(xml, new FileOutputStream(file));
	}

	public static void writeXML(Node xml, OutputStream outputStream)
			throws TransformerException {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.transform(new DOMSource(xml), new StreamResult(outputStream));
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

	}

	public static void removeNodes(Element parent, NodeList elements) {
		// Copy elements in case of NodeList is a live implementation
		// that would shrink while deleting elments
		List<Node> nodesToRemove = new ArrayList<Node>();

		for (int i = 0; i < elements.getLength(); i++) {
			Node item = elements.item(i);

			if (item.getParentNode() == parent) {
				nodesToRemove.add(item);
			}
		}

		for (Node n : nodesToRemove) {
			parent.removeChild(n);
		}
	}

	public static List<Element> getChildElementsByName(Element element,
			String localName) {
		List<Element> elements = new ArrayList<Element>();

		for (Element e : getChildElements(element)) {
			// DOM Level 1 API (document.createElement) creates elements
			// that have an empty localname
			if (e.getLocalName() == null && localName.equals(e.getNodeName())) {
				elements.add(e);
			} else if (localName.equals(e.getLocalName())) {
				elements.add(e);
			}
		}

		return elements;
	}

	public static List<Element> getChildElements(Element element) {
		NodeList childNodes = element.getChildNodes();

		List<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node n = childNodes.item(i);
			if (n instanceof Element) {
				elements.add((Element) n);
			}
		}
		return elements;
	}

	public static void addAsFirstChild(Element element, Element newChild) {
		if (element.hasChildNodes()) {
			element.insertBefore(newChild, element.getChildNodes().item(0));
		} else {
			element.appendChild(newChild);
		}
	}

	public static QName getQName(Node n) {
		if (n.getLocalName() != null) {
			return new QName(n.getNamespaceURI(), n.getLocalName());
		} else {
			return new QName(n.getNamespaceURI(), n.getNodeName());
		}
	}

	public static void removeAllSubNodesExceptAttributes(Node n) {
		NodeList childNodes = n.getChildNodes();
		List<Node> childNodesToRemove = new ArrayList<Node>();

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node c = childNodes.item(i);

			if (c.getNodeType() != Node.ATTRIBUTE_NODE) {
				childNodesToRemove.add(c);
			}
		}

		for (Node c : childNodesToRemove) {
			n.removeChild(c);
		}
	}

	public static Document createDocument() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not create XML document", e);
		}
	}

	/**
	 * 
	 * @return one large string with the contents of all TextNodes, or null if
	 *         there are non text nodes or no text nodes as children.
	 */
	public static String getContentsOfTextOnlyNode(Node n) {
		NodeList children = n.getChildNodes();
		if (children.getLength() == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.TEXT_NODE) {
				sb.append(node.getNodeValue());
			} else {
				return null;
			}
		}

		return sb.toString();
	}

	public static String getXPathForElement(Node e, NamespaceContext ctx) {
		StringBuffer sb = new StringBuffer();
		List<Node> path = new ArrayList<Node>();

		Node currentNode = e;
		while (currentNode.getParentNode() != currentNode.getOwnerDocument()) {
			path.add(0, currentNode);
			if (currentNode instanceof Attr) {
				Attr a = (Attr) currentNode;
				currentNode = a.getOwnerElement();
			} else {
				currentNode = currentNode.getParentNode();
			}
		}
		path.add(0, currentNode); // We need the root element

		for (Node n : path) {
			sb.append("/");

			if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
				sb.append("@");
			}

			String namespaceURI = n.getNamespaceURI();
			if (namespaceURI != null && !namespaceURI.equals("")) {
				sb.append(ctx.getPrefix(namespaceURI)).append(":");
			}
			sb.append(n.getLocalName());

			if (n.getNodeType() == Node.ELEMENT_NODE) {
				appendElementQualifier(sb, (Element)n);
			}
		}

		return sb.toString();
	}

	/**
	 * Append XPath [expr] like /a/b[1] or /a/b[@name='x']
	 * @param sb
	 * @param n
	 */
	private static void appendElementQualifier(StringBuffer sb, Element n) {
		if(n.getParentNode() == n.getOwnerDocument()) {
			return;
		}
		
		if (n.getAttributes() != null
				&& n.getAttributes().getNamedItem("name") != null) {
			sb.append("[@name='")
					.append(n.getAttributes().getNamedItem("name")
							.getNodeValue()).append("']");
		} else if(getChildrenCount((Element)n.getParentNode(), n.getNamespaceURI(), n.getLocalName()) != 1) {
			sb.append("[").append(getPosition(n)).append("]");
		}
	}

	private static int getChildrenCount(Element parentNode,
			String namespaceURI, String localName) {
		int counter = 0;
		
		List<Element> children = getChildElements(parentNode);
		for(Element c: children) {
			if(namespaceURI.equals(c.getNamespaceURI()) && localName.equals(c.getLocalName())) {
				counter++;
			}
		}
		
		return counter;
	}

	/**
	 * Corresponds to the pos() xpath function
	 * 
	 * @param e
	 * @return pos(), also the start index is 1 not 0
	 */
	public static int getPosition(Element e) {
		Element parent = (Element)e.getParentNode();
		List<Element> children = getChildElements(parent);
		return children.indexOf(e) + 1;
	}

	/**
	 * DOM Level 2 compliant method for adding text to an element
	 * 
	 * @param e
	 *            element to which text should be added
	 * @param contents
	 *            new text to be added
	 * @return
	 */
	public static Text appendTextNode(Element e, String contents) {
		Text textNode = e.getOwnerDocument().createTextNode(contents);
		e.appendChild(textNode);
		return textNode;
	}

	public static String getTextContent(Node e) {
		StringBuilder sb = new StringBuilder();

		NodeList children = e.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node n = children.item(i);
				if (n.getNodeType() == Node.TEXT_NODE) {
					sb.append(n.getNodeValue());
				}
			}
		}

		return sb.toString();
	}

	public static QName resolveQName(String qNameWithPrefix, Element element) {
		String nsPrefix;
		String localName;
		if(qNameWithPrefix.contains(":")) {
			String[] parts = qNameWithPrefix.split(":");
			nsPrefix = parts[0];
			localName = parts[1];
		} else {
			nsPrefix = "";
			localName = qNameWithPrefix;
		}
		
		return new QName(resolveNamespacePrefix(nsPrefix, element), localName);
	}
	
	public static String resolveNamespacePrefix(String prefix, Element element) {
		String namespace = null;
		
		if("".equals(prefix)) {
			namespace = element.getAttribute("xmlns");
		} else {
			namespace = element.getAttribute("xmlns:" + prefix);
		}
		if(namespace != null && !"".equals(namespace)) {
			return namespace;
		}
		
		if(element.getParentNode() instanceof Element) {
			return resolveNamespacePrefix(prefix, (Element)element.getParentNode());
		} else {
			throw new RuntimeException("Cannot resolve prefix " + prefix);
		}
	}
}
