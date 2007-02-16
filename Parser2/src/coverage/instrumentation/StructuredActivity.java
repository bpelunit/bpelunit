package coverage.instrumentation;

import java.util.Hashtable;

import org.jdom.Element;

public class StructuredActivity {

	public static final String SEQUENCE_ACTIVITY="sequence";
	public static final String IF_ACTIVITY="if";
	public static final String WHILE_ACTIVITY="while";
	public static final String REPEATUNTIL_ACTIVITY="repeatUntil";
	public static final String FOREACH_ACTIVITY="forEach";
	public static final String PICK_ACTIVITY="pick";
	public static final String FLOW_ACTIVITY="flow";
	public static final String SCOPE_ACTIVITY="scope";
	private static Hashtable<String,String> basis_activities;
	
	static{
		basis_activities=new Hashtable<String, String>();
		basis_activities.put(SEQUENCE_ACTIVITY, SEQUENCE_ACTIVITY);
		basis_activities.put(IF_ACTIVITY, IF_ACTIVITY);
		basis_activities.put(WHILE_ACTIVITY, WHILE_ACTIVITY);
		basis_activities.put(REPEATUNTIL_ACTIVITY, REPEATUNTIL_ACTIVITY);
		basis_activities.put(FOREACH_ACTIVITY, FOREACH_ACTIVITY);
		basis_activities.put(PICK_ACTIVITY,PICK_ACTIVITY );
//		basis_activities.put(SCOPE_ACTIVITY, SCOPE_ACTIVITY);	
		basis_activities.put(FLOW_ACTIVITY, FLOW_ACTIVITY);	
	}
	public static boolean isStructuredActivity(Element element){
		boolean isStructured=false;
		if(basis_activities.containsKey(element.getName()))
			isStructured=true;
		return isStructured;
	}
}
