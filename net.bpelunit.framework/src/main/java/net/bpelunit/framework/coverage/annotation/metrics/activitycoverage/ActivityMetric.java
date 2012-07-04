package net.bpelunit.framework.coverage.annotation.metrics.activitycoverage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.annotation.MetricsManager;
import net.bpelunit.framework.coverage.annotation.metrics.IMetric;
import net.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkerState;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import net.bpelunit.framework.coverage.result.statistic.IStatistic;
import net.bpelunit.framework.coverage.result.statistic.impl.Statistic;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

/** 
 * Activity Metric class
 * 
 * @author Alex Salnikow, Ronald Becher
 */
public class ActivityMetric implements IMetric {

	public static final String METRIC_NAME = "ActivityCoverage";

	private List<String> activitiesToRespect;

	private IMetricHandler metricHandler;

	private List<Element> elementsOfBPEL = null;

	public ActivityMetric(List<String> activitesToRespect, MarkersRegisterForArchive markersRegistry) {
		activitiesToRespect = new ArrayList<String>();
		if (activitesToRespect != null) {
			for (Iterator<String> iter = activitesToRespect.iterator(); iter
					.hasNext();) {
				String basicActivity = iter.next();
				activitiesToRespect.add(basicActivity);
			}
		}
		metricHandler = new ActivityMetricHandler(markersRegistry);
	}

	/**
	 * 
	 */
	public String getName() {
		return METRIC_NAME;
	}

	/*
	 * Liefert Präfixe von allen Marken dieser Metrik. Sie ermöglichen die
	 * Zuordnung der empfangenen Marken einer Metrik
	 * 
	 * @return Präfixe von allen Marken dieser Metrik
	 */
	/**
	 * Delivers prefixes of all metric's markers.
	 * 
	 * <br />They allow the association to received metrics 
	 * 
	 * @return prefixes
	 */
	public List<String> getMarkersId() {
		return activitiesToRespect;
	}


	/*
	 * Erzeugt Statistiken
	 * 
	 * @param allMarkers
	 *            alle einegfügten Marken (von allen Metriken), nach dem Testen
	 * @return Statistik
	 */
	/**
	 * Generates statistics
	 * 
	 * @param allMarkers
	 *            all inserted markers (after testing)
	 * @return statistic
	 */
	public IStatistic createStatistic(
			Map<String, Map<String, MarkerState>> allMarkers) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		IStatistic subStatistic;
		String label;
		for (Iterator<String> iter = activitiesToRespect.iterator(); iter
				.hasNext();) {
			label = iter.next();
			subStatistic = new Statistic(METRIC_NAME + ": " + label);
			List<MarkerState> statusListe = MetricsManager.getStatus(label,
					allMarkers);
			subStatistic.setStateList(statusListe);
			statistic.addSubStatistic(subStatistic);
		}
		return statistic;
	}


	/*
	 * Erhält die noch nicht modifizierte Beschreibung des BPELProzesses als
	 * XML-Element. Alle für die Instrumentierung benötigten Elemente der
	 * Prozessbeschreibung werden gespeichert
	 * 
	 * @param process
	 *            noch nicht modifiziertes BPEL-Prozess
	 */
	/**
	 * Gets the unmodified bpel process as xml element.
	 * 
	 * <br />All elements needed for instrumentating will be saved
	 * 
	 * @param unmodified bpel process
	 */
	public void setOriginalBPELProcess(Element process) {
		ElementFilter filter = new ElementFilter(process.getNamespace());
		elementsOfBPEL = new ArrayList<Element>();
		for (@SuppressWarnings("unchecked")
		Iterator<Element> iter = process.getDescendants(filter); iter
				.hasNext();) {
			Element basicActivity = iter.next();
			if (activitiesToRespect.contains(basicActivity.getName())) {
				elementsOfBPEL.add(basicActivity);
			}
		}	
	}

	/*
	 * delegiert die Instrumentierungsaufgabe an eigenen Handler
	 * 
	 * @throws BpelException
	 */
	/**
	 * Delegates instrumentating to own handler
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
