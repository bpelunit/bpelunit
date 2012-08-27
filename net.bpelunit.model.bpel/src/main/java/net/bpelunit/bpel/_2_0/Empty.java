package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IEmpty;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;

class Empty extends AbstractActivity<TEmpty> implements IEmpty {

	Empty(TEmpty newEmpty) {
		super(newEmpty);
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
