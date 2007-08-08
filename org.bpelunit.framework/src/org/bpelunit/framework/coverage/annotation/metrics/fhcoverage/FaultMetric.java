package org.bpelunit.framework.coverage.annotation.metrics.fhcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.CATCHALL_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.CATCH_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;

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

public class FaultMetric implements IMetric {



	public static final String METRIC_NAME = "FaultHandlerCoverage";

	private IMetricHandler metricHandler;

	private ArrayList<Element> elementsOfBPEL = null;

	public FaultMetric(MarkersRegisterForArchive markersRegistry) {
		metricHandler = new FaultMetricHandler(markersRegistry);
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
		List<String> list = new ArrayList<String>();
		list.add(FaultMetricHandler.FAULT_HANDLER_LABEL);
		return list;

	}

	public List<String> getConfigInfo() {
		return null;
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
		statistic.setStatusListe(MetricsManager.getStatus(
				FaultMetricHandler.FAULT_HANDLER_LABEL, allMarkers));
		return statistic;
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
		ElementFilter filter = new ElementFilter(getProcessNamespace()) {
			@Override
			public boolean matches(Object obj) {
				boolean result = false;
				String elementName;
				if (super.matches(obj)) {
					elementName = ((Element) obj).getName();
					if (elementName.equals(CATCH_ELEMENT)
							|| elementName.equals(CATCHALL_ELEMENT)) {
						result = true;
					}
				}
				return result;
			}
		};

		elementsOfBPEL = new ArrayList<Element>();
		for (Iterator<Element> iter = process.getDescendants(filter); iter
				.hasNext();) {
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
