package net.bpelunit.model.bpel;

import java.util.List;

public interface IFlow extends IMultiContainer {

	List<?extends ILink> getLinks();

	ILink addLink(String name);

	void removeLink(ILink l);

}
