package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ISingleContainer;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

abstract class AbstractSingleContainer<T extends TExtensibleElements> extends
		AbstractActivity<T> implements ISingleContainer, IContainer {

	private AbstractActivity<?> mainActivity;

	AbstractSingleContainer(T newActivity, IContainer parent) {
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
		removeMainActivity();
		return (Assign) setNewActivity(ActivityType.Assign);
	}

	@Override
	public Compensate setNewCompensate() {
		removeMainActivity();
		return (Compensate) setNewActivity(ActivityType.Compensate);
	}

	@Override
	public CompensateScope setNewCompensateScope() {
		removeMainActivity();
		return (CompensateScope) setNewActivity(ActivityType.CompensateScope);
	}

	@Override
	public Empty setNewEmpty() {
		removeMainActivity();
		return (Empty) setNewActivity(ActivityType.Empty);
	}

	@Override
	public Exit setNewExit() {
		removeMainActivity();
		return (Exit) setNewActivity(ActivityType.Exit);
	}

	@Override
	public Flow setNewFlow() {
		removeMainActivity();
		return (Flow) setNewActivity(ActivityType.Flow);
	}

	@Override
	public ForEach setNewForEach() {
		removeMainActivity();
		return (ForEach) setNewActivity(ActivityType.ForEach);
	}

	@Override
	public If setNewIf() {
		removeMainActivity();
		return (If) setNewActivity(ActivityType.If);
	}

	@Override
	public Invoke setNewInvoke() {
		removeMainActivity();
		return (Invoke) setNewActivity(ActivityType.Invoke);
	}

	@Override
	public Pick setNewPick() {
		removeMainActivity();
		return (Pick) setNewActivity(ActivityType.Pick);
	}

	@Override
	public Receive setNewReceive() {
		removeMainActivity();
		return (Receive) setNewActivity(ActivityType.Receive);
	}

	@Override
	public RepeatUntil setNewRepeatUntil() {
		removeMainActivity();
		return (RepeatUntil) setNewActivity(ActivityType.RepeatUntil);
	}

	@Override
	public Reply setNewReply() {
		removeMainActivity();
		return (Reply) setNewActivity(ActivityType.Reply);
	}

	@Override
	public Rethrow setNewRethrow() {
		removeMainActivity();
		return (Rethrow) setNewActivity(ActivityType.Rethrow);
	}

	@Override
	public Scope setNewScope() {
		removeMainActivity();
		return (Scope) setNewActivity(ActivityType.Scope);
	}

	@Override
	public Throw setNewThrow() {
		removeMainActivity();
		return (Throw) setNewActivity(ActivityType.Throw);
	}

	@Override
	public Validate setNewValidate() {
		removeMainActivity();
		return (Validate) setNewActivity(ActivityType.Validate);
	}

	@Override
	public Wait setNewWait() {
		removeMainActivity();
		return (Wait) setNewActivity(ActivityType.Wait);
	}

	@Override
	public While setNewWhile() {
		removeMainActivity();
		return (While) setNewActivity(ActivityType.While);
	}

	@Override
	public Sequence setNewSequence() {
		removeMainActivity();
		return (Sequence) setNewActivity(ActivityType.Sequence);
	}

	@Override
	public AbstractActivity<?> setNewActivity(ActivityType activityType) {
		removeMainActivity();
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
	public Scope wrapActivityInNewScope(IActivity childActivity) {
		AbstractActivity<?> a = (AbstractActivity<?>) childActivity;
		XmlObject nativeA = (XmlObject) a.getNativeActivity();
		XmlObject copyOfA = nativeA.copy();
		removeMainActivity();
		a.setNativeObject(copyOfA);
		
		// create new scope and make it replace the old child activity
		Scope newScope = setNewScope();
		mainActivity.setName("Scope_Of_" + childActivity.getName());
		newScope.replace(null, a);
		
		return newScope;
	}
	
	@Override
	public Sequence wrapActivityInNewSequence(IActivity childActivity) {
		AbstractActivity<?> a = (AbstractActivity<?>) childActivity;
		XmlObject nativeA = (XmlObject) a.getNativeActivity();
		XmlObject copyOfA = nativeA.copy();
		removeMainActivity();
		a.setNativeObject(copyOfA);
		
		// create new scope and make it replace the old child activity
		Sequence newSequence = setNewSequence();
		mainActivity.setName("Sequence_Of_" + childActivity.getName());
		newSequence.add(a);
		
		return newSequence;
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
	
	@Override
	public void unregister(AbstractActivity<?> a) {
		if(a == mainActivity) {
			XmlCursor c = activity.newCursor();
			for(c.toFirstChild(); c.toNextSibling();) {
				if(c.getObject() == mainActivity.getNativeActivity()) {
					c.removeXml();
					break;
				}
			}
			c.dispose();
	
			mainActivity = null;
		}
	}
	
	@Override
	public void removeMainActivity() {
		if(mainActivity != null) {
			XmlCursor c = mainActivity.getNativeActivity().newCursor();
			c.removeXml();
			c.dispose();
			
			mainActivity.reparent(null);
			mainActivity = null;
		}
	}
	
	public void setMainActivity(AbstractActivity<?> a) {
		removeMainActivity();
		
		AbstractActivity<?> tempActivity = setNewActivity(a.getActivityType());
		XmlObject tempNative = tempActivity.getNativeActivity();
		XmlObject actNative = a.getNativeActivity();
		tempNative.set(actNative);
		mainActivity = a;
		a.reparent(this);
		a.setNativeObject(tempNative);
	}
}
