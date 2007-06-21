package org.bpelunit.framework.coverage.receiver;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.FileStatistic;

public class MarkersRegistryForBPELFile {

	private String fileName;

	private Hashtable<String, Hashtable<String, MarkerState>> allMetricsTable;

	private MetricsManager metricManager;

	public MarkersRegistryForBPELFile(String filename, MetricsManager metricManager) {
		this.fileName = filename;
		this.metricManager=metricManager;
		prepareStructur(metricManager);
	}

	public String getBPELFileName() {
		return fileName;
	}

	public void addMarker(String marke, MarkerState status) {
		String prefix = marke.substring(0, marke
				.indexOf(Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR));
		allMetricsTable.get(prefix).put(marke, status);
	}

	private void prepareStructur(MetricsManager metricManager) {
		List<IMetric> metrics = metricManager.getMetrics();
		allMetricsTable = new Hashtable<String, Hashtable<String, MarkerState>>();
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			IMetric metric = iter.next();
			for (Iterator<String> iterator = metric.getMarkersId()
					.iterator(); iterator.hasNext();) {
				allMetricsTable.put(iterator.next(),
						new Hashtable<String, MarkerState>());
			}
		}
	}

	public IFileStatistic getFileStatistic() {
		IFileStatistic fileStatistic = new FileStatistic(fileName);
		fileStatistic.setStatistics(metricManager
				.createStatistics(allMetricsTable));
		return fileStatistic;
	}

}
