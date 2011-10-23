/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model;

import java.io.File;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

import com.ibm.wsdl.Constants;

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
public class Partner {

	/**
	 * The name of the partner, identifying it in the test suite document and in the URLs of the
	 * partner WSDL.
	 * 
	 */
	private String fName;

	/**
	 * Path to the WSDL file (including file name) of this partner, relative to the test base path,
	 * which denotes the location of the .bpts file.
	 */
	private String fPathToWSDL;

	/**
	 * The URL which this partner simulates (base url plus partner name)
	 */
	private String fSimulatedURL;

	/**
	 * Base path of the test suite (location of .bpts file)
	 */
	private String fBasePath;

	private String fPathToPartnerWSDL;

	private Definition fWSDLDefinition;

	private Definition fPartnerWSDLDefinition;

	public Partner(String name, String testBasePath, String wsdlName, String partnerWSDLName, String baseURL) throws SpecificationException {
		fName= name;
		
		fBasePath= testBasePath;

		fSimulatedURL= baseURL;
		if (!fSimulatedURL.endsWith("/"))
			fSimulatedURL+= "/";
		fSimulatedURL+= fName;

		fPathToWSDL= testBasePath + wsdlName;
		fWSDLDefinition = loadWsdlDefinition(fPathToWSDL);
		
		if(partnerWSDLName != null && !"".equals(partnerWSDLName)) {
			fPathToPartnerWSDL = testBasePath + partnerWSDLName;
			fPartnerWSDLDefinition = loadWsdlDefinition(fPathToPartnerWSDL);
		}
	}

	private Definition loadWsdlDefinition(String wsdlFileName)
			throws SpecificationException {
		// Check file exists
		if (!new File(wsdlFileName).exists())
			throw new SpecificationException(
					"Cannot read WSDL file for partner " + getName()
							+ ": File \"" + wsdlFileName + "\" not found.");

		// load WSDL
		try {
			WSDLFactory factory = WSDLFactory.newInstance();
			WSDLReader reader = factory.newWSDLReader();
			reader.setFeature(Constants.FEATURE_VERBOSE, false);
			return reader.readWSDL(wsdlFileName);
		} catch (WSDLException e) {
			throw new SpecificationException(
					"Error while reading WSDL for partner " + getName()
							+ " from file \"" + wsdlFileName + "\".", e);
		}
	}

	public String getName() {
		return fName;
	}

	public String getSimulatedURL() {
		return fSimulatedURL;
	}

	public String getBasePath() {
		return fBasePath;
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
	
	@Override
	public String toString() {
		return getName();
	}

}
