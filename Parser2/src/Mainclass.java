import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;

import coverage.instrumentation.exception.BpelException;
import coverage.instrumentation.exception.BpelVersionException;
import coverage.instrumentation.metrics.IMetricHandler;
import coverage.instrumentation.metrics.MetricHandler;



public class Mainclass {

	public static void main(String[] args) throws JDOMException, IOException, BpelException, BpelVersionException{
		String filename = args[0];
		IMetricHandler metric_handler=MetricHandler.getInstance();
//		Statementmetric metric=(Statementmetric) metric_handler.addMetric(IMetricHandler.STATEMENT_METRIC);
//		metric.addAllBasicActivities();
		
		metric_handler.addMetric(IMetricHandler.BRANCH_METRIC);
		//if
//		metric_handler.startInstrumentation(new File(filename));
		
		filename = args[1];
		//sequence
//		metric_handler.startInstrumentation(new File(filename));
	
		filename = args[2];
		//pick
//		metric_handler.startInstrumentation(new File(filename));
		
		filename = args[3];
		//repeatUntil
//		metric_handler.startInstrumentation(new File(filename));
		filename = args[4];
		//while
//		metric_handler.startInstrumentation(new File(filename));
		filename = args[5];
		//flow
		metric_handler.startInstrumentation(new File(filename));
		filename = args[6];
		//foreach
		metric_handler.startInstrumentation(new File(filename));
	}
}
