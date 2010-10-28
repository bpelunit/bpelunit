package net.bpelunit.framework.coverage.annotation.metrics.chcoverage;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.COMPENSATION_HANDLER;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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

public class CompensationMetric implements IMetric {
	public static final String METRIC_NAME = "CompensationHandlerCoverage";

	private IMetricHandler metricHandler;

	private ArrayList<Element> elementsOfBPEL = null;

	public CompensationMetric(MarkersRegisterForArchive markersRegistry) {
		metricHandler = new CompensationMetricHandler(markersRegistry);
	}

	public String getName() {
		return METRIC_NAME;
	}

	/*
	 * Liefert Präfixe von allen Marken dieser Metrik. Sie ermöglichen die
	 * Zuordnung der empfangenen Marken einer Metrik
	 * 
	 * @return Präfixe von allen Marken dieser Metrik
	 */
	
	/* (non-Javadoc)
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#getMarkersId()
	 */
	public List<String> getMarkersId() {
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
	
	/*
	 * Erzeugt Statistiken
	 * 
	 * @param allMarkers
	 *            alle einegfügten Marken (von allen Metriken), nach dem Testen
	 * @return Statistik
	 */
	
	/* (non-Javadoc)
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#createStatistic(java.util.Hashtable)
	 */
	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, MarkerState>> allMarkers) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStateList(MetricsManager.getStatus(
				CompensationMetricHandler.COMPENS_HANDLER_LABEL, allMarkers));
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
	/* (non-Javadoc)
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#setOriginalBPELProcess(org.jdom.Element)
	 */
	public void setOriginalBPELProcess(Element process) {
		Iterator<Element> compensHandlers = process
				.getDescendants(new ElementFilter(COMPENSATION_HANDLER,
						getProcessNamespace()));
		elementsOfBPEL = new ArrayList<Element>();
		while (compensHandlers.hasNext()) {
			elementsOfBPEL.add(compensHandlers.next());
		}
	}

	/*
	 * delegiert die Instrumentierungsaufgabe an eigenen Handler
	 * 
	 * @throws BpelException
	 */
	/* (non-Javadoc)
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#insertMarkers()
	 */
	public void insertMarkers() throws BpelException {
		if (elementsOfBPEL != null) {
			metricHandler.insertMarkersForMetric(elementsOfBPEL);
			elementsOfBPEL = null;
		}
	}
	


}
