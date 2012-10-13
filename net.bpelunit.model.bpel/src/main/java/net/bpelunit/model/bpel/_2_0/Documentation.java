package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.IDocumentation;

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

	public void setDocumentationElement(Node doc) {
		Node docNode = documentation.getDomNode();
//		XMLUtil.removeAllSubNodesExceptAttributes(docNode);

		appendNode(doc, docNode);
	}

	private void appendNode(Object doc, Node docNode) {
		if (doc instanceof Node) {
			docNode.appendChild((Node) doc);
		} else {
			Text textNode = docNode.getOwnerDocument().createTextNode(
					doc.toString());
			docNode.appendChild(textNode);
		}
	}

	public void setDocumentationElements(Element e) {
		Node docNode = documentation.getDomNode();

//		XMLUtil.removeAllSubNodesExceptAttributes(docNode);

//		if (e != null) {
//			for (Object doc : e) {
//				appendNode(doc, docNode);
//			}
//		}
	}
}
