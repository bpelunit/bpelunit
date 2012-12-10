/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.activevos9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.bpelunit.framework.control.deploy.AbstractDeployment;
import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.util.FileUtil;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.activeEndpoints.schemas.catalog.x2006.x07.catalog.CatalogDocument;
import com.activeEndpoints.schemas.catalog.x2006.x07.catalog.WsdlEntryType;

public class ActiveVOS9Deployment extends AbstractDeployment {

	private static final String CATALOG_FILENAME = "META-INF/catalog.xml";

	final class BPELInfo implements IBPELProcess {

		private static final String PATH_TO_BPELUNIT_ADDED_FILES = "wsdl/bpelunit/";

		BPELInfo(File bpelFile, File pddFile, Document pdd)
				throws IOException, JAXBException {
			super();
			this.bpelFile = bpelFile;
			this.pddFile = pddFile;
			FileInputStream in = null;
			try {
				in = new FileInputStream(bpelFile);
				this.bpelModel = BpelFactory.loadProcess(in);
			} finally {
				IOUtils.closeQuietly(in);
			}
			this.name = new QName(bpelModel.getTargetNamespace(),
					bpelModel.getName());
			this.pddXml = pdd;
		}

		private File bpelFile;
		private File pddFile;
		private QName name;
		private IProcess bpelModel;
		private Document pddXml;
		private boolean pddHasChanged = false;
		private boolean bpelHasChanged = false;

		@Override
		public QName getName() {
			return name;
		}

		@Override
		public void addWSDLImport(String wsdlFileName, String namespace,
				InputStream contents) throws DeploymentException {
			String classpath = PATH_TO_BPELUNIT_ADDED_FILES + wsdlFileName;

			// IImport newImport = bpelModel.addImport();
			// newImport.setImportType(IImport.IMPORTTYPE_WSDL);
			// newImport.setLocation(calculateRelativePathTo(wsdlFileName));
			// newImport.setNamespace(namespace);

			String catalogLocation = catalogDoc.getCatalog().getURI() + "/"
					+ PATH_TO_BPELUNIT_ADDED_FILES + wsdlFileName;
			addFileToExplodedBPR(wsdlFileName, contents);
			addCatalogEntryIfNecessary(catalogLocation, classpath);
		}

		String calculateRelativePathTo(String wsdlFileName) {
			int pathElementsInTempDirectory = StringUtils.countMatches(
					tempDirectory.getAbsolutePath().replaceAll("\\\\", "/"),
					"/");
			int pathElementsInBPELFile = StringUtils.countMatches(bpelFile
					.getAbsolutePath().replaceAll("\\\\", "/"), "/");

			StringBuilder relativePath = new StringBuilder();
			for (int i = 0; i < pathElementsInBPELFile
					- pathElementsInTempDirectory - 1; i++) {
				relativePath.append("../");
			}
			relativePath.append(PATH_TO_BPELUNIT_ADDED_FILES);
			relativePath.append(wsdlFileName);

			return relativePath.toString();
		}

		private void addFileToExplodedBPR(String wsdlFileName,
				InputStream contents) throws DeploymentException {
			File bpelUnitOutputDir = new File(tempDirectory,
					PATH_TO_BPELUNIT_ADDED_FILES);
			bpelUnitOutputDir.mkdirs();
			File outputFile = new File(bpelUnitOutputDir, wsdlFileName);
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(outputFile);
			} catch (FileNotFoundException e) {
				throw new DeploymentException("Could not write WSDL: "
						+ e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(contents);
				IOUtils.closeQuietly(out);
			}
		}

		private void addCatalogEntryIfNecessary(String location,
				String classpath) {
			boolean found = false;
			for (WsdlEntryType w : catalogDoc.getCatalog().getWsdlEntryArray()) {
				if (location.equals(w.getLocation())) {
					found = true;
					break;
				}
			}

			if (!found) {
				WsdlEntryType wsdlEntry = catalogDoc.getCatalog()
						.addNewWsdlEntry();
				wsdlEntry.setClasspath(classpath);
				wsdlEntry.setLocation(location);
			}
		}

		@Override
		public void addXSDImport(String xsdFileName, String namespace,
				InputStream contents) {
			// TODO Auto-generated method stub
		}

		@Override
		public IProcess getProcessModel() {
			bpelHasChanged = true;
			return bpelModel;
		}

		Document getPddXml() {
			pddHasChanged = true;
			return pddXml;
		}

		@Override
		public URL addPartnerlink(String name, QName partnerlinkType,
				String processRole, String processEndpointSuffix,
				String partnerRole, QName partnerService, String partnerPort,
				String partnerEndpointURL) {

			getPddXml(); // use this in order to set changed flags correctly
			getProcessModel();

			return null; // TODO
		}

