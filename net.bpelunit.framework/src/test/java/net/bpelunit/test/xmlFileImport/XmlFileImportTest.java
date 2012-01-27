package net.bpelunit.test.xmlFileImport;

import java.io.File;

import net.bpelunit.test.templates.AbstractTemplateTest;
import net.bpelunit.test.util.TestUtil;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class XmlFileImportTest {

	protected static final File TEST_BPTS_DIR = new File(
			FileUtils.toFile(AbstractTemplateTest.class.getResource("/")),
			"xmlFileImport");

	@Test
	public void testSuccessfulXmlFileImport() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"The same BPTS file should always produce the same results",
				new File(TEST_BPTS_DIR, "BookSearch-4results-data.bpts"),
				new File(TEST_BPTS_DIR, "BookSearch-4results-fi.bpts"));
	}

	@Test
	public void testSuccessfulXmlFileImportWithSrcAttribute() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"The same BPTS file should always produce the same results",
				new File(TEST_BPTS_DIR, "BookSearch-4results-data.bpts"),
				new File(TEST_BPTS_DIR, "BookSearch-4results-fi-src.bpts"));
	}
}
