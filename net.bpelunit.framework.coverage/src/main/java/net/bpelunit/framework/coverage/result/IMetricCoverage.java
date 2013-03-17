package net.bpelunit.framework.coverage.result;

import java.util.List;

public interface IMetricCoverage {

	/**
	 * @return human readable name as a copy of metric.getName() so
	 * that this information can be persisted durably
	 */
	String getMetricName();
	
	/**
	 * @return Metric ID for this metric as a technical ID without whitespace characters
	 */
	String getMetricId();
		
	List<?extends ICoverageResult> getCoverageResult();
}
