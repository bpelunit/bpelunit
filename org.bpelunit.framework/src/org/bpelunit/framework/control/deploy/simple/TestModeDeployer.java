/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.deploy.simple;

import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * The test mode deployer is a BPELUnit internal deployer which basically does nothing. It is
 * intended to be used to test BPELUnit internally by NOT deploying a real-life PUT, but rather
 * simulating the PUT along with the client and other partners through another partner track, which
 * then must be attached to the "partner" with the PUTs name.
 * 
 * As the framework takes care of "deploying" a partnerTrack, this class simply does nothing.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
@IBPELDeployerCapabilities
public class TestModeDeployer implements IBPELDeployer {

	@Override
	public void deploy(String path, ProcessUnderTest processUnderTest) throws DeploymentException {
		// do nothing.
	}

	@Override
	public void undeploy(String path, ProcessUnderTest processUnderTest) throws DeploymentException {
		// do nothing.
	}
}
