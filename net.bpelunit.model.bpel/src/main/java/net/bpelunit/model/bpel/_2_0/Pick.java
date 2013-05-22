package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IOnMessage;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;

public class Pick extends AbstractActivity<TPick> implements IPick, IContainer {

	private List<OnMessage> onMessages = new ArrayList<OnMessage>();
	private List<OnAlarm> onAlarms = new ArrayList<OnAlarm>();

	public Pick(TPick wrappedPick, IContainer parent) {
		super(wrappedPick, parent);

		setNativeObject(wrappedPick);
	}

	void setNativeObject(Object wrappedPick) {
		super.setNativeObject(wrappedPick);
		onMessages.clear();
		for (TOnMessage m : getNativeActivity().getOnMessageArray()) {
			onMessages.add(new OnMessage(m, this));
		}

		onAlarms.clear();
		for (TOnAlarmPick a : getNativeActivity().getOnAlarmArray()) {
			onAlarms.add(new OnAlarm(a, this));
		}
	}

	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for (IOnMessage m : onMessages) {
			m.visit(v);
		}

		for (IOnAlarm a : onAlarms) {
			a.visit(v);
		}
	}

	public void setCreateInstance(boolean b) {
		if (b) {
			getNativeActivity().setCreateInstance(TBoolean.YES);
		} else {
			getNativeActivity().setCreateInstance(TBoolean.NO);
		}
	}

	public boolean isCreateInstance() {
		return TBoolean.YES.equals(getNativeActivity().getCreateInstance());
	}

	public List<? extends IOnMessage> getOnMessages() {
		return Collections.unmodifiableList(onMessages);
	}

	public List<? extends IOnAlarm> getOnAlarms() {
		return Collections.unmodifiableList(onAlarms);
	}

	public OnMessage addOnMessage() {
		TOnMessage nativeOnMessage = getNativeActivity().addNewOnMessage();
		OnMessage onMessage = new OnMessage(nativeOnMessage, this);
		onMessages.add(onMessage);

		return onMessage;
	}

	public OnAlarm addOnAlarm() {
		TOnAlarmPick nativeOnAlarm = getNativeActivity().addNewOnAlarm();
		OnAlarm onAlarm = new OnAlarm(nativeOnAlarm, this);
		onAlarms.add(onAlarm);

		return onAlarm;
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if (nativeObject == getNativeActivity()) {
			return this;
		}

		for (OnMessage m : onMessages) {
			IBpelObject o = m.getObjectForNativeObject(nativeObject);
			if (o != null) {
				return o;
			}
		}

		for (OnAlarm a : onAlarms) {
			IBpelObject o = a.getObjectForNativeObject(nativeObject);
			if (o != null) {
				return o;
			}
		}

		return null;
	}

	@Override
	public IScope wrapActivityInNewScope(IActivity childActivity) {
		throw new IllegalArgumentException(
				"Cannot wrap any branches (onMessage, onAlarm) of a pick activity");
	}

	@Override
	public void unregister(AbstractActivity<?> a) {
		throw new IllegalArgumentException(
				"Cannot unregister any branches (onMessage, onAlarm) of a pick activity");
	}

	@Override
	public ISequence wrapActivityInNewSequence(IActivity childActivity) {
		throw new IllegalArgumentException(
		"Cannot wrap any branches (onMessage, onAlarm) of a pick activity");
	}
}
