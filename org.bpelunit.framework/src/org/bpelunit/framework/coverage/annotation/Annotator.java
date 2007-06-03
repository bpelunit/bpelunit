package org.bpelunit.framework.coverage.annotation;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.CATCHALL_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.CATCH_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.COMPENSATION_HANDLER;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createVariableName;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.isStructuredActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.activitycoverage.ActivityMetric;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetricHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivity;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.CMServiceFactory;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivity;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.exceptions.BpelVersionException;
import org.bpelunit.framework.exception.ConfigurationException;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.ElementFilter;

/**
 * Dieses Interface wird von dem Handler implementiert, der dafür zuständig ist,
 * die Instrumentierung der BPEL-Datei zu starten und dabei nur die gewünschten
 * Metriken zu berücksichtigen.
 * 
 * @author Alex Salnikow
 * 
 */
public class Annotator {

	public static final String COVERAGE_LABEL_IDENTIFIER = "@coverageLabel";

	public static final char COVERAGE_LABEL_INNER_SEPARATOR = '_';

	public static final String SEPARATOR = "#";

	public static final String STOP_FLAG = "STOP";



	private Logger logger;

	private CMServiceFactory cmServiceFactory;

	private String assignVariable = createVariableName();

	public Annotator() {
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Startet die Instrumentierung der BPEL-Datei.
	 * 
	 * @param document
	 * @param metrics
	 * @throws BpelException
	 * @throws ConfigurationException
	 */
	public Document insertAnnotation(Document document) throws BpelException {
		Element process_element = document.getRootElement();
		checkVersion(process_element);
		initializeBPELTools(process_element);
		List<IMetric> metrics = MetricsManager.getInstance().getMetrics();
		executeInstrumentation(metrics,process_element);
		createReportInvokesFromCoverageLabels(process_element);
		logger.info("Instrumentation sucessfully completed.");
		return document;
	}

	private List<Element> getActivitiesForMetric(Element process_element,
			IMetric metric) {
		List<Element> list = null;
		if (metric.getName().equals(ActivityMetric.METRIC_NAME)) {

			list = getBasisActivities(process_element, metric.getConfigInfo());
		} else if (metric.getName().equals(BranchMetric.METRIC_NAME)) {
			list = getStructuredActivities(process_element);
		} else if (metric.getName().equals(LinkMetric.METRIC_NAME)) {
			list = getLinks(process_element);
		} else if (metric.getName().equals(FaultMetric.METRIC_NAME)) {
			list = getFaultHandlers(process_element);
		} else if (metric.getName().equals(CompensationMetric.METRIC_NAME)) {
			list = getCompensationHandlers(process_element);
		}
		return list;
	}

	private List<Element> getCompensationHandlers(Element process_element) {
		Iterator<Element> compensHandlers = process_element
				.getDescendants(new ElementFilter(COMPENSATION_HANDLER,
						getProcessNamespace()));
		List<Element> elements_to_log = new ArrayList<Element>();
		while (compensHandlers.hasNext()) {
			elements_to_log.add(compensHandlers.next());
		}
		return elements_to_log;
	}

	private List<Element> getFaultHandlers(Element process_element) {
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

		List<Element> elements_to_log = new ArrayList<Element>();
		for (Iterator<Element> iter = process_element.getDescendants(filter); iter
				.hasNext();) {
			elements_to_log.add(iter.next());

		}
		return elements_to_log;
	}

	private List<Element> getLinks(Element process_element) {

		List<Element> elements_to_log = new ArrayList<Element>();
		Iterator<Element> iter = process_element
				.getDescendants(new ElementFilter(LinkMetricHandler.LINK_TAG,
						process_element.getNamespace()));
		while (iter.hasNext()) {
			elements_to_log.add(iter.next());
		}
		return elements_to_log;
	}

	private List<Element> getStructuredActivities(Element process_element) {
		Element next_element;
		Iterator<Element> iter = process_element
				.getDescendants(new ElementFilter(process_element
						.getNamespace()));
		List<Element> elements_to_log = new ArrayList<Element>();
		while (iter.hasNext()) {
			next_element = iter.next();
			if (isStructuredActivity(next_element)) {
				elements_to_log.add(next_element);
			}
		}
		return elements_to_log;
	}

	private List<Element> getBasisActivities(Element process_element,
			List<String> activitiesToRespect) {

		ElementFilter filter = new ElementFilter(process_element.getNamespace());
		List<Element> elements_to_log = new ArrayList<Element>();
		for (Iterator<Element> iter = process_element.getDescendants(filter); iter
				.hasNext();) {
			Element basicActivity = iter.next();
			if (activitiesToRespect.contains(basicActivity.getName()))
				elements_to_log.add(basicActivity);
		}
		return elements_to_log;
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
		BpelXMLTools.process_element=process_element;
		BasicActivity.initialize();
		StructuredActivity.initialize();
	}

	// *********************** ****************************************


	private void executeInstrumentation(List<IMetric> metrics,Element process_element)
			throws BpelException {
		IMetric metric;
		List<Element> activities;
		for (Iterator<IMetric> i = metrics.iterator(); i.hasNext();) {
			metric = i.next();
			activities = getActivitiesForMetric(process_element, metric);
			if (activities != null) {
				metric.getHandler().insertCoverageLabels(activities);
			}
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
				StructuredActivity.FLOW_ACTIVITY);
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
