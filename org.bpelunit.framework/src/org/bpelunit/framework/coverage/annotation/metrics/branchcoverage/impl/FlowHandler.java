package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;


import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;



public class FlowHandler implements IStructuredActivityHandler {

	public void insertBranchMarkers(Element element)
			throws BpelException {
		loggingOfBranches(element);
	}

	private void loggingOfBranches(Element element) {
		List children = element.getChildren();
		Element child;
		try {
			for (int i = 0; i < children.size(); i++) {
				child = (Element) children.get(i);
				if (isActivity(child)) {
					if (isTargetOfLinks(child)) {
						BranchMetricHandler.insertLabelBevorAllActivities(child);
					} else {
						List<Element> createInstanceElement = getCreateInstanceActivity(child);
						if (createInstanceElement.size()>0) {
							for (Iterator<Element> iter = createInstanceElement
									.iterator(); iter.hasNext();) {
								Element el=iter.next();
								ensureElementIsInSequence(el);
								BranchMetricHandler.insertLabelAfterActivity(el);
							}
							
						} else {
							BranchMetricHandler.insertLabelBevorAllActivities(child);
						}
					}
				}
			}
		} catch (Exception e) {
			LabelsRegistry.getInstance().addInfo(e.getMessage());
			e.printStackTrace();
		}

	}

	private List<Element> getCreateInstanceActivity(Element activity) {
		List<Element> list=new ArrayList<Element>();
		String name = activity.getName();
		if (name.equals(StructuredActivities.PICK_ACTIVITY)
				|| name.equals(BasicActivities.RECEIVE_ACTIVITY)) {
			if (canCreateInstance(activity)) {
				list.add(activity);
				return list;
			}
		}
		if (isStructuredActivity(activity)) {
			Iterator<Element> iter=activity.getDescendants(new ContentFilter(ContentFilter.ELEMENT) {

				@Override
				public boolean matches(Object obj) {
					if (super.matches(obj)) {
						Element el = (Element) obj;
						if(canCreateInstance(el)){
							return true;
						}
					}
					return false;
				}

			});
			while(iter.hasNext()){
				list.add(iter.next());
			}
		}
		return list;
	}

	private boolean isTargetOfLinks(Element element) {
		boolean isTarget = false;
		Namespace ns = getProcessNamespace();
		Element target;
		if (ns.equals(NAMESPACE_BPEL_1_1)) {
			target = element.getChild(TARGET_ELEMENT, ns);
			if (target != null)
				isTarget = true;
		} else if (ns.equals(NAMESPACE_BPEL_2_0)) {
			target = element.getChild(TARGETS_ELEMENT, ns);
			if (target != null)
				isTarget = true;
		}
		return isTarget;
	}

}
