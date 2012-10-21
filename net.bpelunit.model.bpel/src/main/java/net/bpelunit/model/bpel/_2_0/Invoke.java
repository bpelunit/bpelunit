package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IVariable;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;

public class Invoke extends AbstractBasicActivity<TInvoke> implements IInvoke {
	private TInvoke invoke;
	private CompensationHandler compensationHandler;
	
	public Invoke(TInvoke wrappedInvoke) {
		super(wrappedInvoke);
		this.invoke = wrappedInvoke;
		if(invoke.getCompensationHandler() != null) {
			compensationHandler = new CompensationHandler(invoke.getCompensationHandler());
		}
	}

	public String getPartnerLink() {
		return invoke.getPartnerLink();
	}

	public void setPartnerLink(String value) {
		invoke.setPartnerLink(value);
	}

	public QName getPortType() {
		return invoke.getPortType();
	}

	public void setPortType(QName value) {
		invoke.setPortType(value);
	}

	public String getOperation() {
		return invoke.getOperation();
	}

	public void setOperation(String value) {
		invoke.setOperation(value);
	}
	
	public String getInputVariable() {
		return invoke.getInputVariable();
	}

	public void setInputVariable(String value) {
		invoke.setInputVariable(value);
	}
	
	public void setInputVariable(IVariable v) {
		setInputVariable(v.getName());
	}

	public String getOutputVariable() {
		return invoke.getOutputVariable();
	}

	public void setOutputVariable(String value) {
		invoke.setOutputVariable(value);
	}
	
	public void setOutputVariable(IVariable v) {
		setOutputVariable(v.getName());
	}
	
	@Override
	public ICompensationHandler setNewCompensationHandler() {
		if(invoke.getCompensationHandler() != null) {
			invoke.unsetCompensationHandler();
			compensationHandler = null;
		}
		
		compensationHandler = new CompensationHandler(invoke.addNewCompensationHandler());
		
		return compensationHandler;
	}

	@Override
	public ICompensationHandler getCompensationHandler() {
		return compensationHandler;
	}
}