		@Override
		public void changePartnerEndpoint(String partnerLinkName,
				String newEndpoint) throws DeploymentException {
			XPathTool xpathPdd = createXPathToolForPdd();

			Element pddRoot = getPddXml().getDocumentElement();
			try {
				List<Node> address = xpathPdd
						.evaluateAsList(
								"//pdd:partnerLinks/pdd:partnerLink[@name='"
										+ partnerLinkName
										+ "']/pdd:partnerRole/wsa:EndpointReference/wsa:Address",
								pddRoot);
				address.get(0).setTextContent(newEndpoint);

				List<Node> endpointReference = xpathPdd.evaluateAsList(
						"//pdd:partnerLinks/pdd:partnerLink[@name='"
								+ partnerLinkName
								+ "']/pdd:partnerRole/@endpointReference",
						pddRoot);
				endpointReference.get(0).setNodeValue("static");
				List<Node> invokeHandler = xpathPdd.evaluateAsList(
						"//pdd:partnerLinks/pdd:partnerLink[@name='"
								+ partnerLinkName
								+ "']/pdd:partnerRole/@invokeHandler", pddRoot);
				invokeHandler.get(0).setNodeValue("default:Address");
			} catch (Exception e) {
				throw new DeploymentException(
						"Cannot change partner endpoint location for "
								+ partnerLinkName, e);
			}
		}

		public void writeOut() throws IOException, TransformerException {
			if (bpelHasChanged) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(bpelFile);
					bpelModel.save(out);
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
			if (pddHasChanged) {
				XMLUtil.writeXML(getPddXml(), pddFile);
			}
		}
	}

	private File bpr;
	private File tempDirectory = null;
	private File tempBPR = null;
	CatalogDocument catalogDoc = null;

	private List<BPELInfo> allProcesses = new ArrayList<BPELInfo>();
	public static final String NAMESPACE_PDD = "http://schemas.active-endpoints.com/pdd/2006/08/pdd.xsd";

	public ActiveVOS9Deployment(File bpr) throws DeploymentException {
		if (!bpr.isFile() || !bpr.canRead()) {
			throw new DeploymentException(
					"The given BPR does not exist or cannot be read:"
							+ bpr.getAbsolutePath());
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
	 * Called only by ActiveVOS9Deployer in order to get the actual deployment
	 * archive with or without modifications done by instrumentation, mocking,
	 * ...
	 * 
	 * @return contents of the BPR to be deployed
	 * @throws DeploymentException
	 */
	InputStream getUpdatedDeployment() throws DeploymentException {
		if (tempDirectory != null) {
			try {
				// write out all files
				for (BPELInfo bpel : allProcesses) {
					bpel.writeOut();
				}

				catalogDoc.save(new File(tempDirectory, CATALOG_FILENAME));

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
				throw new DeploymentException("BPR does not exist: "
						+ bpr.getAbsolutePath(), e);
			}
		}
	}

	/**
	 * Called only by ActiveVOS9Deployer in order to clean up temp files
	 */
	void cleanUp() throws DeploymentException {
		if (tempBPR != null) {
			tempBPR.delete();
			tempBPR = null;
		}

		if (tempDirectory != null) {
			try {
				FileUtils.deleteDirectory(tempDirectory);
				tempDirectory = null;
			} catch (IOException e) {
				throw new DeploymentException("Cannot delete temp directory: "
						+ tempDirectory.getAbsolutePath(), e);
			}
		}
	}

	/**
	 * This method will extract the BPR file if it isn't already and will
	 * initialize the state of this deployment
	 * 
	 * @throws DeploymentException
	 */
	private synchronized void extractBPRIfNecessary()
			throws DeploymentException {
		if (tempDirectory == null) {
			try {
				tempDirectory = FileUtil.createTempDirectory();
				ZipUtil.unzipFile(bpr, tempDirectory);
				catalogDoc = CatalogDocument.Factory.parse(new File(
						tempDirectory, CATALOG_FILENAME));

				scanForBPELFiles();
			} catch (IOException e) {
				throw new DeploymentException(
						"Error while temporarily extracting the BPR: "
								+ e.getMessage(), e);
			} catch (XmlException e) {
				throw new DeploymentException(
						"Error while parsing XML (catalog.xml): "
								+ e.getMessage(), e);
			}
		}
	}

	private void scanForBPELFiles() throws DeploymentException {
		@SuppressWarnings("unchecked")
		Iterator<File> pddFiles = FileUtils.iterateFiles(tempDirectory,
				new String[] { "pdd" }, true);

		while (pddFiles.hasNext()) {
			File pddFile = pddFiles.next();

			try {
				Document pddXml = XMLUtil
						.parseXML(new FileInputStream(pddFile));
				Element pddRoot = pddXml.getDocumentElement();

				String locationInBPR = pddRoot.getAttribute("location");

				File bpelFile = new File(tempDirectory, locationInBPR);

				allProcesses.add(new BPELInfo(bpelFile, pddFile, pddXml));
			} catch (FileNotFoundException e) {
				throw new DeploymentException("File could not be read: "
						+ e.getMessage(), e);
			} catch (SAXException e) {
				throw new DeploymentException("XML file could not be parsed: "
						+ e.getMessage(), e);
			} catch (IOException e) {
				throw new DeploymentException("XML file could not be read: "
						+ e.getMessage(), e);
			} catch (ParserConfigurationException e) {
				throw new DeploymentException("XML file could not be parsed: "
						+ e.getMessage(), e);
			} catch (JAXBException e) {
				throw new DeploymentException("BPEL file could not be parsed: "
						+ e.getMessage(), e);
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
