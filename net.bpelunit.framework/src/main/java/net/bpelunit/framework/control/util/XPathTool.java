package net.bpelunit.framework.control.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Simple class which helps the user easily make XPath 1.0 queries from
 * a Velocity template. It's much more lightweight than using the selectPath
 * method from XMLBeans, as that'd need the Saxon 9.1-B jars, which are over
 * 5MB in size.
 * 
 * @author Antonio García-Domínguez
 * @version 1.1
 */
public class XPathTool {
	private XPath fXPath;

	public XPathTool(NamespaceContext nsContext) {
		fXPath = XPathFactory.newInstance().newXPath();
		fXPath.setNamespaceContext(nsContext);
	}

	public List<Node> evaluateAsList(String query, Object item) throws XPathExpressionException {
		NodeList nodes = (NodeList)fXPath.evaluate(query, item, XPathConstants.NODESET);
		List<Node> list = new ArrayList<Node>();
		for (int i = 0; i < nodes.getLength(); ++i) {
			list.add(nodes.item(i));
		}
		return list;
	}

	public String evaluateAsString(String query, Object item) throws XPathExpressionException {
		return fXPath.evaluate(query, item);
	}
}
