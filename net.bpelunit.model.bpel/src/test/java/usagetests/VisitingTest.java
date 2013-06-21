package usagetests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.IElse;
import net.bpelunit.model.bpel.IElseIf;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IIf;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IOnMessage;
import net.bpelunit.model.bpel.IOnMessageHandler;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IRepeatUntil;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IWhile;
import net.bpelunit.model.bpel._2_0.Catch;
import net.bpelunit.model.bpel._2_0.CatchAll;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

public class VisitingTest {
	
	private List<Object> activities;

	@Test
	public void testAllActivities() throws Exception {
		IProcess process = createProcessWithAllActivtiyTypes();
		
		GatheringVisitor visitor = new GatheringVisitor(process, activities.toArray());
		process.visit(visitor);
		assertEquals("Additional objects detected in tree: " + visitor.getAdditionalObjects().toString(), 0, visitor.getAdditionalObjects().size());
		assertEquals("Objects not visited in tree: " + visitor.getRemainingObjects().toString(), 0, visitor.getRemainingObjects().size());
	}
	
	@Test
	public void testAllActivitiesSaveAndLoad() throws Exception {
		IProcess process = createProcessWithAllActivtiyTypes();
		
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		process.save(out1);
		
		process = BpelFactory.loadProcess(new ByteArrayInputStream(out1.toByteArray()));
		
		GatheringVisitor visitor = new GatheringVisitor(process);
		process.visit(visitor);
		
		assertEquals("Count of Activities must be same after save: " + visitor.getAdditionalObjects(), activities.size(), visitor.getAdditionalObjects().size());
	}
	
	private IProcess createProcessWithAllActivtiyTypes() throws Exception {
		activities = new ArrayList<Object>();
		
		IProcess process = BpelFactory.createProcess();
		ISequence sequence = process.setNewSequence();
		activities.add(sequence);
		activities.add(sequence.addAssign());
		activities.add(sequence.addCompensate());
		activities.add(sequence.addCompensateScope());
		activities.add(sequence.addEmpty());
		activities.add(sequence.addExit());
		IFlow flow = sequence.addFlow();
		activities.add(flow);
		activities.add(flow.addEmpty());
		activities.add(flow.addEmpty());
		activities.add(flow.addLink("Test"));
		IForEach forEach = sequence.addForEach();
		activities.add(forEach);
		activities.add(forEach.getScope());
		activities.add(forEach.getScope().setNewEmpty());
		IIf iff = sequence.addIf();
		activities.add(iff);
		activities.add(iff.setNewAssign());
		IElseIf elseIf = iff.addNewElseIf();
		activities.add(elseIf);
		activities.add(elseIf.setNewAssign());
		IElse eelse = iff.setNewElse();
		activities.add(eelse);
		activities.add(eelse.setNewEmpty());
		IInvoke invoke = sequence.addInvoke();
		activities.add(invoke);
		ICompensationHandler compensationHandler = invoke.setNewCompensationHandler();
		activities.add(compensationHandler);
		activities.add(compensationHandler.setNewAssign());
		Catch ccatch = invoke.addNewCatch();
		activities.add(ccatch);
		activities.add(ccatch.setNewAssign());
		CatchAll catchAll = invoke.setNewCatchAll();
		activities.add(catchAll);
		activities.add(catchAll.setNewAssign());
		IPick pick = sequence.addPick();
		activities.add(pick);
		IOnMessage onMessage = pick.addOnMessage();
		activities.add(onMessage);
		activities.add(onMessage.setNewAssign());
		IOnAlarm onAlarm = pick.addOnAlarm();
		activities.add(onAlarm);
		activities.add(onAlarm.setNewEmpty());
		activities.add(sequence.addReceive());
		IRepeatUntil repeatUntil = sequence.addRepeatUntil();
		activities.add(repeatUntil);
		activities.add(repeatUntil.setNewEmpty());
		activities.add(sequence.addReply());
		activities.add(sequence.addRethrow());
		IScope scope = sequence.addScope();
		activities.add(scope);
		compensationHandler = scope.setNewCompensationHandler();
		activities.add(compensationHandler);
		activities.add(compensationHandler.setNewAssign());
		IOnAlarmEventHandler onAlarmHandler = scope.addNewOnAlarm();
		activities.add(onAlarmHandler);
		activities.add(onAlarmHandler.getScope());
		activities.add(onAlarmHandler.getScope().setNewEmpty());
		IOnMessageHandler onMessageHandler = scope.addNewOnMessage();
		activities.add(scope.setNewEmpty());
		activities.add(onMessageHandler);
		activities.add(onMessageHandler.getScope());
		activities.add(onMessageHandler.getScope().setNewEmpty());
		ccatch = scope.addNewCatch();
		activities.add(ccatch);
		activities.add(ccatch.setNewAssign());
		catchAll = scope.setNewCatchAll();
		activities.add(catchAll);
		activities.add(catchAll.setNewAssign());
		activities.add(sequence.addThrow());
		activities.add(sequence.addValidate());
		activities.add(sequence.addWait());
		IWhile wwhile = sequence.addWhile();
		activities.add(wwhile);
		activities.add(wwhile.setNewEmpty());
		
		return process;
	}
}
