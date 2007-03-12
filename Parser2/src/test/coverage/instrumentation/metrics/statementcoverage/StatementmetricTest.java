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

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;

public class StatementmetricTest {

	private Statementmetric metric;

	private Namespace ns;

	@Before
	public void setUp() throws Exception {
		metric = new Statementmetric();
		metric.addAllBasicActivities();
		ns = BpelXMLTools.NAMESPACE_BPEL_2;
	}

	@Test
	public void testInsertMarker() {
		Element process = createProzessElement();
		Element receive = createReceiveAktivity();
		process.addContent(receive);
		metric.insertMarker(process);
		assertTrue("Enclosed sequence not inserted",
				isElementInSequence(receive));
		Element sequence = process.getChild(
				StructuredActivity.SEQUENCE_ACTIVITY, ns);
		Element reveive = sequence.getChild(receive.getName(), receive
				.getNamespace());
		assertNotNull("Zu loggende Aktivität ist nicht vorhanden", receive);
		Comment comment = (Comment) sequence.getContent(1);
		assertTrue("Kommentar mit der Markierung fehlt", comment.getText()
				.contains(IMetric.MARKER_IDENTIFIRE));

		process = createProzessElement();
		Element throwElement = createThrowAktivity();
		process.addContent(throwElement);
		metric.insertMarker(process);
		assertTrue("Enclosed sequence not inserted",
				isElementInSequence(throwElement));
		assertTrue("Kommentar mit der Markierung fehlt",
				isCommentBevorActivity(throwElement));

		throwElement = createThrowAktivity();
		receive = createReceiveAktivity();
		process = createProzessElement();
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
		
		int i=0;
		for (Iterator iter = process.getDescendants(new ElementFilter(StructuredActivity.SEQUENCE_ACTIVITY,ns)); iter.hasNext();) {
			iter.next();
			i++;	
		}
		
		assertTrue("Zu viele oder zu wenige Sequence-Elementen eingefügt", i==2);

	}

	private boolean isCommentAfterActivity(Element element) {
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		if (index < parent.getContentSize()-1) {
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

	private boolean isElementInSequence(Element element) {
		Element parent = element.getParentElement();
		if (parent.getName().equals(StructuredActivity.SEQUENCE_ACTIVITY)) {
			return true;
		}
		return false;
	}

	private Element createThrowAktivity() {
		Element element = new Element(BasisActivity.THROW_ACTIVITY, ns);
		return element;

	}

	private Element createReceiveAktivity() {
		Element element = new Element(BasisActivity.RECEIVE_ACTIVITY, ns);
		return element;
	}

	private Element createProzessElement() {
		Element element = new Element("process", ns);
		return element;

	}
}
