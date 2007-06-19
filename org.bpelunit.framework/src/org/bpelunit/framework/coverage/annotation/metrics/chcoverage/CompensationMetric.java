package org.bpelunit.framework.coverage.annotation.metrics.chcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.COMPENSATION_HANDLER;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.bpelunit.framework.coverage.result.statistic.impl.Statistic;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

public class CompensationMetric implements IMetric {
	public static final String METRIC_NAME = "CompensationHandlerCoverage";

	private IMetricHandler metricHandler;

	private ArrayList<Element> elementsOfBPEL = null;

	public CompensationMetric(LabelsRegistry markersRegistry) {
		metricHandler = new CompensationMetricHandler(markersRegistry);
	}

	public String getName() {
		return METRIC_NAME;
	}

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

	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStatusListe(MetricsManager.getStatus(
				CompensationMetricHandler.COMPENS_HANDLER_LABEL, allLabels));
		return statistic;
	}

	public void setOriginalBPELDocument(Element process_element) {
		Iterator<Element> compensHandlers = process_element
				.getDescendants(new ElementFilter(COMPENSATION_HANDLER,
						getProcessNamespace()));
		elementsOfBPEL = new ArrayList<Element>();
		while (compensHandlers.hasNext()) {
			elementsOfBPEL.add(compensHandlers.next());
		}
	}

	public void insertMarkers() throws BpelException {
		if (elementsOfBPEL != null)
			metricHandler.insertMarkersForMetric(elementsOfBPEL);
		elementsOfBPEL=null;

	}

}
