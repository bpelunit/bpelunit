package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAME_ATTR;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createIfActivity;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createIncreesAssign;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createInitializeAssign;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ensureElementIsInSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.insertNewIntVariable;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.isSequence;

import org.bpelunit.framework.coverage.CoverageConstants;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.exprlang.ExpressionLanguage;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;



public class RepeatUntilHandler implements IStructuredActivityHandler {

	private MarkersRegisterForArchive markersRegistry;

	public RepeatUntilHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	public void insertBranchMarkers(Element element)
			throws BpelException {
		branchFromConditionToActivity(element);
		branchFromActivityToCondition(element);
	}

	private void branchFromActivityToCondition(Element element)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(element);
		if (element == null)
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		
		markersRegistry.addMarker(BranchMetricHandler.insertLabelAfterAllActivities(activity));
	}

	private void branchFromConditionToActivity(Element element)
			throws BpelException {
		Element countVariable = insertNewIntVariable(null, null);
		Element initializeAssign = createInitializeAssign(countVariable);
		insert(initializeAssign, element);
		Element increesAssign = createIncreesAssign(countVariable);
		insertIncreesAssign(increesAssign, element);
		insertIfConstruct(element, countVariable);
	}

	/**
	 * Fügt Markierung für den Zweig von Aktivitäten zu der Condition. Dafür
	 * wird eine If-Anfrage eingefügt, die anhand der Zählvariable überprüft, ob
	 * die Aktivitäten in der Schleife ausgeführt wurden.
	 * 
	 * @param element
	 * @param countVariable
	 * @throws BpelException
	 */
	private void insertIfConstruct(Element element, Element countVariable)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(element);
		if (activity == null)
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		
		if (!isSequence(activity))
			activity = ensureElementIsInSequence(activity);

		
		Element if_element = createIfActivity(ExpressionLanguage.getInstance(
				CoverageConstants.EXPRESSION_LANGUAGE).valueOf(
				countVariable.getAttributeValue(NAME_ATTR))
				+ "=1");
		Element sequence = createSequence();
		if_element.addContent(sequence);
		activity.addContent(0, if_element);
		markersRegistry.addMarker(BranchMetricHandler.insertLabelBevorAllActivities(sequence));

	}

	/**
	 * Fügt ein Assign Element an letzter Stelle innerhalb des
	 * RepeatUntil-Konstrukts, das die Zaählvariable um eins erhöht und
	 * registriert damit die Ausführung der Schleife.
	 * 
	 * @param increesAssign
	 * @param element
	 *            RepeatUntil-Element
	 * @throws BpelException
	 */
	private void insertIncreesAssign(Element increesAssign, Element element)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(element);
		if (activity == null)
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
	
		if (!isSequence(activity))
			activity = ensureElementIsInSequence(activity);	
		activity.addContent(increesAssign);

	}

	/**
	 * 
	 * @param initializeAssign
	 *            Assign-Element, das die Variable initialisiert.
	 * @param element
	 *            RepeatUntil-Element
	 */
	private void insert(Element initializeAssign, Element element) {
		Element sequence = ensureElementIsInSequence(element);
		sequence.addContent(sequence.indexOf(element), initializeAssign);
	}

}
