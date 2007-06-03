package org.bpelunit.framework.coverage.annotation;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.activitycoverage.ActivityMetric;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;


public class MetricsManager {
	
	public static List<LabelStatus> getStatus(String label, Hashtable<String,Hashtable<String,LabelStatus>> allLabels) {
		List<LabelStatus> list=new ArrayList<LabelStatus>();
		if (allLabels.get(label) != null) {
			Hashtable activityTable = allLabels.get(label);
			Enumeration e = activityTable.elements();
			while (e.hasMoreElements()) {
				list.add ((LabelStatus) e.nextElement());
			}
		}
		return list;
	}
	
	
	public static IMetric createMetric(String name,List<String> list){
		IMetric metric=null;
		if(name.equals(ActivityMetric.METRIC_NAME)){
			metric=new ActivityMetric(list);
			getInstance().addMetric(metric);
		}else if(name.equals(BranchMetric.METRIC_NAME)){
			metric=new BranchMetric();
			getInstance().addMetric(metric);
		}else if(name.equals(CompensationMetric.METRIC_NAME)){
			metric=new CompensationMetric();
			getInstance().addMetric(metric);
		}else if(name.equals(FaultMetric.METRIC_NAME)){
			metric=new FaultMetric();
			getInstance().addMetric(metric);
		}else if(name.equals(LinkMetric.METRIC_NAME)){
			metric=new LinkMetric();
			getInstance().addMetric(metric);
		}
		return metric;
	}
	
	private static MetricsManager instance=null;
	
	public static MetricsManager getInstance(){
		if(instance==null)
			instance=new MetricsManager();
		return instance;
	}
	
	private MetricsManager(){
		metrics=new ArrayList<IMetric>();
	}
	private List<IMetric> metrics;
	
	private void addMetric(IMetric metric){
		metrics.add(metric);
	}
	
	public List<IMetric> getMetrics() {
		return metrics;
	}
	
	public List<IStatistic> createStatistics(Hashtable<String, Hashtable<String, LabelStatus>> allLabels){
		List<IStatistic> statistics=new ArrayList<IStatistic>();
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			statistics.add(iter.next().createStatistic(allLabels));
		}
		return statistics;
	}


	public void initialize() {
		metrics=new ArrayList<IMetric>();
	}




}
