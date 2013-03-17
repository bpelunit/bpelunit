package net.bpelunit.framework.coverage.result;

public interface ICoverageResult {

	/**
	 * XPath statement refering to an element
	 * 
	 * @return
	 */
	String getBPELElementReference();
	
	/** 
	 * @return Minimum execution count of the represented measurement points
	 */
	double min();
	
	/** 
	 * @return Maximum execution count of the represented measurement points
	 */
	double max();
	
	/** 
	 * @return Average execution count of the represented measurement points
	 */
	double avg();
	
	/** 
	 * @return Total execution count of the represented measurement points
	 */
	int getExecutionCount();

	/** 
	 * @return Coverage of the represented measurement points
	 */
	double coverage();
}
