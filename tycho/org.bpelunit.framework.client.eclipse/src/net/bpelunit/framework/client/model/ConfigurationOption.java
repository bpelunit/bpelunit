/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

/**
 * A configuration option of a deployer.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ConfigurationOption {

	private String fKey;
	private String fValue;

	public ConfigurationOption(String key, String value) {
		fKey= key;
		fValue= value;
	}

	public String getKey() {
		return fKey;
	}

	public String getValue() {
		return fValue;
	}

	public void setValue(String value) {
		fValue= value;
	}

}
