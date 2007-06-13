package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.Statistic;



public class BranchMetric implements IMetric {

	public static final String METRIC_NAME = "BranchCoverage";

	private IMetricHandler metricHandler = new BranchMetricHandler();

	public String getName() {
		return METRIC_NAME;
	}

	public List<String> getMetriclabelsIds() {
		List<String> list = new ArrayList<String>();
		list.add(BranchMetricHandler.BRANCH_LABEL);
		return list;
	}

	public IMetricHandler getHandler() {
		return metricHandler;
	}

	public List<String> getConfigInfo() {
		return null;
	}

	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStatusListe(MetricsManager
				.getStatus(BranchMetricHandler.BRANCH_LABEL, allLabels));
		return statistic;
	}
}
