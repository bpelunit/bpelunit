package coverage.deploy.archivetools;



import java.io.IOException;

import coverage.exception.ArchiveFileException;

import de.schlichtherle.io.File;


public interface IDeploymentArchiveHandler {

	public void setArchiveFile(String pfad);
	
	public java.io.File getArchiveFile();
	
	public int getCountOfBPELFiles();

	public File getBPELFile(int i);

	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException ;
	
//	public File getDeploymentDescriptor() throws ArchiveFileException;
}
