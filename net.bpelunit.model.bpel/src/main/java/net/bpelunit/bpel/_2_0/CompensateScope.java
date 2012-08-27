package net.bpelunit.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TCompensateScope;

import net.bpelunit.bpel.ICompensateScope;
import net.bpelunit.bpel.IScope;
import net.bpelunit.bpel.IVisitor;

class CompensateScope extends AbstractActivity<TCompensateScope> implements
		ICompensateScope {

	private TCompensateScope compensateScope;

	CompensateScope(TCompensateScope wrappedCompensateScope) {
		super(wrappedCompensateScope);
		this.compensateScope = wrappedCompensateScope;
	}

	public void setTarget(String scopeName) {
		compensateScope.setTarget(scopeName);
	}

	public void setTargetScope(IScope scope) {
		compensateScope.setTarget(scope.getName());
	}

	public String getTarget() {
		return compensateScope.getTarget();
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
