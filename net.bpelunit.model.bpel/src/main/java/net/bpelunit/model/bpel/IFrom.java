package net.bpelunit.model.bpel;

import java.util.List;


public interface IFrom {

	public enum Roles {
		PARTNER_ROLE, MY_ROLE
	}

	void setEndpointReference(Roles value);

	Roles getEndpointReference();

	void setPartnerLink(String value);

	String getPartnerLink();

	void setPart(String value);

	String getPart();

	void setVariable(String value);

	String getVariable();

	void setExpressionLanguage(String value);

	String getExpressionLanguage();

	void setContent(List<Object> content);

	List<Object> getContent();

	void setVariable(IVariable v);

	void setExpression(String string);

	void setLiteral(Object content);
	
}
