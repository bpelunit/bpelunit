/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.client.model;

import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.exception.SpecificationException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Deployer Extension
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DeployerExtension extends Extension {

	private String[] fGeneralOptions;
	private String[] fSuiteOptions;

	public DeployerExtension(String id, String name, String[] general_options, String[] suite_options, IConfigurationElement e) {
		super(id, name, e);
		fGeneralOptions= general_options;
		fSuiteOptions= suite_options;
	}

	public IBPELDeployer createNew() throws SpecificationException {
		Object o= getExecutableExtension("deployerClass");
		if (o != null) {
			return (IBPELDeployer) o;
		}
		throw new SpecificationException("Can't intantiate class for deployer " + getId());
	}

	public String[] getGeneralOptions() {
		return fGeneralOptions;
	}

	public String[] getSuiteOptions() {
		return fSuiteOptions;
	}

}
