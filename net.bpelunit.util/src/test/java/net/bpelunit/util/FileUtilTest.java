package net.bpelunit.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testCreateTempDirectory() throws Exception {
		File f = null;
		try {
			f = FileUtil.createTempDirectory();

			assertTrue(f.exists());
			assertTrue(f.isDirectory());
			assertEquals(0, f.list().length);
		} finally {
			if (f != null) {
				f.delete();
			}
		}
	}

	@Test
	public void testReadFile() throws Exception {
		String fileName = "src/test/resources/"
				+ getClass().getName().replaceAll("\\.", "/") + ".txt";

		File f = new File(fileName);
		assertTrue("Test set-up correct: " + f.getAbsolutePath(), f.exists());

		byte[] contents = FileUtil.readFile(f);
		assertEquals(3, contents.length);
		assertArrayEquals(new byte[] { 'a', 'b', 'c' }, contents);
	}
	
	@Test
	public void testGetFileNameWithoutSuffix() throws Exception {
		assertEquals("test", FileUtil.getFileNameWithoutSuffix("test.txt"));
		assertEquals("test", FileUtil.getFileNameWithoutSuffix("test.bpel"));
		assertEquals("test.txt", FileUtil.getFileNameWithoutSuffix("test.txt.bpel"));
		assertEquals("test", FileUtil.getFileNameWithoutSuffix("test.txt"));
	}
}
