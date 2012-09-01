/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public final class FileUtil {

	private FileUtil() {
		// utility class
	}
	
	public static File createTempDirectory() throws IOException {
		File tmp = File.createTempFile("bpelunit", "");
		tmp.delete();
		tmp.mkdir();
		return tmp;
	}
	
	public static byte[] readFile(File f) throws IOException {
		FileInputStream bprInputStream = null;
		try {
			bprInputStream = new FileInputStream(f);
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream((int) f.length());
			IOUtils.copy(bprInputStream, bytesOut);
			
			return bytesOut.toByteArray();
		} finally {
			IOUtils.closeQuietly(bprInputStream);
		}
	}
	
	public static String getFileNameWithoutSuffix(String fileName) {
		int lastIndexOf = fileName.lastIndexOf(".");
		if(lastIndexOf >= 0) {
			return fileName.substring(0, lastIndexOf);
		} else {
			return fileName;
		}
	}
}
