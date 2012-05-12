package net.bpelunit.framework.control.deploymentchanger.waitmocking;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class WaitMocking implements IDeploymentChanger {

	private static final String DURATION_TEMPLATE = "'PT%dS'";
	private static final String BPEL_WAIT_FOR = "for";
	private String duration;
	private String xpathToWait;
	private String bpelName;

	@DeploymentChangerOption(description="XPath expression that selects exactly one wait activity. You need to use the 'bpel' namespace prefix (e.g. //bpel:wait[@name='myWait']) that will be bound to the correct version of the BPEL namespace.")
	public void setWaitToMock(String xpath) {
		this.xpathToWait = xpath;
	}

	@DeploymentChangerOption(description="The new duration in seconds")
	public void setNewDuration(String newDurationInSeconds) {
		this.duration = String.format(DURATION_TEMPLATE, Integer.valueOf(newDurationInSeconds));
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
		
		Element process = null;
		if(processes.size() != 1) {
			checkIsSet("BPELName", bpelName);
			process = getProcessForConfiguredName(processes).getDocumentElement();
		} else {
			process = processes.get(0).getBpelXml().getDocumentElement();
		}
		
		XPathTool xpath = createBpelXPathTool(process.getNamespaceURI());
		try {
			List<Node> waitElements = xpath.evaluateAsList(xpathToWait, process);
			
			for(Node n : waitElements) {
				Element e = validateWaitElement(n);
				
				setDurationForWait(e, duration);
			}
			
		} catch (XPathExpressionException e) {
			throw new DeploymentException("XPath is not valid: " + e.getMessage(), e);
		}
	}

	/**
	 * TODO Move to BPELUtil
	 * 
	 * Sets the new duration for a wait and thereby possibly replaces
	 * the old until or waits
	 * 
	 * Caller needs to validate that this is really a BPEL wait activity.
	 * 
	 * @param waitActivity wait to modify
	 * @param duration the new XPath statement for the new duration to wait for
	 */
	private static void setDurationForWait(Element waitActivity, String duration) {
		String bpelNamespace = waitActivity.getNamespaceURI();
		
		XMLUtil.removeNodes(waitActivity, waitActivity.getElementsByTagNameNS(bpelNamespace, BPEL_WAIT_FOR));
		XMLUtil.removeNodes(waitActivity, waitActivity.getElementsByTagNameNS(bpelNamespace, "until"));
		
		Document doc = waitActivity.getOwnerDocument();
		Element newWaitFor = doc.createElementNS(bpelNamespace, BPEL_WAIT_FOR);
		Text value = doc.createTextNode(duration);
		newWaitFor.appendChild(value);
		
		waitActivity.appendChild(newWaitFor);
	}

	private Element validateWaitElement(Node n) throws DeploymentException {
		if(n instanceof Element && n.getLocalName().equals("wait") ) {
			return (Element) n;
		} else {
			throw new DeploymentException("XPath has selected a non-wait activity: {" + n.getNamespaceURI() + "}" + n.getLocalName());
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

	private Document getProcessForConfiguredQName(List<? extends IBPELProcess> processes, QName parseUtil) throws DeploymentException {
		QName qname = QNameUtil.parseQName(bpelName);
		
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
		XPathTool xpath = new XPathTool(ns);
		return xpath;
	}
}
