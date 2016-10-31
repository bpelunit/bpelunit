package net.bpelunit.utils.bpelstats;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;

import net.bpelunit.utils.bpelstats.sax.SAXStatsGatherer;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ModelVSSAXTest {

	private SAXStatsGatherer saxGatherer = new SAXStatsGatherer();
//	private BpelModelGatherer modelGatherer = new BpelModelGatherer();
	
	@Test
	@Ignore
	public void testMarketplace() throws Exception {
		testStats("src/test/resources/marketplace.bpel");
	}
	
	@Test
	@Ignore
	public void testTravelReservationService() throws Exception {
		testStats("src/test/resources/travelReservationService.bpel");
	}
	
	@Test
	@Ignore
	public void testActivityAssign() throws Exception {
		testStats("src/test/resources/activity-assign.bpel");
	}
	
	@Test
	@Ignore
	public void testTerravis() throws Exception {
		testStats("C:/data/workspaces/terravis-av92/ch.terravis.egvt.process.egvt-ui-bank/bpel/egvt-ui-bank.bpel");
		testStats("C:/data/workspaces/terravis-av92/ch.terravis.egvt.process.egvt4.1/bpel/egvt-4.1.bpel");
	}
	
	
	public void testStats(String fileName) throws Exception {
		long t0 = System.currentTimeMillis();
		BpelStatsFileResult saxResult = saxGatherer.gather(fileName);
		long t1 = System.currentTimeMillis();
//		BpelStatsFileResult modelResult = modelGatherer.gather(fileName);
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("%s; SAX: %d, Model: %d", fileName, t1-t0, t2-t1));
		
		String[] bpelMetrics = { "Assign", "Catch", "CatchAll", "Compensate", "CompensateScope",
				"CompensationHandler", "Copy", "Else", "ElseIf", "Empty", "Exit",
				"Flow", "ForEach", "If", "Invoke", "Link", "OnAlarm", "OnAlarmHandler",
				"OnMessage", "OnMessageHandler", "PartnerLink", "Pick", "Receive", 
				"RepeatUntil", "Reply", "Rethrow", "Scope", "Sequence", "Throw", 
				"Validate", "Variable", "Wait", "While", "AllActivities", "BasicActivities",
				"StructuredActivities", "NonLinearActivities", "ExtensionActivities" };
		
		for(String metric : bpelMetrics) {
			Method getter = BpelStatsFileResult.class.getMethod("getCount" + metric);
			Object saxMetric = getter.invoke(saxResult);
//			Object modelMetric = getter.invoke(modelResult);
//			String assertionDescription = fileName + "." + metric+ " SAX: " + saxMetric + " Model: " + modelMetric;
//			if(!saxMetric.equals(modelMetric)) {
//				System.out.println(assertionDescription);
//			}
			//assertEquals(assertionDescription, saxMetric, modelMetric);
		}
		
		String[] extensionMetrics = { "ActiveVOSSuspend", "B4PPeopleActivity" };
		
		for(String metric : extensionMetrics) {
			Method getter = BpelStatsFileResult.class.getMethod("getCount" + metric);
			Object saxMetric = getter.invoke(saxResult);
//			Object modelMetric = getter.invoke(modelResult);
//			String assertionDescription = fileName + "." + metric+ " SAX: " + saxMetric + " Model: " + modelMetric;
//			if(!saxMetric.equals(modelMetric)) {
//				System.out.println(assertionDescription);
//			}
			//assertEquals(assertionDescription, saxMetric, modelMetric);
		}
	}
	
	@Test
	public void testActiveVOSSignals() throws IOException, SAXException {
		BpelStatsFileResult saxResult = saxGatherer.gather("src/test/resources/signalelements.bpel");
		
		assertEquals(1, saxResult.getCountActiveVOSSignalReceive());
		assertEquals(1, saxResult.getCountActiveVOSSignalSend());
		assertEquals(1, saxResult.getCountActiveVOSOnSignal());
	}
}
