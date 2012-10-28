package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IVariable;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

public class Variable extends AbstractBpelObject implements IVariable {

	private TVariable variable;

	public Variable(TVariable v) {
		super(v);
		this.variable = v;
	}

	public QName getElement() {
		return variable.getElement();
	}

	public QName getMessageType() {
		return variable.getMessageType();
	}

	public String getName() {
		return variable.getName();
	}

	@Override
	public QName getType() {
		return variable.getType();
	}

	public void setElement(QName value) {
		variable.setElement(value);
		if(variable.getType() != null) {
			variable.unsetType();
		}
		
		if(variable.getMessageType() != null) {
		variable.unsetMessageType();
		}
	}

	public void setMessageType(QName value) {
		variable.setMessageType(value);
		
		if(variable.getElement() != null) {
			variable.unsetElement();
		}
		
		if(variable.getType() != null) {
			variable.unsetType();
		}
	}
	
	public void setName(String value) {
		variable.setName(value);
	}

	public void setType(QName value) {
		variable.setType(value);
		if(variable.getMessageType() != null) {
			variable.unsetMessageType();
		}
		
		if(variable.getElement() != null) {
			variable.unsetElement();
		}
	}
}
