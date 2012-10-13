package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.IScope;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensateScope;

class CompensateScope extends AbstractBasicActivity<TCompensateScope> implements
		ICompensateScope {

	private TCompensateScope compensateScope;

	public CompensateScope(TCompensateScope wrappedCompensateScope) {
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
	protected void setNativeActivity(XmlObject newNativeActivity) {
		super.setNativeActivity(newNativeActivity);
		this.compensateScope = (TCompensateScope)newNativeActivity;
	}
}
