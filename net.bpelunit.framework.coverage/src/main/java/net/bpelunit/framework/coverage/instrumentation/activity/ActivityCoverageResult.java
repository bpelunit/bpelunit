package net.bpelunit.framework.coverage.instrumentation.activity;

import net.bpelunit.framework.coverage.result.ICoverageResult;

public class ActivityCoverageResult implements ICoverageResult {

	private String bpelElementReference;
	public ActivityCoverageResult(String bpelElementReference, double min,
			double max, double avg, int executionCount) {
		super();
		this.bpelElementReference = bpelElementReference;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.executionCount = executionCount;
	}

	private double min;
	private double max;
	private double avg;
	private int executionCount;

	public String getBPELElementReference() {
		return bpelElementReference;
	}

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}

	public double avg() {
		return avg;
	}

	public int getExecutionCount() {
		return executionCount;
	}

}
