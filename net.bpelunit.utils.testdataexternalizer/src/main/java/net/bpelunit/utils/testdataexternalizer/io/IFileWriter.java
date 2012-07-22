package net.bpelunit.utils.testdataexternalizer.io;

import java.io.InputStream;

public interface IFileWriter {

	@SuppressWarnings("serial")
	public class FileAlreadyExistsException extends Exception {
		public FileAlreadyExistsException(String fileName) {
			super("File already exists: " + fileName);
		}
	}

	void write(InputStream is, String fileName) throws FileAlreadyExistsException;
	
}
