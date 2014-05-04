/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.ws.LocalHTTPServer;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.TestCaseNotFoundException;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.data.extraction.ExtractedDataContainerUtil;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.ToolManager;

/**
 * A BPELUnit TestSuite is a collection of TestCases, along with the description
 * of PUT and partners, including deployment information for the PUT.
 * 
 * @author Philip Mayer
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public class TestSuite implements ITestArtefact, IExtractedDataContainer, VelocityContextProvider {

	/**
	 * The name of this test suite
	 */
	private String fName;

	/**
	 * The process to be tested.
	 */
	private ProcessUnderTest fProcessUnderTest;

	/**
	 * The test cases. Note: Must be a LinkedHashMap, as ordering is important.
	 * Maps the all-lowercase test case name to the test case object.
	 */
	private Map<String, TestCase> fTestCaseMap;

	/**
	 * The local HTTP server handling incoming requests
	 */
	private LocalHTTPServer fLocalServer;

	/**
	 * Listeners for test results
	 */
	private List<ITestResultListener> fResultListeners;

	/**
	 * If this list contains test cases, only those will be run
	 */
	private List<TestCase> fTestCaseFilter;

	/**
	 * true if a test run is currently in progress
	 */
	private boolean fCurrentlyRunning;

	/**
	 * true if the test run has been aborted by theuser
	 */
	private boolean fAbortedByUser;

	/**
	 * The test case which is currently run
	 */
	private TestCase fCurrentTestCase;

	/**
	 * Status of this object
	 */
	private ArtefactStatus fStatus;

	/**
	 * Base URL for the mockups. Used for initializing the local Jetty server for the mockups.
	 */
	private URL fBaseURL;

	/**
	 * The logger
	 */
	private Logger fLogger;

	private String fSetUpVelocityScript;

	private final ToolManager toolManager = new ToolManager();

	private final Map<String, Object> fExtractedData = new HashMap<String, Object>();

	// ****************************** Initialization **************************

	public TestSuite(String suiteName, URL suiteBaseURL,
			ProcessUnderTest suiteProcessUnderTest) {

		fStatus = ArtefactStatus.createInitialStatus();
		fResultListeners = new ArrayList<ITestResultListener>();
		fLogger = Logger.getLogger(getClass());

		fName = suiteName;
		fProcessUnderTest = suiteProcessUnderTest;
		fCurrentlyRunning = false;
		fTestCaseMap = new LinkedHashMap<String, TestCase>();

		setBaseURL(suiteBaseURL);
	}

	public void addTestCase(TestCase test) {
		fTestCaseMap.put(test.getRawName().toLowerCase(), test);
	}

	/**
	 * Filters this test suite to only run the test case with the specified
	 * name.
	 * 
	 * Convenience method for {@link #setFilter(List)}.
	 * 
	 * @param testCaseName
	 * @throws TestCaseNotFoundException
	 *             If a test case with that name was not found.
	 */
	public void setFilter(String testCaseName) throws TestCaseNotFoundException {
		List<String> tempList = new ArrayList<String>();
		tempList.add(testCaseName);
		setFilter(tempList);
	}

	/**
	 * Filters this test suite to only run the test cases with the names
	 * specified in the list (in that order).
	 * 
	 * @see #setFilter(String)
	 * @param testCaseNames
	 * @throws TestCaseNotFoundException
	 *             If one of the test cases does not exist.
	 */
	public void setFilter(List<String> testCaseNames)
			throws TestCaseNotFoundException {
		List<TestCase> filtered = new ArrayList<TestCase>();
		for (String name : testCaseNames) {
			addTestCaseToFilter(filtered, name);
		}

		fTestCaseFilter = filtered;
	}

	// ************ Running *************

	public void setUp() throws DeploymentException {

		fLogger.info("Now starting local HTTP server...");
		try {
			getLocalServer().startServer();
		} catch (Exception e) {
			fLogger.error("Error starting local HTTP server: " + e.getMessage());
			throw new DeploymentException(
					"Could not start local HTTP server - maybe the address is in use? ",
					e);
		}

		fLogger.info("Now deploying PUT: " + fProcessUnderTest);
		fProcessUnderTest.deploy();
	}

	public void shutDown() throws DeploymentException {
		fLogger.info("Now stopping fixture server...");

		// Stop the server first (before any DeploymentExceptions are thrown by
		// the undeployer)
		try {
			getLocalServer().stopServer();
		} catch (Exception e) {
			fLogger.error("Exception while stopping fixture server", e);
		}

		if (fProcessUnderTest.isDeployed()) {
			fLogger.info("Now undeploying: " + fProcessUnderTest);
			fProcessUnderTest.undeploy();
		}
	}

	public void run() {

		fLogger.info("Now starting test suite: " + this);
		fCurrentTestCase = null;
		fCurrentlyRunning = true;

		boolean error = false;
		boolean failure = false;

		if (fTestCaseFilter == null) {
			fTestCaseFilter = new ArrayList<TestCase>(fTestCaseMap.values());
		}

		for (TestCase testCase : fTestCaseFilter) {
			fCurrentTestCase = testCase;
			testCase.run();
			try {
			  fProcessUnderTest.cleanUpAfterTestCase();
			} catch (Exception ex) {
				fLogger.error(ex.getMessage(), ex);
			    failure = true;
			}
			if (testCase.getStatus().isError()) {
				error = true;
				if (BPELUnitRunner.isHaltOnError()) {
					break;
				}
			}
			if (testCase.getStatus().isFailure()) {
				failure = true;
				if (BPELUnitRunner.isHaltOnFailure()) {
					break;
				}
			}

			if (fAbortedByUser) {
				break;
			}
		}

		fCurrentTestCase = null;

		if (error) {
			fStatus = ArtefactStatus
					.createErrorStatus("A test case had an error");
		} else if (failure) {
			fStatus = ArtefactStatus
					.createFailedStatus("A test case had a failure");
		} else if (fAbortedByUser) {
			fStatus = ArtefactStatus.createAbortedStatus("Aborted by user");
		} else {
			fStatus = ArtefactStatus.createPassedStatus();
		}

		fCurrentlyRunning = false;
		fLogger.info("Now stopping test suite: " + this);
	}

	// ************* Result Listeners and reporting *************

	public void addResultListener(ITestResultListener listener) {
		fResultListeners.add(listener);
	}

	public void removeResultListener(ITestResultListener listener) {
		fResultListeners.remove(listener);
	}

	public void startTestCase(TestCase case1) {
		for (ITestResultListener listener : fResultListeners) {
			listener.testCaseStarted(case1);
		}
	}

	public void endTestCase(TestCase case1) {
		for (ITestResultListener listener : fResultListeners) {
			listener.testCaseEnded(case1);
		}
	}

	// ******************* Getters ******************************

	public boolean hasTestCase(String testCaseName) {
		return fTestCaseMap.containsKey(testCaseName.toLowerCase());
	}

	public int getTestCaseCount() {
		return fTestCaseMap.size();
	}

	public ProcessUnderTest getProcessUnderTest() {
		return fProcessUnderTest;
	}

	public void abortTest() {
		if (isRunning()) {
			fAbortedByUser = true;
			if (fCurrentTestCase != null) {
				fCurrentTestCase.abortTest();
			}
		}
	}

	public boolean isRunning() {
		return fCurrentlyRunning;
	}

	public LocalHTTPServer getLocalServer() {
		// We use lazy initialization so the user can change programmatically
		// the base URL (and thus the port BPELUnit listens on) after creating
		// the TestSuite but before calling #setUp.
		if (fLocalServer == null) {
			fLocalServer = new LocalHTTPServer(
					getBaseURL().getPort(), getBaseURL().getPath());
		}
		return fLocalServer;
	}

	public URL getBaseURL() {
		return fBaseURL;
	}

	/**
	 * final because called from constructor
	 * 
	 * @param fBaseURL
	 */
	public final void setBaseURL(URL fBaseURL) {
		this.fBaseURL = fBaseURL;
	}

	/**
	 * Creates a new VelocityContext with information about this test suite.
	 * If necessary, it will initialize Velocity.
	 *
	 * This method extends the Velocity context with the latest copies of the
	 * extracted data from all the ancestors of <code>artefact</code> (including
	 * itself) that are {@link IExtractedDataContainer}s, from the oldest ancestor
	 * to the youngest one.
	 *
	 * NOTE: to keep test cases and activities isolated, this context should
	 * not be wrapped, but rather be cloned and then extended.
	 * @throws DataSourceException 
	 */
	public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException  {
		try {
			Velocity.init();
		} catch(Exception e) {
			// XXX DL: This is stupid but it seems that the logger
			// cannot be initialized on the first try when running
			// under Eclipse and ATM I don't know a better solution
			try {
				Velocity.init();
			} catch (Exception e1) {
				throw new DataSourceException(e);
			}
		}

		final WrappedContext ctx = new WrappedContext(toolManager.createContext());
		ctx.putReadOnly("baseURL", getBaseURL().toString());
		ctx.putReadOnly("collections", java.util.Collections.class);
		ctx.putReadOnly("putName", fProcessUnderTest.getName());
		ctx.putReadOnly("testSuiteName", this.getRawName());
		ctx.putReadOnly("testCaseCount", this.getTestCaseCount());

		if (fSetUpVelocityScript != null) {
			StringWriter sW = new StringWriter();
			try {
				Velocity.evaluate(ctx, sW, "setUpTestSuite", fSetUpVelocityScript);
			} catch (Exception e) {
				throw new DataSourceException(e);
			}
		}

		ExtractedDataContainerUtil.addExtractedDataFromAncestors(ctx, artefact);
		return ctx;
	}

	// *********** ITestArtefact ************

	public String getName() {
		return "Test Suite " + fName;
	}

	public String getRawName() {
		return fName;
	}
	
	public String getSafeName() {
		return fName.replaceAll("\\.|/|\\s|\\\\", "-");
	}

	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children = new ArrayList<ITestArtefact>();
		for (TestCase testCase : fTestCaseMap.values()) {
			children.add(testCase);
		}
		return children;
	}

	public ITestArtefact getParent() {
		// no parent.
		return null;
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public List<StateData> getStateData() {
		return fStatus.getAsStateData();
	}

	public synchronized void reportProgress(ITestArtefact artefact) {
		for (ITestResultListener listener : fResultListeners) {
			listener.progress(artefact);
		}
	}

	// *********** IExtractedDataContainer ************

	@Override
	public void putExtractedData(String name, Object value) {
		fExtractedData.put(name, value);
	}

	@Override
	public Object getExtractedData(String name) {
		return fExtractedData.get(name);
	}

	@Override
	public Collection<String> getAllExtractedDataNames() {
		return fExtractedData.keySet();
	}
	
	// *********************** Other ******************************

	private void addTestCaseToFilter(List<TestCase> filtered, String name)
			throws TestCaseNotFoundException {
		TestCase testCase = fTestCaseMap.get(name.toLowerCase());
		if (testCase != null) {
			filtered.add(testCase);
		} else {
			throw new TestCaseNotFoundException("Test Case with name \"" + name
					+ "\" does not exist in this suite.");
		}
	}

	@Override
	public String toString() {
		return "TestSuite \"" + getName() + "\" (" + getTestCaseCount()
				+ " test cases)";
	}

	// //HIER
	public List<String> getTestCases() {
		List<String> testCases = new ArrayList<String>();
		for (Iterator<TestCase> iter = fTestCaseMap.values().iterator(); iter
				.hasNext();) {
			testCases.add(iter.next().getName());
		}
		return testCases;
	}

	// ********************** setUp/tearDown ***********************

	public String getSetUpVelocityScript() {
		return fSetUpVelocityScript;
	}

	public void setSetUpVelocityScript(String script) {
		fSetUpVelocityScript = script;
	}

	public Collection<TestCase> getTestCasesToExecute() {
		if(fTestCaseFilter == null || fTestCaseFilter.size() == 0) {
			return fTestCaseMap.values();
		} else {
			return Collections.unmodifiableList(fTestCaseFilter);
		}
	}
}
