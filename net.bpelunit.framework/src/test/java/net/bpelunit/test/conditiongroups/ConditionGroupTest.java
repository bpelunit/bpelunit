package net.bpelunit.test.conditiongroups;

import java.io.File;

import net.bpelunit.test.templates.AbstractTemplateTest;
import net.bpelunit.test.util.TestUtil;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ConditionGroupTest {

	protected static final File TEST_BPTS_DIR = new File(
			FileUtils.toFile(AbstractTemplateTest.class.getResource("/")),
			"conditiongroups");

	@Test
	public void testSuccessfulAssertionGroupRun() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"The same BPTS file should always produce the same results",
				new File(TEST_BPTS_DIR, "BookSearch-4results-data.bpts"),
				new File(TEST_BPTS_DIR, "BookSearch-4results-cg.bpts"));
	}
}
