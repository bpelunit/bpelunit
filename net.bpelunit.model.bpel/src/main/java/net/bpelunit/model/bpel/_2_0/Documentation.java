package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.util.XMLUtil;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Documentation implements IDocumentation {

	private TDocumentation documentation;

	public Documentation(TDocumentation wrappedDocumentation) {
		this.documentation = wrappedDocumentation;
	}

	public List<Node> getDocumentationElements() {
		List<Node> result = new ArrayList<Node>();
		NodeList children = documentation.getDomNode().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() != Node.ATTRIBUTE_NODE) {
				result.add(n);
			}
		}

		return result;
	}

	@Override
	public String getStringContent() {
		Node firstChild = documentation.getDomNode().getFirstChild();
		
		if(firstChild != null && firstChild.getNodeType() == Node.TEXT_NODE) {
			return firstChild.getNodeValue();
		} else {
			return null;
		}
		
	}
	
	public Element addDocumentationElement(String namespaceURI, String qualifiedName) {
		Node docNode = documentation.getDomNode();
		Element e = docNode.getOwnerDocument().createElementNS(namespaceURI, qualifiedName);
		
		docNode.appendChild(e);
		return e;
	}

	@Override
	public void setStringContent(String content) {
		Node domNode = documentation.getDomNode();
		
		XMLUtil.removeAllSubNodesExceptAttributes(domNode);
		Text textNode = domNode.getOwnerDocument().createTextNode(content);
		domNode.appendChild(textNode);
	}
}
