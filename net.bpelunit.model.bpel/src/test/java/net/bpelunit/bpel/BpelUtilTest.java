package net.bpelunit.bpel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import net.bpelunit.bpel.BpelUtil;

import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TAssign;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TFlow;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TProcess;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TSequence;


public class BpelUtilTest {

	@Test
	public void testProcessGetChildActivityFlow() throws Exception {
		TProcess p = new TProcess();
		TFlow flow = new TFlow();
		p.setFlow(flow);
		
		assertSame(flow, BpelUtil.getChildActivity(p));
	}
	
	@Test
	public void testProcessGetChildActivitySequence() throws Exception {
		TProcess p = new TProcess();
		TSequence seq = new TSequence();
		p.setSequence(seq);
		
		assertSame(seq, BpelUtil.getChildActivity(p));
	}
	
	@Test
	public void testGetActivityType() throws Exception {
		assertEquals("Assign", BpelUtil.getActivityType(new TAssign()));
		assertEquals("Empty", BpelUtil.getActivityType(new TEmpty()));
	}
	
}
