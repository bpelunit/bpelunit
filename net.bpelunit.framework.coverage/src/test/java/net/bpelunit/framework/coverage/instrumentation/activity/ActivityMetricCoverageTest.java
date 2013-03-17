package net.bpelunit.framework.coverage.instrumentation.activity;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.bpelunit.framework.coverage.result.ICoverageResult;
import net.bpelunit.model.bpel.IActivity;

import org.junit.Before;
import org.junit.Test;


public class ActivityMetricCoverageTest {

	private HashMap<String, IActivity> markerMapping = new HashMap<String, IActivity>();
	private HashMap<String, Integer> markerCounter = new HashMap<String, Integer>();
	private List<String> markers = new ArrayList<String>();
	
	@Before
	public void setUp() {
		markerMapping.clear();
		markerCounter.clear();
		markers.clear();
	}
	
	@Test
	public void testMetaData() throws Exception {
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		assertEquals("ACTIVITY", amc.getMetricId());
		assertEquals("Activity Coverage", amc.getMetricName());
	}
	
	@Test
	public void testOneActivityTypeOneActivity() throws Exception {
		addActivityEntry("A1", "assign", 10);
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		
		List<ICoverageResult> result = amc.getCoverageResult();
		assertEquals(3, result.size());
		
		ICoverageResult currentResult = result.get(0);
		assertEquals("//assign['A1']", currentResult.getBPELElementReference());
		assertEquals(10, currentResult.getExecutionCount());
		assertEquals(10.0, currentResult.avg(), 0.001);
		assertEquals(10.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
		
		currentResult = result.get(1);
		assertEquals("//assign", currentResult.getBPELElementReference());
		assertEquals(10, currentResult.getExecutionCount());
		assertEquals(10.0, currentResult.avg(), 0.001);
		assertEquals(10.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
		
		currentResult = result.get(2);
		assertEquals("Overall", currentResult.getBPELElementReference());
		assertEquals(10, currentResult.getExecutionCount());
		assertEquals(10.0, currentResult.avg(), 0.001);
		assertEquals(10.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
	}

	@Test
	public void testOneActivityTypeTwoActivities() throws Exception {
		addActivityEntry("A1", "assign", 10);
		addActivityEntry("A2", "assign", 1);
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		
		List<ICoverageResult> result = amc.getCoverageResult();
		assertEquals(4, result.size());
		
		ICoverageResult currentResult = result.get(0);
		assertEquals("//assign['A1']", currentResult.getBPELElementReference());
		assertEquals(10, currentResult.getExecutionCount());
		assertEquals(10.0, currentResult.avg(), 0.001);
		assertEquals(10.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(1);
		assertEquals("//assign['A2']", currentResult.getBPELElementReference());
		assertEquals(1, currentResult.getExecutionCount());
		assertEquals(1.0, currentResult.avg(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(2);
		assertEquals("//assign", currentResult.getBPELElementReference());
		assertEquals(11, currentResult.getExecutionCount());
		assertEquals(5.5, currentResult.avg(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(3);
		assertEquals("Overall", currentResult.getBPELElementReference());
		assertEquals(11, currentResult.getExecutionCount());
		assertEquals(5.5, currentResult.avg(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.coverage(), 0.001);
	}
	
	@Test
	public void testTwoActivityTypesTwoActivitiesEach() throws Exception {
		addActivityEntry("E1", "empty", 20);
		addActivityEntry("A1", "assign", 10);
		addActivityEntry("A2", "assign", 1);
		addActivityEntry("E2", "empty", 2);
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		
		List<ICoverageResult> result = amc.getCoverageResult();
		assertEquals(7, result.size());
		
		ICoverageResult currentResult = result.get(1);
		assertEquals("//assign['A1']", currentResult.getBPELElementReference());
		assertEquals(10, currentResult.getExecutionCount());
		assertEquals(10.0, currentResult.avg(), 0.001);
		assertEquals(10.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);
		
		currentResult = result.get(2);
		assertEquals("//assign['A2']", currentResult.getBPELElementReference());
		assertEquals(1, currentResult.getExecutionCount());
		assertEquals(1.0, currentResult.avg(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		
		currentResult = result.get(0);
		assertEquals("//empty['E1']", currentResult.getBPELElementReference());
		assertEquals(20, currentResult.getExecutionCount());
		assertEquals(20.0, currentResult.avg(), 0.001);
		assertEquals(20.0, currentResult.min(), 0.001);
		assertEquals(20.0, currentResult.max(), 0.001);
		
		currentResult = result.get(3);
		assertEquals("//empty['E2']", currentResult.getBPELElementReference());
		assertEquals(2, currentResult.getExecutionCount());
		assertEquals(2.0, currentResult.avg(), 0.001);
		assertEquals(2.0, currentResult.min(), 0.001);
		assertEquals(2.0, currentResult.max(), 0.001);
		
		currentResult = result.get(4);
		assertEquals("//assign", currentResult.getBPELElementReference());
		assertEquals(11, currentResult.getExecutionCount());
		assertEquals(5.5, currentResult.avg(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(10.0, currentResult.max(), 0.001);

		currentResult = result.get(5);
		assertEquals("//empty", currentResult.getBPELElementReference());
		assertEquals(22, currentResult.getExecutionCount());
		assertEquals(11.0, currentResult.avg(), 0.001);
		assertEquals(2.0, currentResult.min(), 0.001);
		assertEquals(20.0, currentResult.max(), 0.001);
		
		currentResult = result.get(6);
		assertEquals("Overall", currentResult.getBPELElementReference());
		assertEquals(33, currentResult.getExecutionCount());
		assertEquals(8.25, currentResult.avg(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(20.0, currentResult.max(), 0.001);
	}
	@Test
	public void testTwoActivityTypesTwoActivitiesEachNot100Percent() throws Exception {
		addActivityEntry("E1", "empty", 20);
		addActivityEntry("A1", "assign", 10);
		addActivityEntry("A2", "assign", 0);
		addActivityEntry("E2", "empty", 0);
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		
		List<ICoverageResult> result = amc.getCoverageResult();
		assertEquals(7, result.size());
		
		ICoverageResult currentResult = result.get(1);
		assertEquals("//assign['A1']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(2);
		assertEquals("//assign['A2']", currentResult.getBPELElementReference());
		assertEquals(0.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(0);
		assertEquals("//empty['E1']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(3);
		assertEquals("//empty['E2']", currentResult.getBPELElementReference());
		assertEquals(0.0, currentResult.coverage(), 0.001);
		
		currentResult = result.get(4);
		assertEquals("//assign", currentResult.getBPELElementReference());
		assertEquals(0.5, currentResult.coverage(), 0.001);
		
		currentResult = result.get(5);
		assertEquals("//empty", currentResult.getBPELElementReference());
		assertEquals(0.5, currentResult.coverage(), 0.001);
		
		currentResult = result.get(6);
		assertEquals("Overall", currentResult.getBPELElementReference());
		assertEquals(0.5, currentResult.coverage(), 0.001);
	}
	
	private void addActivityEntry(String activityName, String activityType, int counter) {
		IActivity a = new DummyActivity(activityName, activityType);
		String marker = activityName;
		markers.add(marker);
		markerMapping.put(marker, a);
		markerCounter.put(marker, counter);
	}
	
	@Test
	public void testSimpleProcessOnePath() throws Exception {
		// process is receive->if(assign, else:assign)->reply
		addActivityEntry("Receive", "receive", 1);
		addActivityEntry("A1", "assign", 1);
		addActivityEntry("A2", "assign", 0);
		addActivityEntry("Reply", "reply", 1);
		
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		
		List<ICoverageResult> result = amc.getCoverageResult();
		assertEquals(8, result.size());
		
		ICoverageResult currentResult = result.remove(0);
		assertEquals("//receive['Receive']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//assign['A1']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//assign['A2']", currentResult.getBPELElementReference());
		assertEquals(0.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//reply['Reply']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);

		currentResult = result.remove(0);
		assertEquals("//assign", currentResult.getBPELElementReference());
		assertEquals(0.5, currentResult.coverage(), 0.001);
		assertEquals(0.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		assertEquals(0.5, currentResult.avg(), 0.001);
		assertEquals(1, currentResult.getExecutionCount());
		
		currentResult = result.remove(0);
		assertEquals("//receive", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.avg(), 0.001);
		assertEquals(1, currentResult.getExecutionCount());
		
		currentResult = result.remove(0);
		assertEquals("//reply", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.avg(), 0.001);
		assertEquals(1, currentResult.getExecutionCount());
		
		currentResult = result.remove(0);
		assertEquals("Overall", currentResult.getBPELElementReference());
		assertEquals(0.75, currentResult.coverage(), 0.001);
		assertEquals(0.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		assertEquals(0.75, currentResult.avg(), 0.001);
		assertEquals(3, currentResult.getExecutionCount());
		
	}
	
	@Test
	public void testSimpleProcessTwoPaths() throws Exception {
		// process is receive->if(assign, else:assign)->reply
		addActivityEntry("Receive", "receive", 2);
		addActivityEntry("A1", "assign", 1);
		addActivityEntry("A2", "assign", 1);
		addActivityEntry("Reply", "reply", 2);
		
		ActivityMetricCoverage amc = new ActivityMetricCoverage(markers, markerMapping, markerCounter);
		
		List<ICoverageResult> result = amc.getCoverageResult();
		assertEquals(8, result.size());
		
		ICoverageResult currentResult = result.remove(0);
		assertEquals("//receive['Receive']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//assign['A1']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//assign['A2']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//reply['Reply']", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		
		currentResult = result.remove(0);
		assertEquals("//assign", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(1.0, currentResult.max(), 0.001);
		assertEquals(1.0, currentResult.avg(), 0.001);
		assertEquals(2, currentResult.getExecutionCount());
		
		currentResult = result.remove(0);
		assertEquals("//receive", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		assertEquals(2.0, currentResult.min(), 0.001);
		assertEquals(2.0, currentResult.max(), 0.001);
		assertEquals(2.0, currentResult.avg(), 0.001);
		assertEquals(2, currentResult.getExecutionCount());
		
		currentResult = result.remove(0);
		assertEquals("//reply", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		assertEquals(2.0, currentResult.min(), 0.001);
		assertEquals(2.0, currentResult.max(), 0.001);
		assertEquals(2.0, currentResult.avg(), 0.001);
		assertEquals(2, currentResult.getExecutionCount());
		
		currentResult = result.remove(0);
		assertEquals("Overall", currentResult.getBPELElementReference());
		assertEquals(1.0, currentResult.coverage(), 0.001);
		assertEquals(1.0, currentResult.min(), 0.001);
		assertEquals(2.0, currentResult.max(), 0.001);
		assertEquals(1.5, currentResult.avg(), 0.001);
		assertEquals(6, currentResult.getExecutionCount());
		
	}
}
