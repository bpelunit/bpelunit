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
import org.bpelunit.framework.coverage.receiver.MarkerState;
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

	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, MarkerState>> allLabels) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStatusListe(MetricsManager.getStatus(
				FaultMetricHandler.FAULT_HANDLER_LABEL, allLabels));
		return statistic;
	}

	public void setOriginalBPELDocument(Element process_element) {
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
		for (Iterator<Element> iter = process_element.getDescendants(filter); iter
				.hasNext();) {
			elementsOfBPEL.add(iter.next());
		}
	}

	public void insertMarkers() throws BpelException {
		if (elementsOfBPEL != null) {
			metricHandler.insertMarkersForMetric(elementsOfBPEL);
			elementsOfBPEL = null;
		}

	}
	


}
