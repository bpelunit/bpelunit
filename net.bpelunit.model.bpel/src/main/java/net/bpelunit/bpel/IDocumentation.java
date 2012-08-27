package net.bpelunit.bpel;

import java.util.List;

public interface IDocumentation {

	/**
	 * Writable list of documentation elements
	 * @return
	 */
	List<Object> getDocumentationElements();
	
	void setDocumentationElement(String doc);
	
	void setDocumentationElements(List<Object> e);
}
