package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.xml.suite.XMLVariableScope;
import net.bpelunit.framework.xml.suite.XMLVariableScope.Enum;

import org.w3c.dom.Element;

/**
 * Test artifact class for a data extraction request within a receive activity.
 *
 * This request asks for a certain piece of information (specified by an XPath
 * expression) to be extracted from the incoming message and stored in a
 * variable that can be reused in the following Velocity-based templates within
 * a certain scope.
 * 
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public class DataExtraction implements ITestArtefact {

	/**
	 * Expression to be evaluated.
	 */
	private final String fExpression;

	/**
	 * Desired name for the target variable.
	 */
	private final String fVariable;

	/**
	 * Desired scope for the target variable.
	 */
	private final Enum fScope;

	/**
	 * Extracted value.
	 */
	private Object fExtracted;

	/**
	 * Status of this object.
	 */
	private ArtefactStatus fStatus;

	/**
	 * Parent test artefact.
	 */
	private DataSpecification fParent;

	public DataExtraction(DataSpecification parent, String expression, String variable, Enum scope) throws SpecificationException {
		if (expression == null) {
			throw new SpecificationException("expression must not be null");
		}
		if (variable == null) {
			throw new SpecificationException("variable must not be null");
		}
		if (scope == null) {
			throw new SpecificationException("scope must not be null");
		}

		this.fParent = parent;
		this.fExpression = expression;
		this.fVariable = variable;
		this.fScope = scope;

		this.fStatus = ArtefactStatus.createInitialStatus();
	}

	/**
	 * Returns the XPath expression that should be evaluated on the raw
	 * incoming message (including the SOAP elements).
	 */
	public String getExpression() {
		return fExpression;
	}

	/**
	 * Returns the name of the target variable.
	 */
	public String getVariable() {
		return fVariable;
	}

	/**
	 * Returns the scope of the target variable. Valid values are listed in
	 * the {@link XMLVariableScope} interface and are immutable singleton
	 * objects that can be tested for using the regular <code>==</code> operator.
	 */
	public Enum getScope() {
		return fScope;
	}

	/**
	 * Returns the extracted value.
	 */
	public Object getExtracted() {
		return fExtracted;
	}

	/**
	 * Changes the extracted value.
	 */
	public void setExtracted(Object extracted) {
		this.fExtracted = extracted;
	}

	// ************************** Implementation *************************

	public void evaluate(ActivityContext context, Element literalData,	NamespaceContext namespaceContext, ContextXPathVariableResolver variableResolver) {
		final XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(namespaceContext);
		if (variableResolver != null) {
			xpath.setXPathVariableResolver(variableResolver);
		}
	
		try {
			fExtracted = xpath.evaluate(fExpression, literalData, XPathConstants.NODE);
			fStatus = ArtefactStatus.createPassedStatus();

			// TODO: think of how to propagate the extracted value.
			final IExtractedDataContainer targetContainer = getTargetContainer();
			targetContainer.putExtractedData(fVariable, fExtracted);
		} catch (Exception e) {
			Throwable root = BPELUnitUtil.findRootThrowable(e);
			fStatus = ArtefactStatus.createErrorStatus(root.getMessage());
		}
	}

	private IExtractedDataContainer getTargetContainer() {
		switch (fScope.intValue()) {
		case XMLVariableScope.INT_ACTIVITY:
			return getActivity();
		case XMLVariableScope.INT_PARTNERTRACK:
			return getPartnerTrack();
		case XMLVariableScope.INT_TESTCASE:
			return getTestCase();
		case XMLVariableScope.INT_TESTSUITE:
			return getTestSuite();
		default:
			throw new IllegalArgumentException("Unknown scope: " + fScope);
		}
	}

	private Activity getActivity() {
		final ITestArtefact dataSpecParent = fParent.getParent();
		if (dataSpecParent instanceof Activity) {
			return (Activity) dataSpecParent;
		} else {
			throw new IllegalArgumentException(
					"The parent of the data specification should be an Activity, but it's a "
							+ dataSpecParent.getClass().getName());
		}
	}

	private PartnerTrack getPartnerTrack() {
		return getActivity().getPartnerTrack();
	}

	private TestCase getTestCase() {
		final ITestArtefact partnerTrackParent = getPartnerTrack().getParent();
		if (partnerTrackParent instanceof TestCase) {
			return (TestCase) partnerTrackParent;
		} else {
			throw new IllegalArgumentException(
					"The parent of the partner track should be a TestCase, but it's a "
							+ partnerTrackParent.getClass().getName());
		}
	}

	private TestSuite getTestSuite() {
		final ITestArtefact testCaseParent = getTestCase().getParent();
		if (testCaseParent instanceof TestSuite) {
			return (TestSuite) testCaseParent;
		} else {
			throw new IllegalArgumentException(
					"The parent of the test case should be a TestSuite, but it's a "
							+ testCaseParent.getClass().getName());
		}
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return String.format("Data extraction to %s (scope %s)", fVariable, fScope);
	}

	@Override
	public ArtefactStatus getStatus() {
		return fStatus;
	}

	@Override
	public ITestArtefact getParent() {
		return fParent;
	}

	@Override
	public List<ITestArtefact> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		stateData.add(new StateData("Expression", fExpression));
		stateData.add(new StateData("Variable", fVariable));
		stateData.add(new StateData("Scope", fScope.toString()));
		stateData.add(new StateData("Extracted Value", fExtracted != null ? fExtracted.toString() : "(not yet)"));
		return stateData;
	}

	@Override
	public void reportProgress(ITestArtefact artefact) {
		fParent.reportProgress(artefact);
	}

}
