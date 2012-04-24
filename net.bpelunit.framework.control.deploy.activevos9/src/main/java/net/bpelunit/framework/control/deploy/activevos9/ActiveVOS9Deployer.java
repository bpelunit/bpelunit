package net.bpelunit.framework.control.deploy.activevos9;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;

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
	private boolean doUndeploy = false;	
	
	private ActiveVOSAdministrativeFunctions administrativeFunctions = null;
	private List<AesContribution> previouslyDeployedContributions;
	
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

	@IBPELDeployerOption(defaultValue="false")
	public void setDoUndeploy(String doUndeploy) {
		String value = doUndeploy.toLowerCase();
		
		if("true".equals(value) || "yes".equals(value)) {
			this.doUndeploy = true;
		} else {
			this.doUndeploy = false;
		}
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
		
		ActiveVOSAdministrativeFunctions activevos = getAdministrativeFunctions();
		previouslyDeployedContributions = activevos.getAllContributions();
		
		try {
			File bprFile = new File(getArchiveLocation(pathToTest));

			if(!bprFile.isFile() || !bprFile.canRead()) {
				throw new DeploymentException("Cannot find or read BPR archive '" + bprFile.getAbsolutePath() + "'");
			}
			
			byte[] bprContents = FileUtil.readFile(bprFile);
			String fileName = new File(deploymentLocation).getName();
			
			activevos.deployBpr(fileName, bprContents);
			
		} catch(IOException e) {
			e.printStackTrace();
			throw new DeploymentException("Error while deploying: " + e.getMessage(), e);
		}
	}

	@Override
	public void undeploy(String testPath, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		
		if(doUndeploy) {
			ActiveVOSAdministrativeFunctions activevos = getAdministrativeFunctions();
			List<AesContribution> allContributions = activevos.getAllContributions();
			
			List<Integer> previousIds = activevos.extractContributionIds(previouslyDeployedContributions);
			List<Integer> idsToRemove = activevos.extractContributionIds(allContributions);
			
			idsToRemove.removeAll(previousIds);
			
			for(Integer contributionId : idsToRemove) {
				try {
					activevos.takeContributionOffline(contributionId);
					activevos.deleteContribution(contributionId, true);
				} catch (AdminAPIFault e) {
					throw new DeploymentException("Cannot undeploy process: " + e.getMessage(), e);
				}
			}
		}
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
