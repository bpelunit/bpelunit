package net.bpelunit.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.bpel.IReply;
import net.bpelunit.bpel.IVariable;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TReply;

public class Reply extends AbstractActivity<TReply> implements IReply {

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
	public void setVariable(IVariable value) {
		getNativeActivity().setVariable(value.getName());
	}

	@Override
	public QName getFaultName() {
		return getNativeActivity().getFaultName();
	}

	@Override
	public void setFaultName(QName value) {
		getNativeActivity().setFaultName(value);
	}

	public Reply(TReply wrappedReply) {
		super(wrappedReply);
	}

	@Override
	public boolean isBasicActivity() {
		return true;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
	}

}
