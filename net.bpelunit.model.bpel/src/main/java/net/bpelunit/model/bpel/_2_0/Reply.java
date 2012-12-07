package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IReply;
import net.bpelunit.model.bpel.IVariable;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;

public class Reply extends AbstractBasicActivity<TReply> implements IReply {

	public Reply(TReply wrappedReply, IContainer parent) {
		super(wrappedReply, parent);
	}

	public QName getFaultName() {
		return getNativeActivity().getFaultName();
	}

	public String getOperation() {
		return getNativeActivity().getOperation();
	}

	public String getPartnerLink() {
		return getNativeActivity().getPartnerLink();
	}

	public QName getPortType() {
		return getNativeActivity().getPortType();
	}

	public String getVariable() {
		return getNativeActivity().getVariable();
	}

	public void setFaultName(QName value) {
		getNativeActivity().setFaultName(value);
	}
	
	public void setOperation(String value) {
		getNativeActivity().setOperation(value);
	}

	public void setPartnerLink(String value) {
		getNativeActivity().setPartnerLink(value);
	}

	public void setPortType(QName value) {
		getNativeActivity().setPortType(value);
	}

	public void setVariable(IVariable value) {
		getNativeActivity().setVariable(value.getName());
	}

	public void setVariable(String value) {
		getNativeActivity().setVariable(value);
	}
}
