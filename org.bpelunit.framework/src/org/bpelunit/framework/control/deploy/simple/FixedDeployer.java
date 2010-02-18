/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.deploy.simple;

import java.util.Map;

import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IDeployment;
import org.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import org.bpelunit.framework.coverage.ICoverageMeasurementTool;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * The "fixed" deployer is not a real deployer, but a place-holder for PUTs
 * which have already been deployed externally.
 * 
 * This might be useful if the deployment process is too complicated and no
 * deployer is available for BPELUnit, or if the engine stores process
 * information in a log file which is only accessible during the time the PUT is
 * deployed.
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio García-Domínguez (added cleanUpAfterTestCase)
 * 
 */
@IBPELDeployerCapabilities
public class FixedDeployer implements IBPELDeployer {

	public void deploy(String path, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// do nothing.
		if (BPELUnitRunner.measureTestCoverage()) {
			ICoverageMeasurementTool tool = BPELUnitRunner
					.getCoverageMeasurmentTool();
			tool
					.setErrorStatus("Test coverage can not be measured by the Fixed Deployer!");
		}
	}

	public void undeploy(String path, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// do nothing.
	}

	public void setConfiguration(Map<String, String> options) {
		// do nothing.
	}

	public IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getArchiveLocation(String pathToTest) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setArchiveLocation(String archive) {
		// do nothing.
	}

    public void cleanUpAfterTestCase() {
        // do nothing.
    }

}
