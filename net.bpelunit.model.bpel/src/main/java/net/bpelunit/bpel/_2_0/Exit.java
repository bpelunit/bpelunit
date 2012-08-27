package net.bpelunit.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TExit;

import net.bpelunit.bpel.IExit;
import net.bpelunit.bpel.IVisitor;

public class Exit extends AbstractActivity<TExit> implements IExit {

	public Exit(TExit wrappedExit) {
		super(wrappedExit);
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
