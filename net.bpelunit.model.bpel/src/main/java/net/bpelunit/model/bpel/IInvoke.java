package net.bpelunit.model.bpel;

import javax.xml.namespace.QName;

public interface IInvoke extends IActivity, ICompensationHandlerContainer, ICatchContainer {

	String getPartnerLink();

	void setPartnerLink(String value);

	QName getPortType();

	void setPortType(QName value);

	String getOperation();

	void setOperation(String value);

	String getInputVariable();

	void setInputVariable(String value);

	void setInputVariable(IVariable v);

	String getOutputVariable();

	void setOutputVariable(String value);

	void setOutputVariable(IVariable v);

}
