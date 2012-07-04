package net.bpelunit.utils.testsuitestats;

import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTrack;

public class StatisticsGatherer {

	public IStatisticEntry gatherStatistics(XMLTestSuite ts) {
		StatisticGroup testSuiteStats = new StatisticGroup(ts.getName());

		for (XMLTestCase tc : ts.getTestCases().getTestCaseList()) {
			testSuiteStats.add(gatherStatistics(tc));
		}

		return testSuiteStats;
	}

	private IStatisticEntry gatherStatistics(XMLTestCase tc) {
		StatisticGroup testCaseStats = new StatisticGroup(tc.getName());

		for (XMLHumanPartnerTrack h : tc.getHumanPartnerTrackList()) {
			testCaseStats.add(gatherStatistics(h, tc.getName()));
		}

		for (XMLPartnerTrack p : tc.getPartnerTrackList()) {
			testCaseStats.add(gatherStatistics(p, tc.getName()));
		}

		testCaseStats.add(gatherStatistics(tc.getClientTrack(), tc.getName()));

		return testCaseStats;
	}

	private IStatisticEntry gatherStatistics(XMLTrack c,
			String testCaseName) {
		int countReceiveOnly = c.getReceiveOnlyList().size();
		int countReceiveSend = c.getReceiveSendList().size();
		int countSendOnly = c.getSendOnlyList().size();
		int countSendReceive = c.getSendReceiveList().size();
		int countWait = c.getWaitList().size();
		int countReceiveSendAsync = c.getReceiveSendAsynchronousList().size();
		int countSendReceiveAsync = c.getSendReceiveAsynchronousList().size();
		boolean partnerTrackUsed = (0 + countReceiveOnly + countReceiveSend
				+ countSendOnly + countSendReceive + countWait) > 0;

		AbstractStatisticEntry partnerStats = new StatisticLeaf(testCaseName + "."
				+ "client", 0, countReceiveOnly, countReceiveSend,
				countSendOnly, countSendReceive, countWait,
				countSendReceiveAsync, countReceiveSendAsync, partnerTrackUsed);

		return partnerStats;
	}

	private IStatisticEntry gatherStatistics(XMLHumanPartnerTrack h,
			String testCaseName) {
		int countCompleteHumanTask = h.getCompleteHumanTaskList().size();
		AbstractStatisticEntry partnerStats = new StatisticLeaf(testCaseName + "."
				+ h.getName(), countCompleteHumanTask, 0, 0, 0, 0, 0, 0, 0,
				false);

		return partnerStats;
	}

	private IStatisticEntry gatherStatistics(XMLPartnerTrack p,
			String testCaseName) {
		int countReceiveOnly = p.getReceiveOnlyList().size();
		int countReceiveSend = p.getReceiveSendList().size();
		int countSendOnly = p.getSendOnlyList().size();
		int countSendReceive = p.getSendReceiveList().size();
		int countWait = p.getWaitList().size();
		int countReceiveSendAsync = p.getReceiveSendAsynchronousList().size();
		int countSendReceiveAsync = p.getSendReceiveAsynchronousList().size();

		AbstractStatisticEntry partnerStats = new StatisticLeaf(testCaseName + "."
				+ p.getName(), 0, countReceiveOnly, countReceiveSend,
				countSendOnly, countSendReceive, countWait,
				countSendReceiveAsync, countReceiveSendAsync, false);

		return partnerStats;
	}

}
