/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.ext;

import java.util.Map;

import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * The IBPELDeployer interface represents a BPEL Deployer, i.e. an entity which is responsible for
 * deploying a given BPEL process into an engine, and also for undeploying it.
 * 
 * This interface is intended to be implemented by deployers for concrete engine implementations.
 * Please note that to be recognized by BPELUnit, new deployers must be registered in the concrete
 * BPELUnit runner instance (for example, in the command line runner, the deployer must be added to
 * the configuration.xml file, which resides in the BPELUnit configuration directory).
 * 
 * For each deployed PUT, a new instance will be created. It is thus safe to store undeployment data
 * in the deployer instance.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface IBPELDeployer {

	/**
	 * Deploy the PUT. This method must block until the PUT is fully deployed and ready to accept
	 * incoming calls. In case of an error, a <code>DeploymentException</code> must be thrown.
	 * 
	 * @param testPath path in the file system to the test files
	 * @param processUnderTest the PUT
	 * @throws DeploymentException
	 */
	public void deploy(String testPath, ProcessUnderTest processUnderTest) throws DeploymentException;

	/**
	 * Undeploy the PUT. This method may return when undeployment is triggered, however it is good
	 * practise to only return after the PUT has been fully undeployed. This method may be called by
	 * the framework even if deployment did not succeed.
	 * 
	 * In case of an error, a <code>DeploymentException must</code> be thrown.
	 * 
	 * @param testPath path in the file system to the test files
	 * @param processUnderTest the PUT
	 * @throws DeploymentException
	 */
	public void undeploy(String testPath, ProcessUnderTest processUnderTest) throws DeploymentException;

	/**
	 * Adds configuration options for this deployment instance. The configuration options are loaded
	 * from the test suite document.
	 * 
	 * This method is called before any of the other methods.
	 * 
	 * @param options the options
	 */
	public void setConfiguration(Map<String, String> options);

}
