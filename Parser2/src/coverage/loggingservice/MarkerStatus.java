package coverage.loggingservice;

import java.util.HashSet;
import java.util.Set;

public class MarkerStatus {

	private boolean status=false;
	private Set<String> testcases=new HashSet<String>();
	
	public void setStatus(boolean tested,String testcase){
		status=tested;
		testcases.add(testcase);
	}

	public boolean isStatus() {
		return status;
	}

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
