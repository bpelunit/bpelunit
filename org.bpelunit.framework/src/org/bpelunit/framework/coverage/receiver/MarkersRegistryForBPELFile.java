package org.bpelunit.framework.coverage.receiver;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.FileStatistic;

/**
 * 
 * Die Klasse verwaltet alle eingefügten Coverage-Marken für eine BPEL-Datei.
 * 
 * @author Alex Salnikow
 * 
 */

public class MarkersRegistryForBPELFile {

	private String fileName;

	private Hashtable<String, Hashtable<String, MarkerStatus>> allMetricsTable;

	private MetricsManager metricManager;

	public MarkersRegistryForBPELFile(String filename,
			MetricsManager metricManager) {
		this.fileName = filename;
		this.metricManager = metricManager;
		prepareStructur(metricManager);
	}

	public String getBPELFileName() {
		return fileName;
	}

	/**
	 * registriert eingefügte Marken
	 * 
	 * @param marke Coverage Marke
	 * @param status
	 *            Coverage-Marke
	 */
	public void registerMarker(String marke, MarkerStatus status) {
		String prefix = marke.substring(0, marke
				.indexOf(Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR));
		allMetricsTable.get(prefix).put(marke, status);
	}

	/**
	 * Initialisiert Datenstruktur für die Speicherung der Marken für alle
	 * Metriken.
	 * 
	 * @param metricManager
	 */
	private void prepareStructur(MetricsManager metricManager) {
		List<IMetric> metrics = metricManager.getMetrics();
		allMetricsTable = new Hashtable<String, Hashtable<String, MarkerStatus>>();
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			IMetric metric = iter.next();
			for (Iterator<String> iterator = metric.getMarkersId().iterator(); iterator
					.hasNext();) {
				allMetricsTable.put(iterator.next(),
						new Hashtable<String, MarkerStatus>());
			}
		}
	}

	/**
	 * 
	 * @return Statistik der BPEL-Datei
	 */
	public IFileStatistic getFileStatistic() {
		IFileStatistic fileStatistic = new FileStatistic(fileName);
		fileStatistic.setStatistics(metricManager
				.createStatistics(allMetricsTable));
		return fileStatistic;
	}

}
