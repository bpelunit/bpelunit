package coverage.instrumentation.metrics.branchcoverage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import coverage.exception.BpelException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;

public class FlowActivityHandler implements IStructuredActivity {

	private static final String LINK_TAG = "link";

	private static final String LINKS_TAG = "links";

	private static final String SOURCE_TAG = "source";

	private static final String TARGET_TAG = "target";

	private static final String TARGETS_TAG = "targets";

	private static final String TRANSITION_CONDITION_TAG = "transitionCondition";

	private static final String ATTRIBUTE_LINKNAME = "linkName";

	private static final String ATTRIBUTE_NAME = "name";

	private static final String COPY_LINK_POSTFIX = "_copy";

	private static final String COPY_LINK_NEGIERT_POSTFIX = "_negiert";

	private static final String COPY_LINK_DPE_POSTFIX = "_dpe";

	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		loggingOfLinks(element);
		loggingOfBranches(element);
	}

	private void loggingOfBranches(Element element) {
		List children = element.getChildren();
		Element child;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (BpelXMLTools.isActivity(child)) {
				BranchMetric.insertMarkerForBranch(child, "");
			}
		}

	}

	private void loggingOfLinks(Element element) throws BpelException {
		Element linksElement = element.getChild(LINKS_TAG, BpelXMLTools
				.getBpelNamespace());
		if (linksElement != null) {
			// List links = linksElement.getChildren(LINK_TAG,
			// ActivityTools.NAMESPACE_BPEL_2);
			// int size=links.size(); //TODO HE????
			// for (int i = 0; i < size; i++) {
			// createMarkerForLink((Element) links.get(i), element);
			// }
			Iterator iter = linksElement.getDescendants(new ElementFilter(
					LINK_TAG, BpelXMLTools.getBpelNamespace()));
			List<Element> links = new ArrayList<Element>();
			while (iter.hasNext()) {
				links.add((Element) iter.next());
			}
			for (int i = 0; i < links.size(); i++) {
				createMarkerForLink((Element) links.get(i), element);
			}
		}
	}

	private void createMarkerForLink(Element link, Element flow)
			throws BpelException {
		Element sourceElement = searchSourceElement(link, flow);
		if (sourceElement == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		Element enclosedFlow = BpelXMLTools.encloseElementInFlow(sourceElement
				.getParentElement().getParentElement());
		Element new_link = insertPostivLink(enclosedFlow, sourceElement, link);
		insertLoggingMarker(new_link, enclosedFlow, null);

		new_link = insertNegativLink(enclosedFlow, sourceElement, link);
		Comment negativLinkLoggingMarker = (Comment) insertLoggingMarker(
				new_link, enclosedFlow, null).clone();
		new_link = insertDPELink(enclosedFlow, sourceElement, link);
		insertLoggingMarker(new_link, enclosedFlow, negativLinkLoggingMarker);

	}

	private Element insertPostivLink(Element flow, Element sourceElement,
			Element link) {
		Element link_copy = createLinkCopy(link, flow, COPY_LINK_POSTFIX);
		Element new_source_element = new Element(SOURCE_TAG, BpelXMLTools
				.getBpelNamespace());
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		Element transConditionElement = sourceElement.getChild(
				TRANSITION_CONDITION_TAG, BpelXMLTools.getBpelNamespace());
		if (transConditionElement != null) {
			new_source_element.addContent((Element) transConditionElement
					.clone());
		}
		sourceElement.getParentElement().addContent(new_source_element);
		return link_copy;
	}

	private Element createLinkCopy(Element link, Element flow, String postfix) {
		Element link_copy = (Element) link.clone();
		link_copy.setAttribute(ATTRIBUTE_NAME, link
				.getAttributeValue(ATTRIBUTE_NAME)
				+ postfix);
		Element links = flow.getChild(LINKS_TAG, BpelXMLTools
				.getBpelNamespace());
		if (links == null) {
			links = new Element(LINKS_TAG, BpelXMLTools.getBpelNamespace());
			flow.addContent(0, links);
		}
		links.addContent(link_copy);
		return link_copy;
	}

	private Element insertDPELink(Element flow, Element sourceElement,
			Element link) {
		Element link_copy = createLinkCopy(link, flow, COPY_LINK_DPE_POSTFIX);
		Element new_source_element = new Element(SOURCE_TAG, BpelXMLTools
				.getBpelNamespace());
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
//			logging = new Comment(BranchMetric.getNextLabel() + " flow");
		}
		Element sequence = BpelXMLTools.createSequence();
		Element targetElement = new Element(TARGET_TAG, BpelXMLTools
				.getBpelNamespace());
		targetElement.setAttribute(ATTRIBUTE_LINKNAME, new_link
				.getAttributeValue(ATTRIBUTE_NAME));
		sequence.addContent(new Element(TARGETS_TAG, BpelXMLTools
				.getBpelNamespace()).addContent(targetElement));
		sequence.addContent(logging);
		enclosedFlow.addContent(sequence);
		return logging;

	}

	private Element insertNegativLink(Element flow, Element sourceElement,
			Element link) {
		Element link_copy = createLinkCopy(link, flow,
				COPY_LINK_NEGIERT_POSTFIX);
		Element new_source_element = new Element(SOURCE_TAG, BpelXMLTools
				.getBpelNamespace());
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		Element transConditionElement = sourceElement.getChild(
				TRANSITION_CONDITION_TAG, BpelXMLTools.getBpelNamespace());
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

	private Element searchSourceElement(Element link, Element flow) {
		Iterator sources = flow.getDescendants(new ElementFilter(SOURCE_TAG,
				BpelXMLTools.getBpelNamespace()));
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
