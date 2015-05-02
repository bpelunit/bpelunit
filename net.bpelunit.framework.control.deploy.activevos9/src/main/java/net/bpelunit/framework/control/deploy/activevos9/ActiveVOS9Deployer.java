/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.activevos9;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;

import net.bpelunit.framework.control.deploy.activevos9.ActiveVOSAdministrativeFunctions.DeployException;
import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.deploy.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.util.FileUtil;

/**
 * This class is the deployer for ActiveVOS 9.x. It contains the logic to 
 * deploy and undeploy BPRs as part of the BPELUnit test run.
 * 
 * TODOs:
 * - Coverage Support
 * - Scenario testing with mocked ActiveVOSAdministrativeFunctions
 * - Logging
 * Done:
 * - Endpoint Replacement
 * 
 * @author Daniel Luebke
 */
@IBPELDeployerCapabilities(canDeploy=true, canIntroduceMocks=false, canMeasureTestCoverage=false)
public class ActiveVOS9Deployer implements IBPELDeployer {

	private Logger logger = Logger.getLogger(getClass());
	
	private String deploymentLocation = "";
	private String deploymentServiceEndpoint = "http://localhost:8080/active-bpel/services/ActiveBpelAdmin";
	private String deployerUserName = "bpelunit";
	private String deployerPassword = "";
	private boolean doUndeploy = false;	
	private boolean terminatePendingProcessesBeforeTestSuiteIsRun = false; 
	private boolean terminatePendingProcessesAfterEveryTestCase = false;
	
	private ActiveVOSAdministrativeFunctions administrativeFunctions = null;
	private List<AesContribution> previouslyDeployedContributions;
	private ActiveVOS9Deployment deployment;
	
	@IBPELDeployerOption(
			testSuiteSpecific=true,
			description="The (relative) path to the BPR to be deployed."
	)
	public void setDeploymentLocation(String deploymentLocation) {
		logger.info("Configured BPR location: " + deploymentLocation);
		this.deploymentLocation = deploymentLocation;
	}
	
	@IBPELDeployerOption(
			defaultValue="http://localhost:8080/active-bpel/services/ActiveBpelAdmin",
			description="The URL of ActiveVOS' deployment service."
	)
	public void setDeploymentServiceEndpoint(String deploymentServiceEndpoint) {
		logger.info("Configured deployment service endpoint: " + deploymentServiceEndpoint);
		this.deploymentServiceEndpoint  = deploymentServiceEndpoint;
	}

	@IBPELDeployerOption(
			defaultValue="bpelunit",
			description="The user name of the user that has deploy rights."
	)
	public void setDeployerUserName(String deployerUserName) {
		this.deployerUserName = deployerUserName;
	}
	
	@IBPELDeployerOption(
			defaultValue="",
			description="The password of the user specified in DeployerUserName."
	)
	public void setDeployerPassword(String deployerPassword) {
		this.deployerPassword = deployerPassword;
	}

	@IBPELDeployerOption(
			defaultValue="false",
			description="Controls whether the process should be undeployed after the test suite has run. Valid values are true/false."
	)
	public void setDoUndeploy(String doUndeploy) {
		logger.info("BPR will be undeployed: " + doUndeploy);
		this.doUndeploy = Boolean.valueOf(doUndeploy);
	}
	
	@IBPELDeployerOption(
			defaultValue="false",
			description="If set to true, all running process instances will be terminated before running the test suite. DO USE WITH CARE!"
	)
	public void setTerminatePendingProcessesBeforeTestSuiteIsRun(String terminatePendingProcessesBeforeTestSuiteIsRun) {
		logger.info("Running Processes will be terminated before test suite is run: " + terminatePendingProcessesBeforeTestSuiteIsRun);
		this.terminatePendingProcessesBeforeTestSuiteIsRun = Boolean.valueOf(terminatePendingProcessesBeforeTestSuiteIsRun);
	}

	@IBPELDeployerOption(
			defaultValue="false",
			description="If set to true, all running process instances will be terminated before every test case. DO USE WITH CARE!"
	)
	public void setTerminatePendingProcessesAfterTestCaseIsRun(String terminatePendingProcessesAfterEveryTestCase) {
		logger.info("Running processes will be terminated after every test case: " + terminatePendingProcessesAfterEveryTestCase);
		this.terminatePendingProcessesAfterEveryTestCase = Boolean.valueOf(terminatePendingProcessesAfterEveryTestCase);
	}
	
