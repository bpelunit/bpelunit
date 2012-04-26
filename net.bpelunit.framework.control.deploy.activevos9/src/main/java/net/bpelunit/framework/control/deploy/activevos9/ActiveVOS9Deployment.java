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

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.FileUtil;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class ActiveVOS9Deployment implements IDeployment {
	
	private final class BPELInfo implements IBPELProcess {
		
		BPELInfo(File bpelFile, File pddFile, QName name, Document xml) {
			super();
			this.bpelFile = bpelFile;
			this.pddFile = pddFile;
			this.name = name;
			this.xml = xml;
		}
		
		private File bpelFile;
		private File pddFile;
		private QName name;
		private Document xml;
		
		File getBpelFile() {
			return bpelFile;
		}

		File getPddFile() {
			return pddFile;
		}

		Document getXml() {
			return xml;
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
		public void addPartnerlink(String name, QName partnerlinkType,
				String processRole, String partnerRole) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public Document getXML() {
			ActiveVOS9Deployment.this.checkedOutProcesses.add(this);
			return xml;
		}

		@Override
		public void addPartnerlinkBinding(String partnerlinkName,
				QName service, String port, String endpointURL) {
			// TODO Auto-generated method stub
			
		}
	}

	private File bpr; 
	private File tempDirectory = null;
	private File tempBPR = null;
	
	private Set<BPELInfo> checkedOutProcesses = new HashSet<BPELInfo>();
	private List<BPELInfo> allProcesses = new ArrayList<BPELInfo>();
	
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
	

	private synchronized void extractBPRIfNecessary() throws DeploymentException {
		if(tempDirectory != null) {
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
		Iterator<File> bpelFiles = FileUtils.iterateFiles(tempDirectory, new String[]{"bpel"}, true);
		
		while(bpelFiles.hasNext()) {
			File bpelFile = bpelFiles.next();
			
			try {
				Document d = XMLUtil.parseXML(new FileInputStream(bpelFile));
				Element root = d.getDocumentElement();
				QName name = new QName(
							root.getAttribute("targetNamespace"),
							root.getAttribute("name")
						);
				
				allProcesses.add(new BPELInfo(bpelFile, null, name, d));
			} catch (FileNotFoundException e) {
				throw new DeploymentException("File could not be read: " + bpelFile.getAbsolutePath(), e);
			} catch (SAXException e) {
				throw new DeploymentException("BPEL file could not be parsed: " + bpelFile.getAbsolutePath(), e);
			} catch (IOException e) {
				throw new DeploymentException("BPEL file could not be parsed: " + bpelFile.getAbsolutePath(), e);
			} catch (ParserConfigurationException e) {
				throw new DeploymentException("BPEL file could not be parsed: " + bpelFile.getAbsolutePath(), e);
			}
		}
	}

}
