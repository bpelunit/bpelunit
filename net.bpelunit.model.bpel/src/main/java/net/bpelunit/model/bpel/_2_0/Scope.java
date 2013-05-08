package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCatch;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmEvent;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnEvent;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

class Scope extends AbstractSingleContainer<TScope> implements IScope {

	private List<Variable> variables = new ArrayList<Variable>();
	private List<OnAlarmEventHandler> onAlarms = new ArrayList<OnAlarmEventHandler>();
	private List<OnMessageHandler> onMessages = new ArrayList<OnMessageHandler>();
	private CompensationHandler compensationHandler;
	private List<Catch> catches = new ArrayList<Catch>();
	private CatchAll catchAll;

	public Scope(TScope wrappedScope, IContainer parent) {
		super(wrappedScope, parent);
		setNativeObjectInternal(wrappedScope);

		if (wrappedScope.isSetCompensationHandler()) {
			compensationHandler = new CompensationHandler(
					wrappedScope.getCompensationHandler(), this);
		}
		if (wrappedScope.isSetFaultHandlers()) {
			for (TCatch c : wrappedScope.getFaultHandlers().getCatchArray()) {
				catches.add(new Catch(c, this));
			}
			if (wrappedScope.getFaultHandlers().isSetCatchAll()) {
				catchAll = new CatchAll(wrappedScope.getFaultHandlers().getCatchAll(), this);
			}
		}
	}

	@Override
	void setNativeObject(Object o) {
		super.setNativeObject(o);
		setNativeObjectInternal((TScope) o);
	}

	private final void setNativeObjectInternal(TScope wrappedScope) {
		variables.clear();
		if (wrappedScope.isSetVariables()) {
			for (TVariable v : wrappedScope.getVariables().getVariableArray()) {
				variables.add(new Variable(v));
			}
		}

		onAlarms.clear();
		onMessages.clear();
		if (getNativeActivity().getEventHandlers() != null) {
			for (TOnAlarmEvent e : getNativeActivity().getEventHandlers()
					.getOnAlarmArray()) {
				onAlarms.add(new OnAlarmEventHandler(e, this));
			}
			for (TOnEvent e : getNativeActivity().getEventHandlers()
					.getOnEventArray()) {
				onMessages.add(new OnMessageHandler(e, this));
			}
		}

		if (getNativeActivity().getCompensationHandler() != null) {
			compensationHandler = new CompensationHandler(getNativeActivity()
					.getCompensationHandler(), this);
		} else {
			compensationHandler = null;
		}
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		AbstractActivity<?> mainActivity = getMainActivity();
		if (mainActivity != null) {
			mainActivity.visit(v);
		}
		for (OnAlarmEventHandler e : onAlarms) {
			e.visit(v);
		}
		for (OnMessageHandler e : onMessages) {
			e.visit(v);
		}
		if (compensationHandler != null) {
			compensationHandler.visit(v);
		}
		if (catchAll != null) {
			catchAll.visit(v);
		}
		for (Catch c : catches) {
			c.visit(v);
		}
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if (nativeObject == getNativeActivity()) {
			return this;
		} else {
			if (getNativeActivity() != null) {
				return getMainActivity().getObjectForNativeObject(nativeObject);
			}
		}
		return null;
	}

	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	public IVariable addVariable() {
		if (!getNativeActivity().isSetVariables()) {
			getNativeActivity().addNewVariables();
		}
		TVariable nativeVariable = getNativeActivity().getVariables()
				.addNewVariable();

		Variable variable = new Variable(nativeVariable);
		this.variables.add(variable);
		return variable;
	}

	@Override
	public List<OnAlarmEventHandler> getOnAlarms() {
		return new ArrayList<OnAlarmEventHandler>(onAlarms);
	}

	@Override
	public List<OnMessageHandler> getOnMessages() {
		return new ArrayList<OnMessageHandler>(onMessages);
	}

	@Override
	public IOnAlarmEventHandler addNewOnAlarm() {
		createEventHandlersIfNecessary();

		TOnAlarmEvent nativeOnAlarm = getNativeActivity().getEventHandlers()
				.addNewOnAlarm();
		OnAlarmEventHandler onAlarm = new OnAlarmEventHandler(nativeOnAlarm,
				this);
		onAlarms.add(onAlarm);

		return onAlarm;
	}

	private void createEventHandlersIfNecessary() {
		if (getNativeActivity().getEventHandlers() == null) {
			getNativeActivity().addNewEventHandlers();
		}
	}

	@Override
	public OnMessageHandler addNewOnMessage() {
		createEventHandlersIfNecessary();

		TOnEvent nativeOnMessage = getNativeActivity().getEventHandlers()
				.addNewOnEvent();
		OnMessageHandler onMessage = new OnMessageHandler(nativeOnMessage, this);
		onMessages.add(onMessage);

		return onMessage;
	}

	@Override
	public CompensationHandler setNewCompensationHandler() {
		if (compensationHandler != null) {
			getNativeActivity().unsetCompensationHandler();
		}

		compensationHandler = new CompensationHandler(getNativeActivity()
				.addNewCompensationHandler(), this);
		return compensationHandler;
	}

	@Override
	public CompensationHandler getCompensationHandler() {
		return compensationHandler;
	}

	@Override
	public Catch addNewCatch() {
		addFaultHandlersIfNcessary();

		TCatch nativeCatch = getNativeActivity().getFaultHandlers()
				.addNewCatch();
		Catch c = new Catch(nativeCatch, this);
		catches.add(c);

		return c;
	}

	private void addFaultHandlersIfNcessary() {
		if (getNativeActivity().getFaultHandlers() == null) {
			getNativeActivity().addNewFaultHandlers();
		}
	}

	@Override
	public CatchAll setNewCatchAll() {
		if (getNativeActivity().getFaultHandlers() != null
				&& getNativeActivity().getFaultHandlers().getCatchAll() != null) {
			getNativeActivity().getFaultHandlers().unsetCatchAll();
			catchAll = null;
		}

		addFaultHandlersIfNcessary();

		TActivityContainer nativeCatchAll = getNativeActivity()
				.getFaultHandlers().addNewCatchAll();
		catchAll = new CatchAll(nativeCatchAll, this);

		return catchAll;
	}

}
