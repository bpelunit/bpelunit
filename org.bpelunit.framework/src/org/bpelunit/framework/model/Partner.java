/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model;

import java.io.File;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

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

	private Definition fWSDLDefinition;

	public Partner(String name, String testBasePath, String wsdlName, String baseURL) throws SpecificationException {
		fName= name;
		fPathToWSDL= testBasePath + wsdlName;
		fBasePath= testBasePath;

		fSimulatedURL= baseURL;
		if (!fSimulatedURL.endsWith("/"))
			fSimulatedURL+= "/";
		fSimulatedURL+= fName;

		// Check file exists
		if (!new File(fPathToWSDL).exists())
			throw new SpecificationException("Cannot read WSDL file for partner " + getName() + ": File \"" + fPathToWSDL + "\" not found.");

		// load WSDL
		try {
			WSDLFactory factory= WSDLFactory.newInstance();
			WSDLReader reader= factory.newWSDLReader();
			reader.setFeature(Constants.FEATURE_VERBOSE, false);
			fWSDLDefinition= reader.readWSDL(fPathToWSDL);
		} catch (WSDLException e) {
			throw new SpecificationException("Error while reading WSDL for partner " + getName() + " from file \"" + fPathToWSDL + "\".", e);
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

		return new SOAPOperationCallIdentifier(fWSDLDefinition, service, port, operationName, direction);
	}

	@Override
	public String toString() {
		return getName();
	}

}
