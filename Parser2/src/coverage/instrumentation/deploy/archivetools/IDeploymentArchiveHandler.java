package coverage.instrumentation.deploy.archivetools;

import coverage.exception.ArchiveFileException;

import de.schlichtherle.io.File;

/**
 * Interface dient als Schnittstelle für den Zugriff auf die (deployment)
 * Archivedateien, die spezifisch für die BPELEngine sind.
 * 
 * @author Alex Salnikow
 * 
 */

public interface IDeploymentArchiveHandler {
	
	
	public void setArchiveFile(String pfad);

	public java.io.File getArchiveFile() throws ArchiveFileException;

	public int getCountOfBPELFiles();

	public File getBPELFile(int i);

	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException;

}
