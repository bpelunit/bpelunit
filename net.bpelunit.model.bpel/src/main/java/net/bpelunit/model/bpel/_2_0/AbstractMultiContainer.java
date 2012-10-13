package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IMultiContainer;

import org.apache.xmlbeans.XmlCursor;
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
		AbstractActivity<T> implements IMultiContainer {

	private List<AbstractActivity<?>> wrappedActivities = new ArrayList<AbstractActivity<?>>();
	private List<TActivity> activities = new ArrayList<TActivity>();

	AbstractMultiContainer(T wrappedActivity) {
		super(wrappedActivity);

		this.activities = new ArrayList<TActivity>();
		XmlCursor cursor = wrappedActivity.newCursor();
		int childNo = 0;
		for (boolean hasNext = cursor.toFirstChild(); hasNext; hasNext = cursor.toNextSibling()) {
			if (cursor.getObject() instanceof TActivity) {
				activities.add((TActivity) cursor.getObject());
			}
			childNo++;
		}
		cursor.dispose();

		for (TActivity child : activities) {
			wrappedActivities.add(BpelFactory.INSTANCE.createWrapper(child));
		}
	}

	public List<AbstractActivity<?>> getActivities() {
		return Collections.unmodifiableList(wrappedActivities);
	}

	public void addActivity(IActivity a) {
		AbstractActivity<?> activity = checkForCorrectModel(a);

		wrappedActivities.add(activity);
		activities.add((TActivity) activity.getNativeActivity());
	}

	public void removeActivity(IActivity a) {
		AbstractActivity<?> activity = checkForCorrectModel(a);

		int i = activities.indexOf(activity.getNativeActivity());

		wrappedActivities.remove(i);
		activities.remove(i);
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

	@Override
	public Reply addReply() {
		try {
			TReply nativeReply = (TReply) addNativeActivity("Reply");
			Reply reply = new Reply(nativeReply);
			activities.add(nativeReply);
			wrappedActivities.add(reply);
			return reply;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Assign addAssign() {
		try {
			TAssign nativeAssign = (TAssign) addNativeActivity("Assign");
			Assign assign = new Assign(nativeAssign);
			activities.add(nativeAssign);
			wrappedActivities.add(assign);
			return assign;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Receive addReceive() {
		try {
			TReceive nativeReceive = (TReceive) addNativeActivity("Receive");
			Receive receive = new Receive(nativeReceive);
			activities.add(nativeReceive);
			wrappedActivities.add(receive);
			return receive;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Compensate addCompensate() {
		try {
			TCompensate nativeReceive = (TCompensate) addNativeActivity("Compensate");
			Compensate compensate = new Compensate(nativeReceive);
			activities.add(nativeReceive);
			wrappedActivities.add(compensate);
			return compensate;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public CompensateScope addCompensateScope() {
		try {
			TCompensateScope nativeCompensateScope = (TCompensateScope) addNativeActivity("CompensateScope");
			CompensateScope compensateScope = new CompensateScope(nativeCompensateScope);
			activities.add(nativeCompensateScope);
			wrappedActivities.add(compensateScope);
			return compensateScope;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Empty addEmpty() {
		try {
			TEmpty nativeEmpty = (TEmpty) addNativeActivity("Empty");
			Empty empty = new Empty(nativeEmpty);
			activities.add(nativeEmpty);
			wrappedActivities.add(empty);
			return empty;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Exit addExit() {
		try {
			TExit nativeExit = (TExit) addNativeActivity("Exit");
			Exit exit = new Exit(nativeExit);
			activities.add(nativeExit);
			wrappedActivities.add(exit);
			return exit;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Flow addFlow() {
		try {
			TFlow nativeFlow = (TFlow) addNativeActivity("Flow");
			Flow flow = new Flow(nativeFlow);
			activities.add(nativeFlow);
			wrappedActivities.add(flow);
			return flow;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public ForEach addForEach() {
		try {
			TForEach nativeForEach = (TForEach) addNativeActivity("ForEach");
			ForEach forEach = new ForEach(nativeForEach);
			activities.add(nativeForEach);
			wrappedActivities.add(forEach);
			return forEach;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public If addIf() {
		try {
			TIf nativeIf = (TIf) addNativeActivity("If");
			If receive = new If(nativeIf);
			activities.add(nativeIf);
			wrappedActivities.add(receive);
			return receive;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Invoke addInvoke() {
		try {
			TInvoke nativeInvoke = (TInvoke) addNativeActivity("Invoke");
			Invoke invoke = new Invoke(nativeInvoke);
			activities.add(nativeInvoke);
			wrappedActivities.add(invoke);
			return invoke;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Pick addPick() {
		try {
			TPick nativePick = (TPick) addNativeActivity("Pick");
			Pick pick = new Pick(nativePick);
			activities.add(nativePick);
			wrappedActivities.add(pick);
			return pick;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public RepeatUntil addRepeatUntil() {
		try {
			TRepeatUntil nativeRepeatUntil = (TRepeatUntil) addNativeActivity("RepeatUntil");
			RepeatUntil repeatUntil = new RepeatUntil(nativeRepeatUntil);
			activities.add(nativeRepeatUntil);
			wrappedActivities.add(repeatUntil);
			return repeatUntil;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Rethrow addRethrow() {
		try {
			TRethrow nativeRethrow = (TRethrow) addNativeActivity("Rethrow");
			Rethrow rethrow = new Rethrow(nativeRethrow);
			activities.add(nativeRethrow);
			wrappedActivities.add(rethrow);
			return rethrow;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Scope addScope() {
		try {
			TScope nativeScope = (TScope) addNativeActivity("Scope");
			Scope scope = new Scope(nativeScope);
			activities.add(nativeScope);
			wrappedActivities.add(scope);
			return scope;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Sequence addSequence() {
		try {
			TSequence nativeSequence = (TSequence) addNativeActivity("Sequence");
			Sequence sequence = new Sequence(nativeSequence);
			activities.add(nativeSequence);
			wrappedActivities.add(sequence);
			return sequence;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Throw addThrow() {
		try {
			TThrow nativeThrow = (TThrow) addNativeActivity("Throw");
			Throw tthrow = new Throw(nativeThrow);
			activities.add(nativeThrow);
			wrappedActivities.add(tthrow);
			return tthrow;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Validate addValidate() {
		try {
			TValidate nativeValidate = (TValidate) addNativeActivity("Validate");
			Validate validate = new Validate(nativeValidate);
			activities.add(nativeValidate);
			wrappedActivities.add(validate);
			return validate;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public Wait addWait() {
		try {
			TWait nativeWait = (TWait) addNativeActivity("Wait");
			Wait wait = new Wait(nativeWait);
			activities.add(nativeWait);
			wrappedActivities.add(wait);
			return wait;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	@Override
	public While addWhile() {
		try {
			TWhile nativeWhile = (TWhile) addNativeActivity("While");
			While wwhile = new While(nativeWhile);
			activities.add(nativeWhile);
			wrappedActivities.add(wwhile);
			return wwhile;
		} catch (Exception e) {
			throw new RuntimeException("Error in configuration.", e);
		}
	}

	private TActivity addNativeActivity(String activityName)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method m = getNativeActivity().getClass().getMethod(
				"addNew" + activityName);
		return (TActivity) m.invoke(getNativeActivity());
	}

}
