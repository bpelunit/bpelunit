package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.ICompensate;
import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IEmpty;
import net.bpelunit.model.bpel.IExit;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IIf;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IReceive;
import net.bpelunit.model.bpel.IRepeatUntil;
import net.bpelunit.model.bpel.IReply;
import net.bpelunit.model.bpel.IRethrow;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IThrow;
import net.bpelunit.model.bpel.IValidate;
import net.bpelunit.model.bpel.IWait;
import net.bpelunit.model.bpel.IWhile;

import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;

/**
 * Also tests the abstract base class.
 * 
 * @author dluebke
 */
public class ProcessTest {

	@Test
	public void testQueryByXPath() throws Exception {
		InputStream bpelResource = getClass().getResourceAsStream("waitprocess.bpel");
		assertNotNull(bpelResource);
		Process process = (Process)BpelFactory.loadProcess(bpelResource);
		
		Sequence s = (Sequence)process.getMainActivity();
		Pick p = (Pick)s.getActivities().get(1);

		OnAlarm a = (OnAlarm)p.getOnAlarms().get(1);
		Scope scope = (Scope)a.getMainActivity();
		Sequence s2 = (Sequence)scope.getMainActivity();
		Wait w = (Wait)s2.getActivities().get(0);
		
		TWait nativeWait = w.getNativeActivity();
		assertSame(nativeWait, w.getNativeActivity());
		assertSame(w, s2.getObjectForNativeObject(nativeWait));
		assertSame(w, scope.getObjectForNativeObject(nativeWait));
		assertSame(w, a.getObjectForNativeObject(nativeWait));
		assertSame(w, p.getObjectForNativeObject(nativeWait));
		assertSame(w, s.getObjectForNativeObject(nativeWait));
		assertSame(w, process.getObjectForNativeObject(nativeWait));
		
//		List<IBpelObject> waitToLeave;
//		waitToLeave = process.getElementsByXPath("//*[@name='WaitToLeave']");
//		assertEquals(1, waitToLeave.size());
//		assertSame(w, waitToLeave.get(0));
	}
	
