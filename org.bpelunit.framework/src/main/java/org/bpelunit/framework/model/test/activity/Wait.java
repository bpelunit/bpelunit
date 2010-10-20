package org.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;

public class Wait extends Activity {

	private long waitDuration;

	public Wait(PartnerTrack partnerTrack) {
		super(partnerTrack);
	}

	@Override
	public String getActivityCode() {
		return "Wait";
	}

	@Override
	public int getActivityCount() {
		return 1;
	}

	@Override
	public List<ITestArtefact> getChildren() {
		return new ArrayList<ITestArtefact>();
	}

	@Override
	public String getName() {
		return "Wait";
	}

	@Override
	public ITestArtefact getParent() {
		return getPartnerTrack();
	}

	@Override
	public void run(ActivityContext context) {
		fStatus = ArtefactStatus.createInProgressStatus();

		long waitTill = System.currentTimeMillis() + this.waitDuration;
		
		while(System.currentTimeMillis() < waitTill) {
			try {
				Thread.sleep(Math.max(0, waitTill - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// Ignore this of time being
			}
		}
		
		fStatus = ArtefactStatus.createPassedStatus();
	}

	public void setWaitDuration(long waitForMilliseconds) {
		this.waitDuration = waitForMilliseconds;
	}	
}