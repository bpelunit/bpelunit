package net.bpelunit.framework.control.deploy.activevos9;

import java.io.File;
import java.io.IOException;

import net.bpelunit.framework.control.ext.IBPELDeployer;
import net.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.control.ext.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.util.FileUtil;

@IBPELDeployerCapabilities(canDeploy=true, canIntroduceMocks=false, canMeasureTestCoverage=false)
public class ActiveVOS9Deployer implements IBPELDeployer {

	private String deploymentLocation;
	private String deploymentServiceEndpoint = "http://localhost:8080/active-bpel/services/ActiveBpelDeployBPR";
	private String deployerUserName = "bpelunit";
	private String deployerPassword = "";
	private ActiveVOSAdministrativeFunctions administrativeFunctions = null;
	
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
		
		try {
			File bprFile = new File(getArchiveLocation(pathToTest));

			if(!bprFile.isFile() || !bprFile.canRead()) {
				throw new DeploymentException("Cannot find or read BPR archive '" + bprFile.getAbsolutePath() + "'");
			}
			
			byte[] bprContents = FileUtil.readFile(bprFile);
			String fileName = new File(deploymentLocation).getName();
			
			getAdministrativeFunctions().deployBpr(fileName, bprContents);
			
		} catch(IOException e) {
			e.printStackTrace();
			throw new DeploymentException("Error while deploying: " + e.getMessage(), e);
		}
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
		return new File(pathToTest, this.deploymentLocation).getAbsolutePath();
	}

	@Override
	public void setArchiveLocation(String archive) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanUpAfterTestCase() throws Exception {
		// TODO Auto-generated method stub

	}

	public synchronized ActiveVOSAdministrativeFunctions getAdministrativeFunctions() {
		if(this.administrativeFunctions == null) {
			this.administrativeFunctions = new ActiveVOSAdministrativeFunctions(this.deploymentServiceEndpoint, this.deployerUserName, this.deployerPassword);
		}
		
		return administrativeFunctions;
	}

}