	protected String getDeployerUserName() {
		return deployerUserName;
	}
	
	protected String getDeployerPassword() {
		return deployerPassword;
	}

	protected String getDeploymentLocation() {
		return deploymentLocation;
	}

	protected String getDeploymentServiceEndpoint() {
		return deploymentServiceEndpoint;
	}
	
	@Override
	public void deploy(String pathToTest, ProcessUnderTest processUnderTest)
			throws DeploymentException {

		checkThatSpecified(this.deploymentLocation, "Location for Deployment Archive (BPR) was not configured.");
		
		ActiveVOSAdministrativeFunctions activevos = getAdministrativeFunctions();
		
		if(terminatePendingProcessesBeforeTestSuiteIsRun) {
			logger.info("Terminating running processes prior to test suite run...");
			activevos.terminateAllProcessInstances();
		}
		
		if(doUndeploy) {
			logger.info("Fetching current contributions on server...");
			previouslyDeployedContributions = activevos.getAllContributions();
		}
		
		try {
			File bprFile = new File(getArchiveLocation(pathToTest));

			if(!bprFile.isFile() || !bprFile.canRead()) {
				throw new DeploymentException("Cannot find or read BPR archive '" + bprFile.getAbsolutePath() + "'");
			}
			
			byte[] bprContents = FileUtil.readFile(bprFile);
			String fileName = new File(deploymentLocation).getName();
			
			logger.info("Deploying " + fileName + "...");
			activevos.deployBpr(fileName, bprContents);
			
		} catch(IOException e) {
			throw new DeploymentException("Error while deploying: " + e.getMessage(), e);
		} catch (DeployException e) {
			throw new DeploymentException("Error while deploying: " + e.getMessage(), e);
		}
	}

	private void checkThatSpecified(String value, String msg) throws DeploymentException {
		if(value == null || "".equals(value)) {
			throw new DeploymentException(msg);
		}
	}

	@Override
	public void undeploy(String testPath, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		
		if(doUndeploy) {
			logger.info("Undeploying...");
			ActiveVOSAdministrativeFunctions activevos = getAdministrativeFunctions();
			List<AesContribution> allContributions = activevos.getAllContributions();
			
			List<Integer> previousIds = activevos.extractContributionIds(previouslyDeployedContributions);
			List<Integer> idsToRemove = activevos.extractContributionIds(allContributions);
			
			idsToRemove.removeAll(previousIds);
			
			for(Integer contributionId : idsToRemove) {
				try {
					logger.info("Undeploying Contribution " + contributionId);
					activevos.takeContributionOffline(contributionId);
					logger.info("Deleting Contribution " + contributionId + " and all associated process instances");
					activevos.deleteContribution(contributionId, true);
				} catch (AdminAPIFault e) {
					throw new DeploymentException("Cannot undeploy process: " + e.getMessage(), e);
				}
			}
		}
	}

	public IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException {
		if(this.deployment == null) {
			this.deployment = new ActiveVOS9Deployment(new File(deploymentLocation));
		}
//		return this.deployment;
		// TODO Change
		return null;
	}

	private String getArchiveLocation(String pathToTest) {
		return new File(pathToTest, this.deploymentLocation).getAbsolutePath();
	}

	@Override
	public void cleanUpAfterTestCase() throws DeploymentException {
		if(terminatePendingProcessesAfterEveryTestCase) {
			logger.info("Terminating process instances after Test Case...");
			getAdministrativeFunctions().terminateAllProcessInstances();
		}
	}

	public synchronized ActiveVOSAdministrativeFunctions getAdministrativeFunctions() {
		if(this.administrativeFunctions == null) {
			this.administrativeFunctions = new ActiveVOSAdministrativeFunctions(this.deploymentServiceEndpoint, this.deployerUserName, this.deployerPassword);
		}
		
		return administrativeFunctions;
	}

	/**
	 * For testing only
	 * 
	 * @param mock
	 */
	protected void setAdministrativeFunctions(
			ActiveVOSAdministrativeFunctions mock) {
		this.administrativeFunctions = mock;
		
	}

}
