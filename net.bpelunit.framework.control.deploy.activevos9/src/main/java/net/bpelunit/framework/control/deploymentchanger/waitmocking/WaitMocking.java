package net.bpelunit.framework.control.deploymentchanger.waitmocking;

import java.util.List;

import javax.xml.namespace.QName;

import org.w3c.dom.Document;

import net.bpelunit.framework.control.deploy.activevos9.IBPELProcess;
import net.bpelunit.framework.control.deploy.activevos9.IDeployment;
import net.bpelunit.framework.control.ext.IDeploymentChanger;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.QNameUtil;

public class WaitMocking implements IDeploymentChanger {

	private String duration;
	private String xpathToWait;
	private String bpelName;

	@DeploymentChangerOption(description="XPath expression that selects exactly one wait activity")
	public void setWaitToMock(String xpath) {
		this.xpathToWait = xpath;
	}

	@DeploymentChangerOption(description="The new duration in seconds")
	public void setNewDuration(String newDuration) {
		this.duration = newDuration;
	}
	
	@DeploymentChangerOption
	public void setBPELName(String bpelToChange) {
		this.bpelName = bpelToChange;
	}
	
	@Override
	public void changeDeployment(IDeployment d) throws DeploymentException {
		List<? extends IBPELProcess> processes = d.getBPELProcesses();
		
		checkIsSet("Duration", duration);
		checkIsSet("WaitToMock", xpathToWait);
		if(processes.size() != 1) {
			checkIsSet("BPELName", bpelName);
		}
		
		getProcessForConfiguredName();
	}

	private Document getProcessForConfiguredName() {
		if(QNameUtil.isQName(bpelName)) {
			return getProcessForConfiguredQName(QNameUtil.parseUtil(bpelName));
		} else {
			return getProcessForConfiguredLocalName();
		}
		
	}

	private Document getProcessForConfiguredLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	private Document getProcessForConfiguredQName(QName parseUtil) {
		// TODO Auto-generated method stub
		return null;
	}

	private void checkIsSet(String option, String value) throws DeploymentException {
		if(value == null) {
			throw new DeploymentException("Option " + option + " is mandatory but not set for Wait Mocking");
		}
	}

}
