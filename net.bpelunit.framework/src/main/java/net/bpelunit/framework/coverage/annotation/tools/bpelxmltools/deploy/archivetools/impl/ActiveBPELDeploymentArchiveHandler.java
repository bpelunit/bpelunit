package net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools.impl;

import static net.bpelunit.framework.coverage.CoverageConstants.ADDRESS_OF_SERVICE;
import static net.bpelunit.framework.coverage.CoverageConstants.COVERAGETOOL_NAMESPACE;
import static net.bpelunit.framework.coverage.CoverageConstants.PARTNERLINK_NAME;
import static net.bpelunit.framework.coverage.CoverageConstants.PARTNERLINK_NAMESPACE;
import static net.bpelunit.framework.coverage.CoverageConstants.PORT_OF_SERVICE;
import static net.bpelunit.framework.coverage.CoverageConstants.PREFIX_COPY_OF_ARCHIVEFILE;
import static net.bpelunit.framework.coverage.CoverageConstants.SERVICE_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools.IDeploymentArchiveHandler;
import net.bpelunit.framework.coverage.exceptions.ArchiveFileException;
import net.bpelunit.framework.coverage.exceptions.BpelException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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

	private static String wsdlFileInArchive;

	private Map<String, File> bpelFiles;

	private File archiveFile;

	private Logger fLogger;

	public ActiveBPELDeploymentArchiveHandler() {
		fLogger = Logger.getLogger(getClass());
		bpelFiles = new Hashtable<String, File>();
	}

	/**
	 * Erzeugt eine Kopie des Archives und unresucht das Archive nach
	 * BPEL-Dateien
	 * 
	 * @throws ArchiveFileException
	 */
	public String createArchivecopy(String archive) throws ArchiveFileException {
		this.archiveFile = createCopy(archive);
		File copyFile = archiveFile;
		searchBPELFiles();
		return copyFile.getName();
	}

	private File createCopy(String archive) throws ArchiveFileException {
		// TODO
		String fileName = FilenameUtils.getName(archive);
		String pfad = FilenameUtils.getFullPath(archive);
		String nameOfCopy = PREFIX_COPY_OF_ARCHIVEFILE + fileName;
		File copyFile = new File(FilenameUtils.concat(pfad, nameOfCopy));
		File file = new File(archive);
		copyFile.copyAllFrom(file);
		try {
			File.umount(true, true, true, true);
		} catch (ArchiveException e) {
			throw new ArchiveFileException(
					"Could not create copy of bpr-archive", e);
		}
		fLogger.info("CoverageTool:Copy of BPR-archive " + nameOfCopy
				+ " is created.");
		return copyFile;
	}

	// ********************* BPEL files in bpr-archive *********************

	/**
	 * @param i
	 *            index der BPEL-Datei
	 * @return file: BPEL-Datei
	 */
	public File getBPELFile(int i) {
		return bpelFiles.get(i);
	}

	private void searchBPELFiles() {
		fLogger.info("CoverageTool:Search for BPEL-files is started");
		File file = new File(archiveFile.getPath());
		searchChildrenBPEL(file);
	}

	/**
	 * Sucht die Verzeichnisse nach BPEL-Dateien rekursiv durch.
	 * 
	 * @param file
	 */
	private void searchChildrenBPEL(File file) {
		java.io.File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				searchChildrenBPEL((File) files[i]);
			}
		} else if (FilenameUtils.getExtension(file.getName()).equals("bpel")) {
				bpelFiles.put(file.getInnerEntryName(), file);
		}
	}

	// ********************* add WSDL file to bpr-archive *********************

	/**
	 * Fügt WSDLFile in das Archive ein. Dabei wird WSDl-Catalog und
	 * DeploymentDescriptor angepasst.
	 * 
	 * @param wsdlFile
	 */
	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException {
		wsdlFileInArchive = WSDL_DIRECTORY_IN_ARCHIVE + wsdlFile.getName();
		fLogger.info("CoverageTool: Adding WSDL-file " + wsdlFile.getPath()
				+ " for CoverageLogging in bpr-archive");
		FileOutputStream out = null;
		try {
			adaptWsdlCatalog();
			out = new FileOutputStream(archiveFile.getPath() + "/"
					+ wsdlFileInArchive);
			out.write(FileUtils.readFileToByteArray(wsdlFile));
			fLogger.info("CoverageTool: WSDL-file sucessfull added.");

		} catch (IOException e) {
			throw new ArchiveFileException(
					"Could not add WSDL file for coverage measurement tool ("
							+ wsdlFile.getName() + ") in deployment archive ",
					e);
		} finally {
			IOUtils.closeQuietly(out);
		}
		prepareDeploymentDescriptor();
	}

	/**
	 * Rgestriert WSDL-File in dem WSDL-Catalog
	 * 
	 * @throws ArchiveFileException
	 */
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
			wsdlEntry.setAttribute(LOCATION_ATTR, wsdlFileInArchive);
			wsdlEntry.setAttribute(CLASSPATH_ATTR, wsdlFileInArchive);
			wsdlCatalog.addContent(wsdlEntry);
		} catch (JDOMException e) {
			throw new ArchiveFileException(
					"An XML reading error occurred when reading the WSDL catalog.",
					e);
		} catch (IOException e) {
			e.printStackTrace(System.out);
			throw new ArchiveFileException(
					"An I/O error occurred when reading the WSDL catalog.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
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
				}
			}
		}
	}

	private File getWSDLCatalog() {
		String pfad = FilenameUtils.concat(FilenameUtils.concat(archiveFile
				.getPath(), "META-INF"), "wsdlCatalog.xml");
		return new File(pfad);
	}

	/**
	 * 
	 * Fügt Endpoint des Coverage Logging Services und WSDl-Eintrag in den
	 * Deployment Descriptor ein.
	 * 
	 * @throws ArchiveFileException
	 */
	private void prepareDeploymentDescriptor() throws ArchiveFileException {
		FileInputStream is = null;
		FileWriter writer = null;
		List<String> pddFiles = getDeploymentDescriptors();

		de.schlichtherle.io.File descriptor;
		for (Iterator<String> iter = pddFiles.iterator(); iter.hasNext();) {
			descriptor = new File(archiveFile.getPath() + "/" + iter.next());
			try {
				Document doc;
				SAXBuilder builder = new SAXBuilder();
				is = new FileInputStream(descriptor);
				doc = builder.build(is);
				Element process = doc.getRootElement();
				addPartnerLinkEndpoint(process);
				addWSDLEntry(process);
				writer = new FileWriter(descriptor);
				XMLOutputter xmlOutputter = new XMLOutputter(Format
						.getPrettyFormat());
				xmlOutputter.output(doc, writer);
			} catch (IOException e) {

				throw new ArchiveFileException(
						"An I/O error occurred when writing deployment descriptor: "
								+ descriptor.getName(), e);
			} catch (JDOMException e) {
				throw new ArchiveFileException(
						"An XML reading error occurred when reading the deployment descriptor: "
								+ descriptor.getName(), e);
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(writer);
			}
		}
	}

	private void addWSDLEntry(Element process) {
		Element wsdl = new Element(WSDL_ELEMENT, process.getNamespace());
		wsdl.setAttribute(LOCATION_OF_WSDL_ATTR, wsdlFileInArchive);
		wsdl.setAttribute(NAMESPACE_ATTR, COVERAGETOOL_NAMESPACE.getURI());
		Element references = getReferencesElement(process);
		references.addContent(wsdl);
		fLogger
				.info("CoverageTool:Reference of _LogService.wsdl in BPEL added.");
	}

	private Element getReferencesElement(Element process) {
		Element references = null;
		references = process.getChild("references", process.getNamespace());
		if (references == null) {
			references = process.getChild("wsdlReferences", process
					.getNamespace());
		}
		if (references == null) {
			references = new Element("references", process.getNamespace());
			process.addContent(references);
		}
		return references;
	}

	private List<String> getDeploymentDescriptors() throws ArchiveFileException {
		String[] content = archiveFile.list();
		List<String> pddFiles = new ArrayList<String>();
		String name = null;
		for (int i = 0; i < content.length; i++) {
			name = content[i];
			if (FilenameUtils.getExtension(name).equals("pdd")) {
				pddFiles.add(name);
			}
		}
		
		if (pddFiles.size() == 0) {
			throw new ArchiveFileException(
					"Process deployment descriptor in bpr-archive not found");
		}
		fLogger.info("CoverageTool: gefunden " + name + " pdd-Datei");
		
		return pddFiles;
	}

	private void addPartnerLinkEndpoint(Element process) {

		Namespace ns = Namespace.getNamespace("wsa", PARTNERLINK_NAMESPACE);
		process.addNamespaceDeclaration(ns);
		Element adress = new Element("Address", ns);
		adress.setText(ADDRESS_OF_SERVICE);
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
		if (partnerLinks == null) {
			partnerLinks = new Element(BpelXMLTools.PARTNERLINKS_ELEMENT,
					process.getNamespace());
			process.addContent(partnerLinks);
		}
		partnerLinks.addContent(partnerLink);
		fLogger
				.info("CoverageTool:PartnerLink for Covergae_Logging_Service in BPEL added.");
	}

	/**
	 * Extrahiert BPEL-File aus dem Archive
	 * @return BPEL-File als XML-Dokument
	 */
	public Document getDocument(String bpelFile) throws BpelException {
		de.schlichtherle.io.File file = bpelFiles.get(bpelFile);
		return readBPELDocument(file);
	}

	/**
	 * Schreibt den BPEL-Prozess in Form eines XML-Dokumentes in den Archiv
	 * @param doc BPEL-Prozess als XML-Dokument
	 * @param fileName Name der BPEL-Datei
	 * @throws ArchiveFileException
	 */
	public void writeDocument(Document doc, String fileName)
			throws ArchiveFileException {
		writeBPELDocument(bpelFiles.get(fileName), doc);
	}

	private Document readBPELDocument(File file) throws BpelException {
		FileInputStream is = null;
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			is = new FileInputStream(file);
			doc = builder.build(is);
		} catch (IOException e) {
			throw new BpelException(
					"An I/O error occurred when reading the BPEL file: "
							+ file.getName(), e);
		} catch (JDOMException e) {
			throw new BpelException(
					"An XML reading error occurred reading the BPEL file "
							+ file.getName(), e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return doc;
	}

	private void writeBPELDocument(File file, Document doc)
			throws ArchiveFileException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);

			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (IOException e) {
			throw new ArchiveFileException(
					"An I/O error occurred when writing the BPEL file: "
							+ file.getName(), e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	public Set<String> getAllBPELFileNames() {
		return bpelFiles.keySet();
	}

	/**
	 * Gibt die reservierten Ressourcen (Streams) frei. 
	 *
	 */
	public void closeArchive() {
		try {
			de.schlichtherle.io.File.umount(true, true, true, true);
		} catch (ArchiveException e) {
		}
	}

}
