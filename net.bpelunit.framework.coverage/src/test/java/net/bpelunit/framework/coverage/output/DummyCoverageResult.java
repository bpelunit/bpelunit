package net.bpelunit.framework.coverage.output;

import net.bpelunit.framework.coverage.result.ICoverageResult;

public class DummyCoverageResult implements ICoverageResult {

	private double avg;
	private String elementReference;
	private int executionCount;
	private double max;
	private double min;
	
	public DummyCoverageResult(String elementReference, int executionCount,
			double min, double max, double avg) {
		super();
		this.elementReference = elementReference;
		this.executionCount = executionCount;
		this.min = min;
		this.max = max;
		this.avg = avg;
	}

	public double avg() {
		return avg;
	}

	public String getBPELElementReference() {
		return elementReference;
	}

	public int getExecutionCount() {
		return executionCount;
	}

	public double max() {
		return max;
	}

	public double min() {
		return min;
	}

}
