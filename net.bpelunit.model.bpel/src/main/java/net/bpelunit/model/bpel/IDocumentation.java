package net.bpelunit.model.bpel;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IDocumentation {

	List<Node> getDocumentationElements();
	
	String getStringContent();
	
	void setStringContent(String content);
	
	Element addDocumentationElement(String namespaceURI, String localName);
}
