package net.bpelunit.utils.bpelstats.hertis;

import static org.junit.Assert.*;

import net.bpelunit.utils.bpelstats.sax.SAXExecutor;

import org.junit.Test;

public class CFCGathererTest {

	@Test
	public void testAssign() throws Exception {
		CFCGatherer g = new CFCGatherer();
		SAXExecutor.execute("src/test/resources/activity-assign.bpel", g);
		
		assertEquals(1.0, g.getValue(), 0.001);
	}
	
	@Test
	public void testTravelReservationService() throws Exception {
		CFCGatherer g = new CFCGatherer();
		SAXExecutor.execute("src/test/resources/travelReservationService.bpel", g);
		
		assertEquals(51.0, g.getValue(), 0.001);
	}
	
}
