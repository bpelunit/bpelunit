package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.isStructuredActivity;

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

	public List<String> getMarkersId() {
		List<String> list = new ArrayList<String>();
		list.add(BranchMetricHandler.BRANCH_LABEL);
		return list;
	}


	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, MarkerState>> allLabels) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		statistic.setStatusListe(MetricsManager.getStatus(
				BranchMetricHandler.BRANCH_LABEL, allLabels));
		return statistic;
	}

	public void setOriginalBPELDocument(Element process_element) {
		Element next_element;
		Iterator<Element> iter = process_element
				.getDescendants(new ElementFilter(process_element
						.getNamespace()));
		elementsOfBPEL = new ArrayList<Element>();
		while (iter.hasNext()) {
			next_element = iter.next();
			if (isStructuredActivity(next_element)) {
				elementsOfBPEL.add(next_element);
			}
		}

	}

	public void insertMarkers() throws BpelException {
		if (elementsOfBPEL != null) {
			metricHandler.insertMarkersForMetric(elementsOfBPEL);
			elementsOfBPEL = null;
		}
	}

}
