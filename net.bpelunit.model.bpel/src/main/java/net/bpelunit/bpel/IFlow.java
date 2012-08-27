package net.bpelunit.bpel;

import java.util.List;

public interface IFlow {

	List<?extends ILink> getLinks();

	ILink addLink(String name);

	void removeLink(ILink l);

}
