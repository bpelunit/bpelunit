package net.bpelunit.bpel._2_0;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.bpel.ITo;
import net.bpelunit.bpel.IVariable;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TQuery;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TTo;

public class To implements ITo {

	private TTo to;

	@Override
	public List<Object> getContent() {
		return Collections.unmodifiableList(to.getContent());
	}
	
	@Override
	public void setContent(List<Object> content) {
		to.getContent().clear();
		to.getContent().addAll(content);
	}

	@Override
	public String getExpressionLanguage() {
		return to.getExpressionLanguage();
	}

	@Override
	public void setExpressionLanguage(String value) {
		to.setExpressionLanguage(value);
	}

	@Override
	public String getVariable() {
		return to.getVariable();
	}

	@Override
	public void setVariable(String value) {
		to.setVariable(value);
	}
	
	@Override
	public void setVariable(IVariable v) {
		this.setVariable(v.getName());
	}

	@Override
	public String getPart() {
		return to.getPart();
	}

	@Override
	public void setPart(String value) {
		to.setPart(value);
	}

	@Override
	public void setProperty(QName value) {
		to.setProperty(value);
	}

	@Override
	public String getPartnerLink() {
		return to.getPartnerLink();
	}

	@Override
	public void setPartnerLink(String value) {
		to.setPartnerLink(value);
	}

	public To(TTo wrappedTo) {
		this.to = wrappedTo;
	}

	TTo getNativeTo() {
		return to;
	}

	@Override
	public void setExpression(String expr) {
		TQuery tq = new TQuery();
		tq.getContent().add(expr);
		
		this.to.getContent().clear();
		this.to.getContent().add(tq);
	}
}
