package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.*;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ForEachDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;


public class ForEachTest extends AbstractBasicActivityTest<ForEach> {

	private TForEach nativeForEach;
	private ForEach forEach;

	@Before
	public void setUp() {
		ForEachDocument forEachDoc = ForEachDocument.Factory.newInstance();
		nativeForEach = forEachDoc.addNewForEach();
		forEach = new ForEach(nativeForEach, null);
		
		setActivity(forEach);
	}
	
	@Override
	public void testIsBasicActivity() {
		assertFalse(forEach.isBasicActivity());
	}
	
	@Test
	public void testCounterName() {
		assertNull(forEach.getCounterName());
		
		forEach.setCounterName("Counter");
		assertEquals("Counter", forEach.getCounterName());
		
		forEach.setCounterName("counter");
		assertEquals("counter", forEach.getCounterName());
	}
	
	@Test
	public void testParallel() throws Exception {
		assertFalse(forEach.isParallel());
		
		forEach.setParallel(true);
		assertTrue(forEach.isParallel());
		
		forEach.setParallel(false);
		assertFalse(forEach.isParallel());
	}
	

	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.ForEach, forEach.getActivityType());
	}
}
