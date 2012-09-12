package net.bpelunit.framework.coverage.result;

public interface ICoverageResult {

	/**
	 * XPath statement refering to an element
	 * 
	 * @return
	 */
	String getBPELElementReference();
	
	double min();
	
	double max();
	
	double avg();
	
	int getExecutionCount();
}
