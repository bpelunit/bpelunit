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

	@Override
	public String getBPELElementReference() {
		return bpelElementReference;
	}

	@Override
	public double min() {
		return min;
	}

	@Override
	public double max() {
		return max;
	}

	@Override
	public double avg() {
		return avg;
	}

	@Override
	public int getExecutionCount() {
		return executionCount;
	}

}
