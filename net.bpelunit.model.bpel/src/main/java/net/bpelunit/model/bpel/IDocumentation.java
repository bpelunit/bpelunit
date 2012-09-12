package net.bpelunit.model.bpel;

import java.util.List;

public interface IDocumentation {

	/**
	 * Writable list of documentation elements
	 * @return
	 */
	List<Object> getDocumentationElements();
	
	void setDocumentationElement(Object doc);
	
	void setDocumentationElements(List<Object> e);
}
