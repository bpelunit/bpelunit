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
import org.bpelunit.framework.coverage.receiver.MarkerStatus;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;

/**
 * 
 * Die Klasse verwaltet die Metriken, die bei einem Testlauf ermittelt werden sollen.
 * @author Alex Salnikow
 *
 */
public class MetricsManager {
	
	public static List<MarkerStatus> getStatus(String label, Hashtable<String,Hashtable<String,MarkerStatus>> allLabels) {
		List<MarkerStatus> list=new ArrayList<MarkerStatus>();
		if (allLabels.get(label) != null) {
			Hashtable activityTable = allLabels.get(label);
			Enumeration e = activityTable.elements();
			while (e.hasMoreElements()) {
				list.add ((MarkerStatus) e.nextElement());
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param name 
	 * @param list eine Liste mit Konfigurationsinformation
	 * @param markersRegistry wird verwendet um beim Instrumentieren, die ingefügten Marken zu registrieren.
	 * @return Metrik
	 */
	public static IMetric createMetric(String name,List<String> list, MarkersRegisterForArchive markersRegistry){
		IMetric metric=null;
		if(name.equals(ActivityMetric.METRIC_NAME)){
			metric=new ActivityMetric(list,markersRegistry);
		}else if(name.equals(BranchMetric.METRIC_NAME)){
			metric=new BranchMetric(markersRegistry);
		}else if(name.equals(CompensationMetric.METRIC_NAME)){
			metric=new CompensationMetric(markersRegistry);
		}else if(name.equals(FaultMetric.METRIC_NAME)){
			metric=new FaultMetric(markersRegistry);
		}else if(name.equals(LinkMetric.METRIC_NAME)){
			metric=new LinkMetric(markersRegistry);
		}
		return metric;
	}
	
	
	public MetricsManager(){
		metrics=new ArrayList<IMetric>();
	}
	private List<IMetric> metrics;
	
	public void addMetric(IMetric metric){
		metrics.add(metric);
	}
	
	public List<IMetric> getMetrics() {
		return metrics;
	}
	
	public boolean hasMetric(String metricname){
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			if(iter.next().getName().equals(metricname))
				return true;
		}
		return false;
	}
	
	/**
	 * Erzeugt Statistiken nach dem Testlauf (wird an die Statistiken weiterdeliegiert). 
	 * 
	 * @param allMarkers eine Liste mit allen eingefügten Marken (nach dem Testlauf)
	 * @return alle erzeugten Statistiken
	 */
	public List<IStatistic> createStatistics(Hashtable<String, Hashtable<String, MarkerStatus>> allMarkers){
		List<IStatistic> statistics=new ArrayList<IStatistic>();
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			statistics.add(iter.next().createStatistic(allMarkers));
		}
		return statistics;
	}


}
