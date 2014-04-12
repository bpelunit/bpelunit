/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLUtilTest {

	@Test
	public void testWriteXMLToStream() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
				"simple.xml"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		XMLUtil.writeXML(doc, out);

		ByteArrayOutputStream reference = new ByteArrayOutputStream();
		IOUtils.copy(getClass().getResourceAsStream("simple.xml"), reference);

		String referenceString = normalize(reference.toString());
		String actualString = normalize(out.toString());

		assertEquals(referenceString, actualString);
	}

	@Test
	public void testWriteXMLToFile() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
				"simple.xml"));
		File f = File.createTempFile("bpelunit", ".xml");

		try {
			XMLUtil.writeXML(doc, f);
			ByteArrayOutputStream reference = new ByteArrayOutputStream();
			IOUtils.copy(getClass().getResourceAsStream("simple.xml"),
					reference);

			byte[] actual = FileUtil.readFile(f);
			
			String referenceString = normalize(reference.toString("UTF-8"));
			String actualString = normalize(new String(actual));
			
			assertEquals(referenceString, actualString);

		} finally {
			f.delete();
		}
	}

	@Test
	public void testRemoveNodes() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"simple.xml"));
		
		Element root = doc.getDocumentElement();
		
		List<Element> children = XMLUtil.getChildElements(root);
		Element a = children.get(0);
		Element b = children.get(1);
		Element c = children.get(2);
		
		assertEquals(1, root.getElementsByTagName("a").getLength());
		assertEquals(1, root.getElementsByTagName("b").getLength());
		assertEquals(1, root.getElementsByTagName("c").getLength());
		
		XMLUtil.removeNodes(root, new NodeListMock(a, b));
		
		assertEquals(0, root.getElementsByTagName("a").getLength());
		assertEquals(0, root.getElementsByTagName("b").getLength());
		assertEquals(1, root.getElementsByTagName("c").getLength());
		
		XMLUtil.removeNodes(root, new NodeListMock(a, c));
		
		assertEquals(0, XMLUtil.getChildElements(root).size());
	}
	
	@Test
	public void testGetChildElements() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"simple.xml"));
		
		Element root = doc.getDocumentElement();
		
		List<Element> children = XMLUtil.getChildElements(root);
		assertEquals(3, children.size());
		assertEquals("a", children.get(0).getLocalName());
		assertEquals("b", children.get(1).getLocalName());
		assertEquals("c", children.get(2).getLocalName());
		
		assertEquals(0, XMLUtil.getChildElements(children.get(0)).size());
	}
	
	@Test
	public void testGetChildElementsByName() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"simple.xml"));
		
		Element root = doc.getDocumentElement();
		
		assertEquals(1, XMLUtil.getChildElementsByName(root, "a").size());
		assertEquals(0, XMLUtil.getChildElementsByName(root, "d").size());
	}
	
	@Test
	public void addAsFirstChildInNonEmptyElement() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"simple.xml"));
		
		Element root = doc.getDocumentElement();
		
		Element element = doc.createElement("d");
		XMLUtil.addAsFirstChild(root, element);
		
		assertEquals(4, XMLUtil.getChildElements(root).size());
		assertEquals(1, XMLUtil.getChildElementsByName(root, "d").size());
		assertSame(element, XMLUtil.getChildElements(root).get(0));
	}
	
	@Test
	public void addAsFirstChildInEmptyElement() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"simple.xml"));
		Element root = doc.getDocumentElement();
		
		XMLUtil.removeNodes(root, root.getChildNodes());
		
		assertEquals(0, XMLUtil.getChildElements(root).size());
		
		Element element = doc.createElement("d");
		XMLUtil.addAsFirstChild(root, element);
		
		assertEquals(1, XMLUtil.getChildElements(root).size());
		assertEquals(1, XMLUtil.getChildElementsByName(root, "d").size());
		assertSame(element, XMLUtil.getChildElements(root).get(0));
	}
	
	@Test
	public void testGetQName() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"simple.xml"));
		Element root = doc.getDocumentElement();
		
		assertEquals(new QName("", "simple"), XMLUtil.getQName(root));
		assertEquals(new QName("", "b"), XMLUtil.getQName(doc.createElement("b")));
		assertEquals(new QName("a", "b"), XMLUtil.getQName(doc.createElementNS("a", "b")));
	}

	@Test
	public void testRemoveAllSubNodesExceptAttributes() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream(
		"testRemoveAllSubNodesExceptAttributes.xml"));
		Element root = doc.getDocumentElement();
	
		NodeList childNodes = root.getChildNodes();
		assertEquals(5, childNodes.getLength());
		assertEquals("a", root.getAttribute("a"));
		assertEquals("b", root.getAttribute("b"));
		
		XMLUtil.removeAllSubNodesExceptAttributes(root);
		
		childNodes = root.getChildNodes();
		assertEquals(0, childNodes.getLength());
		assertEquals("a", root.getAttribute("a"));
		assertEquals("b", root.getAttribute("b"));
	}
	
	@Test
	public void testGetContentsOfTextOnlyNodeOnlyTextNodes() throws Exception {
		Document doc = XMLUtil.createDocument();
		
		Element e = doc.createElement("a");
		doc.appendChild(e);
		
		e.appendChild(doc.createTextNode("a"));
		e.appendChild(doc.createTextNode("b"));
		e.appendChild(doc.createTextNode("c"));
		
		assertEquals("abc", XMLUtil.getContentsOfTextOnlyNode(e));
	}
	
	@Test
	public void testGetContentsOfTextOnlyNodeNotOnlyTextNodes() throws Exception {
		Document doc = XMLUtil.createDocument();
		
		Element e = doc.createElement("a");
		doc.appendChild(e);
		
		e.appendChild(doc.createTextNode("a"));
		e.appendChild(doc.createElement("b"));
		e.appendChild(doc.createTextNode("c"));
		
		assertNull(XMLUtil.getContentsOfTextOnlyNode(e));
	}

	@Test
	public void testGetXPathForElement() throws Exception {
		Document xml = XMLUtil.parseXML(getClass().getResourceAsStream("GetXPathForElement.xml"));
		
		SimpleNamespaceContext ctx = new SimpleNamespaceContext();
		ctx.addNamespace("a", "a");
		ctx.addNamespace("b", "b");
		
		Element a = xml.getDocumentElement();
		Element b = (Element) XMLUtil.getChildElementsByName(a, "B").get(0);
		Element c = (Element) XMLUtil.getChildElementsByName(b, "C").get(0);
		Element d1 = (Element) XMLUtil.getChildElementsByName(c, "D").get(0);
		Element d2 = (Element) XMLUtil.getChildElementsByName(c, "D").get(1);
		Element d3 = (Element) XMLUtil.getChildElementsByName(b, "D").get(0);
		Attr id = c.getAttributeNode("id");
		
		assertEquals("/a:A/b:B/a:C", XMLUtil.getXPathForElement(c, ctx));
		assertEquals("/a:A/b:B/a:C/@id", XMLUtil.getXPathForElement(id, ctx));
		assertEquals("/a:A/b:B/a:C/a:D[@name='testName']", XMLUtil.getXPathForElement(d1, ctx));
		assertEquals("/a:A/b:B/a:C/a:D[2]", XMLUtil.getXPathForElement(d2, ctx));
		assertEquals("/a:A/b:B/a:D", XMLUtil.getXPathForElement(d3, ctx));
	}
	
	@Test
	public void testAddTextNode() {
		Document xml = XMLUtil.createDocument();
		Element element = xml.createElement("a");
		Text t = XMLUtil.appendTextNode(element, "abc");
		
		assertEquals("abc", element.getTextContent());
		assertEquals(1, element.getChildNodes().getLength());
		assertSame(t, element.getChildNodes().item(0));
	}
	
	@Test
	public void testGetTextContent() {
		Document xml = XMLUtil.createDocument();
		Element element = xml.createElement("a");
		
		XMLUtil.appendTextNode(element, "abc");
		assertEquals("abc", XMLUtil.getTextContent(element));
		
		XMLUtil.appendTextNode(element, "def");
		assertEquals("abcdef", XMLUtil.getTextContent(element));
	}

	@Test
	public void testGetPosition() throws Exception {
		Document xml = XMLUtil.parseXML(getClass().getResourceAsStream("Position.xml"));
		List<Element> children = XMLUtil.getChildElements(xml.getDocumentElement());
		assertEquals(4, children.size());
		assertEquals(1, XMLUtil.getPosition(children.get(0)));
		assertEquals(2, XMLUtil.getPosition(children.get(1)));
		assertEquals(3, XMLUtil.getPosition(children.get(2)));
		assertEquals(4, XMLUtil.getPosition(children.get(3)));
	}
	
	@Test
	public void testResolveNamespacePrefix_NormalPrefix() throws Exception {
		Document xml = XMLUtil.parseXML("<ns:a xmlns:ns=\"ns1\">ns:b</ns:a>");
		String namespace = XMLUtil.resolveNamespacePrefix("ns", xml.getDocumentElement());
		assertEquals("ns1", namespace);
	}
	
	@Test
	public void testResolveNamespacePrefix_NormalPrefixInHierarchy() throws Exception {
		Document xml = XMLUtil.parseXML("<ns:a xmlns:ns=\"ns1\"><ns:b>ns:b</ns:b></ns:a>");
		String namespace = XMLUtil.resolveNamespacePrefix("ns", (Element)xml.getDocumentElement().getChildNodes().item(0));
		assertEquals("ns1", namespace);
	}
	
	@Test
	public void testResolveNamespacePrefix_DefaultNamespace() throws Exception {
		Document xml = XMLUtil.parseXML("<a xmlns=\"ns1\">b</a>");
		String namespace = XMLUtil.resolveNamespacePrefix("", xml.getDocumentElement());
		assertEquals("ns1", namespace);
	}
	
	@Test
	public void testResolveQName_NormalPrefix() throws Exception {
		Document xml = XMLUtil.parseXML("<ns:a xmlns:ns=\"ns1\">ns:b</ns:a>");
		QName qname = XMLUtil.resolveQName("ns:a", xml.getDocumentElement());
		assertEquals("ns1", qname.getNamespaceURI());
		assertEquals("a", qname.getLocalPart());
	}
	
	@Test
	public void testResolveQName_DefaultNamespace() throws Exception {
		Document xml = XMLUtil.parseXML("<a xmlns=\"ns1\">b</a>");
		QName qname = XMLUtil.resolveQName("a", xml.getDocumentElement());
		assertEquals("ns1", qname.getNamespaceURI());
		assertEquals("a", qname.getLocalPart());
	}
	
	private static class NodeListMock implements NodeList {

		Node[] nodes;
		
		public NodeListMock(Node... n) {
			nodes = n;
		}
		
		@Override
		public int getLength() {
			return nodes.length;
		}

		@Override
		public Node item(int index) {
			return nodes[index];
		}
		
	}

	private String normalize(String s) {
		return s.trim().replaceAll("\r", "");
	}

}
