package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.*;

import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public abstract class AbstractBasicActivityTest<T extends AbstractActivity<?>> {

	private AbstractActivity<?> activity;

	protected void setActivity(AbstractActivity<?> a) {
		this.activity = a;
	}

	@Test
	public void testSetGetName() throws Exception {
		activity.setName("Name");
		assertEquals("Name", activity.getName());
		assertEquals("Name", ((TActivity)activity.getNativeActivity()).getName());
	}
	
	@Test
	public void testSuppressJoinFailure() {
		activity.setSuppressJoinFailure(true);
		assertTrue(activity.getSuppressJoinFailure());
		
		activity.setSuppressJoinFailure(false);
		assertFalse(activity.getSuppressJoinFailure());
	}
	
	@Test
	public void testIsBasicActivity() {
		assertTrue(activity.isBasicActivity());
	}
	
}
