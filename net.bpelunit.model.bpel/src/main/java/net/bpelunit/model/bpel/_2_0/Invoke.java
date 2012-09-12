package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IVariable;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TInvoke;

public class Invoke extends AbstractBasicActivity<TInvoke> implements IInvoke {
	private TInvoke invoke;
	
	public Invoke(TInvoke wrappedInvoke, BpelFactory f) {
		super(wrappedInvoke, f, IInvoke.class);
		invoke = wrappedInvoke;
	}

	@Override
	public String getPartnerLink() {
		return invoke.getPartnerLink();
	}

	@Override
	public void setPartnerLink(String value) {
		invoke.setPartnerLink(value);
	}

	@Override
	public QName getPortType() {
		return invoke.getPortType();
	}

	@Override
	public void setPortType(QName value) {
		invoke.setPortType(value);
	}

	@Override
	public String getOperation() {
		return invoke.getOperation();
	}

	@Override
	public void setOperation(String value) {
		invoke.setOperation(value);
	}
	
	@Override
	public String getInputVariable() {
		return invoke.getInputVariable();
	}

	@Override
	public void setInputVariable(String value) {
		invoke.setInputVariable(value);
	}
	
	@Override
	public void setInputVariable(IVariable v) {
		setInputVariable(v.getName());
	}

	@Override
	public String getOutputVariable() {
		return invoke.getOutputVariable();
	}

	@Override
	public void setOutputVariable(String value) {
		invoke.setOutputVariable(value);
	}
	
	@Override
	public void setOutputVariable(IVariable v) {
		setOutputVariable(v.getName());
	}
}
