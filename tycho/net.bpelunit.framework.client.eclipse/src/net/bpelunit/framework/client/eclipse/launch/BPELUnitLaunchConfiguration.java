package net.bpelunit.framework.client.eclipse.launch;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public class BPELUnitLaunchConfiguration {

	private ILaunchConfiguration config;

	public BPELUnitLaunchConfiguration(ILaunchConfiguration wrappedConfiguration) {
		this.config = wrappedConfiguration;
	}

	public String getProjectName() {
		String projectName = LaunchConstants.EMPTY_STRING;
		try {
			projectName = config.getAttribute(
					LaunchConstants.ATTR_PROJECT_NAME,
					LaunchConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		return projectName;
	}

	public String getTestSuiteFileName() {
		String suiteName = LaunchConstants.EMPTY_STRING;

		try {
			suiteName = config.getAttribute(
					LaunchConstants.ATTR_SUITE_FILE_NAME,
					LaunchConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		return suiteName;
	}
	
	/**
	 * @return list of test cases to be executed. An empty list if all test cases shall be executed. Always not null.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTestCaseNamesToBeExecuted() {
		List<String> testCaseNames = new ArrayList<String>();
		try {
			testCaseNames = ((java.util.List<String>) config.getAttribute(
					LaunchConstants.ATTR_TEST_CASES_NAMES,
					LaunchConstants.EMPTY_LIST));
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		
		return testCaseNames;
	}
}
