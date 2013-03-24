package net.bpelunit.test.conditiongroups;

import java.io.File;

import net.bpelunit.test.util.TestUtil;

import org.junit.Test;

public class ConditionGroupTest {

	protected static final File TEST_BPTS_DIR = new File(
			ConditionGroupTest.class.getResource("/conditiongroups").getPath()
	);

	@Test
	public void testSuccessfulAssertionGroupRun() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"The same BPTS file should always produce the same results",
				new File(TEST_BPTS_DIR, "BookSearch-4results-data.bpts"),
				new File(TEST_BPTS_DIR, "BookSearch-4results-cg.bpts"));
	}
	
	@Test
	public void testAssertionGroupInheritance() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"The same BPTS file should always produce the same results",
				new File(TEST_BPTS_DIR, "BookSearch-4results-data.bpts"),
				new File(TEST_BPTS_DIR, "BookSearch-4results-cg-inheritance.bpts"));
	}
}
