package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IReceive;
import net.bpelunit.model.bpel.IVariable;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;

public class Receive extends AbstractBasicActivity<TReceive> implements IReceive {

	public Receive(TReceive r, IContainer parent) {
		super(r, parent);
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

	public boolean isCreateInstance() {
		return TBoolean.YES.equals(getNativeActivity().getCreateInstance());
	}

	public void setCreateInstance(boolean b) {
		if (b) {
			getNativeActivity().setCreateInstance(TBoolean.YES);
		} else {
			getNativeActivity().setCreateInstance(TBoolean.NO);
		}
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

	public void setVariable(IVariable v) {
		getNativeActivity().setVariable(v.getName());
	}

	public void setVariable(String value) {
		getNativeActivity().setVariable(value);
	}
}
