package net.bpelunit.framework.coverage.annotation;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createVariableName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bpelunit.framework.coverage.annotation.metrics.IMetric;
import net.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import net.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.CMServiceFactory;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.exceptions.BpelVersionException;
import net.bpelunit.util.JDomUtil;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

/**
 * Class responsible for preparations and start of instrumentation
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public class Instrumenter {

	public static final String COVERAGE_LABEL_IDENTIFIER = "@coverageLabel";

	public static final char COVERAGE_LABEL_INNER_SEPARATOR = '_';

	public static final String SEPARATOR = "#";

	private CMServiceFactory cmServiceFactory;

	private String assignVariable = createVariableName();

	public Instrumenter() {
		// logger = Logger.getLogger(getClass());
	}

	/*
	 * F�hrt die Instrumentierung der BPEL-Datei durch.
	 * 
	 * @param document BPEL-Prozess
	 * 
	 * @param metricManager
	 * 
	 * @return instrumentierter BPEL-Prozess
	 * 
	 * @throws BpelException
	 */
	/**
	 * Executes instrumentation of the BPEL file
	 * 
	 * @param document
	 *            BPEL process
	 * @param metricManager
	 * @return instrumentated BPEL-Prozess
	 * @throws BpelException
	 */
	public Document insertAnnotations(Document document,
			MetricsManager metricManager) throws BpelException {
		Element process_element = document.getRootElement();
		checkVersion(process_element);
		initializeBPELTools(process_element);
		if (metricManager.hasMetric(FaultMetric.METRIC_NAME)
				|| metricManager.hasMetric(CompensationMetric.METRIC_NAME)) {
			replaceInlineHandler(process_element);
		}
		List<IMetric> metrics = metricManager.getMetrics();
		saveOriginalBPELElements(metrics, process_element);
		executeInstrumentation(metrics);
		createReportInvokesFromCoverageLabels(process_element);
		return document;
	}

	/*
	 * Ersetzt die inline-Handler durch explizite Scopes
	 * 
	 * @param process_element
	 */
	/**
	 * Replaces inline header with explicit scopes
	 * 
	 * @param process
	 *            element
	 */
	@SuppressWarnings("serial")
	private void replaceInlineHandler(Element process_element) {
		Iterator<Element> iter = JDomUtil.getDescendants(process_element,
				new ElementFilter(BasicActivities.INVOKE_ACTIVITY,
						process_element.getNamespace()) {

					@Override
					public boolean matches(Object o) {
						if (super.matches(o)) {
							Element invoke = (Element) o;
							List<Element> children = JDomUtil.getChildren(
									invoke, BpelXMLTools.CATCH_ELEMENT);
							if (children.size() > 0) {
								return true;
							}
							children = JDomUtil.getChildren(invoke,
									BpelXMLTools.CATCHALL_ELEMENT);
							if (children.size() > 0) {
								return true;
							}
							children = JDomUtil.getChildren(invoke,
									BpelXMLTools.COMPENSATION_HANDLER);
							if (children.size() > 0) {
								return true;
							}
						}
						return false;
					}

				});
		while (iter.hasNext()) {
			replaceInlineHandlerForInvoke(iter.next());
		}
	}

	private void replaceInlineHandlerForInvoke(Element element) {
		Element scope = null;
		List<Element> inlineElements = JDomUtil.getChildren(element,
				BpelXMLTools.CATCH_ELEMENT);
		List<Element> inlineElements2 = JDomUtil.getChildren(element,
				BpelXMLTools.CATCHALL_ELEMENT);
		if (inlineElements.size() > 0 || inlineElements2.size() > 0) {
			scope = BpelXMLTools
					.createBPELElement(StructuredActivities.SCOPE_ACTIVITY);
			Element faultHandler = BpelXMLTools
					.createBPELElement(BpelXMLTools.FAULT_HANDLERS);
			scope.addContent(faultHandler);
			for (Iterator<Element> iter = inlineElements.iterator(); iter
					.hasNext();) {
				faultHandler.addContent(iter.next().detach());
			}
			if (inlineElements2.size() > 0) {
				faultHandler.addContent(inlineElements2.get(0).detach());
			}
		}

		inlineElements = JDomUtil.getChildren(element,
				BpelXMLTools.COMPENSATION_HANDLER);
		if (inlineElements.size() > 0) {
			if (scope == null) {
				scope = BpelXMLTools
						.createBPELElement(StructuredActivities.SCOPE_ACTIVITY);
			}
			scope.addContent(inlineElements.get(0).detach());
		}
		if (scope != null) {
			int i = element.getParentElement().indexOf(element);
			scope.setContent(i, scope);
			scope.addContent(element);
		}

	}

	/**
	 * Saves the process elements needed by the metrics
	 * 
	 * @param metrics
	 * @param process
	 *            element
	 */
	private void saveOriginalBPELElements(List<IMetric> metrics,
			Element process_element) {
		IMetric metric;
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			metric = iter.next();
			metric.setOriginalBPELProcess(process_element);
		}
	}

	/**
	 * Check BPEL version
	 * 
	 * @param process
	 *            element
	 * @throws BpelVersionException
	 */
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

	/**
	 * Exercices initializing of the tools
	 * 
	 * @param process
	 *            element
	 * @throws BpelVersionException
	 */
	private void initializeBPELTools(Element process_element)
			throws BpelVersionException {
		BpelXMLTools.processElement = process_element;
		BasicActivities.initialize();
		StructuredActivities.initialize();
	}

	/**
	 * Starts the process of instrumentating for each metric
	 * 
	 * @param metrics
	 * @throws BpelException
	 */
	private void executeInstrumentation(List<IMetric> metrics)
			throws BpelException {
		IMetric metric;
		for (Iterator<IMetric> i = metrics.iterator(); i.hasNext();) {
			metric = i.next();
			metric.insertMarkers();
		}
	}

	/**
	 * Inserts invoke and assign activities after annotating the process for
	 * included markings
	 * 
	 * @param process_element
	 *            BPEL process root element
	 */
	private void createReportInvokesFromCoverageLabels(Element process_element) {
		cmServiceFactory = CMServiceFactory.getInstance();
		cmServiceFactory.prepareBPELFile(process_element);
		handleCoverageLabelsInElement(process_element, null);
	}

	private void handleCoverageLabelsInElement(Element element, String variable) {
		List<Element> childElements = new ArrayList<Element>();
		List<Element> children = JDomUtil.getElementsInContent(element);
		for (int i = 0; i < children.size(); i++) {
			childElements.add(children.get(i));
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
		List<Comment> children;
		children = JDomUtil.getCommentsInContent(element);
		int indexOfLastMarker = -1;
		int index;
		Comment comment;
		String marker = "";
		String commentText;
		for (int i = children.size() - 1; i > -1; i--) {
			comment = children.get(i);
			index = element.indexOf(comment);
			commentText = comment.getText();
			if (isCoverageLabel(commentText)) {
				if (indexOfLastMarker - 1 == index) {
					marker = marker
							+ getMarker(commentText, COVERAGE_LABEL_IDENTIFIER)
							+ SEPARATOR;
				} else {
					if (marker.length() > 0) {
						insertInvokeForLabels(marker, indexOfLastMarker,
								element, variable);
					}
					marker = getMarker(commentText, COVERAGE_LABEL_IDENTIFIER)
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
	}

	private void insertInvokeForLabels(String marker, int index,
			Element element, String variable) {

		try {
			if (variable == null) {
				variable = assignVariable;
			}
			Element assign = cmServiceFactory.createAssignElement(marker,
					variable);
			Element invoke = cmServiceFactory
					.createInvokeElementForLoggingService(variable);
			element.addContent(index, invoke);
			element.addContent(index, assign);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Extracts the marking from the comment
	 * 
	 * @param complettLabel
	 * @param identifier
	 * @return marker
	 */
	private String getMarker(String complettLabel, String identifier) {
		int startIndex = complettLabel.indexOf(identifier)
				+ identifier.length();
		return complettLabel.substring(startIndex);

	}

	/**
	 * Checks whether comment is a coverage marking
	 * 
	 * @param label
	 * @return yes, iff comment is a marker
	 */
	private boolean isCoverageLabel(String label) {
		return (label.startsWith(COVERAGE_LABEL_IDENTIFIER));
	}

}
