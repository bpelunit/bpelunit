import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;

import coverage.instrumentation.BasisActivity;
import coverage.instrumentation.IMetricHandler;
import coverage.instrumentation.MetricHandler;
import coverage.instrumentation.branchcoverage.BranchMetric;
import coverage.instrumentation.exception.BpelException;
import coverage.instrumentation.exception.BpelVersionException;
import coverage.instrumentation.statementcoverage.Statementmetric;



public class Mainclass {

	public static void main(String[] args) throws JDOMException, IOException, BpelException, BpelVersionException{
		String filename = args[0];
		IMetricHandler metric_handler=MetricHandler.getInstance();
		Statementmetric statementMetric=new Statementmetric();
		statementMetric.addBasicActivity(BasisActivity.ASSIGN_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.COMPENSATE_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.COMPENSATESCOPE_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.EMPTY_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.INVOKE_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.RECEIVE_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.REPLY_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.RETHROW_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.THROW_ACTIVITY);
		statementMetric.addBasicActivity(BasisActivity.WAIT_ACTIVITY);
		metric_handler.addMetric(statementMetric);
		
//		metric_handler.addMetric(new BranchMetric());
//		metric_handler.startInstrumentation(new File(filename));
		
		filename = args[1];
//		metric_handler.startInstrumentation(new File(filename));
	
		filename = args[2];
//		metric_handler.startInstrumentation(new File(filename));
		
		filename = args[3];
//		metric_handler.startInstrumentation(new File(filename));
		filename = args[4];
//		metric_handler.startInstrumentation(new File(filename));
		filename = args[5];
		metric_handler.startInstrumentation(new File(filename));
	}
}
