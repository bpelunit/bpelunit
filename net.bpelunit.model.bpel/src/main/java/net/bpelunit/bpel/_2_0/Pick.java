package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IPick;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TBoolean;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPick;

public class Pick extends AbstractActivity<TPick> implements IPick {

	public Pick(TPick wrappedPick) {
		super(wrappedPick);
	}

	@Override
	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
	}

	@Override
	public void setCreateInstance(boolean b) {
		if(b) {
			getNativeActivity().setCreateInstance(TBoolean.YES);
		} else {
			getNativeActivity().setCreateInstance(TBoolean.NO);
		}
	}

	@Override
	public boolean isCreateInstance() {
		return TBoolean.YES.equals(getNativeActivity().getCreateInstance());
	}
	
	
}
