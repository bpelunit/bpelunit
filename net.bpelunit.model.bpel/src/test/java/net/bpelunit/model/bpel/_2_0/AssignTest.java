package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;


public class AssignTest {

	private Assign assign;
	private TAssign nativeAssign;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeAssign = TAssign.Factory.newInstance();
		assign = new Assign(nativeAssign, f);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(assign, assign.getObjectForNativeObject(nativeAssign));
	}
	
	
}
