package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.ILink;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TLink;

class Link extends AbstractBpelObject implements ILink {

	private TLink link;
	
	Link(TLink l) {
		super(l);
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
}
