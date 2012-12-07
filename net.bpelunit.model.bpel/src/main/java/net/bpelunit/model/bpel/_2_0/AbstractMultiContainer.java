package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IMultiContainer;
import net.bpelunit.model.bpel.IScope;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensate;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensateScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExit;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRepeatUntil;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRethrow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TThrow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TValidate;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;

abstract class AbstractMultiContainer<T extends TActivity> extends
		AbstractActivity<T> implements IMultiContainer, IContainer {

	private List<AbstractActivity<?>> wrappedActivities = new ArrayList<AbstractActivity<?>>();
	private List<TActivity> activities = new ArrayList<TActivity>();

	AbstractMultiContainer(T wrappedActivity, IContainer parent) {
		super(wrappedActivity, parent);

		setNativeObjectInternal(wrappedActivity);
	}

	void setNativeObject(Object newNativeObject) {
		super.setNativeObject(newNativeObject);
		setNativeObjectInternal(newNativeObject);
	}

	private final void setNativeObjectInternal(Object newNativeObject) {
		extractNativeActivities(newNativeObject);

		wrappedActivities.clear();
		for (TActivity child : activities) {
			wrappedActivities.add(BpelFactory.INSTANCE.createWrapper(child,
					this));
		}
	}

	private void extractNativeActivities(Object newNativeObject) {
		TActivity wrappedActivity = (TActivity) newNativeObject;
		this.activities.clear();
		XmlCursor cursor = wrappedActivity.newCursor();
		for (boolean hasNext = cursor.toFirstChild(); hasNext; hasNext = cursor
				.toNextSibling()) {
			if (cursor.getObject() instanceof TActivity) {
				activities.add((TActivity) cursor.getObject());
			}
		}
		cursor.dispose();
	}

	public List<AbstractActivity<?>> getActivities() {
		return Collections.unmodifiableList(wrappedActivities);
	}

	public boolean isBasicActivity() {
		return false;
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		IBpelObject o = super.getObjectForNativeObject(nativeObject);
		if (o != null) {
			return o;
		} else {
			for (AbstractActivity<?> child : wrappedActivities) {
				o = child.getObjectForNativeObject(nativeObject);
				if (o != null) {
					return o;
				}
			}
		}

		return null;
	}

	AbstractActivity<?> addActivity(ActivityType a) {
		Method method;
		try {
			method = getClass().getMethod("add" + a.name());
			return (AbstractActivity<?>) method.invoke(this);
		} catch (Exception e) {
			throw new RuntimeException("Illegal configuration: No add found for " + a);
		}
	}
	
	@Override
	public Reply addReply() {
		TReply nativeReply = (TReply) addNativeActivity("Reply");
		Reply reply = new Reply(nativeReply, this);
		activities.add(nativeReply);
		wrappedActivities.add(reply);
		return reply;
	}

	@Override
	public Assign addAssign() {
		TAssign nativeAssign = (TAssign) addNativeActivity("Assign");
		Assign assign = new Assign(nativeAssign, this);
		activities.add(nativeAssign);
		wrappedActivities.add(assign);
		return assign;
	}

	@Override
	public Receive addReceive() {
		TReceive nativeReceive = (TReceive) addNativeActivity("Receive");
		Receive receive = new Receive(nativeReceive, this);
		activities.add(nativeReceive);
		wrappedActivities.add(receive);
		return receive;
	}

	@Override
	public Compensate addCompensate() {
		TCompensate nativeReceive = (TCompensate) addNativeActivity("Compensate");
		Compensate compensate = new Compensate(nativeReceive, this);
		activities.add(nativeReceive);
		wrappedActivities.add(compensate);
		return compensate;
	}

	@Override
	public CompensateScope addCompensateScope() {
		TCompensateScope nativeCompensateScope = (TCompensateScope) addNativeActivity("CompensateScope");
		CompensateScope compensateScope = new CompensateScope(
				nativeCompensateScope, this);
		activities.add(nativeCompensateScope);
		wrappedActivities.add(compensateScope);
		return compensateScope;
	}

	@Override
	public Empty addEmpty() {
		TEmpty nativeEmpty = (TEmpty) addNativeActivity("Empty");
		Empty empty = new Empty(nativeEmpty, this);
		activities.add(nativeEmpty);
		wrappedActivities.add(empty);
		return empty;
	}

	@Override
	public Exit addExit() {
		TExit nativeExit = (TExit) addNativeActivity("Exit");
		Exit exit = new Exit(nativeExit, this);
		activities.add(nativeExit);
		wrappedActivities.add(exit);
		return exit;
	}

	@Override
	public Flow addFlow() {
		TFlow nativeFlow = (TFlow) addNativeActivity("Flow");
		Flow flow = new Flow(nativeFlow, this);
		activities.add(nativeFlow);
		wrappedActivities.add(flow);
		return flow;
	}

	@Override
	public ForEach addForEach() {
		TForEach nativeForEach = (TForEach) addNativeActivity("ForEach");
		ForEach forEach = new ForEach(nativeForEach, this);
		activities.add(nativeForEach);
		wrappedActivities.add(forEach);
		return forEach;
	}

	@Override
	public If addIf() {
		TIf nativeIf = (TIf) addNativeActivity("If");
		If receive = new If(nativeIf, this);
		activities.add(nativeIf);
		wrappedActivities.add(receive);
		return receive;
	}

	@Override
	public Invoke addInvoke() {
		TInvoke nativeInvoke = (TInvoke) addNativeActivity("Invoke");
		Invoke invoke = new Invoke(nativeInvoke, this);
		activities.add(nativeInvoke);
		wrappedActivities.add(invoke);
		return invoke;
	}

	@Override
	public Pick addPick() {
		TPick nativePick = (TPick) addNativeActivity("Pick");
		Pick pick = new Pick(nativePick, this);
		activities.add(nativePick);
		wrappedActivities.add(pick);
		return pick;
	}

	@Override
	public RepeatUntil addRepeatUntil() {
		TRepeatUntil nativeRepeatUntil = (TRepeatUntil) addNativeActivity("RepeatUntil");
		RepeatUntil repeatUntil = new RepeatUntil(nativeRepeatUntil, this);
		activities.add(nativeRepeatUntil);
		wrappedActivities.add(repeatUntil);
		return repeatUntil;
	}

	@Override
	public Rethrow addRethrow() {
		TRethrow nativeRethrow = (TRethrow) addNativeActivity("Rethrow");
		Rethrow rethrow = new Rethrow(nativeRethrow, this);
		activities.add(nativeRethrow);
		wrappedActivities.add(rethrow);
		return rethrow;
	}

	@Override
	public Scope addScope() {
		TScope nativeScope = (TScope) addNativeActivity("Scope");
		Scope scope = new Scope(nativeScope, this);
		activities.add(nativeScope);
		wrappedActivities.add(scope);
		return scope;
	}

	@Override
	public Sequence addSequence() {
		TSequence nativeSequence = (TSequence) addNativeActivity("Sequence");
		Sequence sequence = new Sequence(nativeSequence, this);
		activities.add(nativeSequence);
		wrappedActivities.add(sequence);
		return sequence;
	}

	@Override
	public Throw addThrow() {
		TThrow nativeThrow = (TThrow) addNativeActivity("Throw");
		Throw tthrow = new Throw(nativeThrow, this);
		activities.add(nativeThrow);
		wrappedActivities.add(tthrow);
		return tthrow;
	}

	@Override
	public Validate addValidate() {
		TValidate nativeValidate = (TValidate) addNativeActivity("Validate");
		Validate validate = new Validate(nativeValidate, this);
		activities.add(nativeValidate);
		wrappedActivities.add(validate);
		return validate;
	}

	@Override
	public Wait addWait() {
		TWait nativeWait = (TWait) addNativeActivity("Wait");
		Wait wait = new Wait(nativeWait, this);
		activities.add(nativeWait);
		wrappedActivities.add(wait);
		return wait;
	}

	@Override
	public While addWhile() {
		TWhile nativeWhile = (TWhile) addNativeActivity("While");
		While wwhile = new While(nativeWhile, this);
		activities.add(nativeWhile);
		wrappedActivities.add(wwhile);
		return wwhile;

	}

	private TActivity addNativeActivity(String activityName) {
		try {
			Method m = getNativeActivity().getClass().getMethod(
					"addNew" + activityName);
			return (TActivity) m.invoke(getNativeActivity());
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public IScope wrapActivityInNewScope(IActivity childActivity) {
		AbstractActivity<?> a = (AbstractActivity<?>) childActivity;

		getIndexOfChildActivity(a);

		Scope newScope = addScope();
		newScope.setName("ScopeOf" + a.getName());
		moveBefore(newScope, a);

		newScope.setMainActivity(a);

		return newScope;
	}

	private int getIndexOfChildActivity(AbstractActivity<?> a) {
		int indexOfActivity = wrappedActivities.indexOf(a);
		if (indexOfActivity < 0) {
			throw new IllegalArgumentException("Cannot work with "
					+ a
					+ " because it is not a child of this activity");
		}
		return indexOfActivity;
	}

	void replace(AbstractActivity<?> oldActivity,
			AbstractActivity<?> newActivity) {
		if (oldActivity == newActivity) {
			return;
		}

		int index = getIndexOfChildActivity(oldActivity);

		XmlCursor oldCursor = oldActivity.getNativeActivity().newCursor();
		XmlCursor newCursor = null;
		try {
			newCursor = newActivity.getNativeActivity().newCursor();
			newCursor.moveXml(oldCursor);
			oldCursor.removeXml();

			wrappedActivities.remove(newActivity);
			wrappedActivities.add(index, newActivity);
			wrappedActivities.remove(oldActivity);
		} finally {
			oldCursor.dispose();
			newCursor.dispose();
		}
		
		updateNativeActivities();
	}

	@Override
	public Sequence wrapActivityInNewSequence(IActivity childActivity) {
		Sequence newSequence = addSequence();
		newSequence.setName("SequenceOf" + childActivity.getName());
		moveBefore(newSequence, childActivity);
		newSequence.add((AbstractActivity<?>) childActivity);
		
		return newSequence;
	}
	
	void add(AbstractActivity<?> a) {
		AbstractActivity<?> tempActivity = addActivity(a.getActivityType());

		XmlObject tempNative = tempActivity.getNativeActivity();
		XmlObject actNative = a.getNativeActivity();
		tempNative.set(actNative);
		
		a.setNativeObject(tempNative);
		XmlCursor c = actNative.newCursor();
		try {
			c.removeXml();
		} catch(IllegalStateException e) {
			// ignore: Can happen if native activity is a document or has already been removed
		} finally {
			c.dispose();
		}
		
		wrappedActivities.add(a);
		wrappedActivities.remove(tempActivity);
		a.reparent(this);
	}
	
	private void updateNativeActivities() {
		extractNativeActivities(activity);
		
		for(int i = 0; i < activities.size(); i++) {
			wrappedActivities.get(i).setNativeObject(activities.get(i));
		}
	}
	
	@Override
	public void remove(IActivity a) {
		AbstractActivity<?> activityToDelete = (AbstractActivity<?>)a;
		int index = wrappedActivities.indexOf(a);
		
		if(index < 0) {
			throw new RuntimeException(activityToDelete.getName() + " is not a child of this activity.");
		}
		
		wrappedActivities.remove(activityToDelete);
		XmlCursor cursor = activityToDelete.getNativeActivity().newCursor();
		cursor.removeXml();
		cursor.dispose();
		
		activityToDelete.reparent(null);
		updateNativeActivities();
	}
	
	@Override
	public void moveBefore(IActivity toMove, IActivity moveBefore) {
		if(toMove == moveBefore) {
			return;
		}
		
		int newIndex = wrappedActivities.indexOf(moveBefore);
		int oldIndex = wrappedActivities.indexOf(toMove);
		
		if(newIndex < 0 || oldIndex < 0) {
			throw new RuntimeException("One of the activities is not a child of this activity");
		}
		
		XmlCursor moveToCursor = ((AbstractActivity<?>)moveBefore).getNativeActivity().newCursor();
		XmlCursor moveFromCursor = ((AbstractActivity<?>)toMove).getNativeActivity().newCursor();
		
		moveFromCursor.moveXml(moveToCursor);
		
		moveToCursor.dispose();
		moveFromCursor.dispose();
		
		wrappedActivities.remove(toMove);
		if(oldIndex < newIndex) {
			newIndex--;
		}
		wrappedActivities.add(newIndex, (AbstractActivity<?>)toMove);
		
		updateNativeActivities();
	}
	
	@Override
	public void moveToEnd(IActivity toMove) {
		Empty dummyActivity = addEmpty();
		
		moveBefore(toMove, dummyActivity);
		remove(dummyActivity);
	}
	
	@Override
	public void unregister(AbstractActivity<?> a) {
		int index = wrappedActivities.indexOf(a);
		
		if(index >= 0) {
			wrappedActivities.remove(index);
			XmlObject nativeActivity = activities.get(index);
			
			XmlCursor c = activity.newCursor();
			for(c.toFirstChild(); c.toNextSibling(); ) {
				if(c.getObject() == nativeActivity) {
					c.removeXml();
					break;
				}
			}
			c.dispose();
			activities.remove(index);
		}
	}
}
