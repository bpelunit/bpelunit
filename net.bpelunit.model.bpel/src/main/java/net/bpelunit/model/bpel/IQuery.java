package net.bpelunit.model.bpel;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public interface IQuery {

	String getQueryLanguage();
	void setQueryLanguage(String queryLanguageUrn);
	
	String getQueryContents();
	void setQueryContents(String query);
	
	List<Node> getNodes();
	Element addNewElement(String namespaceUri, String localName);
	Text addNewTextNode(String text);
}
