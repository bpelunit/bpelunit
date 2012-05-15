package net.bpelunit.utils.testsuitestats;

import static org.junit.Assert.*;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;

import org.junit.Test;

public class StatisticsGathererTest {

	@Test
	public void testGatheringBPTSWithOneTestCaseWithClientTrackAndPartnerTrack() throws Exception {
		XMLTestSuiteDocument bptsDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestSuite ts = bptsDoc.addNewTestSuite();
		XMLTestCasesSection tcs = ts.addNewTestCases();
		XMLTestCase tc = tcs.addNewTestCase();
		tc.setName("TC1");
		createClientTrack(tc);
		createPartnerTrack(tc);
		
		IStatisticEntry stats = new StatisticsGatherer().gatherStatistics(bptsDoc.getTestSuite());
		
		assertEquals("One Test Case", 1, stats.getSubStatistics().size());
		assertEquals("Two Tracks", 2, stats.getSubStatistics().get(0).getSubStatistics().size());
		
		assertEquals(0, stats.getCountCompleteHumanTask());
		assertTrue(stats.isClientTrackUsed());
		assertEquals(40, stats.getCountAllReceives());
		assertEquals(40, stats.getCountAllSends());
		assertEquals(8, stats.getCountReceiveOnly());
		assertEquals(8, stats.getCountReceiveSend());
		assertEquals(8, stats.getCountReceiveSendAsync());
		assertEquals(8, stats.getCountSendOnly());
		assertEquals(8, stats.getCountSendReceive());
		assertEquals(8, stats.getCountSendReceiveAsync());
		assertEquals(8, stats.getCountWait());
	}

	@Test
	public void testGatheringBPTSWithOneTestCaseWithHumanTrackWithoutClientTrack() throws Exception {
		XMLTestSuiteDocument bptsDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestSuite ts = bptsDoc.addNewTestSuite();
		XMLTestCasesSection tcs = ts.addNewTestCases();
		XMLTestCase tc = tcs.addNewTestCase();
		tc.setName("TC1");
		tc.addNewClientTrack();
		
		XMLHumanPartnerTrack h = tc.addNewHumanPartnerTrack();
		h.addNewCompleteHumanTask();
		h.addNewCompleteHumanTask();
		
		IStatisticEntry stats = new StatisticsGatherer().gatherStatistics(bptsDoc.getTestSuite());
		
		assertEquals("One Test Case", 1, stats.getSubStatistics().size());
		assertEquals("Two Tracks", 2, stats.getSubStatistics().get(0).getSubStatistics().size());
		
		assertEquals(2, stats.getCountCompleteHumanTask());
		assertFalse(stats.isClientTrackUsed());
		assertEquals(0, stats.getCountAllReceives());
		assertEquals(0, stats.getCountAllSends());
		assertEquals(0, stats.getCountReceiveOnly());
		assertEquals(0, stats.getCountReceiveSend());
		assertEquals(0, stats.getCountReceiveSendAsync());
		assertEquals(0, stats.getCountSendOnly());
		assertEquals(0, stats.getCountSendReceive());
		assertEquals(0, stats.getCountSendReceiveAsync());
		assertEquals(0, stats.getCountWait());

	}
	
	private void createPartnerTrack(XMLTestCase tc) {
		XMLPartnerTrack t = tc.addNewPartnerTrack();
		t.addNewWait();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceive();
		t.addNewSendReceive();
		t.addNewSendReceive();
		t.addNewSendOnly();
		t.addNewSendOnly();
		t.addNewSendOnly();
		t.addNewSendOnly();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSend();
		t.addNewReceiveSend();
		t.addNewReceiveSend();
		t.addNewReceiveSend();
		t.addNewReceiveSend();
		t.addNewReceiveSend();
		t.addNewReceiveOnly();
		t.addNewReceiveOnly();
		t.addNewReceiveOnly();
		t.addNewReceiveOnly();
		t.addNewReceiveOnly();
		t.addNewReceiveOnly();
		t.addNewReceiveOnly();
	}

	private void createClientTrack(XMLTestCase tc) {
		XMLTrack t = tc.addNewClientTrack();
		t.addNewReceiveOnly();
		t.addNewReceiveSend();
		t.addNewReceiveSend();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSendAsynchronous();
		t.addNewReceiveSendAsynchronous();
		t.addNewSendOnly();
		t.addNewSendOnly();
		t.addNewSendOnly();
		t.addNewSendOnly();
		t.addNewSendReceive();
		t.addNewSendReceive();
		t.addNewSendReceive();
		t.addNewSendReceive();
		t.addNewSendReceive();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceiveAsynchronous();
		t.addNewSendReceiveAsynchronous();
		t.addNewWait();
		t.addNewWait();
		t.addNewWait();
		t.addNewWait();
		t.addNewWait();
		t.addNewWait();
		t.addNewWait();
	}
	
}
