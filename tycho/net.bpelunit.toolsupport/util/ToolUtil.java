/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlObject;
import net.bpelunit.framework.xml.suite.XMLAnyElement;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Some utility methods for the tool support.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ToolUtil {

	public static XmlObject parseSendBlock(XMLTestSuite suite, String sendXML) {
		if (!"".equals(sendXML)) {
			XmlObject any= null;
			try {
				Node rootNode= parseSendBlockWithException(suite, sendXML);
				return XMLAnyElement.Factory.parse(rootNode);
			} catch (Exception e) {
				// caught beforehand
			}
			return any;
		}
		return null;
	}

	public static Node parseSendBlockWithException(XMLTestSuite suite, String sendXML) throws ParserConfigurationException, SAXException, IOException {

		if ("".equals(sendXML))
			return null;

		Map<Object, Object> namespaceMap= new HashMap<Object, Object>();

		// Add dummy element.
		suite.newCursor().getAllNamespaces(namespaceMap);
		StringBuffer dummyElement= new StringBuffer();
		dummyElement.append("<dummy ");
		for (Object prefix : namespaceMap.keySet()) {
			dummyElement.append("xmlns:" + prefix + "=\"" + namespaceMap.get(prefix) + "\" ");
		}
		dummyElement.append(">");
		dummyElement.append(sendXML);
		dummyElement.append("</dummy>");

		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder= factory.newDocumentBuilder();
		Document document= builder.parse(new InputSource(new StringReader(dummyElement.toString())));

		// dummy node
		assert document.getFirstChild() != null;
		// root node
		assert document.getFirstChild().getFirstChild() != null;

		return document.getFirstChild().getFirstChild();
	}
}
