package coverage.instrumentation.metrics.statementcoverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.wstools.CoverageRegistry;

/**
 * Klasse instrumentiert ein BPEL-Prozess, um die Abdeckung der BasicActivities
 * bei der Ausführung zu messen. Die einzelnen Aktivitäten, die bei der Messung
 * berücksichtigt werden müssen, müssen explizit angegeben werden ({@link #addBasicActivity(String)}).
 * 
 * @author Alex Salnikow
 */
public class Statementmetric implements IMetric {

	public static final String METRIC_NAME = "Statementmetric";

	private static int count = 0;

	private HashMap<String, String> logging_before_activity;

	private HashMap<String, String> activities_to_respekt;

	public Statementmetric() {
		activities_to_respekt = new HashMap<String, String>();
		logging_before_activity = new HashMap<String, String>();
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
	 * Diese Methode fügt die Markierungen in BPEL-Process-Element ein
	 * (Instrumentierung). Anhand dieser Markierungen werden danach
	 * entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 *            Prozess-Element der BPEL
	 */
	public void insertMarker(Element element) {
		// BasicActivitiesFilter filter = new
		// BasicActivitiesFilter(BpelXMLTools.getBpelNamespace(),
		// activities_to_respekt);
		ElementFilter filter = new ElementFilter(BpelXMLTools
				.getBpelNamespace()) {
			@Override
			public boolean matches(Object obj) {
				boolean result = false;
				Element element;
				if (super.matches(obj)) {
					element = (Element) obj;
					if (activities_to_respekt.containsKey(element.getName())) {
						result = true;
					}
				}
				return result;
			}
		};
		List<Element> elements_to_log = new ArrayList<Element>();
		for (Iterator iter = element.getDescendants(filter); iter.hasNext();) {
			elements_to_log.add((Element) iter.next());
		}
		insertMarkerForEachActivity(elements_to_log);
	}

	/**
	 * Fügt Markierungen für jede Aktivität ein. Wenn es notwendig ist, dann
	 * werden die Aktivität und die Markierung in ein Sequence-Element
	 * eingeschloßen, sa dass die Markierung im Kontrollfluß direkt vor oder
	 * nach der zu loggenden Aktivität ist.
	 * 
	 * @param elements_to_log
	 *            Aktivitäten, die geloggt werden sollen.
	 */
	private void insertMarkerForEachActivity(List<Element> elements_to_log) {
		Element element;
		Element targetElement;
		for (int i = 0; i < elements_to_log.size(); i++) {
			element = elements_to_log.get(i);
			Element parent = element.getParentElement();
			targetElement = element.getChild("targets", BpelXMLTools
					.getBpelNamespace());
			if (targetElement != null) {
				Element sequence = BpelXMLTools.encloseInSequence(element);
				sequence.addContent(0, targetElement.detach());
			} else if (!parent.getName().equals(
					StructuredActivity.SEQUENCE_ACTIVITY)) {
				BpelXMLTools.ensureElementIsInSequence(element);
			}
			insertMarkerForActivity(element);
		}
	}

	/**
	 * Fügt eine Markierung für die Aktivität ein. Die Markierung wird entweder
	 * direkt vor oder nach der Aktivität eingefügt. Die Aktivitäten, die in
	 * {@link #logging_before_activity} eingetragen sind, müssen vor der
	 * Ausführung geloggt werden.
	 * 
	 * @param element
	 *            Aktivität
	 */
	private void insertMarkerForActivity(Element element) {
		Element parent = element.getParentElement();
		String element_name = element.getName();
		String marker=element_name + "_" + (count++);
		CoverageRegistry.getInstance().addMarker(marker);
		Comment comment = new Comment(MARKER_IDENTIFIRE + marker);
		int index = parent.indexOf(element);
		if (logging_before_activity.containsKey(element_name)) {
			parent.addContent(index, comment);
		} else {
			parent.addContent(index + 1, comment);
		}

	}

	/**
	 * Registriert Aktivitäten, die bei der Messung der Abdeckung berücksichtigt
	 * werden müssen.
	 * 
	 * @param activity_name
	 *            Aktivität
	 */
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
		for (Iterator<String> e = activities_to_respekt.values().iterator(); e
				.hasNext();) {
			name = name + e.next() + ", ";
		}
		return name;
	}

	public Set<String> getBasisActivities() {
		return activities_to_respekt.keySet();
	}

	public String getName() {
		return METRIC_NAME;
	}
}
