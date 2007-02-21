package coverage.instrumentation.metrics;

import java.io.File;
import java.io.FileWriter;
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

import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.exception.BpelException;
import coverage.instrumentation.exception.BpelVersionException;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;

/**
 * Die Klasse implementiert das Interface IMetricHandler.
 * 
 * @author Alex Salnikow
 */
public class MetricHandler implements IMetricHandler {
	
	private static final Namespace NAMESPACE_BPEL_2=Namespace
	.getNamespace("http://docs.oasis-open.org/wsbpel/2.0/process/executable");
	
	private static IMetricHandler instance = null;

	private Hashtable metrics;

	private Element process_element;

	private Logger logger;

	public static IMetricHandler getInstance() {
		if (instance == null) {
			instance = new MetricHandler();
		}
		System.out.println("!!!!!!!!!!!!!!!!HAT GEKLAPPT!!!!!!!!!!!!!");
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
		IMetric metric=null;
		if(metricName.equals(STATEMENT_METRIC)){
			metric=Statementmetric.getInstance();
			metrics.put(STATEMENT_METRIC,metric);
		}else if(metricName.equals(BRANCH_METRIC)){
			metric=BranchMetric.getInstance();
			metrics.put(BRANCH_METRIC,metric);
		}
		return metric;
	}
	

	public void remove(String metricName) {
		if(metricName.equals(STATEMENT_METRIC)){
			metrics.remove(Statementmetric.getInstance());
		}else if(metricName.equals(BRANCH_METRIC)){
			metrics.remove(Statementmetric.getInstance());
		}

	}

	public File startInstrumentation(File file) throws JDOMException,
			IOException, BpelException, BpelVersionException {
		logger.info("Die Instrumentierung der Datei "+file.getName()+" wird gestartet.");
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(file);
		process_element = doc.getRootElement();
		if (!process_element.getName().equalsIgnoreCase(
				BpelXMLTools.PROCESS_ELEMENT)) {
			throw (new BpelException(BpelException.NO_VALIDE_BPEL));
		}
		if (!process_element.getNamespace().equals(
				NAMESPACE_BPEL_2)) {
			throw (new BpelVersionException(BpelVersionException.WRONG_VERSION));
		}
		BpelXMLTools.process_element=process_element;
		IMetric metric;
		for (Enumeration<IMetric> i = metrics.elements(); i.hasMoreElements();) {
			metric = i.nextElement();
			logger.info(metric);
			metric.insertMarker(process_element);
		}
		String[] name_analyse = getFileName(file);
		// File instrumentation_file=new
		// File(name_analyse[0]+"_instr_"+name_analyse[1]);
		File instrumentation_file = new File("ergebnis.bpel");
		logger.info("Instrumentierung erfolgreich ausgeführt.");
		logger.info("Die instrumentierte BPEL-Datei wird in "+instrumentation_file+" geschrieben.");
		
		FileWriter writer = new FileWriter(instrumentation_file);
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(doc, writer);
		return instrumentation_file;
	}

	private String[] getFileName(File file) {
		String name = file.getName();
		int index = name.lastIndexOf('.');
		String[] name_analyse = new String[2];
		if (index > -1) {
			name_analyse[0] = name.substring(0, index);
			name_analyse[1] = name.substring(index);
		} else {

			name_analyse[0] = name;
			name_analyse[1] = "";
		}
		return name_analyse;
	}

	public IMetric getMetric(String metricName) {
		IMetric metric=null;
		if(metricName.equals(STATEMENT_METRIC)){
			metric=Statementmetric.getInstance();
		}else if(metricName.equals(BRANCH_METRIC)){
			metric=BranchMetric.getInstance();
		}
		return metric;
	}
	


}
