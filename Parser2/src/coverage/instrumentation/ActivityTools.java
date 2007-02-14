package coverage.instrumentation;

import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

public class ActivityTools {

	public static Element ensureActivityInSequence(Element activity) {
		if (!activity.getName().equals(StructuredActivity.SEQUENCE_ACTIVITY)) {
			Namespace namespace = activity.getNamespace();
			Element parent = activity.getParentElement();
			int index = parent.indexOf(activity);
			Element sequence = new Element(
					StructuredActivity.SEQUENCE_ACTIVITY, namespace);
			activity.detach();
			sequence.addContent(activity);
			parent.addContent(index, sequence);
			activity = sequence;
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
				activity = element;
				break;
			}

		}
		return activity;
	}
}
