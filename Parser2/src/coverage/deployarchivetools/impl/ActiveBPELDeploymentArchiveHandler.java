package coverage.deployarchivetools.impl;

import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.deployarchivetools.IDeploymentArchiveHandler;

import de.schlichtherle.io.ArchiveException;
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

	public File getBPELFile(int i) throws FileNotFoundException {
		File bpelFile = new File(bprFile + "/bpel/"
				+ bpelFiles[i]);
		return bpelFile;
	}

	private File getWSDLCatalog() throws FileNotFoundException {
		File wsdlCatalog = new File(bprFile+"/META-INF/wsdlCatalog.xml");
		return wsdlCatalog;
	}

	public int getCountOfBPELFiles() {
		return countOfBpelFiles;
	}

	private String[] searchBPELFiles() {
		System.out.println("BpelFiles suche gestartet");
		// String[] list=archiveFile.list(new BpelFileFilter());
		String[] list = new File(bprFile+"/bpel/").list();
		System.out.println("BpelFiles suche beendet");
		return list;
	}

	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException {
		System.out.println("WSDL-File wird hinzugefügt");
		FileInputStream is = null;
		FileWriter writer = null;
		OutputStream out=null;
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
			out = new FileOutputStream(bprFile+"/wsdl/"+wsdlFile.getName());
			System.out.println(wsdlFile);
			out.write(FileUtils.readFileToByteArray(wsdlFile));
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
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class BpelFileFilter implements FilenameFilter {

		public boolean accept(java.io.File arg0, String arg1) {
			System.out.println(arg1);
			if (FilenameUtils.getExtension(arg1).equals("bpel")) {
				return true;
			}
			return false;
		}

	}

	public java.io.File getArchiveFile() {
//		archiveFile.renameTo(new File(FilenameUtils.getBaseName(archiveFile.getName())+".jar"));
		return archiveFile;
	}

	public void setArchiveFile(String archive)
			throws ArchiveFileException {
		try {
			this.archiveFile = createCopy(archive);
			System.out.println("BPR-File kopiert "+archive);
			bpelFiles = searchBPELFiles();
			countOfBpelFiles = bpelFiles.length;
			System.out.println("Suche der BPEL-Files abgeschlossen"+countOfBpelFiles);
		} catch (ArchiveException e) {
			throw new ArchiveFileException("", e);
		}

	}

	private File createCopy(String archive) throws ArchiveException {
		String fileName=FilenameUtils.getName(archive);
		String pfad=FilenameUtils.getFullPath(archive);
		bprFile=FilenameUtils.concat(pfad,"_"+fileName );
		File copyFile = new File(bprFile);
		File file=new File(archive);
		copyFile.copyAllFrom(file);
		return copyFile;

	}

	public File getDeploymentDescriptor() throws ArchiveFileException {
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
		return new File(bprFile+ "/"+name);
	}

}
