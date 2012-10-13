package net.bpelunit.model.bpel;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IDocumentation {

	/**
	 * Writable list of documentation elements
	 * @return
	 */
	List<Node> getDocumentationElements();
	
	void setDocumentationElement(Node doc);
	
	void setDocumentationElements(Element e);
}
