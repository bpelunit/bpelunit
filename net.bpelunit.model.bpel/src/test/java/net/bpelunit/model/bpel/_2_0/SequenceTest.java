package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;


public class SequenceTest {

	private Sequence sequence;
	private TSequence nativeSequence;
	private Empty activity1;
	private TEmpty nativeActivity1;
	private Empty activity2;
	private TEmpty nativeActivity2;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeSequence = TSequence.Factory.newInstance();
		sequence = new Sequence(nativeSequence, f);
		
		nativeActivity1 = TEmpty.Factory.newInstance();
		activity1 = new Empty(nativeActivity1, f);
		sequence.addActivity(activity1);
		
		nativeActivity2 = TEmpty.Factory.newInstance();
		activity2 = new Empty(nativeActivity2, f);
		sequence.addActivity(activity2);
	}
	
	@Test
	public void testAddActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		assertSame(activity1, sequence.getActivities().get(0));
		assertSame(activity2, sequence.getActivities().get(1));
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(sequence, sequence.getObjectForNativeObject(nativeSequence));
		assertSame(activity1, sequence.getObjectForNativeObject(nativeActivity1));
		assertSame(activity2, sequence.getObjectForNativeObject(nativeActivity2));
	}
	
}
