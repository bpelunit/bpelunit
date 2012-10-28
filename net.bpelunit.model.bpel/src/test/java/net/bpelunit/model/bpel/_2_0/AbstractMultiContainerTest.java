package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public abstract class AbstractMultiContainerTest<T extends AbstractMultiContainer<?>> extends AbstractBasicActivityTest<T> {

	private AbstractMultiContainer<?> activity;
	
	protected void setContainerActivity(AbstractMultiContainer<?> a) {
		super.setActivity(a);
		this.activity = a;
	}
	
	@Override
	public void testIsBasicActivity() {
		assertFalse(activity.isBasicActivity());
	}
	
	@Test
	public void testAddAllActivities() throws Exception {
		Assign assign = activity.addAssign();
		Compensate compensate = activity.addCompensate();
		CompensateScope compensateScope = activity.addCompensateScope();
		Empty empty = activity.addEmpty();
		Exit exit = activity.addExit();
		Flow flow = activity.addFlow();
		ForEach forEach = activity.addForEach();
		If iif = activity.addIf();
		Invoke invoke = activity.addInvoke();
		Pick pick = activity.addPick();
		Receive receive = activity.addReceive();
		RepeatUntil repeatUntil = activity.addRepeatUntil();
		Reply reply = activity.addReply();
		Rethrow rethrow = activity.addRethrow();
		Scope scope = activity.addScope();
		Sequence sequence = activity.addSequence();
		Throw tthrow = activity.addThrow();
		Validate validate = activity.addValidate();
		Wait wait = activity.addWait();
		While wwhile = activity.addWhile();
		Assign assign2 = activity.addAssign();
		
		assertNotNull(assign);
		assertNotNull(compensate);
		assertNotNull(compensateScope);
		assertNotNull(empty);
		assertNotNull(exit);
		assertNotNull(flow);
		assertNotNull(forEach);
		assertNotNull(iif);
		assertNotNull(invoke);
		assertNotNull(pick);
		assertNotNull(receive);
		assertNotNull(repeatUntil);
		assertNotNull(reply);
		assertNotNull(rethrow);
		assertNotNull(scope);
		assertNotNull(sequence);
		assertNotNull(tthrow);
		assertNotNull(validate);
		assertNotNull(wait);
		assertNotNull(wwhile);
		assertNotNull(assign2);
		
		List<AbstractActivity<?>> activities = activity.getActivities();
		assertEquals(21, activities.size());
		assertSame(assign, activities.get(0));
		assertSame(compensate, activities.get(1));
		assertSame(compensateScope, activities.get(2));
		assertSame(empty, activities.get(3));
		assertSame(exit, activities.get(4));
		assertSame(flow, activities.get(5));
		assertSame(forEach, activities.get(6));
		assertSame(iif, activities.get(7));
		assertSame(invoke, activities.get(8));
		assertSame(pick, activities.get(9));
		assertSame(receive, activities.get(10));
		assertSame(repeatUntil, activities.get(11));
		assertSame(reply, activities.get(12));
		assertSame(rethrow, activities.get(13));
		assertSame(scope, activities.get(14));
		assertSame(sequence, activities.get(15));
		assertSame(tthrow, activities.get(16));
		assertSame(validate, activities.get(17));
		assertSame(wait, activities.get(18));
		assertSame(wwhile, activities.get(19));
		assertSame(assign2, activities.get(20));
		
		XmlCursor cursor = ((XmlObject)activity.getNativeActivity()).newCursor();
		moveToFirstActivity(cursor);
		assertSame(assign, assign.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(compensate, compensate.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(compensateScope, compensateScope.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(empty, empty.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(exit, exit.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(flow, flow.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(forEach, forEach.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(iif, iif.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(invoke, invoke.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(pick, pick.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(receive, receive.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(repeatUntil, repeatUntil.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(reply, reply.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(rethrow, rethrow.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(scope, scope.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(sequence, sequence.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(tthrow, tthrow.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(validate, validate.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(wait, wait.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(wwhile, wwhile.getObjectForNativeObject(cursor.getObject()));
		assertTrue(cursor.toNextSibling());
		assertSame(assign2, assign2.getObjectForNativeObject(cursor.getObject()));
		assertFalse(cursor.toNextSibling());
	}

	private void moveToFirstActivity(XmlCursor cursor) {
		cursor.toFirstChild();
		while(!(cursor.getObject() instanceof TActivity)) {
			cursor.toNextSibling();
		}
	}
	
}
