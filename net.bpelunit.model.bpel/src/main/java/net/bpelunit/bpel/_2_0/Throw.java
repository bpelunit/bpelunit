package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IThrow;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TThrow;

public class Throw extends AbstractActivity<TThrow> implements IThrow {

	public Throw(TThrow t) {
		super(t);
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
