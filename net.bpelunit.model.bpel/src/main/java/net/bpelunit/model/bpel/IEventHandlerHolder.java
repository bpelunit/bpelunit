package net.bpelunit.model.bpel;

import java.util.List;

public interface IEventHandlerHolder {

	List<?extends IOnAlarmEventHandler> getOnAlarms();
	List<?extends IOnMessageHandler> getOnMessages();

	IOnAlarmEventHandler addNewOnAlarm();
	IOnMessageHandler addNewOnMessage();
}
