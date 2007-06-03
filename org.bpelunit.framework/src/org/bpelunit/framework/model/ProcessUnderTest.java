/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model;

import java.util.ArrayList;
import java.util.List;

import org.bpelunit.framework.control.ext.DeploymentOption;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.exception.SpecificationException;

/**
 * The ProcessUnderTest is the internal representation of the BPEL process which is tested in a
 * particular test suite. The process is characterized by name, a deployer, and deployment options.
 * 
 * The PUT is a subclass of Partner, as it is an extended version of a partner, which is capable of
 * being deployed outside of BPELUnit, whereas a partner is always simulated by the framework.
 * 
 * Note that in test mode, the PUT is not deployed, but simulated by the framework.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ProcessUnderTest extends Partner {

	/**
	 * The deployer which will be called to deploy this PUT.
	 */
	private IBPELDeployer fDeployer;

	/**
	 * The deployment options taken from the test suite which will be passed on to the deployer. May
	 * be null.
	 */
	private List<DeploymentOption> fXMLDeploymentOptions;

	/**
	 * True if the PUT is currently deployed.
	 */
	private boolean isDeployed;

	public ProcessUnderTest(String name, String testBasePath, String wsdlFile, String baseURL) throws SpecificationException {
		super(name, testBasePath, wsdlFile, baseURL);
		fXMLDeploymentOptions= new ArrayList<DeploymentOption>();
	}

	public void setDeployer(IBPELDeployer deployer) {
		fDeployer= deployer;
	}

	public void deploy() throws DeploymentException {
		fDeployer.deploy(getBasePath(), this);
		// if no exception was thrown, the service is deployed.
		isDeployed= true;
	}

	public void undeploy() throws DeploymentException {
		fDeployer.undeploy(getBasePath(), this);
		// no exception -> undeploy was successful.
		isDeployed= false;
	}

	public boolean isDeployed() {
		return isDeployed;
	}

	public void setXMLDeploymentOption(String name, String stringValue) {
		fXMLDeploymentOptions.add(new DeploymentOption(name, stringValue));
	}

	public String getDeploymentOption(String key) {
		for (DeploymentOption option : fXMLDeploymentOptions) {
			if (option.getKey().equals(key))
				return option.getValue();
		}
		return null;
	}

}
