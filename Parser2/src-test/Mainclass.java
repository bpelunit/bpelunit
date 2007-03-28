
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.jdom.JDOMException;

import coverage.CoverageMeasurementTool;
import coverage.exception.BpelException;
import coverage.exception.BpelVersionException;
import de.schlichtherle.io.File;



public class Mainclass {

	public static void main(String[] args) throws JDOMException, IOException, BpelException, BpelVersionException{
		String filename = args[0];
//		IMetricHandler metric_handler=MetricHandler.getInstance();
////		Statementmetric metric=(Statementmetric) metric_handler.addMetric(IMetricHandler.STATEMENT_METRIC);
////		metric.addAllBasicActivities();
//		
//		metric_handler.addMetric(IMetricHandler.BRANCH_METRIC);
//		//if
////		metric_handler.startInstrumentation(new File(filename));
//		
//		filename = args[1];
//		//sequence
////		metric_handler.startInstrumentation(new File(filename));
//	
//		filename = args[2];
//		//pick
////		metric_handler.startInstrumentation(new File(filename));
//		
//		filename = args[3];
//		//repeatUntil
////		metric_handler.startInstrumentation(new File(filename));
//		filename = args[4];
//		//while
////		metric_handler.startInstrumentation(new File(filename));
//		filename = args[5];
//		//flow
//		metric_handler.startInstrumentation(new File(filename));
//		filename = args[6];
//		//foreach
//		metric_handler.startInstrumentation(new File(filename));
		
		filename=args[7];
		try {
			Logger logger = Logger.getLogger("test");
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
			new CoverageMeasurementTool(new File(""),"").prepareArchiveForCoverageMeasurement("D:/Alex/daten/Masterarbeit/workspaceBPELUnit/Parser2/bsp/",filename,null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
