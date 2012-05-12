package net.bpelunit.framework.control.deploymentchanger.timemocking;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.control.ext.IDeploymentChanger;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.QNameUtil;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.util.bpel.BPELFacade;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Locking for mocking wait activities in BPEL. Can be added as a deployment
 * component when BPELUnit deploys the PUT.
 * 
 * Options configurable from test:
 * - ActivityToMock: XPath expression selecting at least one wait or onAlarm which should be changed
 * - NewDuration: The new duration given in seconds that will be set as a for-parameter to the wait activity - even if it was a deadline expression (until) before.
 * - BPELName: Necessary only if deployment contains more than one BPEL process. The local name or the QName (given as {namespace}local-name) to which this change should be applied
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public class TimeMocking implements IDeploymentChanger {

	private static final String DURATION_TEMPLATE = "'PT%dS'";
	private String duration;
	private String xpathToWait;
	private String bpelName;

	/**
	 * See annotation for description
	 * 
	 * @param xpath see annotation for description
	 */
	@DeploymentChangerOption(description="XPath expression that selects at least one wait activity. You need to use the 'bpel' namespace prefix (e.g. //bpel:wait[@name='myWait']) that will be bound to the correct version of the BPEL namespace.")
	public void setActivityToMock(String xpath) {
		this.xpathToWait = xpath;
	}

	/**
	 * See annotation for description
	 * 
	 * @param xpath see annotation for description
	 */
	@DeploymentChangerOption(description="The new duration in seconds")
	public void setNewDuration(String newDurationInSeconds) {
		this.duration = String.format(DURATION_TEMPLATE, Integer.valueOf(newDurationInSeconds));
	}
	
	/**
	 * See annotation for description
	 * 
	 * @param xpath see annotation for description
	 */
	@DeploymentChangerOption
	public void setBPELName(String bpelToChange) {
		this.bpelName = bpelToChange;
	}
	
	/**
	 * Executes the change to the deployment. The option values have to be set
	 * before.
	 * 
	 * @param d the deployment that is to be changed
	 */
	@Override
	public void changeDeployment(IDeployment d) throws DeploymentException {
		List<? extends IBPELProcess> processes = d.getBPELProcesses();
		
		checkIsSet("Duration", duration);
		checkIsSet("WaitToMock", xpathToWait);
		
		Element process = null;
		if(processes.size() > 1) {
			checkIsSet("BPELName", bpelName);
			process = getProcessForConfiguredName(processes).getDocumentElement();
		} else {
			process = getFirstProcess(processes);
		}
		
		XPathTool xpath = createBpelXPathTool(process.getNamespaceURI());
		BPELFacade f = BPELFacade.getInstance(process.getNamespaceURI());
		try {
			List<Node> timeElements = xpath.evaluateAsList(xpathToWait, process);
			
			for(Node n : timeElements) {
				if(n instanceof Element) {
					f.setFor((Element)n, duration);
				} else {
					throw new DeploymentException("XPath does not (only) reference BPEL Activities: " + XMLUtil.getQName(n));
				}
			}
		} catch (XPathExpressionException e) {
			throw new DeploymentException("XPath is not valid: " + e.getMessage(), e);
		}
	}

	private Element getFirstProcess(List<? extends IBPELProcess> processes) throws DeploymentException {
		if(processes != null && processes.size() > 0) {
			return processes.get(0).getBpelXml().getDocumentElement();
		} else {
			throw new DeploymentException("There no processes in the deployment!");
		}
	}

	private Document getProcessForConfiguredName(List<? extends IBPELProcess> processes) throws DeploymentException {
		if(QNameUtil.isQName(bpelName)) {
			return getProcessForConfiguredQName(processes, QNameUtil.parseQName(bpelName));
		} else {
			return getProcessForConfiguredLocalName(processes);
		}
	}

	private Document getProcessForConfiguredLocalName(List<? extends IBPELProcess> processes) throws DeploymentException {
		for (IBPELProcess p : processes) {
			if(bpelName.equals(p.getName().getLocalPart())) {
				return p.getBpelXml();
			}
		}
		
		throw new DeploymentException("No BPEL Process with local-name '" + bpelName + "' found in deployment.");
	}

	private Document getProcessForConfiguredQName(List<? extends IBPELProcess> processes, QName qname) throws DeploymentException {
		for (IBPELProcess p : processes) {
			if(qname.equals(p.getName())) {
				return p.getBpelXml();
			}
		}
		
		throw new DeploymentException("No BPEL Process with qname '" + qname + "' found in deployment.");
	}

	private void checkIsSet(String option, String value) throws DeploymentException {
		if(value == null) {
			throw new DeploymentException("Option " + option + " is mandatory but not set for Wait Mocking");
		}
	}

	XPathTool createBpelXPathTool(String bpelNamespace) {
		NamespaceContextImpl ns = new NamespaceContextImpl();
		ns.setNamespace("bpel", bpelNamespace);
		return new XPathTool(ns);
	}
}
