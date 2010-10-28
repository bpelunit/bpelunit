package net.bpelunit.framework.coverage.receiver;

import java.util.HashSet;
import java.util.Set;

/*
 * Die Klasse repräsentiert eine Coverage-Marke, die mit einem Status (getestet oder nicht)  behaftet ist. 
 * Außerdem werden die zugehörigen  Testfälle gespeichert.
 * @author Alex Salnikow
 *
 */
/**
 * Class representing a coverage marking.
 * 
 * <br />The marking has a boolean status (either tested or not tested). The
 * associated test cases will be saved alongside.
 * 
 * @author Alex Salnikow, Ronald Becher
 */
public class MarkerState {

	private boolean status = false;
	private Set<String> testcases = new HashSet<String>();

	/**
	 * Set the status
	 * @param tested
	 * @param testcase
	 */
	public void setState(boolean tested, String testcase) {
		status = tested;
		testcases.add(testcase);
	}

	public boolean isTested() {
		return status;
	}

	/*
	 * 
	 * @return alle Testfälle, die diese Marke "getestet" haben.
	 */
	/**
	 * Get test cases (which tested this marking)
	 * @return test cases
	 */
	public Set<String> getTestcases() {
		return testcases;
	}

	/**
	 * Checks whether the marking was tested with named test case
	 * @param test case
	 * @return tested with test case
	 */
	public boolean isTestedWithTestcase(String testcaseName) {
		if (testcases.contains(testcaseName)) {
			return true;
		}
		return false;
	}

}
