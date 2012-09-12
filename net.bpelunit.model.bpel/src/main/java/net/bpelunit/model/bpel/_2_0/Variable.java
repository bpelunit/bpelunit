package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariable;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

public class Variable extends AbstractBpelObject implements IVariable {

	private TVariable variable;

	public Variable(TVariable v, BpelFactory f) {
		super(v, f);
		this.variable = v;
	}

	@Override
	public QName getElement() {
		return variable.getElement();
	}

	@Override
	public QName getMessageType() {
		return variable.getMessageType();
	}

	@Override
	public String getName() {
		return variable.getName();
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == variable) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public QName getType() {
		return variable.getType();
	}

	@Override
	public void setElement(QName value) {
		variable.setElement(value);
		variable.setType(null);
		variable.setMessageType(null);
	}

	@Override
	public void setMessageType(QName value) {
		variable.setMessageType(value);
		variable.setElement(null);
		variable.setType(null);
	}
	
	@Override
	public void setName(String value) {
		variable.setName(value);
	}

	@Override
	public void setType(QName value) {
		variable.setType(value);
		variable.setMessageType(null);
		variable.setElement(null);
	}
	
	@Override
	void visit(IVisitor v) {
	}
}
