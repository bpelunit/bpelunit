package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.CompensateDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensate;


public class CompensateTest {

	private Compensate compensate;
	private TCompensate nativeCompensate;
	
	@Before
	public void setUp() throws Exception {
		CompensateDocument compensateDoc = CompensateDocument.Factory.newInstance();
		nativeCompensate = compensateDoc.addNewCompensate();
		compensate = new Compensate(nativeCompensate, null);
	}
	
	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Compensate, compensate.getActivityType());
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(compensate, compensate.getObjectForNativeObject(nativeCompensate));
	}
}
