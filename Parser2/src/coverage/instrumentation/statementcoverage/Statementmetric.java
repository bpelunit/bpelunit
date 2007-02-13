package coverage.instrumentation.statementcoverage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

import coverage.instrumentation.IMetric;

public class Statementmetric implements IMetric {

	private Hashtable<String, String> logging_before_activity;

	private Hashtable<String, String> activities_to_respekt;

	private List<Element> elements_to_log;
	
	private static int count=0;

	public Statementmetric() {
		activities_to_respekt = new Hashtable<String, String>();
		elements_to_log = new ArrayList<Element>();
		logging_before_activity = new Hashtable<String, String>();
		logging_before_activity.put("throw", "throw");
		logging_before_activity.put("rethrow", "rethrow");
		logging_before_activity.put("compensate", "compensate");
		logging_before_activity.put("compensateScope", "compensateScope");
		logging_before_activity.put("exit", "exit");
	}

	public void insertMarker(Element element) {
		BasicActivitiesFilter filter = new BasicActivitiesFilter(element
				.getNamespace(), activities_to_respekt);
		for (Iterator iter = element.getDescendants(filter); iter.hasNext();) {
			elements_to_log.add((Element) iter.next());
		}
		insertMarkerForEachElement(elements_to_log);
	}

	private void insertMarkerForEachElement(List<Element> elements_to_log2) {
		Element element;
		Element targetelement;
		for (Iterator<Element> iter = elements_to_log2.iterator(); iter
				.hasNext();) {
			element = iter.next();
			Element parent = element.getParentElement();
			targetelement = element.getChild("target");
			if (targetelement != null || !parent.getName().equals("sequence")) {
				encloseInSequence(element, targetelement);
			}
			insertMarkerForThisActivity(element);
		}
	}

	private void insertMarkerForThisActivity(Element element) {
		Element parent = element.getParentElement();
		String element_name = element.getName();
		Comment mark = new Comment(element_name+(count++));
		int index = parent.indexOf(element);
		if (logging_before_activity.contains(element_name)) {
			parent.addContent(index, mark);
		} else {
			parent.addContent(index + 1, mark);
		}

	}

	private void encloseInSequence(Element element, Element targetelement) {
		Namespace namespace = element.getNamespace();
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		Element sequence = new Element("sequence", namespace);
		if (targetelement != null) {
			targetelement.detach();
			sequence.addContent(targetelement);
		}
		element.detach();
		sequence.addContent(element);
		parent.addContent(index, sequence);

	}

	public void addBasicActivity(String activity_name) {
		activities_to_respekt.put(activity_name, activity_name);
	}

	public void remove(String activity_name) {
		activities_to_respekt.remove(activity_name);
	}
}
