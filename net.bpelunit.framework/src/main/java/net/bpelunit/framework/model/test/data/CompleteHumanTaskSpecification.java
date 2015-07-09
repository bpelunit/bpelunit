package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.NamespaceContext;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.data.extraction.DataExtraction;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.xml.suite.XMLAnyElement;

import org.apache.velocity.context.Context;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;

public class CompleteHumanTaskSpecification extends DataSpecification {

	private final PartnerTrack partnerTrack;
	private List<ReceiveCondition> conditions;

	private XmlObject inputXMLData;
	private final String templateText;
	private XMLAnyElement outputXMLData;
	private List<DataExtraction> dataExtractions = new ArrayList<DataExtraction>();

	public CompleteHumanTaskSpecification(Activity parent,
			NamespaceContext nsContext, Element xmlLiteralOutputData,
			String templateText, PartnerTrack partnerTrack)
			throws SpecificationException {
		super(parent, nsContext);
		try {
			this.templateText = templateText;
			this.outputXMLData = xmlLiteralOutputData != null ? XMLAnyElement.Factory.parse(xmlLiteralOutputData, null) : null;
			this.partnerTrack = partnerTrack;
		} catch (XmlException e) {
			throw new SpecificationException("Could not save XML Element data: " +e.getMessage(), e);
		}
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
		stateData.addAll(getStatus().getAsStateData());
		return stateData;
	}

	public String getOutputXMLData() {
		return outputXMLData.xmlText();
	}

	public String getTemplateText() {
		return templateText;
	}

	public XMLAnyElement handle(ActivityContext context, XmlObject input) {
		this.inputXMLData = input;
		
		validateConditions();
		extractData(context, dataExtractions, (Element)input.getDomNode());

		if (templateText != null) {
			try {
				outputXMLData = XMLAnyElement.Factory.parse(generateLiteralDataFromTemplate(context, templateText));
			} catch (XmlException e) {
				setStatus(ArtefactStatus.createFailedStatus(String.format(
					"Could not generate the reply message from the template: %s",
					e.getLocalizedMessage())));
			}
		}

		return outputXMLData;
	}

	public void setConditions(List<ReceiveCondition> conditions) {
		this.conditions = conditions;
	}

	public void setDataExtractions(List<DataExtraction> dataExtractions) {
		this.dataExtractions = dataExtractions;
	}

	private void validateConditions() {
		// Create Velocity context for the conditions
		Context conditionContext;
		try {
			conditionContext = partnerTrack.createVelocityContext(this);
		} catch (Exception e) {
			setStatus(ArtefactStatus.createFailedStatus(String.format(
				"Could not create the Velocity context for this condition: %s",
				e.getLocalizedMessage())));
			return;
		}
		ContextXPathVariableResolver variableResolver = new ContextXPathVariableResolver(conditionContext);

		for (ReceiveCondition c : conditions) {
			c.evaluate(partnerTrack, (Element)inputXMLData.getDomNode(), getNamespaceContext(), variableResolver);

			if (c.isFailure()) {
				setStatus(ArtefactStatus.createFailedStatus(String.format(
						"Condition '%s=%s' did not hold: %s",
						c.getExpression(), c.getExpectedValue(), c.getStatus()
								.getMessage())));
				break;
			} else if (c.isError()) {
				setStatus(ArtefactStatus.createErrorStatus(String.format(
						"Condition '%s=%s' had an error: %s.", c
								.getExpression(), c.getExpectedValue(), c
								.getStatus().getMessage())));
				break;
			}
		}
	}
}
