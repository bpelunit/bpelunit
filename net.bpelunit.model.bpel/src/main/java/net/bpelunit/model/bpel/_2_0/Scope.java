package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmEvent;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnEvent;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

class Scope extends AbstractSingleContainer<TScope> implements IScope {

	private List<Variable> variables = new ArrayList<Variable>();
	private List<OnAlarmEventHandler> onAlarms = new ArrayList<OnAlarmEventHandler>();
	private List<OnMessageHandler> onMessages = new ArrayList<OnMessageHandler>();
	private CompensationHandler compensationHandler;

	public Scope(TScope wrappedScope, IContainer parent) {
		super(wrappedScope, parent);

		setNativeObjectInternal(wrappedScope);
	}

	@Override
	void setNativeObject(Object o) {
		super.setNativeObject(o);
		setNativeObjectInternal((TScope)o);
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
					.getCompensationHandler());
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
		if(!getNativeActivity().isSetVariables()) {
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
		OnAlarmEventHandler onAlarm = new OnAlarmEventHandler(nativeOnAlarm, this);
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
		getNativeActivity().unsetCompensationHandler();

		compensationHandler = new CompensationHandler(getNativeActivity()
				.addNewCompensationHandler());
		return compensationHandler;
	}

	@Override
	public CompensationHandler getCompensationHandler() {
		return compensationHandler;
	}

	

}
