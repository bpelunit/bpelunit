package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.ICompensate;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TCompensate;

class Compensate extends AbstractActivity<TCompensate> implements ICompensate {

	Compensate(TCompensate wrappedCompensate) {
		super(wrappedCompensate);
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
