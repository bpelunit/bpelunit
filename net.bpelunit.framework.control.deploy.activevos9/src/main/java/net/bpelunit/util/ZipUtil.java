/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public final class ZipUtil {

	private ZipUtil() {
		// utility class
	}
	
	public static void unzipFile(File zip, File dir) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		ZipFile zipFile = new ZipFile(zip);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (!entry.getName().endsWith("/")) {
				File unzippedFile = new File(dir, entry.getName());
				try {
					in = zipFile.getInputStream(entry);
					unzippedFile.getParentFile().mkdirs();

					out = new FileOutputStream(unzippedFile);

					IOUtils.copy(in, out);
				} finally {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(out);
				}
			}
		}
	}

	public static void zipDirectory(File directory, File zipFile) throws IOException {
		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(directory, null, true);
		
		FileOutputStream fzos = null; 
		ZipOutputStream zos = null;
		try {
			fzos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fzos);
		
			for(File f : files) {
				String fileNameInZIP = directory.toURI().relativize(f.toURI()).getPath();
				ZipEntry zipEntry = new ZipEntry(fileNameInZIP);
				zos.putNextEntry(zipEntry);
				FileInputStream fileInputStream = new FileInputStream(f);
				try {
					IOUtils.copy(fileInputStream, zos);
				} finally {
					IOUtils.closeQuietly(fileInputStream);
				}
			}
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fzos);
		}
	}

	
}
