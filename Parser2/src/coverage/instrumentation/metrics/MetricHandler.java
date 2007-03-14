package coverage.instrumentation.metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
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

import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import coverage.loggingservice.CoverageRegestry;
import coverage.loggingservice.LoggingServiceConfiguration;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileWriter;
import exception.BpelException;
import exception.BpelVersionException;

/**
 * Die Klasse implementiert das Interface IMetricHandler.
 * 
 * @author Alex Salnikow
 */
public class MetricHandler implements IMetricHandler {

	public static final String MARKER_SEPARATOR = "#";

	public static final String STOP_FLAG = "STOP";

	private HashMap<String, IMetric> metrics;

	private Element process_element;

	private Logger logger;

	private LoggingServiceConfiguration configLogService;

	public MetricHandler() {
		metrics = new HashMap<String, IMetric>();
		logger = Logger.getLogger(getClass());
		;
	}

	public IMetric addMetric(String metricName) {
		IMetric metric = null;
		if (metricName.equals(STATEMENT_METRIC)) {
			metric = new Statementmetric();
			metrics.put(STATEMENT_METRIC, metric);
		} else if (metricName.equals(BRANCH_METRIC)) {
			metric = new BranchMetric();
			metrics.put(BRANCH_METRIC, metric);
		}
		return metric;
	}

	public void remove(String metricName) {
		metrics.remove(metricName);
	}

	public void startInstrumentation(File file) throws BpelException {
		FileWriter writer = null;
		FileInputStream is = null;
		try {
			logger.info("Instrumentation of file " + file.getName()
					+ " is started.");
			SAXBuilder builder = new SAXBuilder();
			is = new FileInputStream(file);
			Document doc = builder.build(is);
			process_element = doc.getRootElement();
			if (!process_element.getName().equalsIgnoreCase(
					BpelXMLTools.PROCESS_ELEMENT)) {

				throw (new BpelException(BpelException.NO_VALIDE_BPEL));

			}
			if (!process_element.getNamespace().equals(
					BpelXMLTools.NAMESPACE_BPEL_2)) {
				throw (new BpelVersionException(
						BpelVersionException.WRONG_VERSION));
			}
			BpelXMLTools.process_element = process_element;
			insertImportElementForLogWSDL();

			IMetric metric;
			for (Iterator<IMetric> i = metrics.values().iterator(); i.hasNext();) {
				metric = i.next();
				logger.info(metric);
				CoverageRegestry.getInstance().addMetric(metric);
				metric.insertMarker(process_element);
			}
			insertInvokesForMarker();
			writer = new FileWriter(file);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
			logger.info("Instrumentation sucessfully completed.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BpelException("", e);
		} catch (BpelVersionException e) {
			// TODO Auto-generated catch block
			throw new BpelException("", e);
		} catch (JDOMException e) {
			throw new BpelException("BPEL-file "
					+ FilenameUtils.getName(file.getName())
					+ " can not be parsed.", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
		configLogService = new LoggingServiceConfiguration("", process_element);
		analyzeDirectChildren(process_element);
//		insertLastInvoke(process_element);

	}

	private void insertLastInvoke(Element process_element) {
		Element sequenceElement = BpelXMLTools.createSequence();
		List children = process_element.getContent();
		List<Content> childList = new ArrayList<Content>();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			childList.add((Content) iter.next());
		}

		for (Iterator<Content> iter = childList.iterator(); iter.hasNext();) {
			sequenceElement.addContent(iter.next().detach());
		}
		insertInvokeForMarker(STOP_FLAG, sequenceElement.getContentSize(),
				sequenceElement);
		process_element.addContent(sequenceElement);
	}

	private void analyzeDirectChildren(Element element) {
		List<Element> childElements = new ArrayList<Element>();
		List children = element.getContent(new ContentFilter(
				ContentFilter.ELEMENT));
		for (int i = 0; i < children.size(); i++) {
			childElements.add((Element) children.get(i));
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
								element);
					}
					marker = getMarker(commentText) + MARKER_SEPARATOR;
				}
				comment.detach();
			}
			indexOfLastMarker = index;
			if (i == 0 && marker.length() > 0) {
				insertInvokeForMarker(marker, indexOfLastMarker, element);
			}
		}
		for (int i = 0; i < childElements.size(); i++) {
			analyzeDirectChildren(childElements.get(i));
		}

	}

	private void insertInvokeForMarker(String marker, int index, Element element) {
		Element assign = configLogService.createAssignElement(marker);
		Element invoke = configLogService.createInvokeElement();
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

	public IMetric getMetric(String metricName) {
		IMetric metric = metrics.get(metricName);
		return metric;
	}

}
