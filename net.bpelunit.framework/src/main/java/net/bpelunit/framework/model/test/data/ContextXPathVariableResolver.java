/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.model.test.data;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathVariableResolver;

import org.apache.velocity.context.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * XPath variable resolver backed by a Velocity context. Converts ArrayLists
 * into NodeLists, by placing every element in the array into an XML <element>
 * element. Elements in nested ArrayLists are recursively placed into nested
 * XML <element> elements.
 * 
 * A requests for XPath variable $x will be converted into a request for the 'x'
 * member of the Velocity context. Converted ArrayLists will be cached to improve
 * performance.
 * 
 * @author Antonio García-Domínguez
 */
public class ContextXPathVariableResolver implements XPathVariableResolver {

	private final Context templateContext;
	private final Map<String, Object> conversionCache = new HashMap<String, Object>();

	public ContextXPathVariableResolver(Context templateContext) {
		this.templateContext = templateContext;
	}

	public Object resolveVariable(QName arg0) {
		final String varName = arg0.getLocalPart();
		final Object varValue = templateContext.get(varName);
		if (varValue instanceof Iterable<?>) {
			if (conversionCache.get(varName) != null) {
				return conversionCache.get(varName);
			}

			NodeList nl = convertIterableToTree(varValue);
			conversionCache.put(varName, nl);
			return nl;
		}
		return varValue;
	}

	private NodeList convertIterableToTree(final Object varValue) {
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			return null;
		}

		Element elem = doc.createElement("elements");
		doc.appendChild(elem);
		addElementsAsChildren((Iterable<?>)varValue, doc, elem);

		// Just returning elem.getChildNodes() won't work: count($x) would
		// return -1 in that case, for some reason.
		ArrayNodeList nl = new ArrayNodeList();
		NodeList children = elem.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			nl.add(children.item(i));
		}
		return nl;
	}

	private void addElementsAsChildren(Iterable<?> collection, Document doc, Element currentElem) {
		for (Object item : collection) {
			Element childElem = doc.createElement("element");
			currentElem.appendChild(childElem);
			if (item instanceof Iterable<?>) {
				addElementsAsChildren((Iterable<?>)item, doc, childElem);
			} else {
				childElem.setTextContent(item.toString());
			}
		}
	}

}