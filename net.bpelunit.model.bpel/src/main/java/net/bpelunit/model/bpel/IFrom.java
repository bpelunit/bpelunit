package net.bpelunit.model.bpel;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;



public interface IFrom extends IBpelObject {

	public enum Roles {
		PARTNER_ROLE, MY_ROLE
	}

	void setEndpointReference(Roles value);

	Roles getEndpointReference();

	void setPartnerLink(String value);

	String getPartnerLink();

	void setPart(String value);

	String getPart();

	void setVariable(String value);

	String getVariable();

	void setExpressionLanguage(String value);

	String getExpressionLanguage();

	void setVariable(IVariable v);

	IQuery setNewQuery();
	IQuery getQuery();
	
	Element setNewLiteral(String namespaceUri, String localName);
	Element getLiteral();

	QName getProperty();

	void setProperty(QName string);
	
	void addGlobalNamespace(String prefix, String namespaceUri);
	void addLocalNamespace(String prefix, String namespaceUri);
	String getNamespacePrefix(String namespaceUri);
}
