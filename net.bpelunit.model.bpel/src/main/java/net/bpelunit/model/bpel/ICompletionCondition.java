package net.bpelunit.model.bpel;

public interface ICompletionCondition extends IExpression {

	boolean getSuccessfulBranchesOnly();
	void setSuccessfulBranchesOnly(boolean newValue);
	
}
