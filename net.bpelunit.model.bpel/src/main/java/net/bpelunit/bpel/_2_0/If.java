package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IIf;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TIf;

public class If extends AbstractActivity<TIf> implements IIf {

	public If(TIf i) {
		super(i);
	}

	@Override
	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
	}
}
