package coverage.instrumentation.deploy.archivetools.impl;

import static coverage.CoverageConstants.*;
import java.io.IOException;
import java.io.OutputStream;

import javax.wsdl.Definition;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.exception.ArchiveFileException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.deploy.archivetools.IDeploymentArchiveHandler;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;
import de.schlichtherle.io.FileWriter;

/**
 * 
 * @author Alex Salnikow
 * 
 */
public class ActiveBPELDeploymentArchiveHandler implements
		IDeploymentArchiveHandler {

	private static final String WSDL_DIRECTORY_IN_ARCHIVE = "wsdl/";

	private static final String WSDLENTRY_ELEMENT = "wsdlEntry";

	private static final String LOCATION_ATTR = "location";

	private static final String CLASSPATH_ATTR = "classpath";

	/* import WSDL in BPEL */
	private static final String WSDL_ELEMENT = "wsdl";

	private static final String LOCATION_OF_WSDL_ATTR = LOCATION_ATTR;

	private static final String NAMESPACE_ATTR = "namespace";

	// PartnerLink in PDD
	private static final String PORTNAME_ATTR = "PortName";

	private static final String ENDPOINT_REFERENCE_ATTR = "endpointReference";

	private static final String ENDPOINT_REFERENCE_ATTR_VALUE = "static";

	private static final String NAME_ATTR = "name";

	private static final String SERVICENAME_ELEMENT = "ServiceName";

	private static String WSDL_FILE_IN_ARCHIVE;

	private String[] bpelFiles;

	private File archiveFile;

	private int countOfBpelFiles;

	private String bprFile;

	private Logger fLogger;

	private Definition fWSDLDefinition;

	public ActiveBPELDeploymentArchiveHandler() {
		fLogger = Logger.getLogger(getClass());
	}

	/**
	 * @param i
	 *            index der BPEL-Datei
	 * @return file: BPEL-Datei
	 */
	public File getBPELFile(int i) {

		File bpelFile = new File(bprFile + "/bpel/" + bpelFiles[i]);
		return bpelFile;
	}

	private File getWSDLCatalog() {
		File wsdlCatalog = new File(bprFile + "/META-INF/wsdlCatalog.xml");
		return wsdlCatalog;
	}

	/**
	 * @return Anzahl der BPEL-Dateien im Archive
	 */
	public int getCountOfBPELFiles() {
		return countOfBpelFiles;
	}

	private String[] searchBPELFiles() {
		fLogger.info("CoverageTool:Search for BPEL-files is started");
		// String[] list=archiveFile.list(new BpelFileFilter());
		String[] list = new File(bprFile + "/bpel/").list();
		fLogger.info("CoverageTool: " + list.length + " BPEL-files detected.");
		return list;
	}

	/**
	 * Fügt WSDLFile in das Archive ein. Dabei wird WSDl-Catalog und
	 * DeploymentDescriptor angepasst.
	 * 
	 * @param wsdlFile
	 */
	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException {
		WSDL_FILE_IN_ARCHIVE = WSDL_DIRECTORY_IN_ARCHIVE + wsdlFile.getName();
		fLogger.info("CoverageTool: Adding WSDL-file " + wsdlFile.getName()
				+ " for CoverageLogging in bpr-archive");

		OutputStream out = null;
		try {
			adaptWsdlCatalog();
			out = new FileOutputStream(bprFile + "/" + WSDL_FILE_IN_ARCHIVE);
			out.write(FileUtils.readFileToByteArray(wsdlFile));
			fLogger.info("CoverageTool: WSDL-file sucessfull added.");
			prepareDeploymentDescriptor();

			// WSDLFactory factory= WSDLFactory.newInstance();
			// WSDLReader reader= factory.newWSDLReader();
			// reader.setFeature(Constants.FEATURE_VERBOSE, false);
			// fWSDLDefinition= reader.readWSDL(wsdlFile.getPath());

		} catch (IOException e) {
			throw new ArchiveFileException(
					"Could not add WSDL file for coverage measurement tool ("
							+ wsdlFile.getName() + ") in deployment archive ",
					e);
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void adaptWsdlCatalog() throws ArchiveFileException {
		FileInputStream is = null;
		FileWriter writer = null;

		File file = getWSDLCatalog();
		Document doc = null;
		try {

			is = new FileInputStream(file);

			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(is);
			Element wsdlCatalog = doc.getRootElement();
			Element wsdlEntry = new Element(WSDLENTRY_ELEMENT, wsdlCatalog
					.getNamespace());
			wsdlEntry.setAttribute(LOCATION_ATTR, WSDL_FILE_IN_ARCHIVE);
			wsdlEntry.setAttribute(CLASSPATH_ATTR, WSDL_FILE_IN_ARCHIVE);
			wsdlCatalog.addContent(wsdlEntry);
		} catch (JDOMException e) {
			throw new ArchiveFileException(
					"An XML reading error occurred when reading the WSDL catalog.",
					e);
		} catch (IOException e) {
			throw new ArchiveFileException(
					"An I/O error occurred when reading the WSDL catalog.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			writer = new FileWriter(file);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (IOException e) {
			throw new ArchiveFileException(
					"An I/O error occurred when writing the WSDL catalog.", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void prepareDeploymentDescriptor() throws ArchiveFileException {
		de.schlichtherle.io.File descriptor;
		FileInputStream is = null;
		FileWriter writer = null;
		descriptor = getDeploymentDescriptor();
		Document doc;
		try {
			SAXBuilder builder = new SAXBuilder();
			is = new FileInputStream(descriptor);
			doc = builder.build(is);
		} catch (IOException e) {

			throw new ArchiveFileException(
					"An I/O error occurred when reading deployment descriptor: "
							+ descriptor.getName(), e);
		} catch (JDOMException e) {
			throw new ArchiveFileException(
					"An XML reading error occurred when reading the deployment descriptor: "
							+ descriptor.getName(), e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		Element process = doc.getRootElement();
		addPartnerLink(process);
		addWSDLEntry(process);
		try {
			writer = new FileWriter(descriptor);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (IOException e) {

			throw new ArchiveFileException(
					"An I/O error occurred when writing deployment descriptor: "
							+ descriptor.getName(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private void addWSDLEntry(Element process) {
		Element wsdl = new Element(WSDL_ELEMENT, process.getNamespace());
		wsdl.setAttribute(LOCATION_OF_WSDL_ATTR, WSDL_FILE_IN_ARCHIVE);
		wsdl.setAttribute(NAMESPACE_ATTR, COVERAGETOOL_NAMESPACE.getURI());
		Element references = process.getChild("references", process
				.getNamespace());
		references.addContent(wsdl);

		fLogger
				.info("CoverageTool:Reference of _LogService.wsdl in BPEL added.");
	}

	public java.io.File getArchiveFile() throws ArchiveFileException {
		// archiveFile.renameTo(new
		// File(FilenameUtils.getBaseName(archiveFile.getName())+".jar"));
		try {
			File.umount(archiveFile, true);
		} catch (ArchiveException e) {
			e.printStackTrace();
			throw new ArchiveFileException(
					"Error occur when writing in archive file: "
							+ archiveFile.getName(), e);
		}
		return archiveFile;
	}

	/**
	 * Erzeugt eine Kopie des Archives und unresucht das Archive nach
	 * BPEL-Dateien
	 */
	public void setArchiveFile(String archive) {
		this.archiveFile = createCopy(archive);
		bpelFiles = searchBPELFiles();
		countOfBpelFiles = bpelFiles.length;
	}

	private File createCopy(String archive) {
		String fileName = FilenameUtils.getName(archive);
		String pfad = FilenameUtils.getFullPath(archive);
		String name = PREFIX_COPY_OF_ARCHIVEFILE + fileName;
		bprFile = FilenameUtils.concat(pfad, name);
		File copyFile = new File(bprFile);
		File file = new File(archive);
		copyFile.copyAllFrom(file);
		fLogger.info("CoverageTool:Copy of BPR-archive " + name
				+ " is created.");
		return copyFile;
	}

	private File getDeploymentDescriptor() throws ArchiveFileException {
		String[] content = archiveFile.list();
		String name = null;
		for (int i = 0; i < content.length; i++) {
			name = content[i];
			if (FilenameUtils.getExtension(name).equals("pdd"))
				break;
		}
		if (name == null) {
			throw new ArchiveFileException(
					"Process deployment descriptor in bpr-archive not found");
		}
		return new File(bprFile + "/" + name);
	}

	private void addPartnerLink(Element process) {

		Namespace ns = Namespace.getNamespace("wsa", PARTNERLINK_NAMESPACE);
		process.addNamespaceDeclaration(ns);
		Element adress = new Element("Address", ns);
		adress.setText(ADDRESS_OF_SERVICE + SERVICE_NAME);
		Element serviceName = new Element(SERVICENAME_ELEMENT, ns);
		serviceName.setAttribute(PORTNAME_ATTR, PORT_OF_SERVICE);
		serviceName.setText(COVERAGETOOL_NAMESPACE.getPrefix() + ":"
				+ SERVICE_NAME);

		Element endpointReference = new Element("EndpointReference", ns);
		endpointReference.addNamespaceDeclaration(COVERAGETOOL_NAMESPACE);
		endpointReference.addContent(adress);
		endpointReference.addContent(serviceName);
		Element partnerRole = new Element(
				BpelXMLTools.PARTNERROLE_ATTR_AND_ELEMENT, process
						.getNamespace());
		partnerRole.setAttribute(ENDPOINT_REFERENCE_ATTR,
				ENDPOINT_REFERENCE_ATTR_VALUE);
		partnerRole.addContent(endpointReference);

		Element partnerLink = new Element(BpelXMLTools.PARTNERLINK_ELEMENT,
				process.getNamespace());
		partnerLink.setAttribute(NAME_ATTR, PARTNERLINK_NAME);
		partnerLink.addContent(partnerRole);

		Element partnerLinks = process.getChild(
				BpelXMLTools.PARTNERLINKS_ELEMENT, process.getNamespace());
		partnerLinks.addContent(partnerLink);

		fLogger
				.info("CoverageTool:PartnerLink for Covergae_Logging_Service in BPEL added.");
	}

}
