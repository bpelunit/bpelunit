package org.bpelunit.framework.coverage.annotation.metrics.activitycoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.Statistic;



public class ActivityMetric implements IMetric {

	public static final String METRIC_NAME = "ActivityCoverage";

	private List<String> activities_to_respekt;
	
	private IMetricHandler metricHandler;

	public ActivityMetric(List<String> activitesToRespect) {
		activities_to_respekt = new ArrayList<String>();
		if (activitesToRespect != null) {
			for (Iterator<String> iter = activitesToRespect.iterator(); iter
					.hasNext();) {
				String basicActivity = iter.next();
				activities_to_respekt.add(basicActivity);
			}
		}
		metricHandler=new ActivityMetricHandler();
	}

	public String getName() {
		return METRIC_NAME;
	}

	public List<String> getMetriclabelsIds() {
		return activities_to_respekt;
	}

	public List<String> getConfigInfo() {
		return activities_to_respekt;
	}

	public IMetricHandler getHandler() {
		return metricHandler;
	}

	public IStatistic createStatistic(Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic statistic=new Statistic(METRIC_NAME);
		IStatistic subStatistic;
		String label;
		for (Iterator<String> iter = activities_to_respekt.iterator(); iter.hasNext();) {
			label=iter.next();
			subStatistic=new Statistic(METRIC_NAME+": "+label);
			List<LabelStatus> statusListe=MetricsManager.getStatus(label, allLabels);
			subStatistic.setStatusListe(statusListe);
			statistic.addSubStatistik(subStatistic);
		}
		return statistic;
	}



}
