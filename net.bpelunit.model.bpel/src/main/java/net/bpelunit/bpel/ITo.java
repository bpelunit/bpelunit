package net.bpelunit.bpel;

import java.util.List;

import javax.xml.namespace.QName;

public interface ITo {

	void setPartnerLink(String value);

	String getPartnerLink();

	void setProperty(QName value);

	void setPart(String value);

	String getPart();

	void setVariable(String value);

	String getVariable();

	void setExpressionLanguage(String value);

	String getExpressionLanguage();

	void setContent(List<Object> content);

	List<Object> getContent();

	void setVariable(IVariable response);

	void setExpression(String string);

}
