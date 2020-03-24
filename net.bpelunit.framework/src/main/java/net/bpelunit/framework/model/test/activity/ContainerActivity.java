package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;

public abstract class ContainerActivity<A extends Activity> extends Activity {

	private List<A> activities = new ArrayList<A>();
	
	public ContainerActivity(PartnerTrack pt) {
		super(pt);
	}
	
	public ContainerActivity(Activity a) {
		super(a);
	}
	
	public ContainerActivity(ITestArtefact parent) {
		super(parent);
	}

	public List<A> getActivities() {
		return activities;
	}
	
	public void addActivity(A e) {
		activities.add(e);
	}
	
	@Override
	public int getActivityCount() {
		int result = 1;
		for(A activity : getActivities()) {
			result += activity.getActivityCount();
		}
		
		return result;
	}
	
	@Override
	public List<? extends ITestArtefact> getChildren() {
		return new ArrayList<ITestArtefact>(getActivities());
	}

	public abstract void runNext(ActivityContext context);
	public abstract void runNext(ActivityContext context, IncomingMessage message);

	public void abort(String message) {
		this.setStatus(ArtefactStatus.createAbortedStatus(message));
	}
	
}
