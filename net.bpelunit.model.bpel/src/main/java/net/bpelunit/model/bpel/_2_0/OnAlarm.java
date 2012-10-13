package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IOnAlarm;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;

public class OnAlarm extends AbstractSingleContainer<TOnAlarmPick> implements IOnAlarm {

	OnAlarm(TOnAlarmPick nativeOnAlarm) {
		super(nativeOnAlarm);
	}
}
