package org.bpelunit.framework.coverage.annotation.metrics.linkcoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.Statistic;


public class LinkMetric implements IMetric {


	public static final String METRIC_NAME = "LinkCoverage";
	private IMetricHandler metricHandler=new LinkMetricHandler();

	public String getName() {
		return METRIC_NAME;
	}

	public List<String> getMetriclabelsIds() {
		List<String> list=new ArrayList<String>(2);
		list.add(LinkMetricHandler.POSITIV_LINK_LABEL);
		list.add(LinkMetricHandler.NEGATIV_LINK_LABEL);
		
		return list;
	}

	public List<String> getConfigInfo() {
		return null;
	}

	public IMetricHandler getHandler() {
		return metricHandler;
	}

	public IStatistic createStatistic(Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic statistic=new Statistic(METRIC_NAME);
			statistic.addSubStatistik(createSubstatistic(LinkMetricHandler.POSITIV_LINK_LABEL,allLabels));
			statistic.addSubStatistik(createSubstatistic(METRIC_NAME+": "+LinkMetricHandler.NEGATIV_LINK_LABEL,allLabels));
		return statistic;
	}

	private IStatistic createSubstatistic(String name,Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic subStatistic;
		subStatistic=new Statistic(METRIC_NAME+": "+name);
		List<LabelStatus> statusListe=MetricsManager.getStatus(name, allLabels);
		subStatistic.setStatusListe(statusListe);
		return subStatistic;
	}


}
