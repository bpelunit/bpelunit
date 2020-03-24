package net.bpelunit.framework.model.test.activity;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;

public class Sequence extends ContainerActivity<Activity> {

	private int currentIndex = 0;
	
	public Sequence(PartnerTrack partnerTrack) {
		super(partnerTrack);
	}
	
	public Sequence(Activity p) {
		super(p);
	}
	
	public Sequence(ITestArtefact parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return "Sequence";
	}

	@Override
	public void reportProgress(ITestArtefact artefact) {
		getParent().reportProgress(artefact);
	}

	public boolean isStartingWithMessageReceive() {
		if (currentIndex >= 0) {
			Activity nextActivity = getNextActivity();
			return nextActivity.isStartingWithMessageReceive();
		} else {
			return false;
		}
	}

	public boolean canExecute(ActivityContext context, IncomingMessage message) {
		if (currentIndex >= 0) {
			Activity nextActivity = getNextActivity();
			return nextActivity.canExecute(context, message);
		} else {
			return false;
		}
	}

	public void runNext(ActivityContext context) {
		Activity nextActivity = getNextActivity();

		nextActivity.run(context);

		if (nextActivity.hasProblems()) {
			setStatus(nextActivity.getStatus());
		}
		reportProgress(this);

		progressToNextActivity();
	}

	private Activity getNextActivity() {
		if(currentIndex < 0 || getActivities().isEmpty()) {
			return null;
		}
		return getActivities().get(currentIndex);
	}

	@Override
	public void runNext(ActivityContext context, IncomingMessage incoming) {
		Activity nextActivity = getNextActivity();

		nextActivity.run(context, incoming);

		if (nextActivity.hasProblems()) {
			setStatus(nextActivity.getStatus());
		}
		reportProgress(this);

		progressToNextActivity();
	}

	private void progressToNextActivity() {
		currentIndex++;
		if (currentIndex >= getActivities().size()) {
			currentIndex = -1;
			setStatus(getChildren().get(getChildren().size() - 1).getStatus()); // final state
		}
	}

	@Override
	public String getActivityCode() {
		return "Sequence";
	}
	
	@Override
	public void runInternal(ActivityContext context) {
		runInternal(context, null);
	}
	
	@Override
	public void runInternal(ActivityContext context, IncomingMessage message) {
		if(message != null) {
			runNext(context, message);
		}
		
		while(!this.getStatus().isFinal()) {
			runNext(context);
		}
	}
}