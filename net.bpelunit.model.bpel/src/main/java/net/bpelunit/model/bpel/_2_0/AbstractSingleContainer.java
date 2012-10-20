package net.bpelunit.model.bpel._2_0;


import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ISingleContainer;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

abstract class AbstractSingleContainer<T extends TExtensibleElements> extends
		AbstractActivity<T> implements ISingleContainer {

	private AbstractActivity<?> mainActivity;

	AbstractSingleContainer(T newActivity) {
		super(newActivity);
		TActivity childActivity = TComplexContainerHelper
				.getChildActivity(newActivity);
		this.mainActivity = BpelFactory.INSTANCE.createWrapper(childActivity);
	}

	public AbstractActivity<?> getMainActivity() {
		return this.mainActivity;
	}

	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public Assign setNewAssign() {
		return (Assign)setNewActivityOfType("Assign");
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
	public ForEach setNewForEach() {
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
	public Receive setNewReceive() {
		return (Receive)setNewActivityOfType("Receive");
	}
	
	@Override
	public RepeatUntil setNewRepeatUntil() {
		return (RepeatUntil)setNewActivityOfType("RepeatUntil");
	}
	
	@Override
	public Reply setNewReply() {
		return (Reply)setNewActivityOfType("Reply");
	}
	
	@Override
	public Rethrow setNewRethrow() {
		return (Rethrow)setNewActivityOfType("Rethrow");
	}
	
	@Override
	public Scope setNewScope() {
		return (Scope)setNewActivityOfType("Scope");
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
		return (Wait)setNewActivityOfType("Wait");
	}
	
	@Override
	public While setNewWhile() {
		return (While)setNewActivityOfType("While");
	}
	
	@Override
	public Sequence setNewSequence() {
		return (Sequence)setNewActivityOfType("Sequence");
	}

	private AbstractActivity<?> setNewActivityOfType(String activityType) {
		mainActivity = null;
		mainActivity = TComplexContainerHelper.setNewActivityOfType(getNativeActivity(), activityType);
		return mainActivity;
	}
	
	@Override
	public void visit(IVisitor v) {
		super.visit(v);
		AbstractActivity<?> a = getMainActivity();
		if(a != null) {
			a.visit(v);
		}
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == getNativeActivity()) {
			return this;
		} else {
			return getMainActivity().getObjectForNativeObject(nativeObject);
		}
	}
}
