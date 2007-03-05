package coverage.deployarchivetools.impl;

import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.deployarchivetools.IDeploymentArchiveHandler;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileWriter;
import exception.ArchiveFileException;

public class ActiveBPELDeploymentArchiveHandler implements IDeploymentArchiveHandler {

	private String[] bpelFiles;

	private File archiveFile;

	private int countOfBpelFiles;

	public File getBPELFile(int i) throws FileNotFoundException {
		File bpelFile = new File(archiveFile.getName() + "/bpel/"
				+ bpelFiles[i]);
		return bpelFile;
	}

	private File getWSDLCatalog() throws FileNotFoundException {
		File wsdlCatalog=new File(archiveFile.getName() + "/META-INF/wsdlCatalog.xml");
		System.out.println(wsdlCatalog);
		return wsdlCatalog;
	}

	public int getCountOfBPELFiles() {
		return countOfBpelFiles;
	}

	private String[] searchBPELFiles() {
//		String[] list=archiveFile.list(new BpelFileFilter());
		String[] list=new File(archiveFile.getName()+"/bpel/").list();
		return list;
	}

	public void addWSDLFile(java.io.File wsdlFile) throws IOException,
			ArchiveFileException {
		try {
			File file = getWSDLCatalog();
			InputStream is = new FileInputStream(file);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(is);
			is.close();
			Element wsdlCatalog = doc.getRootElement();
			Element wsdlEntry = new Element("wsdlEntry", wsdlCatalog
					.getNamespace());
//			location="wsdl/_LogService_.wsdl" classpath="wsdl/_LogService_.wsdl"
			wsdlEntry.setAttribute("location","wsdl/_LogService_.wsdl" );
			wsdlEntry.setAttribute("classpath","wsdl/_LogService_.wsdl" );
			wsdlCatalog.addContent(wsdlEntry);
			FileWriter writer = new FileWriter(file);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (JDOMException e) {
			throw new ArchiveFileException("", e);
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
		try {
			File.umount(archiveFile, true,true,true,true);
		} catch (ArchiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("!!!!!!!!!!!"+archiveFile.renameTo(new File("HAllo")));
		return archiveFile;
	}

	public void setArchiveFile(java.io.File archive) throws ArchiveFileException {
		try {
			this.archiveFile =createCopy(archive);

			bpelFiles = searchBPELFiles();
			countOfBpelFiles = bpelFiles.length;
		} catch (ArchiveException e) {
			throw new ArchiveFileException("",e);
		}
		
	}
	
	private File createCopy(java.io.File file) throws ArchiveException{
		 File copyFile= new File("_"+FilenameUtils.getBaseName(file.getName())+".jar");
			copyFile.copyAllFrom(file);
			return copyFile;
			
	}

	public File getDeploymentDescriptor() throws ArchiveFileException {
		String[] content=archiveFile.list();
		String name=null;
		for(int i=0;i<content.length;i++){
			name=content[i];
			if(FilenameUtils.getExtension(name).equals("pdd"))
				break;
		}
		if(name==null){
			throw new ArchiveFileException("Process deployment descriptor in bpr-archive not found");
		}		
		return new File(FilenameUtils.concat(archiveFile.getName(),name));
	}
	

}
