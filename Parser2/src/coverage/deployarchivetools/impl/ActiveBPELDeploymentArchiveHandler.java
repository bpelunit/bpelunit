package coverage.deployarchivetools.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.deployarchivetools.IDeploymentArchiveHandler;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;
import de.schlichtherle.io.FileWriter;
import exception.ArchiveFileException;

public class ActiveBPELDeploymentArchiveHandler implements
		IDeploymentArchiveHandler {

	private String[] bpelFiles;

	private File archiveFile;

	private int countOfBpelFiles;

	private String bprFile;

	private Logger fLogger;

	public ActiveBPELDeploymentArchiveHandler() {
		fLogger = Logger.getLogger(getClass());
	}

	public File getBPELFile(int i) throws FileNotFoundException {
		File bpelFile = new File(bprFile + "/bpel/" + bpelFiles[i]);
		return bpelFile;
	}

	private File getWSDLCatalog() throws FileNotFoundException {
		File wsdlCatalog = new File(bprFile + "/META-INF/wsdlCatalog.xml");
		return wsdlCatalog;
	}

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

	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException {

		fLogger.info("CoverageTool: Adding WSDL-file "
				+ FilenameUtils.getName(wsdlFile.getName())
				+ " for CoverageLogging in bpr-archive");

		OutputStream out = null;
		try {
			adaptWsdlCatalog();
			out = new FileOutputStream(bprFile + "/wsdl/" + wsdlFile.getName());
			out.write(FileUtils.readFileToByteArray(wsdlFile));
			fLogger.info("CoverageTool: WSDL-file sucessfull added.");
			prepareDeploymentDescriptor();

		} catch (IOException e) {
			throw new ArchiveFileException("", e);
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void adaptWsdlCatalog() throws ArchiveFileException {
		FileInputStream is = null;
		FileWriter writer = null;
		try {
			File file = getWSDLCatalog();

			is = new FileInputStream(file);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(is);
			Element wsdlCatalog = doc.getRootElement();
			Element wsdlEntry = new Element("wsdlEntry", wsdlCatalog
					.getNamespace());
			wsdlEntry.setAttribute("location", "wsdl/_LogService_.wsdl");
			wsdlEntry.setAttribute("classpath", "wsdl/_LogService_.wsdl");
			wsdlCatalog.addContent(wsdlEntry);
			writer = new FileWriter(file);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (Exception e) {
			throw new ArchiveFileException("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
		try {
			descriptor = getDeploymentDescriptor();
			SAXBuilder builder = new SAXBuilder();
			is = new FileInputStream(descriptor);
			Document doc = builder.build(is);
			Element process = doc.getRootElement();
			addPartnerLink(process);
			addWSDLEntry(process);
			writer = new FileWriter(descriptor);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ArchiveFileException("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
		Element wsdl = new Element("wsdl", process.getNamespace());
		wsdl.setAttribute("location", "wsdl/_LogService_.wsdl");
		wsdl.setAttribute("namespace",
				"http://www.bpelunit.org/coverage/logService");
		Element references = process.getChild("references", process
				.getNamespace());
		references.addContent(wsdl);

		fLogger
				.info("CoverageTool:Reference of _LogService.wsdl in BPEL added.");
	}



	public java.io.File getArchiveFile() {
		// archiveFile.renameTo(new
		// File(FilenameUtils.getBaseName(archiveFile.getName())+".jar"));
		return archiveFile;
	}

	public void setArchiveFile(String archive) {
		this.archiveFile = createCopy(archive);
		bpelFiles = searchBPELFiles();
		countOfBpelFiles = bpelFiles.length;
	}

	private File createCopy(String archive) {
		String fileName = FilenameUtils.getName(archive);
		String pfad = FilenameUtils.getFullPath(archive);
		String name = "_" + fileName;
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

		Namespace ns = Namespace.getNamespace("wsa",
				"http://schemas.xmlsoap.org/ws/2003/03/addressing");
		process.addNamespaceDeclaration(ns);
		Element adress = new Element("Address", ns);
		adress.setText("http://localhost:7777/ws/_LogService_");
		Element serviceName = new Element("ServiceName", ns);
		serviceName.setAttribute("PortName", "_LogService_SOAP");
		serviceName.setText("s:_LogService_");

		Element endpointReference = new Element("EndpointReference", ns);
		endpointReference.addNamespaceDeclaration(Namespace.getNamespace("s",
				"http://www.bpelunit.org/coverage/logService"));
		endpointReference.addContent(adress);
		endpointReference.addContent(serviceName);
		Element partnerRole = new Element("partnerRole",
				process.getNamespace());
		partnerRole.setAttribute("endpointReference", "static");
		partnerRole.addContent(endpointReference);

		Element partnerLink = new Element("partnerLink",
				process.getNamespace());
		partnerLink.setAttribute("name", "PLT_LogService_");
		partnerLink.addContent(partnerRole);

		Element partnerLinks = process.getChild("partnerLinks",
				process.getNamespace());
		partnerLinks.addContent(partnerLink);

		fLogger
				.info("CoverageTool:PartnerLink for Covergae_Logging_Service in BPEL added.");
	}

}
