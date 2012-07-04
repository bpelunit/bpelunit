/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

/**
 * The definition of a partner web service of the PUT. Note that this can also be the client.
 * 
 * A partner knows its name, WSDL specification, and "simulating URL" and which it listens for
 * incoming calls.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class Partner extends AbstractPartner {

	private Definition fWSDLDefinition;

	private Definition fPartnerWSDLDefinition;

	public Partner(String name, Definition processWSDL, Definition partnerWSDL, String baseURL) throws SpecificationException {
		super(name, baseURL);

		fWSDLDefinition = processWSDL;
		fPartnerWSDLDefinition = partnerWSDL;
	}

	public SOAPOperationCallIdentifier getOperation(QName service, String port, String operationName, SOAPOperationDirectionIdentifier direction)
			throws SpecificationException {

		
		Service serviceDef = fWSDLDefinition.getService(service);
		Definition definition = fWSDLDefinition;
		
		if(serviceDef == null && fPartnerWSDLDefinition != null) {
			serviceDef = fPartnerWSDLDefinition.getService(service);
			definition = fPartnerWSDLDefinition;
		}
		
		return new SOAPOperationCallIdentifier(definition, service, port, operationName, direction);
	}
}
