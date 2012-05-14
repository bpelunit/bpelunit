package net.bpelunit.utils.testsuitestats;

import java.util.List;

public interface IStatisticEntry {

	String getTitle();
	
	int getCountAllReceives();

	int getCountAllSends();

	int getCountCompleteHumanTask();

	int getCountReceiveOnly();

	int getCountReceiveSend();

	int getCountReceiveSendAsync();

	int getCountSendOnly();

	int getCountSendReceive();
	
	int getCountSendReceiveAsync();

	int getCountWait();

	boolean isClientTrackUsed();

	List<IStatisticEntry> getSubStatistics();
}