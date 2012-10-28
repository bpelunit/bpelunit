package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmEvent;

public class OnAlarmEventHandler extends AbstractSingleContainer<TOnAlarmEvent> implements IOnAlarmEventHandler {

	public OnAlarmEventHandler(TOnAlarmEvent nativeOnAlarm, IActivityContainer parent) {
		super(nativeOnAlarm, parent);
	}

}
