package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ExitDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExit;

public class ExitTest extends AbstractBasicActivityTest<Empty> {

	private TExit nativeExit;
	private Exit exit;

	@Before
	public void setUp() {
		ExitDocument emptyDoc = ExitDocument.Factory.newInstance();
		nativeExit = emptyDoc.addNewExit();
		exit = new Exit(nativeExit, null);
		setActivity(exit);
	}
	
	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Exit, exit.getActivityType());
	}
}
