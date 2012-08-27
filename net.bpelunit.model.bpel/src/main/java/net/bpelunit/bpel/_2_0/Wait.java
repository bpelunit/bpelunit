package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IVisitor;
import net.bpelunit.bpel.IWait;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TWait;

public class Wait extends AbstractActivity<TWait> implements IWait {

	public Wait(TWait wrappedWait) {
		super(wrappedWait);
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
