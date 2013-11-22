package net.bpelunit.utils.testdataexternalizer.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FileSystemFileWriter implements IFileWriter {

	private File dir;

	public FileSystemFileWriter(File target) {
		if(!target.isDirectory()) {
			throw new IllegalArgumentException("Target must be a directory but it is not: " + target.toString());
		}
		
		this.dir = target;
		this.dir.mkdirs();
	}
	
	@Override
	public void write(InputStream is, String fileName) throws FileAlreadyExistsException {
		File file = new File(this.dir, fileName);
		
		if(file.exists()) {
			throw new FileAlreadyExistsException(fileName);
		}
		
		file.getParentFile().mkdirs();
		
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(file);
			IOUtils.copy(is, fo);
		} catch (Exception e) {
			throw new IllegalArgumentException("To file " + fileName + " cannot be written!");
		} finally {
			IOUtils.closeQuietly(fo);
			IOUtils.closeQuietly(is);
		}
	}

}
