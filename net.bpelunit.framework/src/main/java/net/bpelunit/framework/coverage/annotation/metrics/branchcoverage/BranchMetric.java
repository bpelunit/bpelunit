package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.isStructuredActivity;

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

public class BranchMetric implements IMetric {

	public static final String METRIC_NAME = "BranchCoverage";

	private IMetricHandler metricHandler;

	private List<Element> elementsOfBPEL = null;

	public BranchMetric(MarkersRegisterForArchive markersRegistry) {
		metricHandler = new BranchMetricHandler(markersRegistry);
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
		list.add(BranchMetricHandler.BRANCH_LABEL);
		return list;
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
			Map<String, Map<String, MarkerState>> allMarkers) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStateList(MetricsManager.getStatus(
				BranchMetricHandler.BRANCH_LABEL, allMarkers));
		return statistic;
	}

	/**
	 * Erhält die noch nicht modifizierte Beschreibung des BPELProzesses als
	 * XML-Element. Alle für die Instrumentierung benötigten Elemente der
	 * Prozessbeschreibung werden gespeichert
	 * 
	 * @param process
	 *            noch nicht modifiziertes BPEL-Prozess
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#setOriginalBPELProcess(org.jdom.Element)
	 */
	public void setOriginalBPELProcess(Element process) {
		Element nextElement;
		Iterator<Element> iter = process
				.getDescendants(new ElementFilter(process
						.getNamespace()));
		elementsOfBPEL = new ArrayList<Element>();
		while (iter.hasNext()) {
			nextElement = iter.next();
			if (isStructuredActivity(nextElement)) {
				elementsOfBPEL.add(nextElement);
			}
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
