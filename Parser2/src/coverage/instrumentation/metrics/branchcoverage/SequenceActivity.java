package coverage.instrumentation.metrics.branchcoverage;

import static coverage.instrumentation.bpelxmltools.BpelXMLTools.*;
import java.util.List;

import org.jdom.Element;

/**
 * Die Klasse ist für das Einfügen der Markierungen in der Sequence-Aktivität
 * verantwortlich, die für die Messung der Zweigabdeckung verwendet werden.
 * 
 * @author Alex Salnikow
 */
public class SequenceActivity implements IStructuredActivity {

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
			if (isActivity(child))
				if (previousActivity != null) {
					BranchMetric.insertLabelAfterActivity(previousActivity);
				}
			previousActivity = child;
		}
	}
}
