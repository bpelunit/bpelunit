package net.bpelunit.utils.bpelstats.hertis;

import static org.junit.Assert.*;

import net.bpelunit.utils.bpelstats.sax.SAXExecutor;

import org.junit.Test;

public class CWGathererTest {

	@Test
	public void testAssign() throws Exception {
		CWGatherer g = new CWGatherer();
		SAXExecutor.execute("src/test/resources/activity-assign.bpel", g);
		
		assertEquals(1.0, g.getValue(), 0.001);
	}
	
	@Test
	public void testTravelReservationService() throws Exception {
		CWGatherer g = new CWGatherer();
		SAXExecutor.execute("src/test/resources/travelReservationService.bpel", g);
		
		assertEquals(79.0, g.getValue(), 0.001);
	}
	
}
