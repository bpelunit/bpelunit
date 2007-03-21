package coverage.instrumentation.metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ContentFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.exception.BpelException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
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

	public static final String MARKER_SEPARATOR = "#";

	public static final String STOP_FLAG = "STOP";

	public static final String STATEMENT_METRIC = Statementmetric.METRIC_NAME;

	public static final String BRANCH_METRIC = BranchMetric.METRIC_NAME;

	public static final String MARKER_IDENTIFIRE = "@marker";

	private HashMap<String, IMetric> metrics;

	private Element process_element;

	private Logger logger;

	private CMServiceFactory cmServiceFactory;
	
	private String assignVariable=BpelXMLTools.createVariableName();

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
	 * @param file
	 * @throws BpelException
	 */
	public void startInstrumentation(File file) throws BpelException {
		FileWriter writer = null;
		FileInputStream is = null;
		// try {
		logger.info("Instrumentation of file " + file.getName()
				+ " is started.");
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
		process_element = doc.getRootElement();
		if (!process_element.getName().equalsIgnoreCase(
				BpelXMLTools.PROCESS_ELEMENT)) {

			throw (new BpelException(BpelException.NO_VALIDE_BPEL));

		}
		// if (!process_element.getNamespace().equals(
		// BpelXMLTools.NAMESPACE_BPEL_2)) {
		// throw (new BpelVersionException(BpelVersionException.WRONG_VERSION));
		// }
		BpelXMLTools.process_element = process_element;
		insertImportElementForLogWSDL();
		CoverageRegistry registry = CoverageRegistry.getInstance();
		registry.initialize();
		IMetric metric;
		for (Iterator<IMetric> i = metrics.values().iterator(); i.hasNext();) {
			metric = i.next();
			logger.info(metric);
			registry.addMetric(metric);
			metric.insertMarker(process_element);
		}
		insertInvokesForMarker();
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
		logger.info("Instrumentation sucessfully completed.");

	}

	private void insertImportElementForLogWSDL() {
		Element importElement = new Element("import", BpelXMLTools
				.getBpelNamespace());
		importElement.setAttribute("importType",
				"http://schemas.xmlsoap.org/wsdl/");
		importElement.setAttribute("location", "../wsdl/_LogService_.wsdl");
		importElement.setAttribute("namespace",
				"http://www.bpelunit.org/coverage/logService");
		process_element.addContent(0, importElement);
	}

	private void insertInvokesForMarker() {
		cmServiceFactory = new CMServiceFactory(process_element);
		analyzeDirectChildren(process_element, null);
		insertLastInvoke(process_element);

	}

	private void insertLastInvoke(Element process_element) {
		Element sequenceElement = BpelXMLTools.createSequence();
		Element activity = BpelXMLTools.getFirstActivityChild(process_element);
		sequenceElement.addContent(activity.detach());
		insertInvokeForMarker(STOP_FLAG, sequenceElement.getContentSize(),
				sequenceElement, null);
		process_element.addContent(sequenceElement);
	}

	private void analyzeDirectChildren(Element element, String variable) {
		List<Element> childElements = new ArrayList<Element>();
		List<String> variables = null;
		boolean isFlow = element.getName().equals(
				StructuredActivity.FLOW_ACTIVITY);
		if (isFlow) {
			// Variablen für assign und Invoke in einer Flow
			variables = new ArrayList<String>();
		}

		List children = element.getContent(new ContentFilter(
				ContentFilter.ELEMENT));
		for (int i = 0; i < children.size(); i++) {
			childElements.add((Element) children.get(i));
			if (isFlow) {
				variables.add(BpelXMLTools.createVariableName());
			}
		}
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
			if (isMarker(commentText)) {
				if (indexOfLastMarker - 1 == index) {
					marker = marker + getMarker(commentText) + MARKER_SEPARATOR;
				} else {
					if (marker.length() > 0) {
						insertInvokeForMarker(marker, indexOfLastMarker,
								element, variable);
					}
					marker = getMarker(commentText) + MARKER_SEPARATOR;
				}
				comment.detach();
			}
			indexOfLastMarker = index;
			if (i == 0 && marker.length() > 0) {
				insertInvokeForMarker(marker, indexOfLastMarker, element,
						variable);
			}
		}
		for (int i = 0; i < childElements.size(); i++) {
			if (isFlow) {
				analyzeDirectChildren(childElements.get(i), variables.get(i));
			} else {

				analyzeDirectChildren(childElements.get(i), null);
			}
		}

	}

	private void insertInvokeForMarker(String marker, int index,
			Element element, String variable) {
		if(variable==null){
			variable=assignVariable;
		}
		Element assign = cmServiceFactory.createAssignElement(marker, variable);
		Element invoke = cmServiceFactory.createInvokeElement(variable);
		element.addContent(index, invoke);
		element.addContent(index, assign);
	}

	private String getMarker(String commentText) {
		int startIndex = commentText.indexOf(IMetric.MARKER_IDENTIFIRE)
				+ IMetric.MARKER_IDENTIFIRE.length();
		return commentText.substring(startIndex);

	}

	private boolean isMarker(String commentText) {
		if (commentText.startsWith(IMetric.MARKER_IDENTIFIRE)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param metricName
	 * @return Metrik
	 */
	public IMetric getMetric(String metricName) {
		IMetric metric = metrics.get(metricName);
		return metric;
	}

}
