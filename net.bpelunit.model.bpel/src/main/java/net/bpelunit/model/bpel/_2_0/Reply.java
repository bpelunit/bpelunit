package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IReply;
import net.bpelunit.model.bpel.IVariable;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TReply;

public class Reply extends AbstractBasicActivity<TReply> implements IReply {

	public Reply(TReply wrappedReply, BpelFactory f) {
		super(wrappedReply, f,IReply.class);
	}

	@Override
	public QName getFaultName() {
		return getNativeActivity().getFaultName();
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
	public void setFaultName(QName value) {
		getNativeActivity().setFaultName(value);
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
	public void setVariable(IVariable value) {
		getNativeActivity().setVariable(value.getName());
	}

	@Override
	public void setVariable(String value) {
		getNativeActivity().setVariable(value);
	}
}
