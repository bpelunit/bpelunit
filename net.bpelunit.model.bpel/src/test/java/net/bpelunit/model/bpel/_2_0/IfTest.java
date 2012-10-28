package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.IfDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;


public class IfTest extends AbstractBasicActivityTest<If> {

	private If iif;
	private TIf nativeIf;

	@Before
	public void setUp() {
		IfDocument ifDoc = IfDocument.Factory.newInstance();
		nativeIf = ifDoc.addNewIf();
		iif = new If(nativeIf, null);
		
		setActivity(iif);
	}
	
	@Test
	public void testIsBasicActivity() {
		assertFalse(iif.isBasicActivity());
	}
	

	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.If, iif.getActivityType());
	}	
}
