/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.data.ContextXPathVariableResolver;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.bpelunit.framework.model.test.report.StateData;
import org.w3c.dom.Document;

import com.rits.cloning.Cloner;

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

	private String fAssumption;

	private NamespaceContext fNamespaceContext;

	private Object fTestCaseVelocityContext;

	private static final Cloner fCloner = new Cloner();

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

		if (assumptionHolds(fAssumption)) {
			for (Activity activity : fActivities) {

				if (assumptionHolds(activity.getAssumption())) {
					fLogger.info(getName() + " now starting activity "
							+ activity);
					ActivityContext context = new ActivityContext(fRunner, this);
					activity.run(context);
					fLogger.info(getName() + " returned from activity "
							+ activity);
				} else {
					fLogger.info(getName() + " skipped activity " + activity);
				}

				reportProgress(activity);

				if (activity.hasProblems()) {
					fStatus = activity.getStatus();
					break;
				}
			}
		} else {
			fLogger.info(getName() + " was skipped.");
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

	public void setAssumption(String assumption) {
		fAssumption = assumption;
	}

	public void setNamespaceContext(NamespaceContext context) {
		fNamespaceContext = context;
	}

	// ************* ITestArtefact **********

	public String getName() {
		return "Partner Track " + getPartner().getName();
	}

	public String getRawName() {
		return getPartner().getName();
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

	/* VELOCITY */

	/**
	 * Creates a new partner track-wide Velocity context. It collects
	 * information from the test suite, the test case and this partner track.
	 * This context is isolated from all the following partner tracks: any
	 * changes done to the test suite and test case variables will be lost in
	 * the next partner track.
	 * 
	 * This method obtains and caches the VelocityContext of the test case, so
	 * it only needs to be produced once for every partner track.
	 * 
	 * @return Base VelocityContext for the partner track.
	 */
	public VelocityContext createVelocityContext() throws Exception {
		if (fTestCaseVelocityContext == null) {
			fTestCaseVelocityContext = fRunner.createVelocityContext();
		}
		VelocityContext ctx = (VelocityContext) fCloner
				.deepClone(fTestCaseVelocityContext);
		ctx.put("partnerTrackName", getRawName());
		ctx.put("partnerTrackURL", getPartner().getSimulatedURL());
		return ctx;
	}

	/* PRIVATE METHODS */

	private boolean assumptionHolds(final String assumption) {
		if (assumption == null)
			return true;

		Context context;
		try {
			context = this.createVelocityContext();

			XPath xpath = XPathFactory.newInstance().newXPath();
			xpath.setNamespaceContext(fNamespaceContext);
			xpath.setXPathVariableResolver(new ContextXPathVariableResolver(
					context));

			return (Boolean) xpath.evaluate(assumption, createEmptyDocument(),
					XPathConstants.BOOLEAN);
		} catch (Exception e) {
			fStatus = ArtefactStatus.createErrorStatus(
					"Failed to evaluate the assumption " + fAssumption, e);
			return false;
		}
	}

	private Document createEmptyDocument() throws ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}

}
