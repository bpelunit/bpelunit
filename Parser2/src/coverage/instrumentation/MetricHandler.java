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




public class MetricHandler implements IMetricHandler {

	private static IMetricHandler instance=null;
	
	private List<IMetric> metrics=new LinkedList<IMetric>();

	public static IMetricHandler getInstance() {
		if(instance==null){
			instance=new MetricHandler();
		}
		return instance;
	}
	
	private MetricHandler(){
		
	}
	
	public void addMetric(IMetric metric) {
		metrics.add(metric);

	}

	public void remove(IMetric metric) {
		// TODO Auto-generated method stub

	}

	public void startInstrumentation(File file) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(file);
		XMLOutputter fmt = new XMLOutputter();
		fmt.output(doc, System.out);
		Element process_element=doc.getRootElement();
		IMetric metric;
		for ( Iterator<IMetric> i = metrics.iterator(); i.hasNext(); ) 
		{ 
		  metric = i.next(); 
		  metric.insertMarker(process_element);
		}
	}

}
