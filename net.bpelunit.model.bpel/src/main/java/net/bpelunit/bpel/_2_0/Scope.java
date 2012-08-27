package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IScope;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TScope;

class Scope extends AbstractSingleContainer<TScope> implements IScope {

	private TScope scope;

	Scope(TScope wrappedScope) {
		super(wrappedScope, BpelFactory.getInstance().createActivity(
				TComplexContainerHelper.getChildActivity(wrappedScope)));
	}

	@Override
	TScope getNativeActivity() {
		return this.scope;
	}

	@Override
	protected void setMainActivityBpel(AbstractActivity<?> child) {
		TComplexContainerHelper.removeMainActivity(scope);
		TComplexContainerHelper.setActivity(scope, child.getNativeActivity());
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		getMainActivity().visit(v);
	}
}
