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

	private TScope scope;
	private List<Variable> variables = new ArrayList<Variable>();
	private List<OnAlarmEventHandler> onAlarms = new ArrayList<OnAlarmEventHandler>();
	private List<OnMessageHandler> onMessages = new ArrayList<OnMessageHandler>();
	private CompensationHandler compensationHandler;
	
	public Scope(TScope wrappedScope) {
		super(wrappedScope);
		this.scope = wrappedScope;
		
		if (!wrappedScope.isSetVariables()) {
			wrappedScope.addNewVariables();
		}
		for (TVariable v : wrappedScope.getVariables().getVariableArray()) {
			variables.add(new Variable(v));
		}
		
		if(scope.getEventHandlers() != null) {
			for(TOnAlarmEvent e : scope.getEventHandlers().getOnAlarmArray()) {
				onAlarms .add(new OnAlarmEventHandler(e));
			}
			for(TOnEvent e : scope.getEventHandlers().getOnEventArray()) {
				onMessages.add(new OnMessageHandler(e));
			}
		}
		
		if(scope.getCompensationHandler() != null) {
			compensationHandler = new CompensationHandler(scope.getCompensationHandler());
		}
	}

	@Override
	TScope getNativeActivity() {
		return this.scope;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		AbstractActivity<?> mainActivity = getMainActivity();
		if(mainActivity != null) {
			mainActivity.visit(v);
		}
		for(OnAlarmEventHandler e : onAlarms) {
			e.visit(v);
		}
		for(OnMessageHandler e : onMessages) {
			e.visit(v);
		}
		if(compensationHandler != null) {
			compensationHandler.visit(v);
		}
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == scope) {
			return this;
		} else {
			if(scope != null) {
				return getMainActivity().getObjectForNativeObject(nativeObject);
			}
		}
		return null;
	}
	
	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	public IVariable addVariable() {
		TVariable nativeVariable = scope.getVariables().addNewVariable();

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
		
		TOnAlarmEvent nativeOnAlarm = scope.getEventHandlers().addNewOnAlarm();
		OnAlarmEventHandler onAlarm = new OnAlarmEventHandler(nativeOnAlarm);
		onAlarms.add(onAlarm);
		
		return onAlarm;
	}

	private void createEventHandlersIfNecessary() {
		if(scope.getEventHandlers() == null) {
			scope.addNewEventHandlers();
		}
	}

	@Override
	public OnMessageHandler addNewOnMessage() {
		createEventHandlersIfNecessary();
		
		TOnEvent nativeOnMessage = scope.getEventHandlers().addNewOnEvent();
		OnMessageHandler onMessage = new OnMessageHandler(nativeOnMessage);
		onMessages.add(onMessage);
		
		return onMessage;
	}

	@Override
	public CompensationHandler setNewCompensationHandler() {
		scope.unsetCompensationHandler();
		
		compensationHandler = new CompensationHandler(scope.addNewCompensationHandler());
		return compensationHandler;
	}

	@Override
	public CompensationHandler getCompensationHandler() {
		return compensationHandler;
	}

}
