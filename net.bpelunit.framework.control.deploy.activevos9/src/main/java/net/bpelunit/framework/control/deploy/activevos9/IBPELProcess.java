package net.bpelunit.framework.control.deploy.activevos9;

import java.io.InputStream;

import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.DeploymentException;

import org.w3c.dom.Document;

public interface IBPELProcess {

	/**
	 * Returns the QName targetnamespace/name of the BPEL Process
	 * 
	 * @return full qualified name of the BPEL process
	 */
	QName getName();
	
	/**
	 * Adds a new import to BPEL and updates the deployment accordingly. Changes will immediately reflect on the BPEL DOM Tree
	 * that can be retrieved by getBpelXml().
	 * 
	 * @param wsdlFileName File name of the WSDL, can be changed in order to avoid naming conflicts in the deployment
	 * @param contents Contents of the WSDL file that will be included in the deployment archive and will be referenced by the BPEL Process
	 */
	void addWSDLImport(String wsdlFileName, InputStream contents);
	
	/**
	 * Returns an instance of the BPEL XML code that will be deployed. Changes to the XML tree will be reflected on the
	 * deployment and will get deployed to the server.
	 * 
	 * @return BPEL XML DOM Tree
	 */
	Document getBpelXml();

	
	/**
	 * Adds a new partnerlink to the BPEL process. Changes are immediately visible to the BPEL DOM Tree retrievable by getBpelXml()
	 * 
	 * @param name the name of the new partnerlink
	 * @param partnerlinkType QName of the partnerlinktype that must be referencable/imported by the BPEL process (see addWSDLImport)
	 * @param processRole the role as defined in the partnerlinkType for the process
	 * @param partnerRole the role as defined in the partnerlinkType for the partner
	 * @param service a WSDL service that
	 * @param port
	 * @param endpointURL
	 */
	void addPartnerlink(String name, QName partnerlinkType, String processRole,
			String partnerRole, QName service, String port, String endpointURL);
	
	/**
	 * Changes the endpoint configuration of a partner link to the the new endpoint location 
	 * 
	 * @param partnerLinkName partnerlink as declared in BPEL
	 * @param newEndpoint new endpoint location for the partner role endpoint of the partnerlink
	 * @throws DeploymentException is thrown for wrong parameters (e.g. no partnerlink with this name) or if the deployment archive is corrupt
	 */
	void changePartnerEndpoint(String partnerLinkName, String newEndpoint) throws DeploymentException;
}
