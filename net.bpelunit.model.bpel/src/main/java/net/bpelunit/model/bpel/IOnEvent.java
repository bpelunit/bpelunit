package net.bpelunit.model.bpel;

import javax.xml.namespace.QName;

public interface IOnEvent extends IScopeOnlyContainer, IOnMsgCommon {

	QName getMessageType();
	void setMessageType(QName msgType);
	
	QName getElement();
	void setElement(QName element);
}
