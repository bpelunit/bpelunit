package net.bpelunit.model.bpel;

import javax.xml.namespace.QName;

public interface IReply extends IActivity {

	String getPartnerLink();

	void setPartnerLink(String value);

	QName getPortType();

	void setPortType(QName value);

	String getOperation();

	void setOperation(String value);

	String getVariable();

	void setVariable(String value);

	QName getFaultName();

	void setFaultName(QName value);

	void setVariable(IVariable value);

}
