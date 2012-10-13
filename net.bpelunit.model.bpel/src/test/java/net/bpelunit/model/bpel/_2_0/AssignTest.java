package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;


public class AssignTest extends AbstractBasicActivityTest<Assign> {

	private Assign assign;
	private TAssign nativeAssign;
	
	@Before
	public void setUp() {
		nativeAssign = TAssign.Factory.newInstance();
		assign = new Assign(nativeAssign);
		setActivity(assign);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(assign, assign.getObjectForNativeObject(nativeAssign));
	}
}
