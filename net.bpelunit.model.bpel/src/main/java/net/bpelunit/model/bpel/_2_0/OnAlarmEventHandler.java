package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmEvent;

public class OnAlarmEventHandler extends AbstractSingleContainer<TOnAlarmEvent> implements IOnAlarmEventHandler {

	public OnAlarmEventHandler(TOnAlarmEvent nativeOnAlarm, IContainer parent) {
		super(nativeOnAlarm, parent);
		if(getScope() == null) {
			this.setNewScope();
		}
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		if(getMainActivity() != null) {
			getMainActivity().visit(v);
		}
	}
	
	public Scope getScope() {
		return (Scope)getMainActivity();
	}
	
	@Override
	public String toString() {
		return "OnAlarmEvent";
	}
}
