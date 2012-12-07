package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.bpelunit.model.bpel.ActivityType;

import org.apache.xmlbeans.XmlCursor;
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
	public void testWrapInNewScopeFirstActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		int xmlChildrenCount = sequence.getNativeActivity().getDomNode().getChildNodes().getLength();
		
		sequence.wrapActivityInNewScope(activity1);
		
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
		
		assertEquals(xmlChildrenCount, sequence.getNativeActivity().getDomNode().getChildNodes().getLength());
	}
	
	@Test
	public void testWrapInNewScopeSecondActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		int xmlChildrenCount = sequence.getNativeActivity().getDomNode().getChildNodes().getLength();
		
		sequence.wrapActivityInNewScope(activity2);
		
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
		
		assertEquals(xmlChildrenCount, sequence.getNativeActivity().getDomNode().getChildNodes().getLength());
	}
	
	@Test
	public void testWrapInNewSequenceFirstActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		int xmlChildrenCount = sequence.getNativeActivity().getDomNode().getChildNodes().getLength();
		
		sequence.wrapActivityInNewSequence(activity1);
		
		List<AbstractActivity<?>> activities = sequence.getActivities();
		assertEquals(2, activities.size());
		Sequence sequenceOfActivity1 = (Sequence) activities.get(0);
		assertEquals("SequenceOfActivity1", sequenceOfActivity1.getName());
		assertEquals(1, sequenceOfActivity1.getActivities().size());
		assertSame(activity1, sequenceOfActivity1.getActivities().get(0));
		assertSame(activity2, activities.get(1));
		
		nativeActivity1 = activity1.getNativeActivity();
		TSequence newNativeSequence = (TSequence)activities.get(0).getNativeActivity();
		assertSame(newNativeSequence, nativeSequence.getSequenceArray(0));
		assertSame(nativeActivity1, newNativeSequence.getEmptyArray()[0]);
		
		assertEquals(xmlChildrenCount, sequence.getNativeActivity().getDomNode().getChildNodes().getLength());
	}
	
	@Test
	public void testWrapInNewSequenceSecondActivity() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		int xmlChildrenCount = sequence.getNativeActivity().getDomNode().getChildNodes().getLength();
		
		sequence.wrapActivityInNewSequence(activity2);
		
		List<AbstractActivity<?>> activities = sequence.getActivities();
		assertEquals(2, activities.size());
		assertSame(activity1, activities.get(0));
		Sequence sequenceOfActivity2 = (Sequence) activities.get(1);
		assertEquals("SequenceOfActivity2", sequenceOfActivity2.getName());
		assertEquals(1, sequenceOfActivity2.getActivities().size());
		assertSame(activity2, sequenceOfActivity2.getActivities().get(0));
		
		nativeActivity2 = activity2.getNativeActivity();
		TSequence newNativeSequence = (TSequence)activities.get(1).getNativeActivity();
		assertSame(newNativeSequence, nativeSequence.getSequenceArray(0));
		assertSame(nativeActivity2, newNativeSequence.getEmptyArray()[0]);
		
		assertEquals(xmlChildrenCount, sequence.getNativeActivity().getDomNode().getChildNodes().getLength());
	}
	
	@Test
	public void testReplaceMoveActivityForward() throws Exception {
		Empty firstActivity = activity1;
		Empty oldActivity = activity2;
		Assign newActivity = sequence.addAssign();
		newActivity.setName("NewActivity");
		assertEquals(3, sequence.getActivities().size());
		
		sequence.replace(oldActivity, newActivity);
		List<AbstractActivity<?>> activitiesAfterReplace = sequence.getActivities();
		assertEquals(2, activitiesAfterReplace.size());
		assertSame(firstActivity, activitiesAfterReplace.get(0));
		assertEquals("Activity1", firstActivity.getName());
		assertSame(newActivity, activitiesAfterReplace.get(1));
		assertEquals("NewActivity", newActivity.getName());
		
		assertEquals(1, nativeSequence.getEmptyArray().length);
		assertEquals(1, nativeSequence.getAssignArray().length);
		
		XmlCursor sequenceCursor = nativeSequence.newCursor();
		assertTrue(sequenceCursor.toFirstChild());
		assertSame(firstActivity.getNativeActivity(), sequenceCursor.getObject());
		sequenceCursor.toNextSibling();
		assertSame(newActivity.getNativeActivity(), sequenceCursor.getObject());
	}
	
	@Test
	public void testReplaceMoveActivityBackward() throws Exception {
		Empty firstActivity = activity1;
		Empty newActivity = activity2;
		Assign oldActivity = sequence.addAssign();
		assertEquals(3, sequence.getActivities().size());
		
		sequence.replace(oldActivity, newActivity);
		List<AbstractActivity<?>> activitiesAfterReplace = sequence.getActivities();
		assertEquals(2, activitiesAfterReplace.size());
		assertSame(firstActivity, activitiesAfterReplace.get(0));
		assertSame(newActivity, activitiesAfterReplace.get(1));
		
		assertEquals(2, nativeSequence.getEmptyArray().length);
		
		XmlCursor sequenceCursor = nativeSequence.newCursor();
		assertTrue(sequenceCursor.toFirstChild());
		assertSame(firstActivity.getNativeActivity(), sequenceCursor.getObject());
		sequenceCursor.toNextSibling();
		assertSame(newActivity.getNativeActivity(), sequenceCursor.getObject());
	}
	
	@Test
	public void testRemove() throws Exception {
		assertEquals(2, sequence.getActivities().size());
		
		sequence.remove(activity1);
		assertEquals(1, sequence.getActivities().size());
		assertSame(activity2, sequence.getActivities().get(0));
		assertEquals("Activity2", activity2.getName());
	}
	
	@Test
	public void testMoveBefore() throws Exception {
		Empty activity3 = sequence.addEmpty();
		activity3.setName("Activity3");
		
		sequence.moveBefore(activity3, activity1);
		List<AbstractActivity<?>> activities = sequence.getActivities();
		assertSame(activity3, activities.get(0));
		assertSame(activity1, activities.get(1));
		assertSame(activity2, activities.get(2));
		assertEquals("Activity3", activity3.getName());
		assertEquals("Activity1", activity1.getName());
		assertEquals("Activity2", activity2.getName());
		
		sequence.moveBefore(activity2, activity1);
		activities = sequence.getActivities();
		assertSame(activity3, activities.get(0));
		assertSame(activity2, activities.get(1));
		assertSame(activity1, activities.get(2));
		assertEquals("Activity3", activity3.getName());
		assertEquals("Activity1", activity1.getName());
		assertEquals("Activity2", activity2.getName());
		
		sequence.moveBefore(activity3, activity1);
		activities = sequence.getActivities();
		assertSame(activity2, activities.get(0));
		assertSame(activity3, activities.get(1));
		assertSame(activity1, activities.get(2));
		assertEquals("Activity3", activity3.getName());
		assertEquals("Activity1", activity1.getName());
		assertEquals("Activity2", activity2.getName());
	}
	
	@Test
	public void testMoveToEnd() throws Exception {
		sequence.moveToEnd(activity1);
		
		List<AbstractActivity<?>> activities = sequence.getActivities();
		assertSame(activity2, activities.get(0));
		assertSame(activity1, activities.get(1));
		assertEquals("Activity1", activity1.getName());
		assertEquals("Activity2", activity2.getName());
	}
}
