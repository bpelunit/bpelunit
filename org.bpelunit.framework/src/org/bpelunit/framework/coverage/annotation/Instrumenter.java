package org.bpelunit.framework.coverage.annotation;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createVariableName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetricHandler;
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
import org.jdom.filter.ElementFilter;

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
		logger.info("Instrumentation sucessfully completed.");
		return document;
	}

	private void replaceInlineHandler(Element process_element) {
		Iterator<Element> iter = process_element
				.getDescendants(new ElementFilter(
						BasicActivities.INVOKE_ACTIVITY, process_element
								.getNamespace()) {

					@Override
					public boolean matches(Object arg0) {
						if (super.matches(arg0)) {
							Element invoke = (Element) arg0;
							List<Element> children = invoke
									.getChildren(BpelXMLTools.CATCH_ELEMENT);
							if (children.size() > 0)
								return true;
							children = invoke
									.getChildren(BpelXMLTools.CATCHALL_ELEMENT);
							if (children.size() > 0)
								return true;
							children = invoke
									.getChildren(BpelXMLTools.COMPENSATION_HANDLER);
							if (children.size() > 0)
								return true;
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
		List<Element> inlineElements = element
				.getChildren(BpelXMLTools.CATCH_ELEMENT);
		List<Element> inlineElements2 = element
				.getChildren(BpelXMLTools.CATCHALL_ELEMENT);
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
			if(inlineElements2.size()>0){
				faultHandler.addContent(inlineElements2.get(0).detach());
			}
		}
		
		inlineElements=element.getChildren(BpelXMLTools.COMPENSATION_HANDLER);
		if(inlineElements.size()>0){
			if(scope==null)
				scope=BpelXMLTools
				.createBPELElement(StructuredActivities.SCOPE_ACTIVITY);
			scope.addContent(inlineElements.get(0).detach());
		}
		if(scope!=null){
			int i=element.getParentElement().indexOf(element);
			scope.setContent(i, scope);
			scope.addContent(element);
		}

	}

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
		for (Iterator<IMetric> i = metrics.iterator(); i.hasNext();) {
			metric = i.next();
			metric.insertMarkers();
		}
	}

	private void createReportInvokesFromCoverageLabels(Element process_element) {
		cmServiceFactory = CMServiceFactory.getInstance();
		cmServiceFactory.prepareBPELFile(process_element);
		handleCoverageLabelsInElement(process_element, null);
	}

	private void handleCoverageLabelsInElement(Element element, String variable) {
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
			if (isFlow)
				handleCoverageLabelsInElement(childElements.get(i),
						createVariableName());
			else
				handleCoverageLabelsInElement(childElements.get(i), null);
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
					.createInvokeElementForLog(variable);
			element.addContent(index, invoke);
			element.addContent(index, assign);
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
