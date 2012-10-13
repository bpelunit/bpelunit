package net.bpelunit.model.bpel;

import java.util.List;


public interface IBpelObject {

	List<?extends IDocumentation> getDocumentation();
	IDocumentation addDocumentation();
	
	String getXPathInDocument();
}
