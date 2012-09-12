package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.IScope;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TCompensateScope;

class CompensateScope extends AbstractBasicActivity<TCompensateScope> implements
		ICompensateScope {

	private TCompensateScope compensateScope;

	CompensateScope(TCompensateScope wrappedCompensateScope, BpelFactory f) {
		super(wrappedCompensateScope, f, ICompensateScope.class);
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
}
