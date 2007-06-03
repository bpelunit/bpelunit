package org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools;

import java.io.File;
import java.util.Set;

import org.bpelunit.framework.coverage.exceptions.ArchiveFileException;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Document;

/**
 * Interface dient als Schnittstelle für den Zugriff auf die (deployment)
 * Archivedateien, die spezifisch für die BPELEngine sind.
 * 
 * @author Alex Salnikow
 * 
 */

public interface IDeploymentArchiveHandler {

	public void setArchiveFile(String pfad) throws ArchiveFileException;

	public File getArchiveFile() throws ArchiveFileException;

	public Set<String> getAllBPELFileNames();

	public Document getDocument(String fileName) throws BpelException;

	public void writeDocument(Document doc, String fileName) throws ArchiveFileException;

	public void addWSDLFile(java.io.File wsdlFile) throws ArchiveFileException;

	public void closeArchive();

}
