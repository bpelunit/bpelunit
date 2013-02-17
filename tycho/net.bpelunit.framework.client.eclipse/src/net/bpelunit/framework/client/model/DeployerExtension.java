/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.exception.SpecificationException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Deployer Extension
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DeployerExtension extends Extension {

	public DeployerExtension(String id, String name, IConfigurationElement e) {
		super(id, name, e);
	}

	public IBPELDeployer createNew() throws SpecificationException {
		Object o= getExecutableExtension("deployerClass");
		if (o != null) {
			return (IBPELDeployer) o;
		}
		throw new SpecificationException("Can't intantiate class for deployer " + getId());
	}
}
