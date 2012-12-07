package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IValidate;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TValidate;

public class Validate extends AbstractBasicActivity<TValidate> implements IValidate {

	public Validate(TValidate wrappedValidate, IContainer parent) {
		super(wrappedValidate, parent);
	}
}
