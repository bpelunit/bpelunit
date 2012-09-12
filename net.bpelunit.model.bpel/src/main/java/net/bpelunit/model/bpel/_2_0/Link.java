package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ILink;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TLink;

class Link extends AbstractBpelObject implements ILink {

	private TLink link;
	
	Link(TLink l, BpelFactory f) {
		super(l, f);
		link = l;
	}

	@Override
	public String getName() {
		return link.getName();
	}

	@Override
	public void setName(String value) {
		link.setName(value);
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == link) {
			return this;
		} else {
			return null;
		}
	}
	
	@Override
	void visit(IVisitor v) {
	}
}
