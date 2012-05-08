package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ELSE_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ELSE_IF_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createSequence;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.insertElseBranch;

import java.util.List;

import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;


/**
 * Handler, der die Instrumentierung der
 * if-Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */
public class IfHandler implements IStructuredActivityHandler {

	private MarkersRegisterForArchive markersRegistry;

	public IfHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	/**
	 * Fügt Markierungen, die später durch Invoke-Aufrufe protokolliert werden,
	 * um die Ausführung der Zweige zu erfassen.
	 * 
	 * @param structuredActivity
	 * @throws BpelException
	 */
	public void insertBranchMarkers(Element structuredActivity)
			throws BpelException {
		insertMarkerForIfBranch(getFirstEnclosedActivity(structuredActivity));
		List elseIfBranches = structuredActivity.getChildren(ELSE_IF_ELEMENT,
				getProcessNamespace());
		for (int i = 0; i < elseIfBranches.size(); i++) {
			insertMarkerForElseIfBranches(getFirstEnclosedActivity((Element) elseIfBranches
					.get(i)));
		}
		Element elseElement = structuredActivity.getChild(ELSE_ELEMENT, getProcessNamespace());
		if (elseElement == null) {
			elseElement = insertElseBranch(structuredActivity);
			elseElement.addContent(createSequence());
		}
		insertMarkerForElseBranch(getFirstEnclosedActivity(elseElement));
	}

	/**
	 * 
	 * @param branchActivity
	 *            Aktivität aus dem Else-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForElseBranch(Element branchActivity)
			throws BpelException {
		if (branchActivity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(branchActivity));
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
			
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(branch_activity));
	}

	/**
	 * 
	 * @param branchActivity
	 *            Aktivität aus dem If-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForIfBranch(Element branchActivity)
			throws BpelException {
		if (branchActivity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}

		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(branchActivity));

	}

}
