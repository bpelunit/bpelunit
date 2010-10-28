/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ext;

/**
 * An option for a certain deployer. These options are specified by the user in the test suite
 * document and attached to the PUT, later to be transferred to the actual deployer.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DeploymentOption {

	private String fKey;
	private String fValue;

	public DeploymentOption(String name, String stringValue) {
		fKey= name;
		fValue= stringValue;
	}

	public String getKey() {
		return fKey;
	}

	public String getValue() {
		return fValue;
	}

}
