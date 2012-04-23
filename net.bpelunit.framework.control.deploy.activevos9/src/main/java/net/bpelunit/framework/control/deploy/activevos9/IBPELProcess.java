package net.bpelunit.framework.control.deploy.activevos9;

import java.io.InputStream;

import javax.xml.namespace.QName;

import org.w3c.dom.Document;

public interface IBPELProcess {

	QName getName();
	
	void addWSDLImport(String wsdlFileName, InputStream contents);
	
	void addPartnerlink(String name, QName partnerlinkType, String processRole, String partnerRole);
	
	void addPartnerlinkBinding(String partnerlinkName, QName service, String port, String endpointURL);
	
	Document getXML();
}
