package net.bpelunit.model.bpel;


public interface IForEach extends IActivity, IActivityContainer, IScopeOnlyContainer {

	IScope getScope();
	
	String getCounterName();
	void setCounterName(String newCounterName);
	
	boolean isParallel();
	void setParallel(boolean isParallel);
	
	IExpression getStartCounterValue();
	IExpression getFinalCounterValue();
	
	ICompletionCondition getCompletionCondition();
	ICompletionCondition setNewCompletionCondition();
}
