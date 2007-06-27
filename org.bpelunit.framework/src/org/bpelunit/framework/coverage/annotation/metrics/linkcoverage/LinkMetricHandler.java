package org.bpelunit.framework.coverage.annotation.metrics.linkcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.count;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createBPELElement;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.encloseElementInFlow;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.process_element;

import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
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

	public static final String NEGATIV_LINK_LABEL = "negativLinks";

	public static final String POSITIV_LINK_LABEL = "positivLinks";

	private MarkersRegisterForArchive markersRegistry;



	public LinkMetricHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry=markersRegistry;
	}

	/**
	 * Generiert eindeutige Merkierung für die Links (in der Flow-Umgebung)
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextPositivLinkMarker() {
		return POSITIV_LINK_LABEL + Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
	}

	/**
	 * Generiert eindeutige Merkierung für die Links (in der Flow-Umgebung)
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextNegativLinkMarker() {
		return  NEGATIV_LINK_LABEL + Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
	}

	/**
	 * Fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 * @throws BpelException 
	 */
	public void insertMarkersForMetric(List<Element> activities)
			throws BpelException {
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
			if (transConElement != null)
				transitionCondition = transConElement.getText();
		} else if (ns.equals(NAMESPACE_BPEL_1_1)) {
			transitionCondition = sourceElement
					.getAttributeValue(TRANSITION_CONDITION);
			if (transitionCondition == null)
				transitionCondition = "";
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
			if (attribut != null && attribut.equals(linkName))
				break;
		}
		return source;
	}


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

		String marker;
		if (isPositivValueOfLink) {		
			marker=getNextPositivLinkMarker();
			logging = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER +marker);
		} else {
			marker=getNextNegativLinkMarker();
			logging = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER +marker);
		}
		markersRegistry.registerMarker(marker);

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





}
