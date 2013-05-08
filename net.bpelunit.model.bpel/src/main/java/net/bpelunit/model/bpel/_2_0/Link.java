package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ILink;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TLink;

class Link extends AbstractBpelObject implements ILink {

	TLink link;
	
	Link(TLink l) {
		super(l);
		link = l;
	}

	public String getName() {
		return link.getName();
	}

	public void setName(String value) {
		link.setName(value);
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
	}
	
	@Override
	public String toString() {
		return "Link";
	}
}
