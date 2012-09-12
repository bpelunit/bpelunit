package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.ILink;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TFlow;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TLink;

class Flow extends AbstractMultiContainer<TFlow> implements IFlow {

	private TFlow flow;
	private List<Link> links = new ArrayList<Link>();

	Flow(TFlow wrappedFlow, BpelFactory f) {
		super(wrappedFlow, wrappedFlow.getActivity(), f);
		this.flow = wrappedFlow;

		for (TLink l : wrappedFlow.getLinks().getLink()) {
			links.add(getFactory().createLink(l));
		}
	}

	@Override
	public List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	@Override
	public Link addLink(String name) {
		TLink l = new TLink();
		l.setName(name);

		Link link = getFactory().createLink(l);
		flow.getLinks().getLink().add(l);
		links.add(link);

		return link;
	}

	@Override
	public void removeLink(ILink l) {
		checkForCorrectModel(l);

		int i = links.indexOf(l);

		links.remove(i);
		flow.getLinks().getLink().remove(i);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(IActivity a : getActivities()) {
			a.visit(v);
		}
	}
}
