package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TWait;


public class WaitTest {

	private Wait wait;
	private TWait nativeWait;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeWait = new TWait();
		wait = new Wait(nativeWait, f);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(wait, wait.getObjectForNativeObject(nativeWait));
	}
	
}
