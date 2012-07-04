package net.bpelunit.framework.coverage.annotation.metrics.fhcoverage;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.CATCHALL_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.CATCH_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;

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

public class FaultMetric implements IMetric {

	public static final String METRIC_NAME = "FaultHandlerCoverage";

	private IMetricHandler metricHandler;

	private List<Element> elementsOfBPEL = null;

	public FaultMetric(MarkersRegisterForArchive markersRegistry) {
		metricHandler = new FaultMetricHandler(markersRegistry);
	}

	public String getName() {
		return METRIC_NAME;
	}
	
	
	/**
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#getMarkersId()
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

	/* (non-Javadoc)
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetric#createStatistic(java.util.Hashtable)
	 */
	public IStatistic createStatistic(
			Map<String, Map<String, MarkerState>> allMarkers) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStateList(MetricsManager.getStatus(
				FaultMetricHandler.FAULT_HANDLER_LABEL, allMarkers));
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
		@SuppressWarnings("serial")
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
