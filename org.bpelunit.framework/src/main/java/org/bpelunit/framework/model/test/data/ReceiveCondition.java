/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.bpelunit.framework.model.test.report.StateData;
import org.w3c.dom.Element;

/**
 * A ReceiveCondition is responsible for verifying a certain (XPath-)condition on literal XML data.
 * After the condition has been initialized with the condition, the method
 * {@link #evaluate(Element, NamespaceContext)} may be called to verify the condition.
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio García-Domínguez
 * 
 */
public class ReceiveCondition implements ITestArtefact {

	/**
	 * The XPath expression which evaluates to the value to be tested.
	 */
	private String fExpression;

	/**
	 * The expected value.
	 */
	private String fExpectedValue;

	/**
	 * The value which was found.
	 */
	private String fActualValue;

	/**
	 * Parent activity
	 */
	private ITestArtefact fParent;

	/**
	 * Status of this object
	 */
	private ArtefactStatus fStatus;


	// ******************** Initialization ************************

	public ReceiveCondition(ITestArtefact parent, String condition, String value) {
		fExpression= condition;
		fExpectedValue= value;
		fStatus= ArtefactStatus.createInitialStatus();
		fParent= parent;
		fActualValue= null;
	}


	// ******************** Implementation ***************************

	public void evaluate(Element literalData, NamespaceContext context) {

		XPath xpath= XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(context);
		try {
			String completeXPath= fExpression + "=" + fExpectedValue;
			if (!(Boolean) xpath.evaluate(completeXPath, literalData, XPathConstants.BOOLEAN)) {

				// Get actual result
				String smartGuess= xpath.evaluate(fExpression, literalData);
				if (smartGuess != null) {
					fActualValue= smartGuess;
					if ("".equals(fActualValue))
						fActualValue= "(no data)";
				}

				// Get expected value
				String sExpectedValue = xpath.evaluate(fExpectedValue, literalData);

				fStatus= ArtefactStatus.createFailedStatus(String.format("Condition failed. Obtained value was '%s', expected '%s'", smartGuess, sExpectedValue));

			} else {
				fStatus= ArtefactStatus.createPassedStatus();
			}
		} catch (Exception e) {
			Throwable root= BPELUnitUtil.findRootThrowable(e);
			fStatus= ArtefactStatus.createErrorStatus(root.getMessage());
		}
	}


	// ********************* Getters & Setters *********************


	public String getExpression() {
		return fExpression;
	}


	public String getExpectedValue() {
		return fExpectedValue;
	}


	public String getActualValue() {
		return fActualValue;
	}

	public boolean isFailure() {
		return fStatus.isFailure();
	}

	public boolean isError() {
		return fStatus.isError();
	}


	// ******************** ITestArtefact **********************

	public String getName() {
		return "Receive Condition";
	}

	public ITestArtefact getParent() {
		return fParent;
	}

	public List<ITestArtefact> getChildren() {
		return new ArrayList<ITestArtefact>();
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		stateData.add(new StateData("Expression", fExpression));
		stateData.add(new StateData("Value", fExpectedValue));
		if (fActualValue != null)
			stateData.add(new StateData("Actual Data", fActualValue));
		return stateData;
	}


	public void reportProgress(ITestArtefact artefact) {
		fParent.reportProgress(artefact);
	}

	// ************************** Other Stuff ***********************

	@Override
	public String toString() {
		StringBuffer r= new StringBuffer();
		r.append(fExpression);
		r.append(" => Evaluation: " + fStatus);
		return r.toString();
	}

}
