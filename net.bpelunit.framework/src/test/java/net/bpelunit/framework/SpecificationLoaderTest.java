package net.bpelunit.framework;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class SpecificationLoaderTest {

	@Test
	public void test_getNamespaceMap_TopLevel() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		String xml = "<a xmlns:a=\"a\" xmlns:b=\"b\" xmlns=\"c\"><A xmlns:a=\"A\" /></a>";
		Document document = db.parse(new InputSource(new StringReader(xml)));
		
		Map<String, String> namespaceMap = SpecificationLoader.getNamespaceMap(document.getDocumentElement());
		assertEquals("a", namespaceMap.get("a"));
		assertEquals("b", namespaceMap.get("b"));
		assertEquals("c", namespaceMap.get(""));
	}
	
	@Test
	public void test_getNamespaceMap_NestedElement() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		String xml = "<a xmlns:a=\"a\" xmlns:b=\"b\" xmlns=\"c\"><A xmlns:a=\"A\" /></a>";
		Document document = db.parse(new InputSource(new StringReader(xml)));
		
		Map<String, String> namespaceMap = SpecificationLoader.getNamespaceMap(document.getDocumentElement().getFirstChild());
		assertEquals("A", namespaceMap.get("a"));
		assertEquals("b", namespaceMap.get("b"));
		assertEquals("c", namespaceMap.get(""));
	}

}
