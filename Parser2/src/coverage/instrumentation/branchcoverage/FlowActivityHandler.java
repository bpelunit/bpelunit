package coverage.instrumentation.branchcoverage;

import java.util.Iterator;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import coverage.instrumentation.ActivityTools;
import coverage.instrumentation.StructuredActivity;
import coverage.instrumentation.branchcoverage.IStructuredActivity;

public class FlowActivityHandler implements IStructuredActivity {

	private static final String LINK_TAG = "link";

	private static final String LINKS_TAG = "links";

	private static final String SOURCE_TAG = "source";

	private static final String TARGET_TAG = "target";

	private static final String TRANSITION_CONDITION_TAG = "transitionCondition";

	private static final String ATTRIBUTE_LINKNAME = "linkName";

	private static final String ATTRIBUTE_NAME = "linkName";

	private static final String COPY_LINK_POSTFIX = "_copy";

	private static final String COPY_LINK_NEGIERT_POSTFIX = "_negiert";

	private static final String COPY_LINK_DPE_POSTFIX = "_dpe";

	public void insertMarkerForBranchCoverage(Element element) {
		List links = element.getChildren(LINK_TAG);
		for (int i = 0; i < links.size(); i++) {
			createMarkerForLink((Element) links.get(i), element);
		}
	}

	private void createMarkerForLink(Element link, Element flow) {
		Element sourceElement = searchSourceElement(link, flow);
		if (sourceElement != null) {
			Element enclosedFlow = ActivityTools
					.encloseActivityInFlow(sourceElement.getParentElement()
							.getParentElement());
			Element new_link = insertPostivLink(flow, sourceElement, link);
			insertLoggingMarker(new_link, enclosedFlow, null);
			new_link = insertNegativLink(flow, sourceElement, link);
			Comment negativLinkLoggingMarker = insertLoggingMarker(new_link,
					enclosedFlow, null);
			new_link = insertDPELink(flow, sourceElement, link);
			insertLoggingMarker(new_link, enclosedFlow,
					negativLinkLoggingMarker);
		}
	}

	private Element insertDPELink(Element flow, Element sourceElement,
			Element link) {
		Element link_copy = createLinkCopy(link, flow, COPY_LINK_DPE_POSTFIX);
		Element new_source_element = new Element(SOURCE_TAG, flow
				.getNamespace());
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		Element new_transConditEl = new Element(TRANSITION_CONDITION_TAG, flow
				.getNamespace());
		new_transConditEl.setText("false");
		sourceElement.getParentElement().addContent(new_source_element);
		return link_copy;
	}

	private Comment insertLoggingMarker(Element new_link, Element enclosedFlow,
			Comment loggingMarker) {
		Comment logging;
		if (loggingMarker != null && !loggingMarker.equals("")) {
			logging = loggingMarker;
		} else {
			logging = new Comment(BranchMetric.getNextLinkLabel());
		}
		Element sequence = new Element(StructuredActivity.SEQUENCE_ACTIVITY,
				enclosedFlow.getNamespace());
		sequence
				.addContent(new Element(TARGET_TAG, enclosedFlow.getNamespace()));
		sequence.addContent(loggingMarker);
		enclosedFlow.addContent(sequence);
		return logging;

	}

	private Element insertNegativLink(Element flow, Element sourceElement,
			Element link) {
		Element link_copy = createLinkCopy(link, flow,
				COPY_LINK_NEGIERT_POSTFIX);
		Element new_source_element = new Element(SOURCE_TAG, flow
				.getNamespace());
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		Element transConditionElement = sourceElement
				.getChild(TRANSITION_CONDITION_TAG);
		Element new_transConditEl;
		if (transConditionElement != null) {
			new_transConditEl = (Element) transConditionElement.clone();
			new_transConditEl.setText("!(" + new_transConditEl.getText() + ")");
		} else {
			new_transConditEl = new Element(TRANSITION_CONDITION_TAG, flow
					.getNamespace());
			new_source_element.setText("false");
		}
		sourceElement.getParentElement().addContent(new_source_element);
		return link_copy;
	}

	private Element insertPostivLink(Element flow, Element sourceElement,
			Element link) {
		Element link_copy = createLinkCopy(link, flow, COPY_LINK_POSTFIX);
		Element new_source_element = new Element(SOURCE_TAG, flow
				.getNamespace());
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		Element transConditionElement = sourceElement
				.getChild(TRANSITION_CONDITION_TAG);
		if (transConditionElement != null) {
			new_source_element.addContent((Element) transConditionElement
					.clone());
		}
		sourceElement.getParentElement().addContent(new_source_element);
		return link_copy;
	}

	private Element createLinkCopy(Element link, Element flow, String postfix) {
		Element link_copy = (Element) link.clone();
		link_copy.setAttribute(ATTRIBUTE_LINKNAME, link
				.getAttributeValue(ATTRIBUTE_NAME)
				+ postfix);
		flow.getChild(LINKS_TAG).addContent(link_copy);
		return link_copy;
	}

	private Element searchSourceElement(Element link, Element flow) {
		Iterator sources = flow.getDescendants(new ElementFilter(SOURCE_TAG,
				flow.getNamespace()));
		Element source = null;
		String linkName;
		while (sources.hasNext()) {
			source = (Element) sources.next();
			linkName = source.getAttributeValue(ATTRIBUTE_LINKNAME);
			if (link.getAttribute(ATTRIBUTE_NAME).equals(linkName)) {
				break;
			}
		}
		return source;
	}

}
