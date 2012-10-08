package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.ILink;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TLink;

class Flow extends AbstractMultiContainer<TFlow> implements IFlow {

	private TFlow flow;
	private List<Link> links = new ArrayList<Link>();

	Flow(TFlow wrappedFlow, BpelFactory f) {
		super(wrappedFlow, f);
		setNativeActivity(wrappedFlow);
	}

	@Override
	protected void setNativeActivity(XmlObject newNativeActivity) {
		super.setNativeActivity(newNativeActivity);
		this.flow = (TFlow) newNativeActivity;

		if(!this.flow.isSetLinks()) {
			this.flow.addNewLinks();
		}
		
		for (TLink l : this.flow.getLinks().getLinkArray()) {
			links.add(getFactory().createLink(l));
		}
	}

	public List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public Link addLink(String name) {
		TLink l = flow.getLinks().addNewLink();
		l.setName(name);

		Link link = getFactory().createLink(l);
		links.add(link);

		return link;
	}

	public void removeLink(ILink l) {
		checkForCorrectModel(l);

		int i = links.indexOf(l);

		links.remove(i);
		flow.getLinks().removeLink(i);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(IActivity a : getActivities()) {
			a.visit(v);
		}
	}
}
