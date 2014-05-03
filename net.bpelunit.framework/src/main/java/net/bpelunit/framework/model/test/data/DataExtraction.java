package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.xml.suite.XMLVariableScope;
import net.bpelunit.framework.xml.suite.XMLVariableScope.Enum;

/**
 * Test artifact class for a data extraction request within a receive activity.
 *
 * This request asks for a certain piece of information (specified by an XPath
 * expression) to be extracted from the incoming message and stored in a
 * variable that can be reused in the following Velocity-based templates within
 * a certain scope.
 * 
 * @author Antonio García-Domínguez
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
	private ITestArtefact fParent;

	public DataExtraction(ITestArtefact parent, String expression, String variable, Enum scope) {
		this.fParent = parent;
		this.fExpression = expression;
		this.fVariable = variable;
		this.fScope = scope;
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
