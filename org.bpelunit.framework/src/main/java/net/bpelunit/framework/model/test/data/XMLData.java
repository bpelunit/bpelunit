/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

/**
 * An XMLData object represents a certain XML fragment associated with a name and activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class XMLData implements ITestArtefact {

	/**
	 * Name of this fragment.
	 */
	private String fName;

	/**
	 * Data of this fragment.
	 */
	private String fXmlData;

	/**
	 * Parent of this object.
	 */
	private ITestArtefact fParent;

	/**
	 * Status of this object
	 */
	private ArtefactStatus fStatus;


	// ******************** Initialization ************************

	public XMLData(ITestArtefact parent, String name, String xmlData) {
		fParent= parent;
		fStatus= ArtefactStatus.createPassedStatus();
		fName= name;
		fXmlData= xmlData;
	}


	// ******************** Implementation ***************************

	public String getXmlData() {
		return fXmlData;
	}

	// ************************** ITestArtefact ************************


	public String getName() {
		return fName;
	}

	public ITestArtefact getParent() {
		return fParent;
	}

	public List<ITestArtefact> getChildren() {
		return new ArrayList<ITestArtefact>();
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public List<StateData> getStateData() {
		return new ArrayList<StateData>();
	}

	public void reportProgress(ITestArtefact artefact) {
		fParent.reportProgress(artefact);
	}

}
