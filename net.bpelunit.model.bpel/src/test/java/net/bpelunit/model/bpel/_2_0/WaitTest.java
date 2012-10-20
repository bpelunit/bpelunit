package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
		wait = new Wait(nativeWait);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(wait, wait.getObjectForNativeObject(nativeWait));
	}

	@Test
	public void testGetActivityName() throws Exception {
		assertEquals("Wait", wait.getActivityName());
	}
}
