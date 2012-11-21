package net.bpelunit.framework;

import java.io.File;

import net.bpelunit.test.templates.AbstractTemplateTest;
import net.bpelunit.test.util.TestUtil;

import org.junit.Test;

public class DataSrcAttributeTest {

	protected static final File TEST_BPTS_DIR = new File(AbstractTemplateTest.class.getResource("/datasrc").getPath());

	@Test
	public void dataSrcWorks() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"Using external files or embedding the data in the BPTS should always produce the same results",
				new File(TEST_BPTS_DIR, "BookSearch-4results-data.bpts"),
				new File(TEST_BPTS_DIR, "BookSearch-4results-fi.bpts"));
	}
}
