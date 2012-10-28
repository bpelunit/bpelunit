package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.EmptyDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;


public class EmptyTest extends AbstractBasicActivityTest<Empty> {

	private TEmpty nativeEmpty;
	private Empty empty;

	@Before
	public void setUp() {
		EmptyDocument emptyDoc = EmptyDocument.Factory.newInstance();
		nativeEmpty = emptyDoc.addNewEmpty();
		empty = new Empty(nativeEmpty, null);
		setActivity(empty);
	}
	
	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Empty, empty.getActivityType());
	}
}
