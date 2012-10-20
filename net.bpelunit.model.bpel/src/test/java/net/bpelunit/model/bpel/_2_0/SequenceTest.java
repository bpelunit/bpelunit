package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.SequenceDocument;
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
		SequenceDocument seqDoc = SequenceDocument.Factory.newInstance();
		nativeSequence = seqDoc.addNewSequence();
		sequence = new Sequence(nativeSequence);
		
		activity1 = sequence.addEmpty();
		nativeActivity1 = activity1.getNativeActivity();
		
		activity2 = sequence.addEmpty();
		nativeActivity2 = activity2.getNativeActivity();
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
	
	@Test
	public void testGetActivityName() throws Exception {
		assertEquals("Sequence", sequence.getActivityName());
	}
}
