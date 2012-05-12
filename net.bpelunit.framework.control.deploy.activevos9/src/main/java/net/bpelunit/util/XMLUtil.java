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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

	public static void writeXML(Document xml, File file) throws IOException,
			TransformerException {
		writeXML(xml, new FileOutputStream(file));
	}

	public static void writeXML(Document xml, OutputStream outputStream)
			throws TransformerException {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.transform(new DOMSource(xml), new StreamResult(outputStream));
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

	}

	public static void removeNodes(Element parent,
			NodeList elements) {
		// Copy elements in case of NodeList is a live implementation
		// that would shrink while deleting elments
		List<Node> nodesToRemove = new ArrayList<Node>();
		
		for(int i = 0; i < elements.getLength(); i++) {
			Node item = elements.item(i);
			
			if(item.getParentNode() == parent) {
				nodesToRemove.add(item);
			}
		}
		
		for(Node n : nodesToRemove) {
			parent.removeChild(n);
		}
	}

	public static List<Element> getChildElementsByName(Element element, String localName) {
		List<Element> elements = new ArrayList<Element>();
		
		for(Element e : getChildElements(element)) {
			// DOM Level 1 API (document.createElement) creates elements
			// that have an empty localname
			if(e.getLocalName() == null && localName.equals(e.getNodeName())) {
				elements.add(e);
			} else if(localName.equals(e.getLocalName())) {
				elements.add(e);
			}
		}
		
		return elements;
	}

	public static List<Element> getChildElements(Element element) {
		NodeList childNodes = element.getChildNodes();
		
		List<Element> elements = new ArrayList<Element>();
		for(int i = 0; i < childNodes.getLength(); i++) {
			Node n = childNodes.item(i);
			if(n instanceof Element) {
				elements.add((Element)n);
			}
		}
		return elements;
	}

	public static void addAsFirstChild(Element element, Element newChild) {
		if(element.hasChildNodes()) {
			element.insertBefore(newChild, element.getChildNodes().item(0));
		} else {
			element.appendChild(newChild);
		}
	}

	public static QName getQName(Node n) {
		if(n.getLocalName() != null) {
			return new QName(n.getNamespaceURI(), n.getLocalName());
		} else {
			return new QName(n.getNamespaceURI(), n.getNodeName());
		}
	}
}
