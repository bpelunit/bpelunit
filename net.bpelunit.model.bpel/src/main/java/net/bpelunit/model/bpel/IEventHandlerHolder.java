package net.bpelunit.model.bpel;

import java.util.List;

public interface IEventHandlerHolder {

	List<?extends IOnAlarmEventHandler> getOnAlarms();
	List<?extends IOnMessage> getOnMessages();

	IOnAlarmEventHandler addNewOnAlarm();
	IOnMessage addNewOnMessage();
}
