package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IOnMessage;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;

public class OnMessage extends AbstractSingleContainer<TOnMessage> implements IOnMessage {

	public OnMessage(TOnMessage e, IContainer parent) {
		super(e, parent);
	}
	
	@Override
	public String getPartnerLink() {
		return this.getNativeActivity().getPartnerLink();
	}

	@Override
	public void setPartnerLink(String name) {
		this.getNativeActivity().setPartnerLink(name);
	}

	@Override
	public QName getPortType() {
		return this.getNativeActivity().getPortType();
	}

	@Override
	public void setPortType(QName portType) {
		this.getNativeActivity().setPortType(portType);
	}

	@Override
	public String getOperation() {
		return this.getNativeActivity().getOperation();
	}

	@Override
	public void setOperation(String operationName) {
		this.getNativeActivity().setOperation(operationName);
	}

	@Override
	public String getMessageExchange() {
		return this.getNativeActivity().getMessageExchange();
	}

	@Override
	public void setMessageExchange(String msgExName) {
		this.getNativeActivity().setMessageExchange(msgExName);
	}

	@Override
	public String getVariable() {
		return this.getNativeActivity().getVariable();
	}

	@Override
	public void setVariable(String variableName) {
		this.getNativeActivity().setVariable(variableName);
	}
	
	@Override
	public String toString() {
		return "OnMessage";
	}
}
