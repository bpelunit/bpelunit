package net.bpelunit.bpel;

import javax.xml.namespace.QName;

public interface IReceive extends IActivity, ICreateInstance {

	String getPartnerLink();

	void setPartnerLink(String value);

	QName getPortType();

	void setPortType(QName value);

	String getOperation();

	void setOperation(String value);

	String getVariable();

	void setVariable(String value);

	void setVariable(IVariable v);

}
