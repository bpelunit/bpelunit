package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ISingleContainer;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

abstract class AbstractSingleContainer<T extends TExtensibleElements> extends
		AbstractActivity<T> implements ISingleContainer {

	private AbstractActivity<?> mainActivity;

	AbstractSingleContainer(T newActivity, IActivityContainer parent) {
		super(newActivity, parent);
		setNativeObjectInternal(newActivity);
	}

	@SuppressWarnings("unchecked")
	void setNativeObject(Object o) {
		super.setNativeObject(o);
		setNativeObjectInternal((T)o);
	}
	
	private final void setNativeObjectInternal(T newActivity) {
		TActivity childActivity = TComplexContainerHelper
				.getChildActivity(newActivity);
		this.mainActivity = BpelFactory.INSTANCE.createWrapper(childActivity, this);
	}

	public AbstractActivity<?> getMainActivity() {
		return this.mainActivity;
	}

	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public Assign setNewAssign() {
		return (Assign) setNewActivity(ActivityType.Assign);
	}

	@Override
	public Compensate setNewCompensate() {
		return (Compensate) setNewActivity(ActivityType.Compensate);
	}

	@Override
	public CompensateScope setNewCompensateScope() {
		return (CompensateScope) setNewActivity(ActivityType.CompensateScope);
	}

	@Override
	public Empty setNewEmpty() {
		return (Empty) setNewActivity(ActivityType.Empty);
	}

	@Override
	public Exit setNewExit() {
		return (Exit) setNewActivity(ActivityType.Exit);
	}

	@Override
	public Flow setNewFlow() {
		return (Flow) setNewActivity(ActivityType.Flow);
	}

	@Override
	public ForEach setNewForEach() {
		return (ForEach) setNewActivity(ActivityType.ForEach);
	}

	@Override
	public If setNewIf() {
		return (If) setNewActivity(ActivityType.If);
	}

	@Override
	public Invoke setNewInvoke() {
		return (Invoke) setNewActivity(ActivityType.Invoke);
	}

	@Override
	public Pick setNewPick() {
		return (Pick) setNewActivity(ActivityType.Pick);
	}

	@Override
	public Receive setNewReceive() {
		return (Receive) setNewActivity(ActivityType.Receive);
	}

	@Override
	public RepeatUntil setNewRepeatUntil() {
		return (RepeatUntil) setNewActivity(ActivityType.RepeatUntil);
	}

	@Override
	public Reply setNewReply() {
		return (Reply) setNewActivity(ActivityType.Reply);
	}

	@Override
	public Rethrow setNewRethrow() {
		return (Rethrow) setNewActivity(ActivityType.Rethrow);
	}

	@Override
	public Scope setNewScope() {
		return (Scope) setNewActivity(ActivityType.Scope);
	}

	@Override
	public Throw setNewThrow() {
		return (Throw) setNewActivity(ActivityType.Throw);
	}

	@Override
	public Validate setNewValidate() {
		return (Validate) setNewActivity(ActivityType.Validate);
	}

	@Override
	public Wait setNewWait() {
		return (Wait) setNewActivity(ActivityType.Wait);
	}

	@Override
	public While setNewWhile() {
		return (While) setNewActivity(ActivityType.While);
	}

	@Override
	public Sequence setNewSequence() {
		return (Sequence) setNewActivity(ActivityType.Sequence);
	}

	@Override
	public AbstractActivity<?> setNewActivity(ActivityType activityType) {
		mainActivity = null;
		mainActivity = TComplexContainerHelper.setNewActivityOfType(
				getNativeActivity(), activityType.name(), this);
		return mainActivity;
	}

	@Override
	public void visit(IVisitor v) {
		super.visit(v);
		AbstractActivity<?> a = getMainActivity();
		if (a != null) {
			a.visit(v);
		}
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if (nativeObject == getNativeActivity()) {
			return this;
		} else {
			return getMainActivity().getObjectForNativeObject(nativeObject);
		}
	}

	@Override
	public Scope encapsulateInNewScope(IActivity childActivity) {
		AbstractActivity<?> a = (AbstractActivity<?>) childActivity;
		XmlObject nativeA = (XmlObject) a.getNativeActivity();
		XmlObject copyOfA = nativeA.copy();
		a.setNativeObject(copyOfA);
		
		// create new scope and make it replace the old child activity
		Scope newScope = setNewScope();
		mainActivity.setName("ScopeOf" + childActivity.getName());
		newScope.replace(null, a);
		
		return newScope;
	}
	
	void replace(AbstractActivity<?> oldActivity, AbstractActivity<?> newMainActivity) {
		if(getMainActivity() != oldActivity) {
			throw new IllegalArgumentException("Cannot replace " + oldActivity + " because it is not a child of this activity");
		}
		AbstractActivity<?> tempActivity = setNewActivity(newMainActivity.getActivityType());
		
		XmlObject newNative = ((XmlObject)tempActivity.getNativeActivity()).set(newMainActivity.getNativeActivity());
		
		newMainActivity.setNativeObject(newNative);
		
		mainActivity = newMainActivity;
	}
}
