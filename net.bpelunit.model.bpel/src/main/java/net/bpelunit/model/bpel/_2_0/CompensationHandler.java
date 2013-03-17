package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IRethrow;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;

public class CompensationHandler implements ICompensationHandler {

	private TActivityContainer compensationHandler;
	private AbstractActivity<?> mainActivity = null;
	
	public CompensationHandler(TActivityContainer tActivityContainer) {
		compensationHandler = tActivityContainer;
	}
	
	@Override
	public IActivity getMainActivity() {
		return mainActivity;
	}

	@Override
	public Receive setNewReceive() {
		return (Receive)setNewActivityOfType("Receive");
	}

	@Override
	public Assign setNewAssign() {
		return (Assign)setNewActivityOfType("Assign");
	}

	@Override
	public Reply setNewReply() {
		return (Reply)setNewActivityOfType("Reply");
	}

	@Override
	public Compensate setNewCompensate() {
		return (Compensate)setNewActivityOfType("Compensate");
	}

	@Override
	public CompensateScope setNewCompensateScope() {
		return (CompensateScope)setNewActivityOfType("CompensateScope");
	}

	@Override
	public Empty setNewEmpty() {
		return (Empty)setNewActivityOfType("Empty");
	}

	@Override
	public Exit setNewExit() {
		return (Exit)setNewActivityOfType("Exit");
	}

	@Override
	public Flow setNewFlow() {
		return (Flow)setNewActivityOfType("Flow");
	}

	@Override
	public IForEach setNewForEach() {
		return (ForEach)setNewActivityOfType("ForEach");
	}

	@Override
	public If setNewIf() {
		return (If)setNewActivityOfType("If");
	}

	@Override
	public Invoke setNewInvoke() {
		return (Invoke)setNewActivityOfType("Invoke");
	}

	@Override
	public Pick setNewPick() {
		return (Pick)setNewActivityOfType("Pick");
	}

	@Override
	public RepeatUntil setNewRepeatUntil() {
		return (RepeatUntil)setNewActivityOfType("RepeatUntil");
	}

	@Override
	public IRethrow setNewRethrow() {
		return (Rethrow)setNewActivityOfType("Rethrow");
	}

	@Override
	public Scope setNewScope() {
		return (Scope)setNewActivityOfType("Scope");
	}

	@Override
	public Sequence setNewSequence() {
		return (Sequence)setNewActivityOfType("Sequence");
	}

	@Override
	public Throw setNewThrow() {
		return (Throw)setNewActivityOfType("Throw");
	}

	@Override
	public Validate setNewValidate() {
		return (Validate)setNewActivityOfType("Validate");
	}

	@Override
	public Wait setNewWait() {
		return (Wait)setNewActivityOfType("Validate");
	}

	@Override
	public While setNewWhile() {
		return (While)setNewActivityOfType("While");
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		if(mainActivity != null) {
			mainActivity.visit(v);
		}
	}

	private AbstractActivity<?> setNewActivityOfType(String activityType) {
		mainActivity = null;
		mainActivity = TComplexContainerHelper.setNewActivityOfType(compensationHandler, activityType, this);
		return mainActivity;
	}

	@Override
	public IActivity setNewActivity(ActivityType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IScope wrapActivityInNewScope(IActivity childActivity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ISequence wrapActivityInNewSequence(IActivity childActivity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeMainActivity() {
		mainActivity = null;
		TComplexContainerHelper.removeMainActivity(getMainActivity());
	}
	
	@Override
	public String getXPathInDocument() {
		// TODO Auto-generated method stub
		return null;
	}
}
