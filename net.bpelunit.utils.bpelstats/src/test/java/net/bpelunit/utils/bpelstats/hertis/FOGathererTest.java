package net.bpelunit.utils.bpelstats.hertis;

import static org.junit.Assert.*;

import net.bpelunit.utils.bpelstats.sax.SAXExecutor;

import org.junit.Test;

public class FOGathererTest {

	@Test
	public void testAssign() throws Exception {
		FOGatherer g = new FOGatherer();
		SAXExecutor.execute("src/test/resources/activity-assign.bpel", g);
		
		assertEquals(0.0, g.getValue(), 0.001);
	}
	
	@Test
	public void testTravelReservationService() throws Exception {
		FOGatherer g = new FOGatherer();
		SAXExecutor.execute("src/test/resources/travelReservationService.bpel", g);
		
		assertEquals(6.0, g.getValue(), 0.001);
	}
}
