package net.bpelunit.bpel._2_0;

import java.util.Collections;
import java.util.List;

import net.bpelunit.bpel.IFrom;
import net.bpelunit.bpel.IVariable;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TFrom;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TQuery;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TRoles;

public class From implements IFrom {

	private TFrom from;

	@Override
	public List<Object> getContent() {
		return Collections.unmodifiableList(from.getContent());
	}

	@Override
	public void setContent(List<Object> content) {
		from.getContent().clear();
		if (content != null) {
			from.getContent().addAll(content);
		}
	}

	@Override
	public String getExpressionLanguage() {
		return from.getExpressionLanguage();
	}

	@Override
	public void setExpressionLanguage(String value) {
		from.setExpressionLanguage(value);
	}

	@Override
	public String getVariable() {
		return from.getVariable();
	}

	@Override
	public void setVariable(String value) {
		from.setVariable(value);
	}

	@Override
	public String getPart() {
		return from.getPart();
	}

	@Override
	public void setPart(String value) {
		from.setPart(value);
	}

	@Override
	public String getPartnerLink() {
		return from.getPartnerLink();
	}

	@Override
	public void setPartnerLink(String value) {
		from.setPartnerLink(value);
	}

	@Override
	public Roles getEndpointReference() {
		if (from.getEndpointReference() == TRoles.MY_ROLE) {
			return Roles.MY_ROLE;
		} else if (from.getEndpointReference() == TRoles.PARTNER_ROLE) {
			return Roles.PARTNER_ROLE;
		} else {
			return null;
		}
	}

	@Override
	public void setEndpointReference(Roles value) {
		if (value == Roles.MY_ROLE) {
			from.setEndpointReference(TRoles.MY_ROLE);
		} else if (value == Roles.PARTNER_ROLE) {
			from.setEndpointReference(TRoles.PARTNER_ROLE);
		} else {
			from.setEndpointReference(null);
		}
	}

	From(TFrom wrappedFrom) {
		this.from = wrappedFrom;
	}

	TFrom getNativeFrom() {
		return from;
	}

	@Override
	public void setVariable(IVariable v) {
		this.setVariable(v.getName());
	}

	@Override
	public void setExpression(String expr) {
		TQuery tq = new TQuery();
		tq.getContent().add(expr);
		
		this.from.getContent().clear();
		this.from.getContent().add(tq);
	}
}
