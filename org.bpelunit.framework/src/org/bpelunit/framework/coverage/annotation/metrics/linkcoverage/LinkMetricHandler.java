package org.bpelunit.framework.coverage.annotation.metrics.linkcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createBPELElement;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.encloseElementInFlow;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.process_element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Annotator;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;



public class LinkMetricHandler implements  IMetricHandler {

	public static final String METRIC_NAME = "LinkMetric";

	public static final String LINK_TAG = "link";

	public static final String LINKS_TAG = "links";

	private static final String SOURCE_TAG = "source";

	private static final String TARGET_TAG = "target";

	private static final String TARGETS_TAG = "targets";

	private static final String TRANSITION_CONDITION = "transitionCondition";

	private static final String ATTRIBUTE_LINKNAME = "linkName";

	private static final String ATTRIBUTE_NAME = "name";

	private static final String COPY_LINK_POSTFIX = "_copy";

	private static final String COPY_LINK_NEGIERT_POSTFIX = "_negiert";

	private static int count = 0;

	public static final String NEGATIV_LINK_LABEL = "negativLinks";

	public static final String POSITIV_LINK_LABEL = "positivLinks";

	public String getName() {
		return METRIC_NAME;
	}

	public LinkMetricHandler() {
	}

	/**
	 * Generiert eindeutige Merkierung für die Links (in der Flow-Umgebung)
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextPositivLinkLabel() {
		String marker = POSITIV_LINK_LABEL + Annotator.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
		LabelsRegistry.getInstance().addMarker(marker);
		return Annotator.COVERAGE_LABEL_IDENTIFIER + marker;
	}

	/**
	 * Generiert eindeutige Merkierung für die Links (in der Flow-Umgebung)
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextNegativLinkLabel() {
		String marker = NEGATIV_LINK_LABEL + Annotator.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
		LabelsRegistry.getInstance().addMarker(marker);
		return Annotator.COVERAGE_LABEL_IDENTIFIER + marker;
	}

	public void insertCoverageLabels(List<Element> activities)
			throws BpelException {
		// Iterator<Element> flowElemensIter = process_element
		// .getDescendants(new ElementFilter(
		// StructuredActivity.FLOW_ACTIVITY,
		// bpelFileData.getBPELProcessNamespace()));
		// List<Element> elements_to_log=new ArrayList<Element>();
		// while (flowElemensIter.hasNext()) {
		// elements_to_log.add(flowElemensIter.next());
		// }
		//		
		// for (Iterator<Element> iter = elements_to_log.iterator();
		// iter.hasNext();) {
		// loggingOfLinks(iter.next());
		// }


		for (Iterator<Element> iter = activities.iterator(); iter
				.hasNext();) {
			loggingOfLinks2(iter.next(), process_element);
		}

	}

	private void loggingOfLinks2(Element link, Element processElement) {
		Element sourceElement = searchSourceElement2(link, processElement);
		String transitionCondition = checkTransitionCondition(sourceElement);
		if (!transitionCondition.equals("")&&!transitionCondition.equals("true()")
				&& !transitionCondition.equals("false()")) {
			createMarkerForLink2(link, sourceElement, transitionCondition);
		}

	}

	private void createMarkerForLink2(Element link, Element sourceElement,
			String transitionCondition) {

		// Element flow = link.getParentElement();
		Element sourceActivity = null;
		if (getProcessNamespace().equals(NAMESPACE_BPEL_2_0)) {
			sourceActivity = sourceElement.getParentElement()
					.getParentElement();
		} else if (getProcessNamespace().equals(
				NAMESPACE_BPEL_1_1)) {
			sourceActivity = sourceElement.getParentElement();
		}
		Element enclosedFlow = encloseElementInFlow(sourceActivity);
		Element new_link = insertPostivLink(enclosedFlow, sourceElement, link,
				transitionCondition);
		insertLoggingMarker(new_link, enclosedFlow, true);
		new_link = insertNegativLink(enclosedFlow, sourceElement, link,transitionCondition);
		insertLoggingMarker(new_link, enclosedFlow, false);

	}

	private String checkTransitionCondition(Element sourceElement) {
		Namespace ns = getProcessNamespace();
		String transitionCondition = "";
		Element transConElement;
		if (ns.equals(NAMESPACE_BPEL_2_0)) {
			transConElement = sourceElement.getChild(TRANSITION_CONDITION, ns);
			if (transConElement != null) {
				transitionCondition = transConElement.getText();
			}
		} else if (ns.equals(NAMESPACE_BPEL_1_1)) {
			transitionCondition = sourceElement
					.getAttributeValue(TRANSITION_CONDITION);
			if (transitionCondition == null) {
				transitionCondition = "";
			}
		}
		return transitionCondition.trim();
	}

	private Element searchSourceElement2(Element link, Element processElement) {
		Iterator<Element> iter = processElement
				.getDescendants(new ElementFilter(SOURCE_TAG, getProcessNamespace()));
		Element source = null;
		String linkName = link.getAttributeValue(ATTRIBUTE_NAME);
		while (iter.hasNext()) {
			source = iter.next();
			String attribut = source.getAttributeValue(ATTRIBUTE_LINKNAME);
			if (attribut != null && attribut.equals(linkName)) {
				break;
			}
		}
		return source;
	}

	// private void loggingOfLinks(Element element) throws BpelException {
	// Element linksElement = element.getChild(LINKS_TAG, bpelFileData
	// .getBPELProcessNamespace());
	// if (linksElement != null) {
	// Iterator iter = linksElement.getDescendants(new ElementFilter(
	// LINK_TAG, bpelFileData.getBPELProcessNamespace()));
	// List<Element> links = new ArrayList<Element>();
	// while (iter.hasNext()) {
	// links.add((Element) iter.next());
	// }
	// for (int i = 0; i < links.size(); i++) {
	// createMarkerForLink((Element) links.get(i), element);
	// }
	// }
	// }

	// private void createMarkerForLink(Element link, Element flow)
	// throws BpelException {
	// Element sourceElement = searchSourceElement(link, flow);
	// if (sourceElement == null) {
	// throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
	// }
	// Element enclosedFlow = encloseElementInFlow(sourceElement
	// .getParentElement().getParentElement());
	// Element new_link = insertPostivLink(enclosedFlow, sourceElement, link);
	// insertLoggingMarker(new_link, enclosedFlow, true);
	//
	// new_link = insertNegativLink(enclosedFlow, sourceElement, link);
	// insertLoggingMarker(new_link, enclosedFlow, false);
	// }

	private Element insertPostivLink(Element flow, Element sourceElement,
			Element link, String transitionCondition) {
		Element link_copy = createLinkCopy(link, flow, COPY_LINK_POSTFIX);
		Element new_source_element = createBPELElement(SOURCE_TAG);
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		if (getProcessNamespace().equals(NAMESPACE_BPEL_2_0)) {
			Element transConditionElement = (Element)sourceElement.getChild(
					TRANSITION_CONDITION, getProcessNamespace()).clone();
			new_source_element.addContent((Element) transConditionElement
					.clone());
		} else if (getProcessNamespace().equals(
				NAMESPACE_BPEL_1_1)) {
			new_source_element.setAttribute(TRANSITION_CONDITION,
					transitionCondition);
		}
		sourceElement.getParentElement().addContent(0, new_source_element);
		return link_copy;
	}

	private Element createLinkCopy(Element link, Element flow, String postfix) {
		Element link_copy = (Element) link.clone();
		link_copy.setAttribute(ATTRIBUTE_NAME, link
				.getAttributeValue(ATTRIBUTE_NAME)
				+ postfix);
		Element links = flow.getChild(LINKS_TAG, getProcessNamespace());
		if (links == null) {
			links = createBPELElement(LINKS_TAG);
			flow.addContent(0, links);
		}
		links.addContent(link_copy);
		return link_copy;
	}

	private void insertLoggingMarker(Element new_link, Element enclosedFlow,
			boolean isPositivValueOfLink) {
		Comment logging;

		if (isPositivValueOfLink) {
			logging = new Comment(getNextPositivLinkLabel());
		} else {
			logging = new Comment(getNextNegativLinkLabel());
		}

		Element sequence = createSequence();
		Element targetElement = createBPELElement(TARGET_TAG);
		targetElement.setAttribute(ATTRIBUTE_LINKNAME, new_link
				.getAttributeValue(ATTRIBUTE_NAME));
		if (getProcessNamespace().equals(NAMESPACE_BPEL_2_0)) {
			Element targets = createBPELElement(TARGETS_TAG);
			targets.addContent(targetElement);
			targetElement=targets;
		} 
		sequence.addContent(targetElement);
		sequence.addContent(logging);
		enclosedFlow.addContent(sequence);
	}

	private Element insertNegativLink(Element flow, Element sourceElement,
			Element link, String transitionCondition) {
		Element link_copy = createLinkCopy(link, flow,
				COPY_LINK_NEGIERT_POSTFIX);
		Element new_source_element = createBPELElement(SOURCE_TAG);
		new_source_element.setAttribute(ATTRIBUTE_LINKNAME, link_copy
				.getAttributeValue(ATTRIBUTE_NAME));
		if (getProcessNamespace().equals(NAMESPACE_BPEL_2_0)) {
			Element transConditionElement =(Element) sourceElement.getChild(
					TRANSITION_CONDITION, getProcessNamespace()).clone();
			transConditionElement.setText("not("+transConditionElement.getText()+ ")");
			new_source_element.addContent(transConditionElement);
		} else if (getProcessNamespace().equals(
				NAMESPACE_BPEL_1_1)) {
			new_source_element.setAttribute(TRANSITION_CONDITION, "not("
					+ transitionCondition + ")");
		}
		sourceElement.getParentElement().addContent(0,new_source_element);
		return link_copy;
	}

//	private Element searchSourceElement(Element link, Element flow) {
//		Iterator sources = flow.getDescendants(new ElementFilter(SOURCE_TAG,
//				getProcessNamespace()));
//		Element source = null;
//		String linkName;
//		while (sources.hasNext()) {
//			source = (Element) sources.next();
//			linkName = source.getAttributeValue(ATTRIBUTE_LINKNAME);
//			if (link.getAttribute(ATTRIBUTE_NAME).equals(linkName)) {
//				break;
//			}
//		}
//		return source;
//	}

	public List<String> getPrefix4CovLabeles() {
		List<String> list = new ArrayList<String>(2);
		list.add(LinkMetricHandler.POSITIV_LINK_LABEL);
		list.add(LinkMetricHandler.NEGATIV_LINK_LABEL);

		return list;
	}



}
