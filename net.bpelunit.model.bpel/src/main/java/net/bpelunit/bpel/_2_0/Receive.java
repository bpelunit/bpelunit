package net.bpelunit.bpel._2_0;

import javax.xml.namespace.QName;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TBoolean;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TReceive;

import net.bpelunit.bpel.IReceive;
import net.bpelunit.bpel.IVariable;
import net.bpelunit.bpel.IVisitor;

public class Receive extends AbstractActivity<TReceive> implements IReceive {

	@Override
	public String getPartnerLink() {
		return getNativeActivity().getPartnerLink();
	}

	@Override
	public void setPartnerLink(String value) {
		getNativeActivity().setPartnerLink(value);
	}
	
	@Override
	public QName getPortType() {
		return getNativeActivity().getPortType();
	}

	@Override
	public void setPortType(QName value) {
		getNativeActivity().setPortType(value);
	}

	@Override
	public String getOperation() {
		return getNativeActivity().getOperation();
	}

	@Override
	public void setOperation(String value) {
		getNativeActivity().setOperation(value);
	}

	@Override
	public String getVariable() {
		return getNativeActivity().getVariable();
	}

	@Override
	public void setVariable(String value) {
		getNativeActivity().setVariable(value);
	}
	
	@Override
	public void setVariable(IVariable v) {
		getNativeActivity().setVariable(v.getName());
	}

	public Receive(TReceive r) {
		super(r);
	}

	@Override
	public boolean isBasicActivity() {
		return true;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
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
	public boolean isCreateInstance() {
		return TBoolean.YES.equals(getNativeActivity().getCreateInstance());
	}
}
