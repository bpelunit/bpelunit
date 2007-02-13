import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;

import coverage.instrumentation.IMetricHandler;
import coverage.instrumentation.MetricHandler;
import coverage.instrumentation.branchcoverage.BranchMetric;



public class Mainclass {

	public static void main(String[] args) throws JDOMException, IOException{
		String filename = args[0];
		IMetricHandler metric_handler=MetricHandler.getInstance();
		metric_handler.addMetric(new BranchMetric());
		metric_handler.startInstrumentation(new File(filename));
	}
}
