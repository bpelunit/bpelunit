package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.IProcess;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.AssignDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;


public class AssignTest extends AbstractBasicActivityTest<Assign> {

	private Assign assign;
	private TAssign nativeAssign;
	
	@Before
	public void setUp() {
		AssignDocument assignDoc = AssignDocument.Factory.newInstance();
		nativeAssign = assignDoc.addNewAssign();
		assign = new Assign(nativeAssign, null);
		setActivity(assign);
	}
	
	@Test
	public void testValidateFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-assign.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		Assign a = (Assign)process.getMainActivity();
		assertTrue(a.getValidate());
	}
	
	@Test
	public void testValidateFromModel() throws Exception {
		assertFalse(assign.getValidate());
		assign.setValidate(true);
		assertTrue(assign.getValidate());
		assertEquals(TBoolean.YES, assign.activity.getValidate());
	}

	@Test
	public void testCopyFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-assign.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		IAssign a = (IAssign) process.getMainActivity();
		
		assertEquals(2, a.getCopies().size());
	}
	
	@Test
	public void testCopyFromModel() throws Exception {
		assertEquals(0, assign.getCopies().size());
		
		Copy c1 = assign.addCopy();
		assertEquals(1, assign.getCopies().size());
		assertSame(c1, assign.getCopies().get(0));
		
		Copy c2 = assign.addCopy();
		assertEquals(2, assign.getCopies().size());
		assertSame(c1, assign.getCopies().get(0));
		assertSame(c2, assign.getCopies().get(1));
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(assign, assign.getObjectForNativeObject(nativeAssign));
	}
	
	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Assign, assign.getActivityType());
	}
}
