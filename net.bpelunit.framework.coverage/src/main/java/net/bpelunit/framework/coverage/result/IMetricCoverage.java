package net.bpelunit.framework.coverage.result;

import java.util.List;

public interface IMetricCoverage {

	/**
	 * @return human readable name as a copy of metric.getName() so
	 * that this information can be persisted durably
	 */
	String getMetricName();
	
	/**
	 * @return full class name of the metric that generated the coverage info
	 */
	String getMetricId();
		
	List<?extends ICoverageResult> getCoverageResult();
}
