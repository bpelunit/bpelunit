/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.report;

/**
 * A state data package represents some information on the internal state of a test artefact. It is
 * a convenient accessor to the state for easy representation in a UI.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class StateData {

	/**
	 * Key of this data
	 */
	private String fKey;

	/**
	 * Value of this data
	 */
	private String fValue;

	public StateData(String key, String value) {
		fKey= key;
		fValue= value;
	}

	public String getKey() {
		return fKey;
	}

	public String getValue() {
		return fValue;
	}

}
