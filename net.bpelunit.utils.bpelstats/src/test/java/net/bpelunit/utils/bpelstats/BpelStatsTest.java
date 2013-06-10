package net.bpelunit.utils.bpelstats;

import static org.junit.Assert.*;

import org.junit.Test;

public class BpelStatsTest {

	@Test
	public void testMarketplace() {
		BpelStats.main(new String[]{ "-h", "src/test/resources/marketplace.bpel" });
	}
	
	@Test
	public void testTravelReservationService() {
		BpelStats.main(new String[]{ "-h", "src/test/resources/marketplace.bpel" });
	}

}
