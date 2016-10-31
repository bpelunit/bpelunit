package net.bpelunit.utils.bpelstats.hertis;

import static org.junit.Assert.*;

import net.bpelunit.utils.bpelstats.sax.SAXExecutor;

import org.junit.Test;

public class NDGathererTest {

	@Test
	public void testAssign() throws Exception {
		NDGatherer g = new NDGatherer();
		SAXExecutor.execute("src/test/resources/activity-assign.bpel", g);
		
		assertEquals(0.0, g.getValue(), 0.001);
	}
	
	@Test
	public void testTravelReservationService() throws Exception {
		NDGatherer g = new NDGatherer();
		SAXExecutor.execute("src/test/resources/travelReservationService.bpel", g);
		
		assertEquals(6, g.getValue(), 0.001);
	}
	
}
