package net.bpelunit.bpel;

public interface ICompensateScope extends IActivity {

	void setTarget(String scopeName);
	void setTargetScope(IScope scope);
	
}
