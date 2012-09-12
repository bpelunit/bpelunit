package net.bpelunit.framework.coverage.output.html;

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
	
	@Override
	public String getMetricName() {
		return this.metricId;
	}

	@Override
	public String getMetricId() {
		return this.metricId;
	}

	@Override
	public List<ICoverageResult> getCoverageResult() {
		return coverageResults;
	}

}
