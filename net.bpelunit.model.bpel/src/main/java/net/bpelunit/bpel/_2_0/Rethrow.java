package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IRethrow;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TRethrow;

public class Rethrow extends AbstractActivity<TRethrow> implements IRethrow {

	public Rethrow(TRethrow wrappedRethrow) {
		super(wrappedRethrow);
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
