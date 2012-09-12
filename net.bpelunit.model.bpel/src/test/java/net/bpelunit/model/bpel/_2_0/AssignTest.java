package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TAssign;


public class AssignTest {

	private Assign assign;
	private TAssign nativeAssign;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeAssign = new TAssign();
		assign = new Assign(nativeAssign, f);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(assign, assign.getObjectForNativeObject(nativeAssign));
	}
	
	
}
