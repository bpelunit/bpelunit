/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.report;

import java.util.List;

/**
 * TestArtefacts are entities of interest in a test run. They are part of a tree structure, ranging
 * from the root (test suite) to the leafs (data specifications, XML data). Each test artefact can
 * contribute its own test result data: a name, status, and several data fields with more
 * information about its progress.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface ITestArtefact {

	/**
	 * Returns the human-readable name of this artefact.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Returns the current status of this artefact.
	 * 
	 * @return current status
	 */
	ArtefactStatus getStatus();

	/**
	 * Returns the parent of this artefact
	 * 
	 * @return the parent
	 */
	ITestArtefact getParent();

	/**
	 * Returns the children of this artefact. Must return the empty list if the artefact has no
	 * children
	 * 
	 * @return list of children
	 */
	List<ITestArtefact> getChildren();

	/**
	 * Returns a list of state data of interest. Must return the empty list if there is no state
	 * data.
	 * 
	 * @return list of state data
	 */
	List<StateData> getStateData();

	/**
	 * Reports progress in the given artefact.
	 * 
	 * @param the artefact which has progressed
	 */
	void reportProgress(ITestArtefact artefact);

}
