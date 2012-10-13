package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.IFrom;
import net.bpelunit.model.bpel.IVariable;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TLiteral;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TQuery;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRoles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class From implements IFrom {

	private TFrom from;

	From(TFrom wrappedFrom) {
		this.from = wrappedFrom;
	}

	public List<Node> getContent() {
		NodeList children = from.getDomNode().getChildNodes();
		List<Node> result = new ArrayList<Node>();
		for(int i = 0; i < children.getLength(); i++) {
			result.add(children.item(i));
		}
		return result;
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

	public void setExpression(String expr) {
		if(from.getLiteral() != null) {
			from.unsetLiteral();
		}
		if(from.getQuery() != null) {
			from.unsetQuery();
		}
		TQuery tq = from.addNewQuery();
		Document doc = tq.getDomNode().getOwnerDocument();
		Text exprNode = doc.createTextNode(expr);
		tq.getDomNode().appendChild(exprNode);
	}

	public void setExpressionLanguage(String value) {
		from.setExpressionLanguage(value);
	}

	public void setLiteral(Element e) {
		if(from.getLiteral() != null) {
			from.unsetLiteral();
		}
		if(from.getQuery() != null) {
			from.unsetQuery();
		}
		
		TLiteral literal = from.addNewLiteral();
		literal.getDomNode().appendChild(e);
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
}
