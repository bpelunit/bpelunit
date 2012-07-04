package net.bpelunit.utils.testsuitestats;

import java.util.Collections;
import java.util.List;

public class StatisticLeaf extends AbstractStatisticEntry {
	private int countCompleteHumanTask;
	private int countReceiveOnly;
	private int countReceiveSend;
	private int countReceiveSendAsync;
	private int countSendOnly;
	private int countSendReceive;
	private int countSendReceiveAsync;
	private int countWait;
	private boolean partnerTrackUsed;
	private String title;

	/**
	 * Constructor for this leaf. A leaf does not contain child nodes but
	 * instead contains all the values for the different activities.
	 * 
	 * @param initalTitle
	 * @param completeHumanTask
	 * @param receiveOnly
	 * @param receiveSend
	 * @param sendOnly
	 * @param sendReceive
	 * @param wait
	 * @param receiveSendAsync
	 * @param sendReceiveAsync
	 * @param isPartnerTrackUsed
	 */
	public StatisticLeaf(String initalTitle, int completeHumanTask,
			int receiveOnly, int receiveSend, int sendOnly, int sendReceive,
			int wait, int receiveSendAsync, int sendReceiveAsync,
			boolean isPartnerTrackUsed) {
		this.title = initalTitle;
		this.countCompleteHumanTask = completeHumanTask;
		this.countReceiveOnly = receiveOnly;
		this.countReceiveSend = receiveSend;
		this.countSendOnly = sendOnly;
		this.countSendReceive = sendReceive;
		this.countWait = wait;
		this.countSendReceiveAsync = sendReceiveAsync;
		this.countReceiveSendAsync = receiveSendAsync;
		this.partnerTrackUsed = isPartnerTrackUsed;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountAllReceives()
	 */
	@Override
	public int getCountAllReceives() {
		return countReceiveOnly + countSendReceive + countReceiveSend + countSendReceiveAsync + countReceiveSendAsync;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountAllSends()
	 */
	@Override
	public int getCountAllSends() {
		return countSendOnly + countSendReceive + countReceiveSend + countSendReceiveAsync + countReceiveSendAsync;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountCompleteHumanTask()
	 */
	@Override
	public int getCountCompleteHumanTask() {
		return countCompleteHumanTask;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountReceiveOnly()
	 */
	@Override
	public int getCountReceiveOnly() {
		return countReceiveOnly;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountReceiveSend()
	 */
	@Override
	public int getCountReceiveSend() {
		return countReceiveSend;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountSendOnly()
	 */
	@Override
	public int getCountSendOnly() {
		return countSendOnly;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountSendReceive()
	 */
	@Override
	public int getCountSendReceive() {
		return countSendReceive;
	}

	/**
	 * @see net.bpelunit.utils.testsuitestats.IStatisticEntry#getCountWait()
	 */
	@Override
	public int getCountWait() {
		return countWait;
	}

	/**
	 * @see IStatisticEntry#isClientTrackUsed()
	 */
	@Override
	public boolean isClientTrackUsed() {
		return partnerTrackUsed;
	}

	/**
	 * @see IStatisticEntry#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @see IStatisticEntry#getCountReceiveSendAsync()
	 */
	@Override
	public int getCountReceiveSendAsync() {
		return countReceiveSendAsync;
	}

	/**
	 * @see IStatisticEntry#getCountSendReceiveAsync()
	 */
	@Override
	public int getCountSendReceiveAsync() {
		return countSendReceiveAsync;
	}

	/**
	 * @see IStatisticEntry#getSubStatistics()
	 */
	@Override
	public List<IStatisticEntry> getSubStatistics() {
		return Collections.emptyList();
	}

}