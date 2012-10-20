package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IQuery;
import net.bpelunit.util.XMLUtil;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TQuery;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Query implements IQuery {

	private TQuery query;

	public Query(TQuery nativeQuery) {
		this.query = nativeQuery;
	}
	
	@Override
	public String getQueryLanguage() {
		return query.getQueryLanguage();
	}

	@Override
	public void setQueryLanguage(String queryLanguageUrn) {
		query.setQueryLanguage(queryLanguageUrn);
	}

	@Override
	public String getQueryContents() {
		return XMLUtil.getContentsOfTextOnlyNode(query.getDomNode());
	}

	@Override
	public void setQueryContents(String q) {
		Node domNode = query.getDomNode();
		
		XMLUtil.removeAllSubNodesExceptAttributes(domNode);
		
		Text textNode = domNode.getOwnerDocument().createTextNode(q);
		domNode.appendChild(textNode);
	}

	@Override
	public List<Node> getNodes() {
		List<Node> result = new ArrayList<Node>();
		
		NodeList children = query.getDomNode().getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			result.add(children.item(i));
		}
		
		return Collections.unmodifiableList(result);
	}

	@Override
	public Element addNewElement(String namespaceUri, String localName) {
		Node domNode = query.getDomNode();
		Element element = domNode.getOwnerDocument().createElementNS(namespaceUri, localName);
		domNode.appendChild(element);
		
		return element;
	}

	@Override
	public Text addNewTextNode(String contents) {
		Node domNode = query.getDomNode();
		Text element = domNode.getOwnerDocument().createTextNode(contents);
		domNode.appendChild(element);
		
		return element;
	}

}
