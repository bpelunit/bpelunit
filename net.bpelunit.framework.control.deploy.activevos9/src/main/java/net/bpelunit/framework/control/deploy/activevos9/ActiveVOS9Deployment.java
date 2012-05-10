/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.activevos9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.FileUtil;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class ActiveVOS9Deployment implements IDeployment {
	
	final class BPELInfo implements IBPELProcess {
		
		BPELInfo(File bpelFile, File pddFile, QName name, Document bpel, Document pdd) {
			super();
			this.bpelFile = bpelFile;
			this.pddFile = pddFile;
			this.name = name;
			this.bpelXml = bpel;
			this.pddXml = pdd;
		}
		
		private File bpelFile;
		private File pddFile;
		private QName name;
		private Document bpelXml;
		private Document pddXml;
		
		File getBpelFile() {
			return bpelFile;
		}

		File getPddFile() {
			return pddFile;
		}

		Document getXml() {
			return bpelXml;
		}

		@Override
		public QName getName() {
			return name;
		}
		
		@Override
		public void addWSDLImport(String wsdlFileName, InputStream contents) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public Document getXML() {
			ActiveVOS9Deployment.this.checkedOutProcesses.add(this);
			return bpelXml;
		}

		Document getPddXml() {
			return pddXml;
		}
		
		@Override
		public void addPartnerlink(String name, QName partnerlinkType,
				String processRole, String partnerRole, QName service,
				String port, String endpointURL) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void changePartnerEndpoint(String partnerLinkName,
				String newEndpoint) throws DeploymentException {
			XPathTool xpathPdd = createXPathToolForPdd();
			
			Element pddRoot = getPddXml().getDocumentElement();
			try {
				List<Node> address = xpathPdd.evaluateAsList("//pdd:partnerLinks/pdd:partnerLink[@name='" + partnerLinkName +  "']/pdd:partnerRole/wsa:EndpointReference/wsa:Address", pddRoot);
				address.get(0).setTextContent(newEndpoint);
				
				List<Node> endpointReference = xpathPdd.evaluateAsList("//pdd:partnerLinks/pdd:partnerLink[@name='" + partnerLinkName +  "']/pdd:partnerRole/@endpointReference", pddRoot);
				endpointReference.get(0).setNodeValue("static");
				List<Node> invokeHandler = xpathPdd.evaluateAsList("//pdd:partnerLinks/pdd:partnerLink[@name='" + partnerLinkName +  "']/pdd:partnerRole/@invokeHandler", pddRoot);
				invokeHandler.get(0).setNodeValue("default:Address");
			} catch (Exception e) {
				throw new DeploymentException("Cannot change partner endpoint location for " + partnerLinkName, e);
			}
		}
	}

	private File bpr; 
	private File tempDirectory = null;
	private File tempBPR = null;
	
	private Set<BPELInfo> checkedOutProcesses = new HashSet<BPELInfo>();
	private List<BPELInfo> allProcesses = new ArrayList<BPELInfo>();
	public static final String NAMESPACE_PDD = "http://schemas.active-endpoints.com/pdd/2006/08/pdd.xsd";
	
	public ActiveVOS9Deployment(File bpr) throws DeploymentException {
		if(!bpr.isFile() || !bpr.canRead()) {
			throw new DeploymentException("The given BPR does not exist or cannot be read:" + bpr.getAbsolutePath());
		}
		
		this.bpr = bpr;
	}
	
	/**
	 * @throws DeploymentException 
	 * @see net.bpelunit.framework.control.deploy.activevos9.IDeployment#getBPELProcessAsXML(javax.xml.namespace.QName)
	 */
	@Override
	public List<IBPELProcess> getBPELProcesses() throws DeploymentException {
		extractBPRIfNecessary();
		return new ArrayList<IBPELProcess>(allProcesses);
	}
	
	/**
	 * Called only by ActiveVOS9Deployer in order to get the actual
	 * deployment archive with or without modifications done by 
	 * instrumentation, mocking, ...
	 *  
	 * @return contents of the BPR to be deployed
	 * @throws DeploymentException 
	 */
	InputStream getUpdatedDeployment() throws DeploymentException {
		if(tempDirectory != null) {
			try {
				// write out all files
				for(BPELInfo bpel : checkedOutProcesses) {
					XMLUtil.writeXML(bpel.getXml(), bpel.getBpelFile());
				}
				
				// repackage
				tempBPR = File.createTempFile(bpr.getName(), ".bpr");
				ZipUtil.zipDirectory(tempDirectory, tempBPR);
				return new FileInputStream(tempBPR);
			} catch (Exception e) {
				throw new DeploymentException("Error while repackaging BPR", e);
			}
		} else {
			try {
				return new FileInputStream(bpr);
			} catch (FileNotFoundException e) {
				throw new DeploymentException("BPR does not exist: " + bpr.getAbsolutePath(), e);
			}
		}
	}
	
	/**
	 * Called only by ActiveVOS9Deployer in order to clean up temp files
	 */
	void cleanUp() throws DeploymentException {
		if(tempBPR != null) {
			tempBPR.delete();
			tempBPR = null;
		}
		
		if(tempDirectory != null) {
			try {
				FileUtils.deleteDirectory(tempDirectory);
				tempDirectory = null;
			} catch (IOException e) {
				throw new DeploymentException("Cannot delete temp directory: " + tempDirectory.getAbsolutePath(), e);
			}
		}
	}

	/**
	 * This method will extract the BPR file if it isn't already and
	 * will initialize the state of this deployment
	 * 
	 * @throws DeploymentException
	 */
	private synchronized void extractBPRIfNecessary() throws DeploymentException {
		if(tempDirectory == null) {
			try {
				tempDirectory = FileUtil.createTempDirectory();
				ZipUtil.unzipFile(bpr, tempDirectory);

				scanForBPELFiles();
			} catch (IOException e) {
				throw new DeploymentException("Error while temporarily extracting the BPR: " + e.getMessage(), e);
			}
		}
	}

	private void scanForBPELFiles() throws DeploymentException {
		@SuppressWarnings("unchecked")
		Iterator<File> pddFiles = FileUtils.iterateFiles(
				tempDirectory, new String[]{"pdd"}, true);
		
		while(pddFiles.hasNext()) {
			File pddFile = pddFiles.next();
			
			try {
				Document pddXml = XMLUtil.parseXML(new FileInputStream(pddFile));
				Element pddRoot = pddXml.getDocumentElement();
				
				String locationInBPR = pddRoot.getAttribute("location");
				
				File bpelFile = new File(tempDirectory, locationInBPR);
				Document bpelXml = XMLUtil.parseXML(new FileInputStream(bpelFile));
				Element bpelRoot = bpelXml.getDocumentElement();
				QName name = new QName(
							bpelRoot.getAttribute("targetNamespace"),
							bpelRoot.getAttribute("name")
						);
				
				allProcesses.add(new BPELInfo(bpelFile, pddFile, name, bpelXml, pddXml));
			} catch (FileNotFoundException e) {
				throw new DeploymentException("File could not be read: " + e.getMessage(), e);
			} catch (SAXException e) {
				throw new DeploymentException("XML file could not be parsed: " + e.getMessage(), e);
			} catch (IOException e) {
				throw new DeploymentException("XML file could not be read: " + e.getMessage(), e);
			} catch (ParserConfigurationException e) {
				throw new DeploymentException("XML file could not be parsed: " + e.getMessage(), e);
			}
		}
	}
	
	XPathTool createXPathToolForPdd() {
		NamespaceContextImpl nsc = new NamespaceContextImpl();
		nsc.setNamespace("pdd", ActiveVOS9Deployment.NAMESPACE_PDD);
		nsc.setNamespace("wsa", "http://www.w3.org/2005/08/addressing");
		
		XPathTool xpath = new XPathTool(nsc);
		
		return xpath;
	}

}
