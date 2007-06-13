package org.bpelunit.framework.coverage.annotation.metrics.chcoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.Statistic;



public class CompensationMetric implements IMetric {
	public static final String METRIC_NAME = "CompensationHandlerCoverage";
	private IMetricHandler metricHandler=new CompensationMetricHandler();

	public String getName() {
		return METRIC_NAME;
	}

	public List<String> getMetriclabelsIds() {
		List<String> list = new ArrayList<String>();
		list.add(CompensationMetricHandler.COMPENS_HANDLER_LABEL);
		return list;
	}

	public List<String> getConfigInfo() {
		return null;
	}

	public IMetricHandler getHandler() {
		return metricHandler;
	}

	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStatusListe(MetricsManager
				.getStatus(CompensationMetricHandler.COMPENS_HANDLER_LABEL, allLabels));
		return statistic;
	}
	

}
