package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IVisitor;
import net.bpelunit.bpel.IWhile;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TWhile;

public class While extends AbstractSingleContainer<TWhile> implements IWhile {

	private TWhile w;

	public While(TWhile wrappedWhile) {
		super(wrappedWhile, BpelFactory.getInstance().createActivity(TComplexContainerHelper.getChildActivity(wrappedWhile)));
		this.w = wrappedWhile;
	}
	
	@Override
	protected void setMainActivityBpel(AbstractActivity<?> nativeActivity) {
		TComplexContainerHelper.removeMainActivity(w);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		getMainActivity().visit(v);
	}
}
