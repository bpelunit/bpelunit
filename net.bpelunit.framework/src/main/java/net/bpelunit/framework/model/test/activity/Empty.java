package net.bpelunit.framework.model.test.activity;

import java.util.Collections;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;

public class Empty extends Activity {

	public Empty(PartnerTrack partnerTrack) {
		super(partnerTrack);
	}
	
	public Empty(Activity a) {
		super(a);
	}
	
	@Override
	public int getActivityCount() {
		return 1;
	}

	@Override
	public String getActivityCode() {
		return "empty";
	}

	@Override
	public String getName() {
		return "Empty";
	}

	@Override
	public List<? extends ITestArtefact> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public boolean isStartingWithMessageReceive() {
		return false;
	}
	
	@Override
	public void runInternal(ActivityContext context) {
		setStatus(ArtefactStatus.createPassedStatus());
	}

}
