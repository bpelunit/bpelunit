/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.bpelunit.framework.model.test.report.StateData;

/**
 * A BPELUnit Test Case is a description of an interaction with the BPEL PUT, consisting of a number
 * of PartnerTracks, which run in parallel and contain sequences of activities for interaction with
 * the PUT.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TestCase implements ITestArtefact {

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
			if (partnerTrack.getStatus().isError())
				fStatus= partnerTrack.getStatus();
			else if (partnerTrack.getStatus().isFailure())
				fStatus= partnerTrack.getStatus();
		}

		if (fAbortedByUser)
			fStatus= ArtefactStatus.createAbortedStatus("Aborted by user.");

		if (!fStatus.hasProblems())
			fStatus= ArtefactStatus.createPassedStatus();

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
		for (String key : fMetaDataMap.keySet())
			stateData.add(new StateData(key, fMetaDataMap.get(key)));
		return stateData;
	}

	public void reportProgress(ITestArtefact artefac) {
		fSuite.reportProgress(artefac);
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
		int no= 0;
		for (PartnerTrack partnerTrack : fPartnerTracks)
			no+= partnerTrack.getActivityCount();
		return no;
	}

	@Override
	public String toString() {
		return getName();
	}

}
