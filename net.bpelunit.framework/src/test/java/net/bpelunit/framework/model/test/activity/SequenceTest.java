package net.bpelunit.framework.model.test.activity;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import org.junit.Test;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

public class SequenceTest {

	@Test
	public void test_run_AllDirectlyExecutableChildActivitiesAreExecuted() {
		Sequence sequence = new Sequence((PartnerTrack)null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
				return;
			}
		};
		
		Empty e1 = new Empty(sequence);
		Empty e2 = new Empty(sequence);
		Empty e3 = new Empty(sequence);
		
		sequence.addActivity(e1);
		sequence.addActivity(e2);
		sequence.addActivity(e3);
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy");
		sequence.run(ctx);
		
		assertTrue(e1.getStatus().isPassed());
		assertTrue(e2.getStatus().isPassed());
		assertTrue(e3.getStatus().isPassed());
		assertTrue(sequence.getStatus().isPassed());
	}
	
	@Test
	public void test_run_AllDirectlyExecutableChildActivitiesAreExecuted_WithFailure() {
		Sequence sequence = new Sequence((PartnerTrack)null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
				return;
			}
		};
		
		Empty e1 = new Empty(sequence);
		Empty e2 = new Empty(sequence) {
			@Override
			public void runInternal(ActivityContext context) {
				setStatus(ArtefactStatus.createAbortedStatus("Test Abortion"));
			}
		};
		Empty e3 = new Empty(sequence);
		
		sequence.addActivity(e1);
		sequence.addActivity(e2);
		sequence.addActivity(e3);
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy");
		sequence.run(ctx);
		
		assertTrue(e1.getStatus().isPassed());
		assertTrue(e2.getStatus().isAborted());
		assertTrue(e3.getStatus().isInitial());
		assertTrue(sequence.getStatus().isAborted());
		
	}
	
	@Test
	public void test_runWithMessage_AllDirectlyExecutableChildActivitiesAreExecuted() throws SpecificationException {
		Sequence sequence = new Sequence((PartnerTrack)null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
				return;
			}
		};
		
		ReceiveAsync r1 = new ReceiveAsync(sequence);
		ReceiveDataSpecification spec = new ReceiveDataSpecification(r1, null) {
			@Override
			public void handle(ActivityContext context, IncomingMessage msg) {
			}
		};
		r1.initialize(spec, null);
		Empty e2 = new Empty(sequence);
		Empty e3 = new Empty(sequence);
		
		sequence.addActivity(r1);
		sequence.addActivity(e2);
		sequence.addActivity(e3);
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy") {
			@Override
			public void postAnswer(PartnerTrack track, OutgoingMessage msg)
					throws TimeoutException, InterruptedException {
			}
		};
		IncomingMessage message = new IncomingMessage();
		message.setMessage("<xml />".getBytes());
		sequence.run(ctx, message);
		
		assertTrue(r1.getStatus().isPassed());
		assertTrue(e2.getStatus().isPassed());
		assertTrue(e3.getStatus().isPassed());
		assertTrue(sequence.getStatus().isPassed());
	}

	
}
