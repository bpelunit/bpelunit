package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;

public class OnAlarm extends AbstractSingleContainer<TOnAlarmPick> implements IOnAlarm {

	public OnAlarm(TOnAlarmPick nativeOnAlarm, IContainer parent) {
		super(nativeOnAlarm, parent);
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		if(getMainActivity() != null) {
			getMainActivity().visit(v);
		}
	}
	
	@Override
	public String toString() {
		return "OnAlarm";
	}
}
