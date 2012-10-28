package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.SequenceDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
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
		sequence = new Sequence(nativeSequence, null);
		
		activity1 = sequence.addEmpty();
		nativeActivity1 = activity1.getNativeActivity();
		activity1.setName("Activity1");
		
		activity2 = sequence.addEmpty();
		nativeActivity2 = activity2.getNativeActivity();
		activity2.setName("Activity2");
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
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Sequence, sequence.getActivityType());
	}
	
	@Test
	public void testEncapsulateInNewScopeFirstActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		
		sequence.encapsulateInNewScope(activity1);
		
		List<AbstractActivity<?>> activities = sequence.getActivities();
		assertEquals(2, activities.size());
		Scope scopeOfActivity1 = (Scope) activities.get(0);
		assertEquals("ScopeOfActivity1", scopeOfActivity1.getName());
		assertSame(activity1, scopeOfActivity1.getMainActivity());
		assertSame(activity2, activities.get(1));
		
		nativeActivity1 = activity1.getNativeActivity();
		TScope newNativeScope = (TScope)activities.get(0).getNativeActivity();
		assertSame(newNativeScope, nativeSequence.getScopeArray(0));
		assertSame(nativeActivity1, newNativeScope.getEmpty());
	}
	
	@Test
	public void testEncapsulateInNewScopeSecondActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		
		sequence.encapsulateInNewScope(activity2);
		
		List<AbstractActivity<?>> activities = sequence.getActivities();
		assertEquals(2, activities.size());
		assertSame(activity1, activities.get(0));
		Scope scopeOfActivity2 = (Scope) activities.get(1);
		assertEquals("ScopeOfActivity2", scopeOfActivity2.getName());
		assertSame(activity2, scopeOfActivity2.getMainActivity());
		
		nativeActivity2 = activity2.getNativeActivity();
		TScope newNativeScope = (TScope)activities.get(1).getNativeActivity();
		assertSame(newNativeScope, nativeSequence.getScopeArray(0));
		assertSame(nativeActivity2, newNativeScope.getEmpty());
	}
}
