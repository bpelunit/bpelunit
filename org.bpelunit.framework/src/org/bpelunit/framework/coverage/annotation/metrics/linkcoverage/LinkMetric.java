package org.bpelunit.framework.coverage.annotation.metrics.linkcoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.MarkerStatus;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.Statistic;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

public class LinkMetric implements IMetric {

	public static final String METRIC_NAME = "LinkCoverage";

	private IMetricHandler metricHandler;

	private List<Element> elementsOfBPEL = null;

	public LinkMetric(MarkersRegisterForArchive markersRegistry) {
		metricHandler = new LinkMetricHandler(markersRegistry);
	}

	public String getName() {
		return METRIC_NAME;
	}

	/**
	 * Liefert Präfixe von allen Marken dieser Metrik. Sie ermöglichen die
	 * Zuordnung der empfangenen Marken einer Metrik
	 * 
	 * @return Präfixe von allen Marken dieser Metrik
	 */
	public List<String> getMarkersId() {
		List<String> list = new ArrayList<String>(2);
		list.add(LinkMetricHandler.POSITIV_LINK_LABEL);
		list.add(LinkMetricHandler.NEGATIV_LINK_LABEL);
		return list;
	}

	public IMetricHandler getHandler() {
		return metricHandler;
	}

	/**
	 * Erzeugt Statistiken
	 * 
	 * @param allMarkers
	 *            alle einegfügten Marken (von allen Metriken), nach dem Testen
	 * @return Statistik
	 */
	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, MarkerStatus>> allMarkers) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.addSubStatistik(createSubstatistic(
				LinkMetricHandler.POSITIV_LINK_LABEL, allMarkers));
		statistic.addSubStatistik(createSubstatistic(
				LinkMetricHandler.NEGATIV_LINK_LABEL, allMarkers));
		return statistic;
	}

	private IStatistic createSubstatistic(String name,
			Hashtable<String, Hashtable<String, MarkerStatus>> allLabels) {
		IStatistic subStatistic;
		subStatistic = new Statistic(METRIC_NAME + ": " + name);
		List<MarkerStatus> statusListe = MetricsManager.getStatus(name,
				allLabels);
		subStatistic.setStatusListe(statusListe);
		return subStatistic;
	}

	/**
	 * Erhält die noch nicht modifizierte Beschreibung des BPELProzesses als
	 * XML-Element. Alle für die Instrumentierung benötigten Elemente der
	 * Prozessbeschreibung werden gespeichert
	 * 
	 * @param process
	 *            noch nicht modifiziertes BPEL-Prozess
	 */
	public void setOriginalBPELProcess(Element process) {
		elementsOfBPEL = new ArrayList<Element>();
		Iterator<Element> iter = process
				.getDescendants(new ElementFilter(LinkMetricHandler.LINK_TAG,
						process.getNamespace()));
		while (iter.hasNext()) {
			elementsOfBPEL.add(iter.next());
		}

	}

	/**
	 * delegiert die Instrumentierungsaufgabe an eigenen Handler
	 * 
	 * @throws BpelException
	 */
	public void insertMarkers() throws BpelException {
		if (elementsOfBPEL != null) {
			metricHandler.insertMarkersForMetric(elementsOfBPEL);
			elementsOfBPEL = null;
		}

	}

}
