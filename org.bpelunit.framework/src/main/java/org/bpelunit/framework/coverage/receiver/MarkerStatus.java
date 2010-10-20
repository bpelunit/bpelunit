package org.bpelunit.framework.coverage.receiver;

import java.util.HashSet;
import java.util.Set;

/**
 * Die Klasse repräsentiert eine Coverage-Marke, die mit einem Status (getestet oder nicht)  behaftet ist. 
 * Außerdem werden die zugehörigen  Testfälle gespeichert.
 * @author Alex Salnikow
 *
 */
public class MarkerStatus {

	private boolean status=false;
	private Set<String> testcases=new HashSet<String>();
	
	/**
	 * 
	 * @param tested
	 * @param testcase
	 */
	public void setStatus(boolean tested,String testcase){
		status=tested;
		testcases.add(testcase);
	}

	public boolean isTested() {
		return status;
	}

	/**
	 * 
	 * @return alle Testfälle, die diese Marke "getestet" haben.
	 */
	public Set<String> getTestcases() {
		return testcases;
	}
	
	public boolean isTestedWithTestcase(String testcaseName){
		if(testcases.contains(testcaseName)){
			return true;
		}
		return false;
	}
	
	
}
