package org.bpelunit.framework.coverage.annotation.metrics;

import java.util.Hashtable;
import java.util.List;

import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;




public interface IMetric {

	public String getName();

	public List<String> getMetriclabelsIds();

	public List<String> getConfigInfo();

	public IMetricHandler getHandler();

	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, LabelStatus>> allLabels);
}
