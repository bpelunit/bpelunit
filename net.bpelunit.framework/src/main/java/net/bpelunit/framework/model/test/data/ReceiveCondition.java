/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

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
	 * Parent receive data specification
	 */
	private DataSpecification fParent;

	/**
	 * Status of this object
	 */
	private ArtefactStatus fStatus;

	/**
	 * Velocity template to be used to build the receive condition.
	 */
	private String fTemplate;

	// ******************** Initialization ************************

	/**
	 * Data Specification can reasonably only by a ReceiveDataCondition or a
	 * CompleteHumanTaskSpecification
	 */
	public ReceiveCondition(DataSpecification rSpec, String condition, String template, String value) throws SpecificationException {
		fExpression= condition;
		fTemplate= template;
		fExpectedValue= value;
		fStatus= ArtefactStatus.createInitialStatus();
		fParent= rSpec;
		fActualValue= null;

		if (fExpression != null && fTemplate != null || fExpression == null && fTemplate == null) {
			throw new SpecificationException("Exactly one of (<template>, <expression>) should be used");
		}
	}


	// ******************** Implementation ***************************

	public void evaluate(VelocityContextProvider activityContext, Element literalData, NamespaceContext context, XPathVariableResolver variableResolver) {

		XPath xpath= XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(context);
		if (variableResolver != null) {
			xpath.setXPathVariableResolver(variableResolver);
		}

		try {
			if (fExpression == null) {
				fExpression = fParent.expandTemplateToString(activityContext, fTemplate).trim();
			}
			String completeXPath= "(" + fExpression + ") =" + fExpectedValue;
			if (!(Boolean) xpath.evaluate(completeXPath, literalData, XPathConstants.BOOLEAN)) {

				// Get actual result
				String smartGuess= xpath.evaluate(fExpression, literalData);
				if (smartGuess != null) {
					fActualValue= smartGuess;
					if ("".equals(fActualValue)) {
						fActualValue= "(no data)";
					}
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
		if (fActualValue != null) {
			stateData.add(new StateData("Actual Data", fActualValue));
		}
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
		r.append(" => Evaluation: ").append(fStatus);
		return r.toString();
	}

}