	@Test
	public void testAssignFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-assign.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IAssign);
		assertTrue(process.getMainActivity() instanceof Assign);
		
		assertEquals("/bpel:process/bpel:assign[@name='AssignName']", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testAssignFromModel() throws Exception {
		Process process = (Process) BpelFactory.createProcess();
		Assign assign = process.setNewAssign();
		assertNotNull(assign);
		assertSame(assign, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:assign", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testCompensateFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-compensate.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof ICompensate);
		assertTrue(process.getMainActivity() instanceof Compensate);
		
		assertEquals("/bpel:process/bpel:compensate[@name='Compensate']", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testCompensateFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		ICompensate compensate = process.setNewCompensate();
		assertNotNull(compensate);
		assertSame(compensate, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:compensate", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testCompensateScopeFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-compensateScope.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof ICompensateScope);
		assertTrue(process.getMainActivity() instanceof CompensateScope);
		
		assertEquals("/bpel:process/bpel:compensateScope[@name='CompensateScope']", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testCompensateScopeFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		ICompensateScope compensateScope = process.setNewCompensateScope();
		assertNotNull(compensateScope);
		assertSame(compensateScope, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:compensateScope", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testEmptyFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-empty.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IEmpty);
		assertTrue(process.getMainActivity() instanceof Empty);
		
		assertEquals("/bpel:process/bpel:empty", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testEmptyFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IEmpty empty = process.setNewEmpty();
		assertNotNull(empty);
		assertSame(empty, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:empty", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testExitFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-exit.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IExit);
		assertTrue(process.getMainActivity() instanceof Exit);
		
		assertEquals("/bpel:process/bpel:exit", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testExitFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IExit exit = process.setNewExit();
		assertNotNull(exit);
		assertSame(exit, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:exit", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testFlowFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-flow.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IFlow);
		assertTrue(process.getMainActivity() instanceof Flow);
		
		assertEquals("/bpel:process/bpel:flow", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testFlowFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IFlow flow = process.setNewFlow();
		assertNotNull(flow);
		assertSame(flow, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:flow", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testForEachFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-foreach.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IForEach);
		assertTrue(process.getMainActivity() instanceof ForEach);
		
		assertEquals("/bpel:process/bpel:forEach", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testForEachFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IForEach forEach = process.setNewForEach();
		assertNotNull(forEach);
		assertSame(forEach, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:forEach", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testIfFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-if.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IIf);
		assertTrue(process.getMainActivity() instanceof If);
		
		assertEquals("/bpel:process/bpel:if", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testIfFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IIf iif = process.setNewIf();
		assertNotNull(iif);
		assertSame(iif, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:if", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testInvokeFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-invoke.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IInvoke);
		assertTrue(process.getMainActivity() instanceof Invoke);
		
		assertEquals("/bpel:process/bpel:invoke", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testInvokeFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IInvoke invoke = process.setNewInvoke();
		assertNotNull(invoke);
		assertSame(invoke, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:invoke", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testPickFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-pick.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IPick);
		assertTrue(process.getMainActivity() instanceof Pick);
		
		assertEquals("/bpel:process/bpel:pick", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testPickFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IPick pick = process.setNewPick();
		assertNotNull(pick);
		assertSame(pick, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:pick", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testReceiveFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-receive.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IReceive);
		assertTrue(process.getMainActivity() instanceof Receive);
		
		assertEquals("/bpel:process/bpel:receive", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testReceiveFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IReceive receive = process.setNewReceive();
		assertNotNull(receive);
		assertSame(receive, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:receive", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testRepeatUntilFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-repeatUntil.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IRepeatUntil);
		assertTrue(process.getMainActivity() instanceof RepeatUntil);
		
		assertEquals("/bpel:process/bpel:repeatUntil", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testRepeatUntilFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IRepeatUntil repeatUntil = process.setNewRepeatUntil();
		assertNotNull(repeatUntil);
		assertSame(repeatUntil, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:repeatUntil", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testReplyFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-reply.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IReply);
		assertTrue(process.getMainActivity() instanceof Reply);
		
		assertEquals("/bpel:process/bpel:reply", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testReplyFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IReply reply = process.setNewReply();
		assertNotNull(reply);
		assertSame(reply, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:reply", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testRethrowFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-rethrow.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IRethrow);
		assertTrue(process.getMainActivity() instanceof Rethrow);
		
		assertEquals("/bpel:process/bpel:rethrow", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testRethrowFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IRethrow rethrow = process.setNewRethrow();
		assertNotNull(rethrow);
		assertSame(rethrow, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:rethrow", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testScopeFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-scope.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IScope);
		assertTrue(process.getMainActivity() instanceof Scope);
		
		assertEquals("/bpel:process/bpel:scope", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testScopeFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IScope scope = process.setNewScope();
		assertNotNull(scope);
		assertSame(scope, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:scope", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testSequenceFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-sequence.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof ISequence);
		assertTrue(process.getMainActivity() instanceof Sequence);
		
		assertEquals("/bpel:process/bpel:sequence", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testSequenceFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		ISequence scope = process.setNewSequence();
		assertNotNull(scope);
		assertSame(scope, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:sequence", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testThrowFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-throw.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IThrow);
		assertTrue(process.getMainActivity() instanceof Throw);
		
		assertEquals("/bpel:process/bpel:throw", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testThrowFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IThrow t = process.setNewThrow();
		assertNotNull(t);
		assertSame(t, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:throw", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testValidateFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-validate.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IValidate);
		assertTrue(process.getMainActivity() instanceof Validate);
		
		assertEquals("/bpel:process/bpel:validate", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testValidateFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IValidate validate = process.setNewValidate();
		assertNotNull(validate);
		assertSame(validate, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:validate", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testWaitFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-wait.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IWait);
		assertTrue(process.getMainActivity() instanceof Wait);
		
		assertEquals("/bpel:process/bpel:wait", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testWaitFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IWait wait = process.setNewWait();
		assertNotNull(wait);
		assertSame(wait, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:wait", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testWhileFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertNotNull(process);
		assertNotNull(process.getMainActivity());
		assertTrue(process.getMainActivity() instanceof IWhile);
		assertTrue(process.getMainActivity() instanceof While);
		
		assertEquals("/bpel:process/bpel:while", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testWhileFromModel() throws Exception {
		IProcess process = BpelFactory.createProcess();
		IWhile w = process.setNewWhile();
		assertNotNull(w);
		assertSame(w, process.getMainActivity());
		
		assertEquals("/bpel:process/bpel:while", process.getMainActivity().getXPathInDocument());
	}
	
	@Test
	public void testNewMainActivityDeletsOldOne() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		process.setNewSequence();
		int noOfNodesAfterSequence = process.activity.getDomNode().getChildNodes().getLength();
		
		Flow flow = process.setNewFlow();
		int noOfNodesAfterFlow = process.activity.getDomNode().getChildNodes().getLength();
		
		assertSame(flow, process.getMainActivity());
		assertEquals(noOfNodesAfterSequence, noOfNodesAfterFlow);
	}
	
	@Test
	public void testNameFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertEquals("activity", process.getName());
	}
	
	@Test
	public void testNameFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertEquals(null, process.getName());
		
		process.setName("SomeName");
		assertEquals("SomeName", process.getName());
	}
	
	@Test
	public void testTargetNamespaceFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertEquals("TESTNS", process.getTargetNamespace());
	}
	
	@Test
	public void testTargetNamespaceFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertEquals(null, process.getTargetNamespace());
		
		process.setTargetNamespace("SomeNS");
		assertEquals("SomeNS", process.getTargetNamespace());
	}
	
	@Test
	public void testQueryLanguageFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertEquals(IProcess.URN_XPATH_1_0_IN_BPEL_2_0, process.getQueryLanguage());
	}
	
	@Test
	public void testQueryLanguageFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertEquals(IProcess.URN_XPATH_1_0_IN_BPEL_2_0, process.getQueryLanguage());
		
		process.setQueryLanguage("abc");
		assertEquals("abc", process.getQueryLanguage());
	}
	
	@Test
	public void testExpressionLanguageFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertEquals(IProcess.URN_XPATH_1_0_IN_BPEL_2_0, process.getExpressionLanguage());
	}
	
	@Test
	public void testExpressionLanguageFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertEquals(IProcess.URN_XPATH_1_0_IN_BPEL_2_0, process.getExpressionLanguage());
		
		process.setExpressionLanguage("abc");
		assertEquals("abc", process.getExpressionLanguage());
	}
	
	@Test
	public void testSuppressJoinFailureFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertTrue(process.getSuppressJoinFailure());
	}
	
	@Test
	public void testSuppressJoinFailureFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertFalse(process.getSuppressJoinFailure());
		
		process.setSuppressJoinFailure(true);
		assertTrue(process.getSuppressJoinFailure());
	}
	
	@Test
	public void testExitOnStandardFaultFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		assertTrue(process.getExitOnStandardFault());
	}
	
	@Test
	public void testExitOnStandardFaultFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertFalse(process.getExitOnStandardFault());
		
		process.setExitOnStandardFault(true);
		assertTrue(process.getExitOnStandardFault());
	}
	
	@Test
	public void testDocumentationFromFile() throws Exception {
		InputStream resourceStream = getClass().getResourceAsStream("/activities/_2_0/activity-while.bpel");
		IProcess process = BpelFactory.loadProcess(resourceStream);
		
		List<? extends IDocumentation> documentation = process.getDocumentation();
		assertEquals(2, documentation.size());
		assertEquals("A", documentation.get(0).getStringContent());
		assertEquals("B", documentation.get(1).getStringContent());
	}
	
	@Test
	public void testDocumentationFromModel() throws Exception {
		Process process = (Process)BpelFactory.createProcess();
		
		assertEquals(0, process.getDocumentation().size());
		
		Documentation doc1 = process.addDocumentation();
		doc1.setStringContent("A");
		assertEquals(1, process.getDocumentation().size());
		assertEquals("A", doc1.getStringContent());
		assertSame(doc1, process.getDocumentation().get(0));
		
		Documentation doc2 = process.addDocumentation();
		doc2.setStringContent("B");
		assertEquals(2, process.getDocumentation().size());
		assertEquals("A", doc1.getStringContent());
		assertEquals("B", doc2.getStringContent());
		assertSame(doc1, process.getDocumentation().get(0));
		assertSame(doc2, process.getDocumentation().get(1));
	}
	
	@Test
	public void testEncapsulateInScope() throws Exception {
		Process p = (Process) BpelFactory.createProcess();
		Empty empty = p.setNewEmpty();
		empty.setName("Empty1");
		
		p.wrapActivityInNewScope(empty);
		
		Scope newMainScope = (Scope)p.getMainActivity();
		assertEquals("Scope_Of_Empty1", newMainScope.getName());
		assertSame(empty, newMainScope.getMainActivity());
		assertEquals("Empty1", newMainScope.getMainActivity().getName());
		
		assertNull(p.getNativeActivity().getEmpty());
		assertSame(newMainScope.getNativeActivity(), p.getNativeActivity().getScope());
		assertSame(empty.getNativeActivity(), newMainScope.getNativeActivity().getEmpty());
		
		// Re-do assertions but this time save it and read a clean copy to
		// see if BPEL element names are correct etc.
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		p.save(out);
		p = (Process) BpelFactory.loadProcess(new ByteArrayInputStream(out.toByteArray()));
		
		newMainScope = (Scope)p.getMainActivity();
		assertEquals("Scope_Of_Empty1", newMainScope.getName());
		assertEquals(ActivityType.Empty, newMainScope.getMainActivity().getActivityType());
		assertEquals("Empty1", newMainScope.getMainActivity().getName());
	}
	
	@Test
	public void testWrapActivityInNewSequence() throws Exception {
		Process p = (Process) BpelFactory.createProcess();
		Empty empty = p.setNewEmpty();
		empty.setName("Empty1");
		
		p.wrapActivityInNewSequence(empty);
		
		Sequence newMainSequence = (Sequence)p.getMainActivity();
		assertEquals("Sequence_Of_Empty1", newMainSequence.getName());
		assertEquals(1, newMainSequence.getActivities().size());
		assertSame(empty, newMainSequence.getActivities().get(0));
		assertEquals("Empty1", newMainSequence.getActivities().get(0).getName());
		
		assertNull(p.getNativeActivity().getEmpty());
		assertSame(newMainSequence.getNativeActivity(), p.getNativeActivity().getSequence());
		assertEquals(1, newMainSequence.getNativeActivity().getEmptyArray().length);
		assertSame(empty.getNativeActivity(), newMainSequence.getNativeActivity().getEmptyArray(0));
		
		// Re-do assertions but this time save it and read a clean copy to
		// see if BPEL element names are correct etc.
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		p.save(out);
		p = (Process) BpelFactory.loadProcess(new ByteArrayInputStream(out.toByteArray()));
		
		newMainSequence = (Sequence)p.getMainActivity();
		assertEquals("Sequence_Of_Empty1", newMainSequence.getName());
		assertEquals(1, newMainSequence.getActivities().size());
		assertEquals(ActivityType.Empty, newMainSequence.getActivities().get(0).getActivityType());
		assertEquals("Empty1", newMainSequence.getActivities().get(0).getName());
	}
}
