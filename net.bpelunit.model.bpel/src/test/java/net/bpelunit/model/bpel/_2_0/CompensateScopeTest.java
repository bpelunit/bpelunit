package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IProcess;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.CompensateScopeDocument;


public class CompensateScopeTest {

	private CompensateScope compensateScope;
	
	@Before
	public void setUp() {
		CompensateScopeDocument compensateScopeDoc = CompensateScopeDocument.Factory.newInstance();
		compensateScope = new CompensateScope(compensateScopeDoc.addNewCompensateScope(), null);
	}
	
	@Test
	public void testTargetFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-compensateScope.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		CompensateScope compensateScope = (CompensateScope)process.getMainActivity();
		assertEquals("ScopeName", compensateScope.getTarget());
	}
	
	@Test
	public void testTargetFromModel() throws Exception {
		assertEquals(null, compensateScope.getTarget());
		
		compensateScope.setTarget("someScope");
		assertEquals("someScope", compensateScope.getTarget());
	}

	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.CompensateScope, compensateScope.getActivityType());
	}
}
