package coverage.instrumentation;

import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

public class ActivityTools {
	public static final Namespace NAMESPACE_BPEL_2 = Namespace
			.getNamespace("http://docs.oasis-open.org/wsbpel/2.0/process/executable");

	public static final String PROCESS_ELEMENT = "process";

	public static Element encloseActivityInSequence(Element activity) {
		Element parent = activity.getParentElement();
		int index = parent.indexOf(activity);
		Element sequence = createSequence();
		sequence.addContent(activity.detach());
		parent.addContent(index, sequence);
		activity = sequence;

		return activity;
	}

	public static Element ensureActivityIsInSequence(Element activity) {
		if (!activity.getName().equals(StructuredActivity.SEQUENCE_ACTIVITY)) {
			encloseActivityInSequence(activity);
		}
		return activity;
	}

	public static boolean isActivity(Element element) {
		boolean activity = false;
		if (BasisActivity.isBasisActivity(element)
				|| StructuredActivity.isStructuredActivity(element)) {
			activity = true;
		}
		return activity;
	}

	public static Element getActivity(Element element) {
		Element activity = null;
		List children = element.getChildren();
		Element child;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (BasisActivity.isBasisActivity(child)
					|| StructuredActivity.isStructuredActivity(child)) {
				activity = child;
				break;
			}

		}
		return activity;
	}

	public static Element encloseActivityInFlow(Element activity) {
		if (!activity.getName().equals(StructuredActivity.FLOW_ACTIVITY)) {
			;
			Element parent = activity.getParentElement();
			int index = parent.indexOf(activity);
			Element sequence = new Element(StructuredActivity.FLOW_ACTIVITY,
					NAMESPACE_BPEL_2);
			activity.detach();
			sequence.addContent(activity);
			parent.addContent(index, sequence);
			activity = sequence;
		}

		return activity;
	}

	public static Element createSequence() {
		return new Element(StructuredActivity.SEQUENCE_ACTIVITY,
				NAMESPACE_BPEL_2);
	}

}
