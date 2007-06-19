package org.bpelunit.framework.coverage.annotation.metrics.activitycoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
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

public class ActivityMetric implements IMetric {

	private Logger logger=Logger.getLogger(getClass());
	
	public static final String METRIC_NAME = "ActivityCoverage";

	private List<String> activities_to_respekt;

	private IMetricHandler metricHandler;

	private List<Element> elementsOfBPEL = null;

	public ActivityMetric(List<String> activitesToRespect, LabelsRegistry markersRegistry) {
		activities_to_respekt = new ArrayList<String>();
		if (activitesToRespect != null) {
			for (Iterator<String> iter = activitesToRespect.iterator(); iter
					.hasNext();) {
				String basicActivity = iter.next();
				activities_to_respekt.add(basicActivity);
			}
		}
		metricHandler = new ActivityMetricHandler(markersRegistry);
	}

	public String getName() {
		return METRIC_NAME;
	}

	public List<String> getMarkersId() {
		return activities_to_respekt;
	}



	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, LabelStatus>> allLabels) {
		IStatistic statistic = new Statistic(METRIC_NAME);
		IStatistic subStatistic;
		String label;
		for (Iterator<String> iter = activities_to_respekt.iterator(); iter
				.hasNext();) {
			label = iter.next();
			subStatistic = new Statistic(METRIC_NAME + ": " + label);
			List<LabelStatus> statusListe = MetricsManager.getStatus(label,
					allLabels);
			subStatistic.setStatusListe(statusListe);
			statistic.addSubStatistik(subStatistic);
		}
		return statistic;
	}


	public void setOriginalBPELDocument(Element process_element) {
		ElementFilter filter = new ElementFilter(process_element.getNamespace());
		elementsOfBPEL = new ArrayList<Element>();
		for (Iterator<Element> iter = process_element.getDescendants(filter); iter
				.hasNext();) {
			Element basicActivity = iter.next();
			if (activities_to_respekt.contains(basicActivity.getName()))
				elementsOfBPEL.add(basicActivity);
		}	
		logger.info("!!!!!!!!!!!!!ELEMENTSGELOGGT "+elementsOfBPEL.size());
	}

	public void insertMarkers() throws BpelException {
		if (elementsOfBPEL != null)
			metricHandler.insertMarkersForMetric(elementsOfBPEL);
		elementsOfBPEL = null;
	}
}
