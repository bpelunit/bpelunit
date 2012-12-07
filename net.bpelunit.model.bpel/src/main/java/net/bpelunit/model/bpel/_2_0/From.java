package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IFrom;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.util.XMLUtil;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TLiteral;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TQuery;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRoles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class From implements IFrom {

	private static final String XMLNS = "xmlns:";
	private TFrom from;
	private Query query;
	protected List<Documentation> documentations = new ArrayList<Documentation>();

	From(TFrom wrappedFrom) {
		this.from = wrappedFrom;
	}

	public List<Documentation> getDocumentation() {
		return Collections.unmodifiableList(documentations);
	}

	public Documentation addDocumentation() {

		TDocumentation bpelDoc = from.addNewDocumentation();

		Documentation bpelDocumentation = new Documentation(bpelDoc);

		this.documentations.add(bpelDocumentation);

		return bpelDocumentation;
	}

	public Roles getEndpointReference() {
		if (from.getEndpointReference() == TRoles.MY_ROLE) {
			return Roles.MY_ROLE;
		} else if (from.getEndpointReference() == TRoles.PARTNER_ROLE) {
			return Roles.PARTNER_ROLE;
		} else {
			return null;
		}
	}

	public String getExpressionLanguage() {
		return from.getExpressionLanguage();
	}

	TFrom getNativeFrom() {
		return from;
	}

	public String getPart() {
		return from.getPart();
	}

	public String getPartnerLink() {
		return from.getPartnerLink();
	}

	public String getVariable() {
		return from.getVariable();
	}

	public void setEndpointReference(Roles value) {
		if (value == Roles.MY_ROLE) {
			from.setEndpointReference(TRoles.MY_ROLE);
		} else if (value == Roles.PARTNER_ROLE) {
			from.setEndpointReference(TRoles.PARTNER_ROLE);
		} else {
			from.setEndpointReference(null);
		}
	}

	public void setQuery(String expr) {
		unsetQueryAndLiteral();
		TQuery tq = from.addNewQuery();
		Document doc = tq.getDomNode().getOwnerDocument();
		Text exprNode = doc.createTextNode(expr);
		tq.getDomNode().appendChild(exprNode);
	}

	private void unsetQueryAndLiteral() {
		if (from.getLiteral() != null) {
			from.unsetLiteral();
		}
		if (from.getQuery() != null) {
			from.unsetQuery();
			query = null;
		}
	}

	public void setExpressionLanguage(String value) {
		from.setExpressionLanguage(value);
	}

	public Element setNewLiteral(String namespaceUri, String localName) {
		unsetQueryAndLiteral();

		TLiteral literal = from.addNewLiteral();
		Node domNode = literal.getDomNode();
		Element e = domNode.getOwnerDocument().createElementNS(namespaceUri,
				localName);
		domNode.appendChild(e);

		return e;
	}

	@Override
	public Element getLiteral() {
		if (from.getLiteral() != null) {
			Node n = from.getLiteral().getDomNode();
			NodeList childNodes = n.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					return (Element) childNode;
				}
			}
		}

		return null;
	}

	public void setPart(String value) {
		from.setPart(value);
	}

	public void setPartnerLink(String value) {
		from.setPartnerLink(value);
	}

	public void setVariable(IVariable v) {
		this.setVariable(v.getName());
	}

	public void setVariable(String value) {
		from.setVariable(value);
	}

	@Override
	public QName getProperty() {
		return from.getProperty();
	}

	@Override
	public void setProperty(QName qname) {
		from.setProperty(qname);
	}

	@Override
	public Query setNewQuery() {
		if (from.getLiteral() != null) {
			from.unsetLiteral();
		}
		if (from.getQuery() != null) {
			from.unsetQuery();
		}

		query = new Query(from.addNewQuery());

		return query;
	}

	@Override
	public Query getQuery() {
		return query;
	}

	@Override
	public String getXPathInDocument() {
		NamespaceContext ctx = BpelFactory.INSTANCE.createNamespaceContext();
		return XMLUtil.getXPathForElement((Element) from.getDomNode(), ctx);
	}

	@Override
	public void addGlobalNamespace(String prefix, String namespaceUri) {
		Document ownerDocument = from.getDomNode().getOwnerDocument();

		ownerDocument.getDocumentElement().setAttribute(XMLNS + prefix,
				namespaceUri);
	}

	@Override
	public void addLocalNamespace(String prefix, String namespaceUri) {
		Element domNode = (Element) from.getDomNode();

		domNode.setAttribute(XMLNS + prefix, namespaceUri);
	}

	@Override
	public String getNamespacePrefix(String namespaceUri) {
		Node n = from.getDomNode();

		while (n != null && n != n.getOwnerDocument()) {
			NamedNodeMap attributes = n.getAttributes();
			if (attributes != null) {
				for (int i = 0; i < attributes.getLength(); i++) {
					Node a = attributes.item(i);
					String attributeName = a.getNodeName();
					if (attributeName.startsWith(XMLNS)
							&& namespaceUri.equals(a.getNodeValue())) {
						return attributeName.substring(XMLNS.length());
					}
				}
			}
			n = n.getParentNode();
		}

		return null;
	}
}
