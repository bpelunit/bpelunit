package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.xml.suite.XMLAnyElement;

import org.apache.velocity.context.Context;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;

public class CompleteHumanTaskSpecification extends DataSpecification {

	private PartnerTrack partnerTrack;
	private List<ReceiveCondition> conditions;
	private XmlObject inputXMLData;
	private XMLAnyElement outputXMLData;
	
	public CompleteHumanTaskSpecification(Activity parent,
			NamespaceContext nsContext, XMLAnyElement xmlAnyElement, PartnerTrack partnerTrack) throws SpecificationException {
		super(parent, nsContext);
		this.outputXMLData = xmlAnyElement;
		this.partnerTrack = partnerTrack;
	}


	@Override
	public String getName() {
		return "Complete Task Specification";
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> returner = new ArrayList<ITestArtefact>();
		for (ReceiveCondition c : conditions ) {
			returner.add(c);
		}
		
		if(inputXMLData != null) {
			returner.add(new XMLData(this, "Input XML Data", inputXMLData.xmlText().trim()));
		} else {
			returner.add(new XMLData(this, "Input XML Data", "(no data)"));
		}
		returner.add(new XMLData(this, "Output XML Data", getOutputXMLData().trim()));

		return returner;
	}

	@Override
	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		return stateData;
	}

	public String getOutputXMLData() {
		return outputXMLData.xmlText();
	}

	public XmlObject handle(XmlObject input) {
		this.inputXMLData = input;
		
		validateConditions();
		
		return outputXMLData;
	}

	public void setConditions(List<ReceiveCondition> conditions) {
		this.conditions = conditions;
	}
	
	private void validateConditions() {
		// Create Velocity context for the conditions
		Context conditionContext;
		try {
			conditionContext = partnerTrack.createVelocityContext();
		} catch (Exception e) {
			fStatus = ArtefactStatus.createFailedStatus(String.format(
				"Could not create the Velocity context for this condition: %s",
				e.getLocalizedMessage()));
			return;
		}
		ContextXPathVariableResolver variableResolver = new ContextXPathVariableResolver(conditionContext);

		for (ReceiveCondition c : conditions) {
			c.evaluate(partnerTrack, (Element)inputXMLData.getDomNode(), fNamespaceContext, variableResolver);

			if (c.isFailure()) {
				fStatus = ArtefactStatus.createFailedStatus(String.format(
						"Condition '%s=%s' did not hold: %s",
						c.getExpression(), c.getExpectedValue(), c.getStatus()
								.getMessage()));
				break;
			} else if (c.isError()) {
				fStatus = ArtefactStatus.createErrorStatus(String.format(
						"Condition '%s=%s' had an error: %s.", c
								.getExpression(), c.getExpectedValue(), c
								.getStatus().getMessage()));
				break;
			}
		}
	}
}
