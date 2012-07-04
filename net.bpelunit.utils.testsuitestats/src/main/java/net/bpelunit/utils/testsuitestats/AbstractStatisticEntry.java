package net.bpelunit.utils.testsuitestats;


abstract class AbstractStatisticEntry implements IStatisticEntry {

	@Override
	public String toString() {
		return String.format("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%s",
				getTitle(), 
				getCountAllReceives(),
				getCountAllSends(),
				getCountCompleteHumanTask(),
				getCountReceiveOnly(),
				getCountReceiveSend(),
				getCountSendOnly(),
				getCountSendReceive(),
				getCountSendReceiveAsync(),
				getCountReceiveSendAsync(),
				getCountWait(),
				isClientTrackUsed() + ""
		);
	}

}
