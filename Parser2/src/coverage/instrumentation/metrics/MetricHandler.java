package coverage.instrumentation.metrics;

import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
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

	private static final Namespace NAMESPACE_BPEL_2 = Namespace
			.getNamespace("http://schemas.xmlsoap.org/ws/2003/03/business-process/");

	private static IMetricHandler instance = null;

	private Hashtable metrics;

	private Element process_element;

	private Logger logger;

	public static IMetricHandler getInstance() {
		if (instance == null) {
			instance = new MetricHandler();
		}
		return instance;
	}

	private MetricHandler() {
		metrics = new Hashtable();
		logger = Logger.getLogger(getClass());
		SimpleLayout layout = new SimpleLayout();
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		logger.addAppender(consoleAppender);
		FileAppender fileAppender;
		try {
			fileAppender = new FileAppender(layout, "MeineLogDatei.log", false);
			logger.addAppender(fileAppender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IMetric addMetric(String metricName) {
		IMetric metric = null;
		if (metricName.equals(STATEMENT_METRIC)) {
			metric = Statementmetric.getInstance();
			metrics.put(STATEMENT_METRIC, metric);
		} else if (metricName.equals(BRANCH_METRIC)) {
			metric = BranchMetric.getInstance();
			metrics.put(BRANCH_METRIC, metric);
		}
		return metric;
	}

	public void remove(String metricName) {
		if (metricName.equals(STATEMENT_METRIC)) {
			metrics.remove(Statementmetric.getInstance());
		} else if (metricName.equals(BRANCH_METRIC)) {
			metrics.remove(Statementmetric.getInstance());
		}

	}

	public void startInstrumentation(File file) throws BpelException {
		FileWriter writer = null;
		FileInputStream is = null;
		try {
			logger.info("Die Instrumentierung der Datei " + file.getName()
					+ " wird gestartet.");
			SAXBuilder builder = new SAXBuilder();
			is = new FileInputStream(file);
			Document doc = builder.build(is);
			process_element = doc.getRootElement();
			if (!process_element.getName().equalsIgnoreCase(
					BpelXMLTools.PROCESS_ELEMENT)) {

				throw (new BpelException(BpelException.NO_VALIDE_BPEL));

			}
			if (!process_element.getNamespace().equals(NAMESPACE_BPEL_2)) {
				throw (new BpelVersionException(
						BpelVersionException.WRONG_VERSION));
			}
			BpelXMLTools.process_element = process_element;
			
			Element importElement = new Element("import", BpelXMLTools
					.getBpelNamespace());
			importElement.setAttribute("importType",
					"http://schemas.xmlsoap.org/wsdl/");
			importElement.setAttribute("location", "../wsdl/_LogService_.wsdl");
			importElement.setAttribute("namespace",
					"http://www.bpelunit.org/coverage/logService");
			process_element.addContent(0, importElement);
			//		

			

			IMetric metric;
			for (Enumeration<IMetric> i = metrics.elements(); i
					.hasMoreElements();) {
				metric = i.nextElement();
				logger.info(metric);
				metric.insertMarker(process_element);
			}
			// String[] name_analyse = getFileName(file);
			// File instrumentation_file=new
			// File(name_analyse[0]+"_instr_"+name_analyse[1]);
			File instrumentation_file = new File("ergebnis.bpel");
			logger.info("Instrumentierung erfolgreich ausgeführt.");
			// logger.info("Die instrumentierte BPEL-Datei wird in
			// "+instrumentation_file+" geschrieben.");

			// writer = new FileWriter(instrumentation_file);
			// XMLOutputter xmlOutputter = new XMLOutputter(Format
			// .getPrettyFormat());
			// xmlOutputter.output(doc, writer);
			is.close();

			createInvokesForMarker();
			writer = new FileWriter(file);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());
			xmlOutputter.output(doc, writer);
			writer.close();
		} catch (BpelException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BpelException("", e);
		} catch (BpelVersionException e) {
			// TODO Auto-generated catch block
			throw new BpelException("", e);
		} catch (JDOMException e) {
			throw new BpelException("", e);
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




	private void createInvokesForMarker() {
		LoggingServiceConfiguration config=new LoggingServiceConfiguration("",process_element);
		
		
	}

	public IMetric getMetric(String metricName) {
		IMetric metric = null;
		if (metricName.equals(STATEMENT_METRIC)) {
			metric = Statementmetric.getInstance();
		} else if (metricName.equals(BRANCH_METRIC)) {
			metric = BranchMetric.getInstance();
		}
		return metric;
	}

}
