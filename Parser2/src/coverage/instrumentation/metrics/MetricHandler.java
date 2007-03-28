package coverage.instrumentation.metrics;

import static coverage.instrumentation.bpelxmltools.BpelXMLTools.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ContentFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.CoverageConstants;
import coverage.exception.BpelException;
import coverage.exception.BpelVersionException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.bpelxmltools.exprlang.ExpressionLanguage;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import coverage.wstools.CMServiceFactory;
import coverage.wstools.CoverageRegistry;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileWriter;

/**
 * Dieses Interface wird von dem Handler implementiert, der dafür zuständig ist,
 * die Instrumentierung der BPEL-Datei zu starten und dabei nur die gewünschten
 * Metriken zu berücksichtigen.
 * 
 * @author Alex Salnikow
 * 
 */
public class MetricHandler {

	public static final String SEPARATOR = "#";

	public static final String STOP_FLAG = "STOP";

	public static final String STATEMENT_METRIC = Statementmetric.METRIC_NAME;

	public static final String BRANCH_METRIC = BranchMetric.METRIC_NAME;

	private HashMap<String, IMetric> metrics;

	private Element process_element;

	private Logger logger;

	private CMServiceFactory cmServiceFactory;

	private String assignVariable = createVariableName();

	public MetricHandler() {
		metrics = new HashMap<String, IMetric>();
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Die übergebene Metrik wird bei der Ausführung der BPEL erhoben:
	 * 
	 * @param metricName
	 */
	public void addMetric(IMetric metric) {
		metrics.put(metric.getName(), metric);
	}

	/**
	 * Die Metrik wird bei der Ausführung der BPEL nicht erhoben.
	 * 
	 * @param metricName
	 */
	public void remove(String metricName) {
		metrics.remove(metricName);
	}

	/**
	 * Startet die Instrumentierung der BPEL-Datei.
	 * 
	 * @param BPELFile
	 * @throws BpelException
	 */
	public void executeInstrumentation(File BPELFile) throws BpelException {
		logger.info("Instrumentation of file " + BPELFile.getName()
				+ " is started.");
		Document doc = readBPELDocument(BPELFile);
		process_element = doc.getRootElement();
		valideBPELDoc(process_element);
		BpelXMLTools.process_element = process_element;
		// insertImportElementForLogWSDL();
		executeInstrumentation();
		createReportInvokesFromCoverageLabels();
		writeBPELDocument(BPELFile, doc);
		logger.info("Instrumentation sucessfully completed.");

	}

	private Document readBPELDocument(File file) throws BpelException {
		FileInputStream is = null;
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			is = new FileInputStream(file);

			doc = builder.build(is);
		} catch (IOException e) {
			throw new BpelException(
					"An I/O error occurred when reading the BPEL file: "
							+ file.getName(), e);
		} catch (JDOMException e) {
			throw new BpelException(
					"An XML reading error occurred reading the BPEL file "
							+ file.getName(), e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return doc;
	}

	private void valideBPELDoc(Element process_element) throws BpelException,
			BpelVersionException {
		if (!process_element.getName().equalsIgnoreCase(
				PROCESS_ELEMENT)) {

			throw (new BpelException(BpelException.NO_VALIDE_BPEL));

		}
		// TODO
		// if (!process_element.getNamespace().equals(
		// BpelXMLTools.NAMESPACE_BPEL_2)) {
		// throw (new BpelVersionException(BpelVersionException.WRONG_VERSION));
		// }
	}

	private void executeInstrumentation() throws BpelException {
		CoverageRegistry registry = CoverageRegistry.getInstance();
		registry.initialize();
		IMetric metric;
		for (Iterator<IMetric> i = metrics.values().iterator(); i.hasNext();) {
			metric = i.next();
			logger.info(metric);
			registry.addMetric(metric);
			metric.insertCoverageLabels(process_element);
		}
	}

	private void writeBPELDocument(File file, Document doc)
			throws BpelException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);

			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
		} catch (IOException e) {

			throw new BpelException(
					"An I/O error occurred when writing the BPEL file: "
							+ file.getName(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// private void insertImportElementForLogWSDL() {
	// Element importElement = new Element("import", BpelXMLTools
	// .getBpelNamespace());
	// importElement.setAttribute("importType",
	// "http://schemas.xmlsoap.org/wsdl/");
	// importElement.setAttribute("location", "../wsdl/_LogService_.wsdl");
	// importElement.setAttribute("namespace",
	// "http://www.bpelunit.org/coverage/logService");
	// process_element.addContent(0, importElement);
	// }

	private void createReportInvokesFromCoverageLabels() {
		cmServiceFactory = CMServiceFactory.getInstance();
		handleCoverageLabelsInElement(process_element, null);
		insertLastReportInvoke(process_element);
	}

	private void insertLastReportInvoke(Element process_element) {
		Element sequenceElement = createSequence();
		Element activity = getFirstEnclosedActivity(process_element);
		sequenceElement.addContent(activity.detach());
		insertInvokeForLabels(STOP_FLAG, sequenceElement.getContentSize(),
				sequenceElement, null);
		process_element.addContent(sequenceElement);
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
				if (indexOfLastMarker - 1 == index) {
					marker = marker
							+ getLabel(commentText,
									IMetric.COVERAGE_LABEL_IDENTIFIER)
							+ SEPARATOR;
				} else {
					if (marker.length() > 0) {
						insertInvokeForLabels(marker, indexOfLastMarker,
								element, variable);
					}
					marker = getLabel(commentText,
							IMetric.COVERAGE_LABEL_IDENTIFIER)
							+ SEPARATOR;
				}
				comment.detach();
			} else if (isDynamicLabelInForEach(commentText)) {
				insertInvokesForDynamicLabel(getLabel(commentText,
						IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER), comment,
						variable);
			}
			indexOfLastMarker = index;
			if (i == 0 && marker.length() > 0) {
				insertInvokeForLabels(marker, indexOfLastMarker, element,
						variable);
			}
		}
	}

	private void insertInvokesForDynamicLabel(String content, Comment comment,
			String variableName) {
		String[] strings = parseLabel(content);
		String variable = createVariableName();
		Element scope = getSurroundScope((Content) comment);
		if (scope == null) {
			// TODO new Exception
		}
		Element assign = cmServiceFactory.createAssignElementForRegisterMarker(
				scope, 
				ExpressionLanguage.getInstance(CoverageConstants.EXPRESSION_LANGUAGE).concat(strings), variable);
		Element invoke = cmServiceFactory
				.createInvokeElementForRegisterMarker(variable);
		Element parent = comment.getParentElement();
		int index = parent.indexOf(comment);
		parent.addContent(index, assign);
		parent.addContent(index + 1, invoke);
		comment.detach();
	}

	private String[] parseLabel(String content) {
		Scanner scanner = new Scanner(content);
		scanner.useDelimiter(MetricHandler.SEPARATOR);
		String marker = null;
		String countVariable = null;
		String entity;
		for (int i = 0; scanner.hasNext() && i < 5; i++) {
			entity = scanner.next().trim();
			switch (i) {
			case 0:
				marker = entity;
				break;
			case 1:
				countVariable = entity;
				break;
			}
		}
		if (countVariable == null || marker == null) {
			// TODO new Exception
		}
		String[] strings = new String[2];
		strings[0] = '\'' + marker + '\'';
		strings[1] = ExpressionLanguage.getInstance(CoverageConstants.EXPRESSION_LANGUAGE).valueOf(countVariable);
		return strings;
	}

	private boolean isDynamicLabelInForEach(String label) {
		if (label.startsWith(IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER)) {
			return true;
		}
		return false;
	}

	private void insertInvokeForLabels(String marker, int index,
			Element element, String variable) {
		if (variable == null) {
			variable = assignVariable;
		}
		Element assign = cmServiceFactory.createAssignElement(marker, variable);
		Element invoke = cmServiceFactory.createInvokeElementForLog(variable);
		element.addContent(index, invoke);
		element.addContent(index, assign);
	}

	private String getLabel(String complettLabel, String identifier) {
		int startIndex = complettLabel.indexOf(identifier)
				+ identifier.length();
		return complettLabel.substring(startIndex);

	}

	private boolean isCoverageLabel(String label) {
		if (label.startsWith(IMetric.COVERAGE_LABEL_IDENTIFIER)) {
			return true;
		}
		return false;
	}

}
