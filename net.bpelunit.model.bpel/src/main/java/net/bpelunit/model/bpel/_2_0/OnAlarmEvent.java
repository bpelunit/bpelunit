package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IOnAlarm;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmEvent;

public class OnAlarmEvent extends AbstractSingleContainer<TOnAlarmEvent> implements IOnAlarm {

	public OnAlarmEvent(TOnAlarmEvent onAlarmEvent, IActivityContainer parent) {
		super(onAlarmEvent, parent);
	}
	
}
