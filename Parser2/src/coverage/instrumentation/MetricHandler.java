package coverage.instrumentation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import coverage.instrumentation.activitytools.ActivityTools;
import coverage.instrumentation.exception.BpelException;
import coverage.instrumentation.exception.BpelVersionException;

/**
 * Die Klasse implementiert das Interface IMetricHandler.
 * 
 * @author Alex Salnikow
 */
public class MetricHandler implements IMetricHandler {

	private static IMetricHandler instance = null;

	private List<IMetric> metrics = new LinkedList<IMetric>();

	private Element process_element;

	public static IMetricHandler getInstance() {
		if (instance == null) {
			instance = new MetricHandler();
		}
		return instance;
	}

	private MetricHandler() {
	}

	public void addMetric(IMetric metric) {
		metrics.add(metric);

	}

	public void remove(IMetric metric) {
		// TODO Auto-generated method stub

	}

	public void startInstrumentation(File file) throws JDOMException,
			IOException, BpelException, BpelVersionException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(file);
		XMLOutputter fmt = new XMLOutputter();
		fmt.output(doc, System.out);
		process_element = doc.getRootElement();
		if (!process_element.getName().equalsIgnoreCase(
				ActivityTools.PROCESS_ELEMENT)) {
			throw (new BpelException(BpelException.NO_VALIDE_BPEL));
		}
		if (!process_element.getNamespace().equals(
				ActivityTools.NAMESPACE_BPEL_2)) {
			throw (new BpelVersionException(BpelVersionException.WRONG_VERSION));
		}
		IMetric metric;
		for (Iterator<IMetric> i = metrics.iterator(); i.hasNext();) {
			metric = i.next();
			metric.insertMarker(process_element);
		}

		fmt.output(doc, System.out);
	}

}
