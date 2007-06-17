package org.bpelunit.framework.coverage.annotation;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createVariableName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.CMServiceFactory;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.exceptions.BpelVersionException;
import org.bpelunit.framework.exception.ConfigurationException;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;

/**
 * Dieses Interface wird von dem Handler implementiert, der dafür zuständig ist,
 * die Instrumentierung der BPEL-Datei zu starten und dabei nur die gewünschten
 * Metriken zu berücksichtigen.
 * 
 * @author Alex Salnikow
 * 
 */
public class Instrumenter {

	public static final String COVERAGE_LABEL_IDENTIFIER = "@coverageLabel";

	public static final char COVERAGE_LABEL_INNER_SEPARATOR = '_';

	public static final String SEPARATOR = "#";

	private Logger logger;

	private CMServiceFactory cmServiceFactory;

	private String assignVariable = createVariableName();

	public Instrumenter() {
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Startet die Instrumentierung der BPEL-Datei.
	 * 
	 * @param document
	 * @param metricManager 
	 * @param metrics
	 * @throws BpelException
	 * @throws ConfigurationException
	 */
	public Document insertAnnotations(Document document, MetricsManager metricManager) throws BpelException {
		Element process_element = document.getRootElement();
		checkVersion(process_element);
		initializeBPELTools(process_element);
		List<IMetric> metrics = metricManager.getMetrics();
		saveOriginalBPELElements(metrics, process_element);
		executeInstrumentation(metrics);
		createReportInvokesFromCoverageLabels(process_element);
		logger.info("Instrumentation sucessfully completed.");
		return document;
	}

	// private List<Element> getActivitiesForMetric(Element process_element,
	// IMetric metric) {
	// List<Element> list = null;
	// if (metric.getName().equals(ActivityMetric.METRIC_NAME)) {
	//
	// list = getBasisActivities(process_element, metric.getConfigInfo());
	// } else if (metric.getName().equals(BranchMetric.METRIC_NAME)) {
	// list = getStructuredActivities(process_element);
	// } else if (metric.getName().equals(LinkMetric.METRIC_NAME)) {
	// list = getLinks(process_element);
	// } else if (metric.getName().equals(FaultMetric.METRIC_NAME)) {
	// list = getFaultHandlers(process_element);
	// } else if (metric.getName().equals(CompensationMetric.METRIC_NAME)) {
	// list = getCompensationHandlers(process_element);
	// }
	// return list;
	// }

	private void saveOriginalBPELElements(List<IMetric> metrics,
			Element process_element) {
		IMetric metric;
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			metric = iter.next();
			metric.setOriginalBPELDocument(process_element);
		}
	}

	private void checkVersion(Element process_element)
			throws BpelVersionException {
		Namespace processNamespace = process_element.getNamespace();
		if (!processNamespace.equals(NAMESPACE_BPEL_1_1)
				&& !processNamespace.equals(NAMESPACE_BPEL_2_0)) {
			throw (new BpelVersionException(BpelVersionException.WRONG_VERSION
					+ ". Version " + processNamespace.getURI()
					+ "is not supported. Expected "
					+ NAMESPACE_BPEL_1_1.getURI() + " or "
					+ NAMESPACE_BPEL_2_0.getURI()));
		}
	}

	private void initializeBPELTools(Element process_element)
			throws BpelVersionException {
		BpelXMLTools.process_element = process_element;
		BasicActivities.initialize();
		StructuredActivities.initialize();
	}

	// *********************** ****************************************

	private void executeInstrumentation(List<IMetric> metrics)
			throws BpelException {
		IMetric metric;
		// List<Element> activities;
		// for (Iterator<IMetric> i = metrics.iterator(); i.hasNext();) {
		// metric = i.next();
		// activities = getActivitiesForMetric(process_element, metric);
		// if (activities != null) {
		// metric.getHandler().insertMarkersForMetric(activities);
		// }
		// }

		for (Iterator<IMetric> i = metrics.iterator(); i.hasNext();) {
			metric = i.next();
			metric.insertMarkers();
		}
	}

	private void createReportInvokesFromCoverageLabels(Element process_element) {
		cmServiceFactory = CMServiceFactory.getInstance();
		cmServiceFactory.prepareBPELFile(process_element);
		handleCoverageLabelsInElement(process_element, null);
		// insertLastReportInvoke(process_element);
	}

	private void handleCoverageLabelsInElement(Element element, String variable) {
		// logger.info("CoverageTool:MetricHandler.Es wird in "+
		// element.getName() + " Aktivität Invokes eingefügt");
		List<Element> childElements = new ArrayList<Element>();
		List children = element.getContent(new ContentFilter(
				ContentFilter.ELEMENT));
		for (int i = 0; i < children.size(); i++) {
			childElements.add((Element) children.get(i));
		}
		replaceCoverageLabelsWithReportInvokes(element, variable);
		boolean isFlow = element.getName().equals(
				StructuredActivities.FLOW_ACTIVITY);
		for (int i = 0; i < childElements.size(); i++) {
			if (isFlow) {
				handleCoverageLabelsInElement(childElements.get(i),
						createVariableName());
			} else {
				handleCoverageLabelsInElement(childElements.get(i), null);
			}
		}

	}

	private void replaceCoverageLabelsWithReportInvokes(Element element,
			String variable) {
		List children;
		children = element.getContent(new ContentFilter(ContentFilter.COMMENT));
		int indexOfLastMarker = -1;
		int index;
		Comment comment;
		String marker = "";
		String commentText;
		for (int i = children.size() - 1; i > -1; i--) {
			comment = (Comment) children.get(i);
			index = element.indexOf(comment);
			commentText = comment.getText();
			if (isCoverageLabel(commentText)) {

				// logger.info("CoverageTool:Metrichandler:CoverageLabel
				// gefunden."+ commentText);
				if (indexOfLastMarker - 1 == index) {
					marker = marker
							+ getLabel(commentText, COVERAGE_LABEL_IDENTIFIER)
							+ SEPARATOR;
				} else {
					if (marker.length() > 0) {
						insertInvokeForLabels(marker, indexOfLastMarker,
								element, variable);
					}
					marker = getLabel(commentText, COVERAGE_LABEL_IDENTIFIER)
							+ SEPARATOR;
				}
				comment.detach();
			}
			indexOfLastMarker = index;
			if (i == 0 && marker.length() > 0) {
				insertInvokeForLabels(marker, indexOfLastMarker, element,
						variable);
			}
		}
		// logger.info("CoverageTool:Metrichandler. In " + element.getName()+ "
		// alle Invokes eingefügt");
	}

	private void insertInvokeForLabels(String marker, int index,
			Element element, String variable) {

		// logger.info("CoverageTool:Metrichandler:Invoke für label " + marker+
		// " wird eingefügt.");
		try {
			if (variable == null) {
				variable = assignVariable;
			}
			Element assign = cmServiceFactory.createAssignElement(marker,
					variable);
			Element invoke = cmServiceFactory
					.createInvokeElementForLog(variable);
			element.addContent(index, invoke);
			element.addContent(index, assign);

			// logger.info("CoverageTool:Metrichandler:Invoke für label " +
			// marker+ " wurde eingefügt.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private String getLabel(String complettLabel, String identifier) {
		int startIndex = complettLabel.indexOf(identifier)
				+ identifier.length();
		return complettLabel.substring(startIndex);

	}

	private boolean isCoverageLabel(String label) {
		if (label.startsWith(COVERAGE_LABEL_IDENTIFIER)) {
			return true;
		}
		return false;
	}

}
