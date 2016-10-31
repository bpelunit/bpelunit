package net.bpelunit.utils.bpelstats.hertis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class BPELConstants {

	public static final String BPEL_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	public static final List<QName> BASIC_ACTIVITIES;
	public static final List<QName> STRUCTURED_ACTIVITIES;
	public static final List<QName> HANDLERS;
	public static final QName LINK = new QName(BPEL_NAMESPACE, "link");
	public static final QName SEQUENCE = new QName(BPEL_NAMESPACE, "sequence");
	public static final QName IF = new QName(BPEL_NAMESPACE, "if");
	public static final QName WHILE = new QName(BPEL_NAMESPACE, "while");
	public static final QName FLOW = new QName(BPEL_NAMESPACE, "flow");
	public static final QName PICK = new QName(BPEL_NAMESPACE, "pick");
	public static final QName PROCESS = new QName(BPEL_NAMESPACE, "process");
	
	static {
		List<QName> basicActivities = new ArrayList<QName>();
		basicActivities.add(new QName(BPEL_NAMESPACE, "assign"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "invoke"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "receive"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "reply"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "throw"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "rethrow"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "exit"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "empty"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "extensionActivity"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "validate"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "wait"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "compensate"));
		basicActivities.add(new QName(BPEL_NAMESPACE, "compensateScope"));
		BASIC_ACTIVITIES = Collections.unmodifiableList(basicActivities);
		
		List<QName> structuredActivities = new ArrayList<QName>();
		structuredActivities.add(new QName(BPEL_NAMESPACE, "sequence"));
		structuredActivities.add(new QName(BPEL_NAMESPACE, "if"));
		structuredActivities.add(new QName(BPEL_NAMESPACE, "while"));
		structuredActivities.add(new QName(BPEL_NAMESPACE, "forEach"));
		structuredActivities.add(new QName(BPEL_NAMESPACE, "flow"));
		structuredActivities.add(new QName(BPEL_NAMESPACE, "repeatUntil"));
		structuredActivities.add(new QName(BPEL_NAMESPACE, "pick"));
		STRUCTURED_ACTIVITIES = Collections.unmodifiableList(structuredActivities);
		
		List<QName> handlers = new ArrayList<QName>();
		handlers.add(new QName(BPEL_NAMESPACE, "compensationHandler"));
		handlers.add(new QName(BPEL_NAMESPACE, "faultHandlers"));
		handlers.add(new QName(BPEL_NAMESPACE, "terminationHandler"));
		handlers.add(new QName(BPEL_NAMESPACE, "eventHandlers"));
		HANDLERS = Collections.unmodifiableList(handlers);
	}
	
}
