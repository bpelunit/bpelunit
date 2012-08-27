package net.bpelunit.bpel._2_0;

import javax.xml.namespace.QName;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariable;

import net.bpelunit.bpel.IVariable;

public class Variable extends AbstractBpelObject implements IVariable {

	private TVariable variable;

	@Override
	public String getName() {
		return variable.getName();
	}

	@Override
	public void setName(String value) {
		variable.setName(value);
	}

	@Override
	public QName getMessageType() {
		return variable.getMessageType();
	}

	@Override
	public void setMessageType(QName value) {
		variable.setMessageType(value);
		variable.setElement(null);
		variable.setType(null);
	}

	@Override
	public QName getType() {
		return variable.getType();
	}

	@Override
	public void setType(QName value) {
		variable.setType(value);
		variable.setMessageType(null);
		variable.setElement(null);
	}

	@Override
	public QName getElement() {
		return variable.getElement();
	}

	@Override
	public void setElement(QName value) {
		variable.setElement(value);
		variable.setType(null);
		variable.setMessageType(null);
	}
	
	public Variable(TVariable v) {
		super(v);
		this.variable = v;
	}

}
