package org.bpelunit.framework.coverage.annotation.metrics.chcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.jdom.Comment;
import org.jdom.Element;

public class CompensationMetricHandler implements IMetricHandler {

	public static final String METRIC_NAME = "CompensationHandlerMetric";

	public static final String COMPENS_HANDLER_LABEL = "compHandler";

	private static int count=0;

	/**
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getAndRegisterNextLabel() {
		String label = COMPENS_HANDLER_LABEL + "_" + (count++);

		LabelsRegistry.getInstance().addMarker(label);
		return Instrumenter.COVERAGE_LABEL_IDENTIFIER + label;
	}

	public String getName() {
		return METRIC_NAME;
	}

	public void insertMarkersForMetric(List<Element> activities)
			throws BpelException {
		Element handler;
		for (Iterator<Element> iter = activities.iterator(); iter.hasNext();) {
			handler = iter.next();
			Element activity = getFirstEnclosedActivity(handler);

			if (!activity.getName()
					.equals(StructuredActivities.SEQUENCE_ACTIVITY)) {
				activity = encloseInSequence(activity);
			}
			Comment comment = new Comment(getAndRegisterNextLabel());
			activity.addContent(0, comment);
		}
	}

//	public List<String> getPrefix4CovLabeles() {
//		List<String> list = new ArrayList<String>();
//		list.add(CompensationMetricHandler.COMPENS_HANDLER_LABEL);
//		return list;
//	}


}
