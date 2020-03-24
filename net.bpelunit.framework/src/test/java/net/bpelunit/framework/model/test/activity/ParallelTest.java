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

public class ParallelTest {

	@Test
	public void test_run_AllDirectlyExecutableChildActivitiesAreExecuted() {
		Parallel parallel = new Parallel((PartnerTrack)null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
				return;
			}
		};
		Sequence s1 = new Sequence(parallel);
		parallel.addActivity(s1);
		Empty e1 = new Empty(s1);
		s1.addActivity(e1);
		
		Sequence s2 = new Sequence(parallel);
		parallel.addActivity(s2);
		Empty e2 = new Empty(s2);
		s2.addActivity(e2);
		Empty e3 = new Empty(s2);
		s2.addActivity(e3);
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy");
		parallel.run(ctx);
		
		assertTrue(e1.getStatus().isPassed());
		assertTrue(s1.getStatus().isPassed());
		assertTrue(e2.getStatus().isPassed());
		assertTrue(e3.getStatus().isPassed());
		assertTrue(s2.getStatus().isPassed());
		assertTrue(parallel.getStatus().isPassed());
	}
	
	@Test
	public void test_run_AllDirectlyExecutableChildActivitiesAreExecuted_WithFailure() {
		Parallel parallel = new Parallel((PartnerTrack)null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
				return;
			}
		};
		Sequence s1 = new Sequence(parallel);
		parallel.addActivity(s1);
		Empty e1 = new Empty(s1) {
			@Override
			public void runInternal(ActivityContext context) {
				// TODO Auto-generated method stub
				setStatus(ArtefactStatus.createErrorStatus("ERROR"));
			}
		};
		s1.addActivity(e1);
		
		Sequence s2 = new Sequence(parallel);
		parallel.addActivity(s2);
		Empty e2 = new Empty(s2);
		s2.addActivity(e2);
		Empty e3 = new Empty(s2);
		s2.addActivity(e3);
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy");
		parallel.run(ctx);
		
		assertTrue(e1.getStatus().isError());
		assertTrue(s1.getStatus().isError());
		assertTrue(s2.getStatus().isAborted());
		assertTrue(parallel.getStatus().isError());
		
	}
	
}
