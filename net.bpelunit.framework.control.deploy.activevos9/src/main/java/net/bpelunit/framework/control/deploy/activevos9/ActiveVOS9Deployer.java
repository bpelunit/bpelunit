package net.bpelunit.framework.control.deploy.activevos9;

import net.bpelunit.framework.control.ext.IBPELDeployer;
import net.bpelunit.framework.control.ext.IDeployment;
import net.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;

@IBPELDeployerCapabilities(canDeploy=true, canIntroduceMocks=false, canMeasureTestCoverage=false)
public class ActiveVOS9Deployer implements IBPELDeployer {

	private String deploymentLocation;
	private String deploymentServiceEndpoint = "http://localhost:8080/active-bpel/services/ActiveBpelDeployBPR";
	private String deployerUserName = "bpelunit";
	private String deployerPassword = "";
	
	@IBPELDeployerOption(defaultValue="")
	public void setDeploymentLocation(String deploymentLocation) {
		this.deploymentLocation = deploymentLocation;
	}
	
	@IBPELDeployerOption(defaultValue="http://localhost:8080/active-bpel/services/ActiveBpelDeployBPR")
	public void setDeploymentServiceEndpoint(String deploymentServiceEndpoint) {
		this.deploymentServiceEndpoint  = deploymentServiceEndpoint;
	}

	@IBPELDeployerOption(defaultValue="bpelunit")
	public void setDeployerUserName(String deployerUserName) {
		this.deployerUserName = deployerUserName;
	}
	
	@IBPELDeployerOption(defaultValue="")
	public void setDeployerPassword(String deployerPassword) {
		this.deployerPassword = deployerPassword;
	}

	public String getDeployerUserName() {
		return deployerUserName;
	}
	
	public String getDeployerPassword() {
		return deployerPassword;
	}

	public String getDeploymentLocation() {
		return deploymentLocation;
	}

	public String getDeploymentServiceEndpoint() {
		return deploymentServiceEndpoint;
	}
	
	@Override
	public void deploy(String pathToTest, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void undeploy(String testPath, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Auto-generated method stub

	}

	@Override
	public IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArchiveLocation(String pathToTest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setArchiveLocation(String archive) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanUpAfterTestCase() throws Exception {
		// TODO Auto-generated method stub

	}

}
