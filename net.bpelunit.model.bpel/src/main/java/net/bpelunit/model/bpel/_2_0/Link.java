package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ILink;

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
}
