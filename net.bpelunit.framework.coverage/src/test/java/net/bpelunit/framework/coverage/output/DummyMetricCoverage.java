package net.bpelunit.framework.coverage.output;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.coverage.result.ICoverageResult;
import net.bpelunit.framework.coverage.result.IMetricCoverage;

public class DummyMetricCoverage implements IMetricCoverage {

	private String metricId;
	private List<ICoverageResult> coverageResults = new ArrayList<ICoverageResult>();

	public DummyMetricCoverage(String metricName) {
		this.metricId = metricName;
	}
	
	public String getMetricName() {
		return this.metricId;
	}

	public String getMetricId() {
		return this.metricId;
	}

	public List<ICoverageResult> getCoverageResult() {
		return coverageResults;
	}

}
