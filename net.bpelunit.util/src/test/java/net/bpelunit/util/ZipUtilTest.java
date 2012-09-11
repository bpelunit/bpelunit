/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Test;

public class ZipUtilTest {

	@Test
	public void testZipDirectory() throws Exception {
		File zipFile = createZipForTesting();

		List<String> expectedEntries = new ArrayList<String>();
		expectedEntries.add("a.txt");
		expectedEntries.add("b.txt");
		expectedEntries.add("subdir/a.txt");

		ZipFile f = new ZipFile(zipFile);
		try {
			Enumeration<? extends ZipEntry> entries = f.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				assertTrue(expectedEntries.contains(entry.getName()));
				expectedEntries.remove(entry.getName());
			}
			assertEquals(0, expectedEntries.size());
		} finally {
			f.close();
		}
	}

	@Test
	public void testUnzip() throws Exception {
		File zipArchive = createZipForTesting();

		File tempDir = FileUtil.createTempDirectory();
		ZipUtil.unzipFile(zipArchive, tempDir);

		assertTrue(new File(tempDir, "a.txt").isFile());
		assertTrue(new File(tempDir, "b.txt").isFile());
		assertTrue(new File(tempDir, "subdir").isDirectory());
		assertTrue(new File(tempDir, "subdir/a.txt").isFile());
	}

	private File createZipForTesting() throws IOException {
		File tempDir = FileUtil.createTempDirectory();

		new File(tempDir, "a.txt").createNewFile();
		new File(tempDir, "b.txt").createNewFile();
		File subDir = new File(tempDir, "subdir");
		subDir.mkdir();
		new File(subDir, "a.txt").createNewFile();

		File zipFile = File.createTempFile("bpelunit", ".zip");
		ZipUtil.zipDirectory(tempDir, zipFile);
		return zipFile;
	}
}
