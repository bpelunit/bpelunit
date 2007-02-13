package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

import coverage.instrumentation.BasisActivity;
import coverage.instrumentation.StructuredActivity;

public class IfActivityHandler implements IStructuredActivity {

	private static final String ELSE_ELEMENT = "else";

	private static final String ELSE_IF_ELEMENT = "elseif";

	public void insertMarkerForBranchCoverage(Element element) {
		if (element.getChild(ELSE_ELEMENT) == null) {
			insertElseBranch(element);
		}
		List children = element.getChildren();
		Element branchActivityChild;
		for (int i = 0; i < children.size(); i++) {
			branchActivityChild = (Element) children.get(i);
			insertMarkerForBranch(branchActivityChild);
		}
	}

	private Element getBranchActivity(Element element) {
		Element result = null;
		String name = element.getName();
		List children;
		if (BasisActivity.isBasisActivity(element)
				|| StructuredActivity.isStructuredActivity(element)) {
			result = encloseInSequence(element);
		} else if (StructuredActivity.isStructuredActivity(element)) {
			result = element;
		} else if (name.equals(ELSE_IF_ELEMENT)) {
			children = element.getChildren();
			for (int i = 0; result != null && i < children.size(); i++) {
				result=getBranchActivity((Element)children.get(i));
			}
		} else if (name.equals(ELSE_ELEMENT)) {
			children = element.getChildren();
			for (int i = 0; result != null && i < children.size(); i++) {
				result=getBranchActivity((Element)children.get(i));
			}
		}
		return result;
	}

	private void insertMarkerForBranch(Element element) {
		Comment comment1=new Comment(BranchMetric.getNextLabel());
		Comment comment2=new Comment(BranchMetric.getNextLabel());
		Element parent=element.getParentElement();
		int index=parent.indexOf(element);
		parent.addContent(index+1, comment2);
		parent.addContent(index, comment1);
	}

	private Element encloseInSequence(Element element) {
		Namespace namespace = element.getNamespace();
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		Element sequence = new Element("sequence", namespace);
		element.detach();
		sequence.addContent(element);
		parent.addContent(index, sequence);
		return sequence;
	}

	private void insertElseBranch(Element element) {
		Element elseElement = new Element(ELSE_ELEMENT, element.getNamespace());
		element.addContent(element.getContentSize(), elseElement);
	}

}
