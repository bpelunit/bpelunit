package net.bpelunit.model.bpel;

import javax.xml.namespace.QName;

public interface IVariable extends IBpelObject {

	String getName();

	void setName(String value);

	QName getMessageType();

	void setMessageType(QName value);

	QName getType();

	void setType(QName value);

	QName getElement();

	void setElement(QName value);

}
