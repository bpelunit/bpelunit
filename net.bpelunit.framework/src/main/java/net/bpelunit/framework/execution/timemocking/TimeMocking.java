package net.bpelunit.framework.execution.timemocking;

import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.execution.AbstractTestLifeCycleElement;
import net.bpelunit.framework.execution.IBPELUnitContext;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IWaitingActivity;
import net.bpelunit.util.QNameUtil;

/**
 * Mocking for mocking wait activities in BPEL. Can be added as a deployment
 * component when BPELUnit deploys the PUT.
 * 
 * Options configurable from test:
 * <ul>
 * <li>ActivityToMock: XPath expression selecting at least one wait or onAlarm
 * which should be changed</li>
 * <li>NewDuration: The new duration given in seconds that will be set as a
 * for-parameter to the wait activity - even if it was a deadline expression
 * (until) before.</li>
 * <li>BPELName: Necessary only if deployment contains more than one BPEL
 * process. The local name or the QName (given as {namespace}local-name) to
 * which this change should be applied</li>
 * </ul>
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public class TimeMocking extends AbstractTestLifeCycleElement {

	private static final String DURATION_TEMPLATE = "'PT%dS'";
	private String duration;
	private String xpathToWait;
	private String bpelName;

	/**
	 * See annotation for description
	 * 
	 * @param xpath
	 *            see annotation for description
	 */
	@TestLifeCycleElementOption(description = "XPath expression that selects at least one wait activity. Please do not use any BPEL namespace prefix (e.g. //wait[@name='myWait']).")
	public void setActivityToMock(String xpath) {
		this.xpathToWait = xpath;
	}

	/**
	 * See annotation for description
	 * 
	 * @param xpath
	 *            see annotation for description
	 */
	@TestLifeCycleElementOption(description = "The new duration in seconds")
	public void setNewDuration(String newDurationInSeconds) {
		this.duration = String.format(DURATION_TEMPLATE,
				Integer.valueOf(newDurationInSeconds));
	}

	/**
	 * See annotation for description
	 * 
	 * @param xpath
	 *            see annotation for description
	 */
	@TestLifeCycleElementOption
	public void setBPELName(String bpelToChange) {
		this.bpelName = bpelToChange;
	}

	@Override
	public void doPrepareProcesses(IBPELUnitContext context)
			throws DeploymentException {
		changeDeployment(context.getDeployment(), context.getTestSuite());
	}
	
	/**
	 * Executes the change to the deployment. The option values have to be set
	 * before.
	 * 
	 * @param d
	 *            the deployment that is to be changed
	 */
	public void changeDeployment(IDeployment d, TestSuite testSuite)
			throws DeploymentException {
		List<? extends IBPELProcess> processes = d.getBPELProcesses();

		checkIsSet("Duration", duration);
		checkIsSet("WaitToMock", xpathToWait);

		IProcess process = null;
		if (processes.size() > 1) {
			checkIsSet("BPELName", bpelName);
			process = getProcessForConfiguredName(processes);
		} else {
			process = getFirstProcess(processes);
		}

		List<IBpelObject> objects = process.getElementsByXPath(xpathToWait);

		if (objects.size() == 0) {
			throw new DeploymentException(
					"XPath does not reference any BPEL Activities: " + xpathToWait);
		}

		for (IBpelObject n : objects) {
			if (n instanceof IWaitingActivity) {
				((IWaitingActivity) n).setDuration(duration);
			} else {
				throw new DeploymentException(
						"XPath does not (only) reference timed BPEL Activities: "
								+ xpathToWait + ". Failed activity: " + n.getXPathInDocument());
			}
		}
	}

	private IProcess getFirstProcess(List<? extends IBPELProcess> processes)
			throws DeploymentException {
		if (processes != null && processes.size() > 0) {
			return processes.get(0).getProcessModel();
		} else {
			throw new DeploymentException(
					"There no processes in the deployment!");
		}
	}

	private IProcess getProcessForConfiguredName(
			List<? extends IBPELProcess> processes) throws DeploymentException {
		if (QNameUtil.isQName(bpelName)) {
			return getProcessForConfiguredQName(processes,
					QNameUtil.parseQName(bpelName));
		} else {
			return getProcessForConfiguredLocalName(processes);
		}
	}

	private IProcess getProcessForConfiguredLocalName(
			List<? extends IBPELProcess> processes) throws DeploymentException {
		for (IBPELProcess p : processes) {
			if (bpelName.equals(p.getName().getLocalPart())) {
				return p.getProcessModel();
			}
		}

		throw new DeploymentException("No BPEL Process with local-name '"
				+ bpelName + "' found in deployment.");
	}

	private IProcess getProcessForConfiguredQName(
			List<? extends IBPELProcess> processes, QName qname)
			throws DeploymentException {
		for (IBPELProcess p : processes) {
			if (qname.equals(p.getName())) {
				return p.getProcessModel();
			}
		}

		throw new DeploymentException("No BPEL Process with qname '" + qname
				+ "' found in deployment.");
	}

	private void checkIsSet(String option, String value)
			throws DeploymentException {
		if (value == null) {
			throw new DeploymentException("Option " + option
					+ " is mandatory but not set for Wait Mocking");
		}
	}
}
