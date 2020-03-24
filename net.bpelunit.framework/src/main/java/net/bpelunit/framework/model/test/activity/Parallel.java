package net.bpelunit.framework.model.test.activity;

import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;

public class Parallel extends ContainerActivity<Sequence> {

	@SuppressWarnings("serial")
	private static final class StatusFailedException extends Exception {

		private ArtefactStatus status;
		
		public StatusFailedException(ArtefactStatus status) {
			this.status = status;
		}
		
		public ArtefactStatus getStatus() {
			return status;
		}
		
	}

	@SuppressWarnings("serial")
	private static final class CorrelationError extends Exception {

		private IncomingMessage incomingMessage;

		public CorrelationError(IncomingMessage message) {
			incomingMessage = message;
		}

		public String getIncomingMessageAsString() {
			return incomingMessage.getMessageAsString();
		}
	}

	public Parallel(PartnerTrack pt) {
		super(pt);
	}
	
	public Parallel(Activity a) {
		super(a);
	}

	public Parallel(ITestArtefact parent) {
		super(parent);
	}

	@Override
	public void runInternal(ActivityContext context, IncomingMessage message) {
		try {
			if (message != null) {
				dispatchIncomingMessage(context, message);
			}
			executeReadyActivities(context);

			while (!areAllSequencesComplete()) {
				message = context.receiveMessage(this.getPartnerTrack());
				dispatchIncomingMessage(context, message);
				executeReadyActivities(context);
			}
			
			setStatus(ArtefactStatus.createPassedStatus());
		} catch (CorrelationError e) {
			setStatus(ArtefactStatus.createFailedStatus(
					"Could not find match for incoming message:\n\n" + e.getIncomingMessageAsString()));
			e.printStackTrace();
		} catch (TimeoutException e) {
			setStatus(ArtefactStatus.createErrorStatus("Timeout while waiting for incoming asynchronous message", e));
		} catch (InterruptedException e) {
			setStatus(
					ArtefactStatus.createAbortedStatus("Aborted while waiting for incoming asynchronous messsage", e));
		} catch (StatusFailedException e) {
			abortWithStatus(e.getStatus());
		}
	}

	private void abortWithStatus(ArtefactStatus status) {
		for(Sequence s : getActivities()) {
			if(!s.getStatus().isFinal()) {
				s.abort("Aborted due to failure in parallely executed activity");
			}
		}
		setStatus(status);
		
	}

	private boolean executeReadyActivities(ActivityContext context) throws StatusFailedException {
		boolean hasSomethingBeenExecuted = false;
		for (Sequence ps : getActivities()) {
			while (!ps.isStartingWithMessageReceive() && !ps.getStatus().isFinal()) {
				ps.runNext(context);
				hasSomethingBeenExecuted = true;
				if(ps.hasProblems()) {
					throw new StatusFailedException(ps.getStatus());
				}
			}
		}
		
		return hasSomethingBeenExecuted;
	}

	private void dispatchIncomingMessage(ActivityContext context, IncomingMessage message) throws CorrelationError, StatusFailedException {
		boolean hasBeenHandled = false;
		for (Sequence ps : getActivities()) {
			if (ps.canExecute(context, message)) {
				ps.runNext(context, message);
				
				if(ps.hasProblems()) {
					throw new StatusFailedException(ps.getStatus());
				}
				
				hasBeenHandled = true;
				break;
			}
		}
		if (!hasBeenHandled) {
			throw new CorrelationError(message);
		}
	}

	private boolean areAllSequencesComplete() {
		for (Sequence ps : getActivities()) {
			if (!ps.getStatus().isFinal()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getActivityCode() {
		return "Parallel";
	}

	@Override
	public String getName() {
		return "Parallel";
	}

	@Override
	public boolean isStartingWithMessageReceive() {
		for (Sequence seq : getActivities()) {
			if (seq.isStartingWithMessageReceive()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExecute(ActivityContext context, IncomingMessage message) {
		for (Sequence seq : getActivities()) {
			if (seq.canExecute(context, message)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void runNext(ActivityContext context) {
		try {
			executeReadyActivities(context);
		} catch (StatusFailedException e) {
			abortWithStatus(e.getStatus());
		}
	}

	@Override
	public void runNext(ActivityContext context, IncomingMessage message) {
		try {
			dispatchIncomingMessage(context, message);
		} catch (CorrelationError e) {
			// TODO Auto-generated catch block
		} catch (StatusFailedException e) {
			abortWithStatus(e.getStatus());
		}
	}
	
	@Override
	public void runInternal(ActivityContext context) {
		runInternal(context, null);
	}
}
