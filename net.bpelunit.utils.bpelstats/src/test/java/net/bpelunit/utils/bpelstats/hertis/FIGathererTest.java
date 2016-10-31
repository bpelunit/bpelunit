package net.bpelunit.utils.bpelstats.hertis;

import static org.junit.Assert.*;

import net.bpelunit.utils.bpelstats.sax.SAXExecutor;

import org.junit.Test;

public class FIGathererTest {

	@Test
	public void testAssign() throws Exception {
		FIGatherer g = new FIGatherer();
		SAXExecutor.execute("src/test/resources/activity-assign.bpel", g);
		
		assertEquals(0.0, g.getValue(), 0.001);
	}
	
	@Test
	public void testTravelReservationService() throws Exception {
		FIGatherer g = new FIGatherer();
		SAXExecutor.execute("src/test/resources/travelReservationService.bpel", g);
		
		assertEquals(1.0, g.getValue(), 0.001);
	}
	
	@Test
	public void testPickCreateInstance() throws Exception {
		FIGatherer g = new FIGatherer();
		SAXExecutor.execute("src/test/resources/pick-createinstance.bpel", g);
		
		assertEquals(3.0, g.getValue(), 0.001);
	}
	
}
