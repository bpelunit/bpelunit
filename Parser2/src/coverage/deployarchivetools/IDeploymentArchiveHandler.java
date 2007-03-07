package coverage.deployarchivetools;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.schlichtherle.io.File;

import exception.ArchiveFileException;

public interface IDeploymentArchiveHandler {

	public void setArchiveFile(java.io.File archive, String pfad) throws ArchiveFileException;
	
	public java.io.File getArchiveFile();
	
	public int getCountOfBPELFiles();

	public File getBPELFile(int i) throws FileNotFoundException;

	public void addWSDLFile(java.io.File wsdlFile) throws IOException, ArchiveFileException;
	
	public File getDeploymentDescriptor() throws ArchiveFileException;
}
