package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.List;

import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;


/**
 * Handler, der die Instrumentierung der
 * switch-Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */

public class SwitchHandler implements IStructuredActivityHandler {
	private MarkersRegisterForArchive markersRegistry;

	public SwitchHandler(MarkersRegisterForArchive markersRegistry) {
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
		List<Element> caseBranches = structuredActivity.getChildren(SWITCH_CASE_ELEMENT,
				getProcessNamespace());
		for (int i = 0; i < caseBranches.size(); i++) {
			insertMarkerForCaseBranches(getFirstEnclosedActivity(caseBranches
					.get(i)));
		}
		Element otherwiseElement = structuredActivity
				.getChild(SWITCH_OTHERWISE_ELEMENT, getProcessNamespace());
		if (otherwiseElement == null) {
			otherwiseElement = insertOtherwiseBranch(structuredActivity);
			otherwiseElement.addContent(createSequence());
		}
		insertMarkerForOtherwiseBranch(otherwiseElement);
	}

	/**
	 * 
	 * @param otherwiseElement
	 *            Aktivität aus dem Else-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForOtherwiseBranch(Element otherwiseElement)
			throws BpelException {
		Element branchActivity = getFirstEnclosedActivity(otherwiseElement);
		if (branchActivity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(branchActivity));

	}

	/**
	 * 
	 * @param branchActivity
	 *            Aktivität aus dem ElseIf-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForCaseBranches(Element branchActivity)
			throws BpelException {
		if (branchActivity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(branchActivity));
	}

}
