package net.bpelunit.utils.testsuitestats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticGroup extends AbstractStatisticEntry {

	private List<IStatisticEntry> entries = new ArrayList<IStatisticEntry>();
	private String title;

	public StatisticGroup(String initialTitle) {
		title = initialTitle;
	}

	public StatisticGroup(String title, List<IStatisticEntry> initialEntries) {
		this(title);
		entries.addAll(initialEntries);
	}
	
	public void add(IStatisticEntry e) {
		this.entries.add(e);
	}

	@Override
	public int getCountAllReceives() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountAllReceives();
		}

		return value;
	}

	@Override
	public int getCountAllSends() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountAllSends();
		}

		return value;
	}

	@Override
	public int getCountCompleteHumanTask() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountCompleteHumanTask();
		}

		return value;
	}

	@Override
	public int getCountReceiveOnly() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountReceiveOnly();
		}

		return value;
	}

	@Override
	public int getCountReceiveSend() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountReceiveSend();
		}

		return value;
	}

	@Override
	public int getCountSendOnly() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountSendOnly();
		}

		return value;
	}

	@Override
	public int getCountSendReceive() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountSendReceive();
		}

		return value;
	}

	@Override
	public int getCountWait() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountWait();
		}

		return value;
	}

	@Override
	public boolean isClientTrackUsed() {
		boolean value = false;

		for (IStatisticEntry e : entries) {
			value |= e.isClientTrackUsed();
		}

		return value;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getCountReceiveSendAsync() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountReceiveSendAsync();
		}

		return value;
	}

	@Override
	public int getCountSendReceiveAsync() {
		int value = 0;

		for (IStatisticEntry e : entries) {
			value += e.getCountSendReceiveAsync();
		}

		return value;
	}

	@Override
	public List<IStatisticEntry> getSubStatistics() {
		return Collections.unmodifiableList(entries);
	}

}
