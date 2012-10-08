package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.ITo;
import net.bpelunit.model.bpel.IVariable;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TQuery;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class To implements ITo {

	private TTo to;

	public String getExpressionLanguage() {
		return to.getExpressionLanguage();
	}

	public void setExpressionLanguage(String value) {
		to.setExpressionLanguage(value);
	}

	public String getVariable() {
		return to.getVariable();
	}

	public void setVariable(String value) {
		to.setVariable(value);
	}
	
	public void setVariable(IVariable v) {
		this.setVariable(v.getName());
	}

	public String getPart() {
		return to.getPart();
	}

	public void setPart(String value) {
		to.setPart(value);
	}

	public void setProperty(QName value) {
		to.setProperty(value);
	}

	public String getPartnerLink() {
		return to.getPartnerLink();
	}

	public void setPartnerLink(String value) {
		to.setPartnerLink(value);
	}

	public To(TTo wrappedTo) {
		this.to = wrappedTo;
	}

	TTo getNativeTo() {
		return to;
	}

	public void setExpression(String expr) {
		if(!to.isSetQuery()) {
			to.addNewQuery();
		}
		
		TQuery tq = to.getQuery();
		Document doc = tq.getDomNode().getOwnerDocument();
		Text exprNode = doc.createTextNode(expr);
		tq.getDomNode().appendChild(exprNode);
	}
}
