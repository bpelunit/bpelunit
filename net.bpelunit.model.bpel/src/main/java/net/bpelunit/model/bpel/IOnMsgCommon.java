package net.bpelunit.model.bpel;

import javax.xml.namespace.QName;

public interface IOnMsgCommon {

	String getPartnerLink();

	void setPartnerLink(String name);

	QName getPortType();

	void setPortType(QName portType);

	String getOperation();

	void setOperation(String operationName);

	String getMessageExchange();

	void setMessageExchange(String msgExName);

	String getVariable();

	void setVariable(String variableName);

}
