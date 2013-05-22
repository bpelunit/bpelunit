package net.bpelunit.model.bpel;

public interface ISingleContainer extends IActivityContainer, IVisitable {

	IActivity getMainActivity();

	IActivity setNewActivity(ActivityType type);
	IReceive setNewReceive();
	IAssign setNewAssign();
	IReply setNewReply();
	ICompensate setNewCompensate();
	ICompensateScope setNewCompensateScope();
	IEmpty setNewEmpty();
	IExit setNewExit();
	IFlow setNewFlow();
	IForEach setNewForEach();
	IIf setNewIf();
	IInvoke setNewInvoke();
	IPick setNewPick();
	IRepeatUntil setNewRepeatUntil();
	IRethrow setNewRethrow();
	IScope setNewScope();
	ISequence setNewSequence();
	IThrow setNewThrow();
	IValidate setNewValidate();
	IWait setNewWait();
	IWhile setNewWhile();
	
	void removeMainActivity();
}
