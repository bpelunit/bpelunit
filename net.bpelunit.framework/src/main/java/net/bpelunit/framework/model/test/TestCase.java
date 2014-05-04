/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.control.datasource.DataSourceUtil;
import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.run.TestCaseRunner;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.data.extraction.ExtractedDataContainerUtil;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

import org.apache.velocity.app.Velocity;

import com.rits.cloning.Cloner;

/**
 * A BPELUnit Test Case is a description of an interaction with the BPEL PUT, consisting of a number
 * of PartnerTracks, which run in parallel and contain sequences of activities for interaction with
 * the PUT.
 * 
 * @author Philip Mayer
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public class TestCase implements ITestArtefact, IExtractedDataContainer, VelocityContextProvider {

	private static final Cloner CLONER = new Cloner();

	/**
	 * The test case name
	 */
	private String fName;

	/**
	 * The meta data associated with this test case
	 */
	private Map<String, String> fMetaDataMap;

	/**
	 * Test suite this test case belongs to
	 */
	private TestSuite fSuite;

	/**
	 * The partner tracks of this test case
	 */
	private List<PartnerTrack> fPartnerTracks;

	/**
	 * Status of this object
	 */
	private ArtefactStatus fStatus;

	/**
	 * Runner used to run the partner tracks
	 */
	private TestCaseRunner fRunner;

	/**
	 * If true, the user has aborted this test case
	 */
	private boolean fAbortedByUser;

	private String fSetUpVelocityScript;

	private WrappedContext fTestSuiteVelocityContext;

	private IDataSource fDataSource;

	private int fRowIndex;

	private Map<String, Object> fExtractedData = new HashMap<String, Object>();

	// ****************** Initialization ************************

	public TestCase(TestSuite suite, String name) {
		fSuite= suite;
		fName= name;
		fAbortedByUser= false;

		fMetaDataMap= new HashMap<String, String>();
		fPartnerTracks= new ArrayList<PartnerTrack>();
		fStatus= ArtefactStatus.createInitialStatus();
	}

	public void setName(String name) {
		fName= name;
	}

	public void addPartnerTrack(PartnerTrack track) {
		fPartnerTracks.add(track);
	}

	// *********************** Running ***************************

	public void run() {

		fSuite.startTestCase(this);

		fRunner= new TestCaseRunner(fSuite.getLocalServer(), this);
		fRunner.run();

		for (PartnerTrack partnerTrack : fPartnerTracks) {
			if (partnerTrack.getStatus().isError()) {
				fStatus= partnerTrack.getStatus();
			} else if (partnerTrack.getStatus().isFailure()) {
				fStatus= partnerTrack.getStatus();
			}
		}

		if (fAbortedByUser) {
			fStatus= ArtefactStatus.createAbortedStatus("Aborted by user.");
		}

		if (!fStatus.hasProblems()) {
			fStatus= ArtefactStatus.createPassedStatus();
		}

		fSuite.endTestCase(this);
	}

	public void abortTest() {
		fAbortedByUser= true;
		fRunner.abortTest();
	}

	// ************* ITestArtefact **********

	public String getName() {
		return "Test Case " + fName;
	}

	public String getRawName() {
		return fName;
	}

	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		for (PartnerTrack track : getPartnerTracks()) {
			children.add(track);
		}
		return children;
	}

	public ITestArtefact getParent() {
		return fSuite;
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		for (String key : fMetaDataMap.keySet()) {
			stateData.add(new StateData(key, fMetaDataMap.get(key)));
		}
		return stateData;
	}

	public void reportProgress(ITestArtefact artefac) {
		fSuite.reportProgress(artefac);
	}

	// ********* IExtractedDataContainer **********

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

	// ********************* GETTERS & SETTERS **************************

	public void addProperty(String property, String value) {
		fMetaDataMap.put(property, value);
	}

	public List<PartnerTrack> getPartnerTracks() {
		return fPartnerTracks;
	}

	public TestSuite getSuite() {
		return fSuite;
	}

	public String getProperty(String property) {
		return fMetaDataMap.get(property);
	}

	public boolean hasProblems() {
		return fStatus.hasProblems();
	}

	public int getActivityCount() {
		int no = 0;
		for (PartnerTrack partnerTrack : fPartnerTracks) {
			no += partnerTrack.getActivityCount();
		}
		return no;
	}

	@Override
	public String toString() {
		return getName();
	}

	// ******************** VELOCITY **************************

	/**
	 * Creates a new Velocity context that extends the VelocityContext of the
	 * test suite with information about this test case. To keep test cases
	 * isolated, the context does not wrap the test suite context, but clones
	 * and extends it. To reduce overhead, the test suite context is cached so
	 * it is only produced the first time.
	 *
	 * This method extends the Velocity context with the latest copies of the
	 * extracted data from all the ancestors of <code>artefact</code> (including
	 * itself) that are {@link IExtractedDataContainer}s, from the oldest ancestor
	 * to the youngest one.
	 *
	 * @return VelocityContext with information about the test suite and test
	 * case.
	 * @throws DataSourceException 
	 * */
	public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException {
		if (fTestSuiteVelocityContext == null) {
			fTestSuiteVelocityContext = getSuite().createVelocityContext(artefact);
		}

		final WrappedContext ctx = CLONER.deepClone(fTestSuiteVelocityContext);
		ctx.putReadOnly("testCaseName", getRawName());
		if (fDataSource != null) {
			DataSourceUtil.initializeContext(ctx, fDataSource, fRowIndex);
		}
		if (fSetUpVelocityScript != null) {
			StringWriter sW = new StringWriter();
			try {
				Velocity.evaluate(ctx, sW, "setUpTestCase", fSetUpVelocityScript);
			} catch (Exception e) {
				throw new DataSourceException(e);
			}
		}

		ExtractedDataContainerUtil.addExtractedDataFromAncestors(ctx, artefact);
		return ctx;
	}

	public String getSetUpVelocityScript() {
		return fSetUpVelocityScript;
	}

	public void setSetUpVelocityScript(String script) {
		fSetUpVelocityScript = script;
	}

	public void setDataSource(IDataSource fDataSource) {
		this.fDataSource = fDataSource;
	}

	public IDataSource getDataSource() {
		return fDataSource;
	}

	public void setRowIndex(int fRowIndex) {
		this.fRowIndex = fRowIndex;
	}

	public int getRowIndex() {
		return fRowIndex;
	}
}

