package coverage.deploy.archivetools;



import java.io.IOException;

import de.schlichtherle.io.File;

import exception.ArchiveFileException;

public interface IDeploymentArchiveHandler {

	public void setArchiveFile(String pfad);
	
	public java.io.File getArchiveFile();
	
	public int getCountOfBPELFiles();

	public File getBPELFile(int i);

	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException ;
	
//	public File getDeploymentDescriptor() throws ArchiveFileException;
}
