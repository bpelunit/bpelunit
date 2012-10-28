package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.oasisOpen.docs.wsbpel.x20.process.executable.WaitDocument;


public class WaitTest {

	private Wait wait;
	private TWait nativeWait;
	
	@Before
	public void setUp() {
		WaitDocument waitDoc = WaitDocument.Factory.newInstance();
		nativeWait = waitDoc.addNewWait();
		wait = new Wait(nativeWait, null);
	}
	
	@Test
	public void testDeadline() throws Exception {
		assertNull(wait.getDeadline());
		wait.setDuration("'P2D'");
		assertNull(wait.getDeadline());
		assertEquals("'P2D'", wait.getDuration());
		
		wait.setDeadline("'2012-01-01'");
		assertEquals("'2012-01-01'", wait.getDeadline());
		assertNull(wait.getDuration());
	}
	
	@Test
	public void testDuration() throws Exception {
		assertNull(wait.getDuration());
		wait.setDeadline("'2012-01-01'");
		assertNull(wait.getDuration());
		assertEquals("'2012-01-01'", wait.getDeadline());
		
		wait.setDuration("'P2D'");
		assertEquals("'P2D'", wait.getDuration());
		assertNull(wait.getDeadline());
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(wait, wait.getObjectForNativeObject(nativeWait));
	}


	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Wait, wait.getActivityType());
	}
}
