/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.run.BlackBoard;
import net.bpelunit.framework.control.run.BlackBoardKey;
import net.bpelunit.framework.control.run.TestCaseRunner;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.model.AbstractPartner;
import net.bpelunit.framework.model.Partner;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.data.ContextXPathVariableResolver;
import net.bpelunit.framework.model.test.data.extraction.ExtractedDataContainerUtil;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ArtefactStatus.StatusCode;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

import org.apache.log4j.Logger;
import org.apache.velocity.context.Context;
import org.w3c.dom.Document;

import com.rits.cloning.Cloner;

/**
 * A PartnerTrack represents the sequential list of activities which are
 * executed on behalf of the partner in a certain test case. The PartnerTrack
 * can be seen as the simulated partner itself.
 * 
 * @author Philip Mayer
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public class PartnerTrack implements ITestArtefact, IExtractedDataContainer, Runnable, VelocityContextProvider, BlackBoardKey {

	private static final int DELAY_AT_START = 10;
	private static final Cloner CLONER = new Cloner();

	/**
	 * The parent test case
	 */
	private TestCase fTestCase;

	/**
	 * The activities of this partner track
	 */
	private List<Activity> fActivities = new ArrayList<Activity>();

	/**
	 * The simulated partner
	 */
	private AbstractPartner fPartner;

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

	private WrappedContext fTestCaseVelocityContext;

	private ActivityContext fActivityContext;

	private final Map<String, Object> extractedData = new HashMap<String, Object>();

	public PartnerTrack(TestCase testCase, AbstractPartner client) {
		fPartner = client;
		fTestCase = testCase;
		fStatus = ArtefactStatus.createInitialStatus();
		fLogger = Logger.getLogger(getClass());
	}

	public void initialize(TestCaseRunner runner) {
		fRunner = runner;
	}

	public void setActivities(List<Activity> activities) {
		if(activities != null) {
			fActivities = activities;
		} else {
			fActivities.clear();
		}
	}

	public void addActivity(Activity a) {
		if(fActivities == null) {
			fActivities = new ArrayList<Activity>();
		}
		
		fActivities.add(a);
	}
	
	public void run() {
		try {
			fLogger.info(getName() + " now active.");
			fActivityContext = new ActivityContext(fRunner, this);

			// wait till all partners are active
			// XXX make this better
			Thread.sleep(DELAY_AT_START);

			if (assumptionHolds(fAssumption)) {
				for (Activity activity : fActivities) {
					if (assumptionHolds(activity.getAssumption())) {
						fLogger.info(getName() + " now starting activity " + activity);
						activity.run(fActivityContext);
						fLogger.info(getName() + " returned from activity " + activity);
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
			if (!hasProblems()) {
				fStatus = ArtefactStatus.createPassedStatus();
			}

			// Notify
			fLogger.info(getName() + " finished.");
			reportProgress(this);
		}
		catch (InterruptedException ex) {
			fStatus = ArtefactStatus.createErrorStatus("Partner track " + getName() + " was interrupted");
		}
		finally {
			// Return
			if (hasProblems()) {
				fRunner.doneWithFault(this);
			} else {
				fRunner.done(this);
			}
		}
	}

	public boolean hasProblems() {
		return fStatus.hasProblems();
	}

	public String getPartnerName() {
		return fPartner.getName();
	}

	public AbstractPartner getPartner() {
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

	public boolean isDone() {
		final StatusCode code = fStatus.getCode();
		return code != StatusCode.INPROGRESS && code != StatusCode.NOTYETSPECIFIED;
	}

	// ************* BlackBoardKey **********

	@Override
	public boolean canStillProvideValue(BlackBoard<?, ?> blackboard) {
		return !isDone();
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
	 * This method extends the Velocity context with the latest copies of the
	 * extracted data from all the ancestors of <code>artefact</code> (including
	 * itself) that are {@link IExtractedDataContainer}s, from the oldest ancestor
	 * to the youngest one.
	 *
	 * @return Base VelocityContext for the partner track.
	 * @throws DataSourceException
	 */
	public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException   {
		if (fTestCaseVelocityContext == null) {
			fTestCaseVelocityContext = fRunner.createVelocityContext(artefact);
		}
		WrappedContext ctx = CLONER.deepClone(fTestCaseVelocityContext);
		ctx.putReadOnly("partnerTrackName", getRawName());

		if(getPartner() instanceof Partner) {
			ctx.putReadOnly("partnerTrackURL", ((Partner)getPartner()).getSimulatedURL());
		}
		if (fActivityContext != null) {
			ctx.putReadOnly("request", fActivityContext.getLastRequest());
			ctx.putReadOnly("partnerTrackReceived", fActivityContext.getReceivedMessages());
			ctx.putReadOnly("partnerTrackSent", fActivityContext.getSentMessages());
		}

		ExtractedDataContainerUtil.addExtractedDataFromAncestors(ctx, artefact);
		return ctx;
	}

	/* PRIVATE METHODS */
	private boolean assumptionHolds(final String assumption) {
		if (assumption == null) {
			return true;
		}

		Context context;
		try {
			context = this.createVelocityContext(this);

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

	// ************* IExtractedDataContainer **********

	@Override
	public void putExtractedData(String name, Object value) {
		extractedData.put(name, value);
	}

	@Override
	public Object getExtractedData(String name) {
		return extractedData.get(name);
	}

	@Override
	public Collection<String> getAllExtractedDataNames() {
		return extractedData.keySet();
	}

}
