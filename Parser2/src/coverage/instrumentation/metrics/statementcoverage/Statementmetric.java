package coverage.instrumentation.metrics.statementcoverage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.metrics.IMetric;

/**
 * Klasse instrumentiert ein BPEL-Prozess um die Abdeckung der BasicActivities
 * bei der Ausführung zu messen. Die einzelnen Aktivitäten, die bei der Messung
 * berücksichtigt werden müssen, müssen explizit angegeben werden ({@link #addBasicActivity(String)}).
 * 
 * @author Alex Salnikow
 */
public class Statementmetric implements IMetric {

	public static final String METRIC_NAME = "Statementmetric";



	private static Statementmetric instance = null;

	private static int count = 0;

	private Hashtable<String, String> logging_before_activity;

	public static Statementmetric getInstance() {
		if (instance == null) {
			instance = new Statementmetric();
		}
		return instance;
	}

	private Hashtable<String, String> activities_to_respekt;

	private List<Element> elements_to_log;

	private Statementmetric() {
		activities_to_respekt = new Hashtable<String, String>();
		elements_to_log = new ArrayList<Element>();
		logging_before_activity = new Hashtable<String, String>();
		logging_before_activity.put(BasisActivity.THROW_ACTIVITY,
				BasisActivity.THROW_ACTIVITY);
		logging_before_activity.put(BasisActivity.RETHROW_ACTIVITY,
				BasisActivity.RETHROW_ACTIVITY);
		logging_before_activity.put(BasisActivity.COMPENSATE_ACTIVITY,
				BasisActivity.COMPENSATE_ACTIVITY);
		logging_before_activity.put(BasisActivity.COMPENSATESCOPE_ACTIVITY,
				BasisActivity.COMPENSATESCOPE_ACTIVITY);
		logging_before_activity.put(BasisActivity.EXIT_ACTIVITY,
				BasisActivity.EXIT_ACTIVITY);
	}

	/**
	 * Diese Methode fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 */
	public void insertMarker(Element element) {
		BasicActivitiesFilter filter = new BasicActivitiesFilter(BpelXMLTools
				.getBpelNamespace(), activities_to_respekt);
		for (Iterator iter = element.getDescendants(filter); iter.hasNext();) {
			elements_to_log.add((Element) iter.next());
		}
		insertMarkerForEachActivity(elements_to_log);
		elements_to_log = new ArrayList<Element>();
	}

	private void insertMarkerForEachActivity(List<Element> elements_to_log) {
		Element element;
		Element targetelement;
		for (int i = 0; i < elements_to_log.size(); i++) {
			element = elements_to_log.get(i);
			Element parent = element.getParentElement();
			targetelement = element.getChild("targets", BpelXMLTools
					.getBpelNamespace());
			if (targetelement != null) {
				Element sequence = BpelXMLTools.encloseInSequence(element);
				sequence.addContent(0, targetelement.detach());
			} else if (!parent.getName().equals(
					StructuredActivity.SEQUENCE_ACTIVITY)) {
				BpelXMLTools.ensureElementIsInSequence(element);
			}
			insertMarkerForActivity(element);
		}
	}

	private void insertMarkerForActivity(Element element) {
		Element parent = element.getParentElement();
		String element_name = element.getName();
		Comment mark = new Comment(MARKER_IDENTIFIRE + element_name + (count++));

		int index = parent.indexOf(element);
		if (logging_before_activity.contains(element_name)) {
			parent.addContent(index, mark);
		} else {
			parent.addContent(index + 1, mark);
		}

	}

	public void addBasicActivity(String activity_name) {
		activities_to_respekt.put(activity_name, activity_name);
	}

	public void addAllBasicActivities() {
		addBasicActivity(BasisActivity.ASSIGN_ACTIVITY);
		addBasicActivity(BasisActivity.COMPENSATE_ACTIVITY);
		addBasicActivity(BasisActivity.COMPENSATESCOPE_ACTIVITY);
		addBasicActivity(BasisActivity.EMPTY_ACTIVITY);
		addBasicActivity(BasisActivity.INVOKE_ACTIVITY);
		addBasicActivity(BasisActivity.RECEIVE_ACTIVITY);
		addBasicActivity(BasisActivity.REPLY_ACTIVITY);
		addBasicActivity(BasisActivity.RETHROW_ACTIVITY);
		addBasicActivity(BasisActivity.THROW_ACTIVITY);
		addBasicActivity(BasisActivity.WAIT_ACTIVITY);
	}

	public void removeBasicActivity(String activity_name) {
		activities_to_respekt.remove(activity_name);
	}

	public void removeAllBasicActivities() {
		removeBasicActivity(BasisActivity.ASSIGN_ACTIVITY);
		removeBasicActivity(BasisActivity.COMPENSATE_ACTIVITY);
		removeBasicActivity(BasisActivity.COMPENSATESCOPE_ACTIVITY);
		removeBasicActivity(BasisActivity.EMPTY_ACTIVITY);
		removeBasicActivity(BasisActivity.INVOKE_ACTIVITY);
		removeBasicActivity(BasisActivity.RECEIVE_ACTIVITY);
		removeBasicActivity(BasisActivity.REPLY_ACTIVITY);
		removeBasicActivity(BasisActivity.RETHROW_ACTIVITY);
		removeBasicActivity(BasisActivity.THROW_ACTIVITY);
		removeBasicActivity(BasisActivity.WAIT_ACTIVITY);
	}

	@Override
	public String toString() {
		String name = METRIC_NAME + ": ";
		for (Enumeration<String> e = activities_to_respekt.keys(); e
				.hasMoreElements();) {
			name = name + " " + e.nextElement();
		}
		return name;
	}

	/**
	 * Die Klasse erweitert ElementFilter und realisiert ein Filter, der nur die
	 * benötigte Elemente filtert.
	 * 
	 * @author Alex Salnikow
	 */
	class BasicActivitiesFilter extends ElementFilter {

		private Hashtable<String, String> activities_to_respect;

		BasicActivitiesFilter(Namespace namespace,
				Hashtable<String, String> activities_to_respect) {
			super(namespace);
			this.activities_to_respect = activities_to_respect;
		}

		@Override
		public boolean matches(Object obj) {
			boolean result = false;
			Element element;
			if (super.matches(obj)) {
				element = (Element) obj;
				if (activities_to_respect.containsKey(element.getName())) {
					result = true;
				}
			}
			return result;
		}

	}

}
