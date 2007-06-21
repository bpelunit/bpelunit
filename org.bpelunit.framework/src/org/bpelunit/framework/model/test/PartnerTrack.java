/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.bpelunit.framework.model.test.report.StateData;

/**
 * A PartnerTrack represents the sequential list of activities which are
 * executed on behalf of the partner in a certain test case. The PartnerTrack
 * can be seen as the simulated partner itself.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class PartnerTrack implements ITestArtefact, Runnable {

	/**
	 * The parent test case
	 */
	private TestCase fTestCase;

	/**
	 * The activities of this partner track
	 */
	private List<Activity> fActivities;

	/**
	 * The simulated partner
	 */
	private Partner fPartner;

	/**
	 * The test runner
	 */
	private TestCaseRunner fRunner;

	/**
	 * Status of this object
	 */
	private ArtefactStatus fStatus;

	/**
	 * The logger
	 */
	private Logger fLogger;

	public PartnerTrack(TestCase testCase, Partner client) {
		fPartner = client;
		fTestCase = testCase;
		fStatus = ArtefactStatus.createInitialStatus();
		fLogger = Logger.getLogger(getClass());
	}

	public void initialize(TestCaseRunner runner) {
		fRunner = runner;
	}

	public void setActivities(List<Activity> activities) {
		fActivities = activities;
	}

	public void run() {

		fLogger.info(getName() + " now active.");

		for (Activity activity : fActivities) {

			fLogger.info(getName() + " now starting activity " + activity);

			ActivityContext context = new ActivityContext(fRunner, this);
			activity.run(context);

			fLogger.info(getName() + " returned from activity " + activity);

			reportProgress(activity);

			if (activity.hasProblems()) {
				fStatus = activity.getStatus();
				break;
			}
		}

		// Ensure set status before notification
		if (!hasProblems())
			fStatus = ArtefactStatus.createPassedStatus();

		// Notify
		fLogger.info(getName() + " finished.");
		reportProgress(this);

		// Return
		if (hasProblems())
			fRunner.doneWithFault(this);
		else
			fRunner.done(this);

	}

	public boolean hasProblems() {
		return fStatus.hasProblems();
	}

	public String getPartnerName() {
		return fPartner.getName();
	}

	public Partner getPartner() {
		return fPartner;
	}

	public int getActivityCount() {
		int activityCount = 0;
		for (Activity activity : fActivities) {
			activityCount += activity.getActivityCount();
		}
		return activityCount;
	}

	@Override
	public String toString() {
		return getName();
	}

	// ************* ITestArtefact **********

	public String getName() {
		return "Partner Track " + getPartner().getName();
	}

	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children = new ArrayList<ITestArtefact>();
		for (Activity activity : fActivities) {
			children.add(activity);
		}
		return children;
	}

	public ITestArtefact getParent() {
		return fTestCase;
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public List<StateData> getStateData() {
		return fStatus.getAsStateData();
	}

	public void reportProgress(ITestArtefact artefac) {
		fTestCase.reportProgress(artefac);
	}

}
