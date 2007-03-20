package test.coverage.instrumentation.metrics.statementcoverage;

import static org.junit.Assert.*;

import java.util.Iterator;


import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.junit.Before;
import org.junit.Test;

import test.coverage.instrumentation.Factory;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import coverage.wstools.CoverageRegistry;

public class StatementmetricTest {

	private Statementmetric metric;

	private Namespace ns;

	private Element process;

	@Before
	public void setUp() throws Exception {
		metric = new Statementmetric();
		metric.addAllBasicActivities();
		ns = BpelXMLTools.NAMESPACE_BPEL_2;
		CoverageRegistry.getInstance().addMetric(metric);
		process = Factory.createProzessElement();
		BpelXMLTools.process_element = process;
	}

	@Test
	public void testInsertMarker() {

		testMarkerAfterAktivity();
		process.removeContent();

		testMarkerBeforActivity();
		process.removeContent();

		testSequenceInSequence();

		testAktivityWithTarget();

	}

	private void testSequenceInSequence() {
		Element receive;
		Element sequence;
		Element throwElement;
		throwElement = Factory.createThrowAktivity();
		receive = Factory.createReceiveAktivity();
		sequence = BpelXMLTools.createSequence();
		process.addContent(sequence);
		sequence.addContent(throwElement);
		sequence = BpelXMLTools.createSequence();
		process.addContent(sequence);
		sequence.addContent(receive);

		metric.insertMarker(process);
		assertTrue("Kommentar mit der Markierung fehlt",
				isCommentBevorActivity(throwElement));
		assertTrue("Kommentar mit der Markierung fehlt",
				isCommentAfterActivity(receive));

		int i = 0;
		for (Iterator iter = process.getDescendants(new ElementFilter(
				StructuredActivity.SEQUENCE_ACTIVITY, ns)); iter.hasNext();) {
			iter.next();
			i++;
		}
		assertFalse("Zu wenige Sequence-Elementen eingefügt", i < 2);
		assertFalse("Zu viele Sequence-Elementen eingefügt", i > 2);
	}

	private void testMarkerBeforActivity() {
		Element throwElement = Factory.createThrowAktivity();
		process.addContent(throwElement);
		metric.insertMarker(process);
		assertTrue("Enclosed sequence not inserted",
				Factory.isElementInSequence(throwElement));
		assertTrue("Kommentar mit der Markierung fehlt",
				isCommentBevorActivity(throwElement));
	}

	private Comment testMarkerAfterAktivity() {
		Element receive = Factory.createReceiveAktivity();
		process.addContent(receive);
		metric.insertMarker(process);
		assertTrue("Enclosed sequence not inserted",
				Factory.isElementInSequence(receive));
		Element sequence = process.getChild(
				StructuredActivity.SEQUENCE_ACTIVITY, ns);
		Element reveive = sequence.getChild(receive.getName(), receive
				.getNamespace());
		assertNotNull("Zu loggende Aktivität ist nicht vorhanden", receive);
		Comment comment = (Comment) sequence.getContent(1);
		assertTrue("Kommentar mit der Markierung fehlt", comment.getText()
				.contains(IMetric.MARKER_IDENTIFIRE));
		return comment;
	}

	private void testAktivityWithTarget() {
		Element receive;
		Element throwElement;
		process.removeContent();
		Element flow = new Element(StructuredActivity.FLOW_ACTIVITY, ns);
		Element links = new Element("links", ns);
		Element link = new Element("link", ns);
		link.setAttribute("name", "l1");
		links.addContent(link);
		flow.addContent(links);
		Element sources = new Element("sources", ns);
		Element source = new Element("source", ns);
		source.setAttribute("linkName", "l1");
		sources.addContent(source);
		receive = Factory.createReceiveAktivity();
		receive.addContent(sources);
		throwElement = Factory.createThrowAktivity();
		Element targets = new Element("targets", ns);
		Element target = new Element("target", ns);
		target.setAttribute("linkName", "l1");
		targets.addContent(target);
		throwElement.addContent(targets);
		flow.addContent(receive);
		flow.addContent(throwElement);
		process.addContent(flow);
		BpelXMLTools.sysout(process);
		metric.insertMarker(process);
		Element parent = targets.getParentElement();
		assertEquals("Das targets-Element wurde falsch platziert.", parent
				.getName(), StructuredActivity.SEQUENCE_ACTIVITY);
		BpelXMLTools.sysout(process);
		assertEquals(
				"Die throw-Aktivität und targets-Element sind nicht in einer sequence.",
				parent, throwElement.getParentElement());
	}

	private boolean isCommentAfterActivity(Element element) {
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		if (index < parent.getContentSize() - 1) {
			Content child = parent.getContent(index + 1);
			if (child instanceof Comment) {
				Comment com = (Comment) child;
				if (com.getText().contains(IMetric.MARKER_IDENTIFIRE)) {
					return true;
				}
			}

		}
		return false;
	}

	private boolean isCommentBevorActivity(Element element) {
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		if (index > 0) {
			Content child = parent.getContent(index - 1);
			if (child instanceof Comment) {
				Comment com = (Comment) child;
				if (com.getText().contains(IMetric.MARKER_IDENTIFIRE)) {
					return true;
				}
			}

		}
		return false;
	}


}
