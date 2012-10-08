package net.bpelunit.framework.coverage.instrumentation.activity;

import net.bpelunit.framework.coverage.result.ICoverageResult;

public class ActivityCoverageResult implements ICoverageResult {

	private String bpelElementReference;
	public ActivityCoverageResult(String bpelElementReference, double min,
			double max, double avg, int executionCount, double coverage) {
		super();
		this.bpelElementReference = bpelElementReference;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.executionCount = executionCount;
		this.coverage = coverage;
	}

	private double min;
	private double max;
	private double avg;
	private int executionCount;
	private double coverage;

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

	public double coverage() {
		return coverage;
	}
	
	public int getExecutionCount() {
		return executionCount;
	}

}
