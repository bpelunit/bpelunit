package net.bpelunit.toolsupport.popup.actions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.bpelunit.utils.testdataexternalizer.io.IFileWriter;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

final class EclipseFileWriter implements IFileWriter {
	private final IContainer path;
	private List<IFile> files = new ArrayList<IFile>();

	EclipseFileWriter(IContainer path) {
		this.path = path;
	}

	@Override
	public void write(InputStream is, String fileName) throws FileAlreadyExistsException {
		IFile f = path.getFile(new Path(fileName));
		if(f.exists()) {
			throw new FileAlreadyExistsException(fileName);
		}
		try {
			f.create(is, true, null);
			files.add(f);
		} catch (CoreException e) {
			throw new IllegalArgumentException("File cannot be written: " + f.getName());
		}
		
		try {
			is.close();
		} catch(IOException e) {
			// nothing to do
		}
	}

	public void rollback() {
		for(IFile f : files) {
			try {
				f.delete(true, null);
			} catch (CoreException e) {
				// ignore and proceed
			}
		}
	}
}