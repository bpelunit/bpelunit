package net.bpelunit.model.bpel;

import java.util.List;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IDocumentation {

	List<Node> getDocumentationElements();
	
	String getStringContent();
	
	void setStringContent(String content);
	
	Element addDocumentationElement(String namespaceURI, String localName);
	Element addDocumentationElement(QName name);
}
