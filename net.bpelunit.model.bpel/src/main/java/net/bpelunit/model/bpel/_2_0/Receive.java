package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IReceive;
import net.bpelunit.model.bpel.IVariable;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TBoolean;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TReceive;

public class Receive extends AbstractBasicActivity<TReceive> implements IReceive {

	public Receive(TReceive r, BpelFactory f) {
		super(r, f, IReceive.class);
	}

	@Override
	public String getOperation() {
		return getNativeActivity().getOperation();
	}
	
	@Override
	public String getPartnerLink() {
		return getNativeActivity().getPartnerLink();
	}

	@Override
	public QName getPortType() {
		return getNativeActivity().getPortType();
	}

	@Override
	public String getVariable() {
		return getNativeActivity().getVariable();
	}

	@Override
	public boolean isCreateInstance() {
		return TBoolean.YES.equals(getNativeActivity().getCreateInstance());
	}

	@Override
	public void setCreateInstance(boolean b) {
		if (b) {
			getNativeActivity().setCreateInstance(TBoolean.YES);
		} else {
			getNativeActivity().setCreateInstance(TBoolean.NO);
		}
	}
	
	@Override
	public void setOperation(String value) {
		getNativeActivity().setOperation(value);
	}

	@Override
	public void setPartnerLink(String value) {
		getNativeActivity().setPartnerLink(value);
	}

	@Override
	public void setPortType(QName value) {
		getNativeActivity().setPortType(value);
	}

	@Override
	public void setVariable(IVariable v) {
		getNativeActivity().setVariable(v.getName());
	}

	@Override
	public void setVariable(String value) {
		getNativeActivity().setVariable(value);
	}
}
