package test.coverage.instrumentation;

import org.jdom.Element;
import org.jdom.Namespace;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;

public class Factory {

	public static Namespace ns = BpelXMLTools.NAMESPACE_BPEL_2;

	public static boolean isElementInSequence(Element element) {
		Element parent = element.getParentElement();
		if (parent.getName().equals(StructuredActivity.SEQUENCE_ACTIVITY)) {
			return true;
		}
		return false;
	}

	public static Element createThrowAktivity() {
		Element element = new Element(BasisActivity.THROW_ACTIVITY, ns);
		return element;

	}

	public static Element createReceiveAktivity() {
		Element element = new Element(BasisActivity.RECEIVE_ACTIVITY, ns);
		return element;
	}

	public static Element createProzessElement() {
		Element element = new Element("process", ns);
		return element;

	}
}
