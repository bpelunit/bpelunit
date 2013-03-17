package net.bpelunit.framework.coverage.result;

import java.util.List;

public abstract class AbstractAggregatedCoverageResult implements ICoverageResult {

	private int totalExecutionCount;
	private int relevantExecutedCount; // number of relevant activities executed
	private int totalRelevantCount; // total number of relevant activities
	private double max;
	private double min = Double.MAX_VALUE;

	public final void filterMetricCoverages(List<ICoverageResult> results) {
		for (ICoverageResult r : results) {
			if (filterCoverageResults(r)) {
				totalRelevantCount++;
				totalExecutionCount += r.getExecutionCount();
				max = Math.max(r.max(), max);
				min = Math.min(r.min(), min);
				if (r.getExecutionCount() > 0) {
					relevantExecutedCount++;
				}
			}
		}
	}

	public double avg() {
		return ((double) totalExecutionCount) / ((double) totalRelevantCount);
	}

	public double coverage() {
		return ((double) relevantExecutedCount) / ((double) totalRelevantCount);
	}

	public double max() {
		return max;
	}
	
	public double min() {
		return min;
	}
	
	public int getExecutionCount() {
		return totalExecutionCount;
	}
	
	protected abstract boolean filterCoverageResults(ICoverageResult r);
}
