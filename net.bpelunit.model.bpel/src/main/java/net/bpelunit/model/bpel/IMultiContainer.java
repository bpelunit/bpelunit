package net.bpelunit.model.bpel;

import java.util.List;

public interface IMultiContainer extends IActivityContainer {

	List<? extends IActivity> getActivities();
	
	IReceive addReceive();
	IAssign addAssign();
	IReply addReply();
	ICompensate addCompensate();
	ICompensateScope addCompensateScope();
	IEmpty addEmpty();
	IExit addExit();
	IFlow addFlow();
	IForEach addForEach();
	IIf addIf();
	IInvoke addInvoke();
	IPick addPick();
	IRepeatUntil addRepeatUntil();
	IRethrow addRethrow();
	IScope addScope();
	ISequence addSequence();
	IThrow addThrow();
	IValidate addValidate();
	IWait addWait();
	IWhile addWhile();

	void moveBefore(IActivity toMove, IActivity moveBefore);
	void moveToEnd(IActivity toMove);
	void remove(IActivity activityToDelete);
}
