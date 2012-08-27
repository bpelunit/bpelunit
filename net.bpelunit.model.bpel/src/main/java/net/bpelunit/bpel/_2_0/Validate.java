package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IValidate;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TValidate;

public class Validate extends AbstractActivity<TValidate> implements IValidate {

	public Validate(TValidate wrappedValidate) {
		super(wrappedValidate);
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
