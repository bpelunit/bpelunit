package net.bpelunit.model.bpel;

import java.util.List;

public interface IPick extends IActivity, IActivityContainer, ICreateInstance {
	List<?extends IOnMessage> getOnMessages();
	List<?extends IOnAlarm> getOnAlarms();
	
	IOnMessage addOnMessage();
	IOnAlarm addOnAlarm();
}
