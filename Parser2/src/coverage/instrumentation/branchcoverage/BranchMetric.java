package coverage.instrumentation.branchcoverage;

import java.util.Hashtable;
import java.util.Iterator;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import coverage.instrumentation.IMetric;



public class BranchMetric implements IMetric {
	
	private static final String BRANCH_LABEL="branch";
	
	private static int count=0;

	public static String getNextLabel(){
		return BRANCH_LABEL+(count++);
	}
	
	private Hashtable<String, IStructuredActivity> structured_activity_handler=new Hashtable<String, IStructuredActivity>();
	
	public BranchMetric(){
		structured_activity_handler.put("flow", new FlowActivityHandler());
		structured_activity_handler.put("sequence", new SequenceActivityHandler());
		structured_activity_handler.put("if", new IfActivityHandler());
		structured_activity_handler.put("while", new WhileActivityHandler());
		structured_activity_handler.put("repeatUntil", new RepeatUntilActivityHandler());
		structured_activity_handler.put("forEach", new ForEachActivityHandler());
		structured_activity_handler.put("pick", new PickActivityHandler());
	}
	
	public void insertMarker(Element element) {
		Element next_element;
		String next_element_name;
		for (Iterator iterator = element.getDescendants(new ElementFilter()); iterator.hasNext();) {
			 next_element = (Element) iterator.next();
			 next_element_name=next_element.getName();
			 if(structured_activity_handler.containsKey(next_element_name)){
				 structured_activity_handler.get(next_element_name).insertMarkerForBranchCoverage(next_element);
			 }
			
		}
	}

}
