package coverage.instrumentation.metrics.branchcoverage;

import static coverage.instrumentation.bpelxmltools.BpelXMLTools.*;

import java.util.List;

import org.jdom.Element;

import coverage.exception.BpelException;

/**
 * Die Klasse ist für das Einfügen der Markierungen in der If-Aktivität
 * verantwortlich, die für die Messung der Zweigabdeckung verwendet werden.
 * 
 * @author Alex Salnikow
 */
public class IfActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		insertMarkerForIfBranch(getFirstEnclosedActivity(element));
		List elseif_branches = element.getChildren(ELSE_IF_ELEMENT,
				getBpelNamespace());
		for (int i = 0; i < elseif_branches.size(); i++) {
			insertMarkerForElseIfBranches(getFirstEnclosedActivity((Element) elseif_branches
					.get(i)));
		}
		Element else_el = element.getChild(ELSE_ELEMENT, getBpelNamespace());
		if (else_el == null) {
			else_el = insertElseBranch(element);
		}
		insertMarkerForElseBranch(getFirstEnclosedActivity(else_el));
	}

	/**
	 * 
	 * @param branch_activity
	 *            Aktivität aus dem Else-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForElseBranch(Element branch_activity)
			throws BpelException {
		if (branch_activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertLabelBevorAllActivities(branch_activity);

	}

	/**
	 * 
	 * @param branch_activity
	 *            Aktivität aus dem ElseIf-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForElseIfBranches(Element branch_activity)
			throws BpelException {
		if (branch_activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertLabelBevorAllActivities(branch_activity);

	}

	/**
	 * 
	 * @param branch_activity
	 *            Aktivität aus dem If-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForIfBranch(Element branch_activity)
			throws BpelException {
		if (branch_activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertLabelBevorAllActivities(branch_activity);

	}

}
