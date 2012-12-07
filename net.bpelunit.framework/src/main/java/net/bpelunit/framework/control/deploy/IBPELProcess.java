package net.bpelunit.framework.control.deploy;

import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.model.bpel.IProcess;

public interface IBPELProcess {

	/**
	 * Returns the QName targetnamespace/name of the BPEL Process
	 * 
	 * @return full qualified name of the BPEL process
	 */
	QName getName();

	/**
	 * Adds a new import to BPEL and updates the deployment accordingly. Changes
	 * will immediately reflect on the BPEL Model retrievable by
	 * getProcessModel() that can be retrieved by getBpelXml().
	 * 
	 * @param wsdlFileName
	 *            File name of the WSDL, can be changed in order to avoid naming
	 *            conflicts in the deployment
	 * @param namespace
	 * @param contents
	 *            Contents of the WSDL file that will be included in the
	 *            deployment archive and will be referenced by the BPEL Process
	 * @throws DeploymentException
	 */
	void addWSDLImport(String wsdlFileName, String namespace,
			InputStream contents) throws DeploymentException;

	/**
	 * Adds a new import to BPEL and updates the deployment accordingly. Changes
	 * will immediately reflect on the BPEL DOM Tree that can be retrieved by
	 * getBpelXml().
	 * 
	 * @param xsdFileName
	 *            File name of the WSDL, can be changed in order to avoid naming
	 *            conflicts in the deployment
	 * @param contents
	 *            Contents of the WSDL file that will be included in the
	 *            deployment archive and will be referenced by the BPEL Process
	 * @throws DeploymentException
	 */
	void addXSDImport(String xsdFileName, String namespace, InputStream contents)
			throws DeploymentException;

	/**
	 * Returns an instance of the BPEL XML code that will be deployed. Changes
	 * to the XML tree will be reflected on the deployment and will get deployed
	 * to the server.
	 * 
	 * @return BPEL Model
	 */
	IProcess getProcessModel();

	/**
	 * Adds a new partnerlink to the BPEL process. Changes are immediately
	 * visible to the BPEL Model retrievable by getProcessModel()
	 * 
	 * @param name
	 *            the name of the new partnerlink
	 * @param partnerlinkType
	 *            QName of the partnerlinktype that must be
	 *            referencable/imported by the BPEL process (see addWSDLImport)
	 * @param processRole
	 *            the role as defined in the partnerlinkType for the process
	 *            null if process does not offer a service
	 * @param processEndpointSuffix
	 *            path element used for constructing the service endpoint URL; must not be null if processRole != null
	 * @param partnerRole
	 *            the role as defined in the partnerlinkType for the partner
	 * @param service
	 *            a WSDL service that is being used in the deployment to call
	 *            the service; must not be null if partnerRole != null
	 * @param port
	 *            a WSDL port in service that is being used in the deployment to
	 *            call the service; must not be null if partnerRole != null
	 * @param endpointURL
	 *            physical endpoint to call; if null value from WSDL will be
	 *            used
	 * @return The physical endpoint for the service provided by the process or
	 *         null if processRole was null
	 */
	URL addPartnerlink(String name, QName partnerlinkType, String processRole,
			String processEndpointSuffix, String partnerRole,
			QName partnerService, String partnerPort, String partnerEndpointURL);

	/**
	 * Changes the endpoint configuration of a partner link to the the new
	 * endpoint location
	 * 
	 * @param partnerLinkName
	 *            partnerlink as declared in BPEL
	 * @param newEndpoint
	 *            new endpoint location for the partner role endpoint of the
	 *            partnerlink
	 * @throws DeploymentException
	 *             is thrown for wrong parameters (e.g. no partnerlink with this
	 *             name) or if the deployment archive is corrupt
	 */
	void changePartnerEndpoint(String partnerLinkName, String newEndpoint)
			throws DeploymentException;
}
