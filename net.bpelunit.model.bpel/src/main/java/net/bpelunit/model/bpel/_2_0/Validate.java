package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IValidate;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TValidate;

public class Validate extends AbstractBasicActivity<TValidate> implements IValidate {

	public Validate(TValidate wrappedValidate, BpelFactory f) {
		super(wrappedValidate, f, IValidate.class);
	}
}
