package net.bpelunit.bpel;

import java.util.List;


public interface IBpelObject {

	IBpelFactory getFactory();

	List<?extends IDocumentation> getDocumentation();
	IDocumentation addDocumentation();
	
	String getXPathInDocument();
}
