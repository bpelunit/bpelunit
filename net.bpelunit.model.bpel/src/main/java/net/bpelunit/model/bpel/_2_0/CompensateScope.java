package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.IScope;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensateScope;

class CompensateScope extends AbstractBasicActivity<TCompensateScope> implements
		ICompensateScope {

	public CompensateScope(TCompensateScope wrappedCompensateScope, IContainer parent) {
		super(wrappedCompensateScope, parent);
	}

	public void setTarget(String scopeName) {
		getNativeActivity().setTarget(scopeName);
	}

	public void setTarget(IScope scope) {
		getNativeActivity().setTarget(scope.getName());
	}

	public String getTarget() {
		return getNativeActivity().getTarget();
	}
}
