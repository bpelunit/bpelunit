package net.bpelunit.utils.bpelstats;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class BpelStatsTest {

	@Test
	@Ignore
	public void testMarketplaceBpelStats() throws Exception {
		BpelStats.main(new String[]{ "-h", "src/test/resources/marketplace.bpel" });
	}
	
	@Test
	public void testTerravisSublanguageStats() throws Exception {
		BpelStats.main(new String[]{ "-h", "-s", "C:\\data\\workspaces\\terravis-av92\\ch.terravis.egvt.process.egvt4.1\\bpel\\egvt-4.1.bpel" });
	}
	
	@Test
	public void testTerravisSublanguageStatsReuse() throws Exception {
		BpelStats.main(new String[]{ "-h", "-s", "C:\\data\\workspaces\\terravis-av92\\ch.terravis.egvt.process.egvt4.1\\bpel\\egvt-4.1.bpel" });
		BpelStats.main(new String[]{ "-s", "--reusedir=a,target", "C:\\data\\workspaces\\terravis-av92\\ch.terravis.egvt.process.egvt4.1\\bpel\\egvt-4.1.bpel" });
	}
	
	@Test
	public void testTerravisSublanguageFailing() throws Exception {
		BpelStats.main(new String[]{ "-s", "--reusedir=target", "C:\\data\\workspaces\\activevos-ronchi\\com.innoq.prototype.ronchi\\bpel\\RepairProcess.bpel" });
	}
	
	@Test
	public void testHertisMetrics() throws Exception {
		BpelStats.main(new String[]{ "-h", "-m", "src/test/resources/marketplace.bpel", "src/test/resources/pick-createinstance.bpel", "src/test/resources/activity-assign.bpel"});
	}
}
