package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Element;

import coverage.instrumentation.bpelxmltools.BpelXMLTools;

/**
 * Die Klasse ist für das Einfügen der Markierungen in der Sequence-Aktivität
 * verantwortlich, die für die Messung der Zweigabdeckung verwendet werden.
 * 
 * @author Alex Salnikow
 */
public class SequenceActivityHandler implements IStructuredActivity {

	/**
	 * Fügt Markierungen in Sequence-Elemente ein, die später, um die Ausführung
	 * der Zweige zu erfassen, durch Invoke-Aufrufe protokolliert werden.
	 * 
	 * @param sequence
	 */
	public void insertMarkerForBranchCoverage(Element sequence) {
		List children = sequence.getChildren();
		Element child;
		Element previousActivity = null;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (BpelXMLTools.isActivity(child))
				if (previousActivity != null) {
					BranchMetric.insertMarkerAfterActivity(previousActivity);
				}
			previousActivity = child;
		}
	}
}
