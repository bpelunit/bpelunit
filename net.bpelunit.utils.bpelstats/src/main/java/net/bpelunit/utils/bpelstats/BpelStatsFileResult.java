package net.bpelunit.utils.bpelstats;

import java.util.Map;

public interface BpelStatsFileResult {

	public abstract int getCountAssign();

	public abstract int getCountCatch();

	public abstract int getCountCatchAll();

	public abstract int getCountCompensate();

	public abstract int getCountCompensateScope();

	public abstract int getCountCompensationHandler();

	public abstract int getCountCopy();

	public abstract int getCountElse();

	public abstract int getCountElseIf();

	public abstract int getCountEmpty();

	public abstract int getCountExit();

	public abstract int getCountFlow();

	public abstract int getCountForEach();

	public abstract int getCountIf();

	public abstract int getCountInvoke();

	public abstract int getCountLink();

	public abstract int getCountOnAlarm();

	public abstract int getCountOnAlarmHandler();

	public abstract int getCountOnMessage();

	public abstract int getCountOnMessageHandler();

	public abstract int getCountPartnerLink();

	public abstract int getCountPick();

	public abstract int getCountReceive();

	public abstract int getCountRepeatUntil();

	public abstract int getCountReply();

	public abstract int getCountRethrow();

	public abstract int getCountScope();

	public abstract int getCountSequence();

	public abstract int getCountThrow();

	public abstract int getCountValidate();

	public abstract int getCountVariable();

	public abstract int getCountWait();

	public abstract int getCountWhile();

	public abstract int getCountAllActivities();

	public abstract int getCountBasicActivities();

	public abstract int getCountStructuredActivities();

	public abstract int getCountNonLinearActivities();
	
	public abstract int getCountExtensionActivities();
	
	public abstract int getCountActiveVOSSuspend();
	public abstract int getCountActiveVOSSignalSend();
	public abstract int getCountActiveVOSSignalReceive();
	public abstract int getCountActiveVOSOnSignal();
	
	public abstract int getCountB4PPeopleActivity();

	public abstract int getCountWPSFlow();
	
	public abstract Map<String, Integer> getUsedExtensionActivities();

	public abstract int getCountActiveVOSContinue();

	public abstract int getCountActiveVOSBreak();
}