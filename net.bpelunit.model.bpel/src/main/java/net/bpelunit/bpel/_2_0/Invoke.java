package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IInvoke;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TInvoke;

public class Invoke extends AbstractActivity<TInvoke> implements IInvoke {
	public Invoke(TInvoke wrappedInvoke) {
		super(wrappedInvoke);
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
