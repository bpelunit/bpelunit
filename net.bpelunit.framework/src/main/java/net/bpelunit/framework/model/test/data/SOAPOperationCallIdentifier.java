/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.SpecificationException;

/**
 * <p>
 * A SOAP Operation Call Identifier identifies a certain "call" made to a web service or retrieved
 * from a web service, as specified in a WSDL file.
 * </p>
 * 
 * <p>
 * This class assumes WSDL which uses SOAP 1.1 for encoding and HTTP as the transport layer. This is
 * in compliance with WS-I Basic Profile.
 * </p>
 * 
 * <p>
 * A call is identified by:
 * </p>
 * <p>
 * <ul>
 * <li>qualified service name</li>
 * <li>port name</li>
 * <li>operation name</li>
 * <li>"direction", that is incoming, outgoing, or fault.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * A SOAP Operation Call Identifier is initialized with a WSDL definition (must be read from disk
 * before), and each of the elements listed above.
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio García-Domínguez (getters for WSDL info and getBodyNamespace)
 * 
 */
public class SOAPOperationCallIdentifier {

	/**
	 * WSDL definition.
	 */
	private Definition fDefinition;

	/**
	 * WSDL service inside the definition.
	 */
	private Service fService;

	/**
	 * WSDL Port defined inside the service.
	 */
	private Port fPort;

	/**
	 * Binding for the port type referenced by the port.
	 */
	private Binding fBinding;

	/**
	 * WSDL binding operation to be used.
	 */
	private BindingOperation fOperation;

	/**
	 * Direction in which data is to be sent.
	 */
	private SOAPOperationDirectionIdentifier fDirection;

	/**
	 * Creates a new SOAP Operation Call Identifier
	 * 
	 * @param fWSDLDefinition
	 * @param service
	 * @param port
	 * @param operationName
	 * @param direction
	 * @throws SpecificationException
	 */
	public SOAPOperationCallIdentifier(Definition fWSDLDefinition, QName service, String port, String operationName,
			SOAPOperationDirectionIdentifier direction) throws SpecificationException {

		fDefinition= fWSDLDefinition;

		fService= fWSDLDefinition.getService(service);
		if (fService == null) {
			throw new SpecificationException("Specified service \"" + service + "\" was not found in partner WSDL " + fWSDLDefinition.getQName());
		}

		fPort= fService.getPort(port);
		if (fPort == null) {
			throw new SpecificationException("Specified port \"" + port + "\" was not found in service " + service + " in partner WSDL "
					+ fWSDLDefinition.getQName());
		}
		
		fBinding= fPort.getBinding();

		if (fBinding == null) {
			throw new SpecificationException("Could not find a binding for service \"" + service + "\" and port \"" + port + "\" in partner WSDL "
					+ fWSDLDefinition.getQName());
		}
		
		// Do not use input and output names. This means no overloading is
		// possible/allowed. This is acceptable as per WS-I Basic Profile.
		fOperation= fBinding.getBindingOperation(operationName, null, null);

		if (fOperation == null) {
			throw new SpecificationException("Specified operation \"" + operationName + "\" was not found in binding \"" + fBinding.getQName()
					+ "\" in partner WSDL " + "\"" + fWSDLDefinition.getQName() + "\"");
		}
			
		fDirection= direction;
	}

	// ********************* Simple Getters ***************************

	/**
	 * Returns the target namespace of the WSDL definition.
	 * 
	 * @return target namespace URI
	 */
	public String getTargetNamespace() {
		return fDefinition.getTargetNamespace();
	}

	/**
	 * Returns the simple name of the operation binding
	 * 
	 * @return operation simple name
	 */
	public String getName() {
		return fOperation.getName();
	}

	/**
	 * Returns the direction of this call (either input, output, or fault)
	 * 
	 * @return call direction
	 */
	public SOAPOperationDirectionIdentifier getDirection() {
		return fDirection;
	}

	/**
	 * Returns true if this call expects a fault (i.e., the direction is fault)
	 * 
	 * @return true if fault
	 */
	public boolean isFault() {
		return fDirection == SOAPOperationDirectionIdentifier.FAULT;
	}

	/**
	 * Returns the WSDL service for this call.
	 */
	public Service getService() {
		return this.fService;
	}

	/**
	 * Returns the WSDL port for this call.
	 */
	public Port getPort() {
		return this.fPort;
	}

	/**
	 * Returns the WSDL port binding for this call.
	 */
	public Binding getBinding() {
		return this.fBinding;
	}

	/**
	 * Returns the WSDL operation binding for this call.
	 */
	public BindingOperation getBindingOperation() {
		return this.fOperation;
	}

	// ********************* WSDL-Related Getters *********************

	/**
	 * Returns style and encoding of this operation as simple names, separated by a slash ("/").
	 * 
	 * 
	 * @return
	 * @throws SpecificationException
	 */
	public String getEncodingStyle() throws SpecificationException {

		SOAPBinding sBinding= null;
		List<?> extensibilityElements= fBinding.getExtensibilityElements();
		for (Object supposedSOAPBinding : extensibilityElements) {
			if (supposedSOAPBinding instanceof SOAPBinding) {
				sBinding= (SOAPBinding) supposedSOAPBinding;
			}
		}
		if (sBinding == null) {
			throw new SpecificationException("Could not find SOAP Binding element in binding " + fBinding);
		}

		String style= sBinding.getStyle();

		// Style might only be indicated at operation level.
		if (isFault()) {
		        style = "document"; // according to WSDL 1.1 (section 3.6, last paragraph)
		}
		if (style == null) {
			style= getSOAPOperation().getStyle();
		}
		if (style == null) {
			style= "document"; // default as per WS-I basic profile.
		}
		
		String encoding= getEncoding(fOperation);

		if (encoding == null) {
			encoding= "literal"; // default as per WS-I basic profile.
		}
			
		return style + "/" + encoding;
	}

	/**
	 * Returns the SOAP HTTP Action for this operation.
	 * 
	 * @return
	 */
	public String getSOAPHTTPAction() {
		return getSOAPOperation().getSoapActionURI();
	}

	/**
	 * Returns the SOAP Target URL for this operation.
	 * 
	 * @return
	 * @throws SpecificationException
	 */
	public String getTargetURL() throws SpecificationException {
		List<?> extensibilityElements= fPort.getExtensibilityElements();
		for (Object supposedSOAPAddress : extensibilityElements) {
			if (supposedSOAPAddress instanceof SOAPAddress) {
				SOAPAddress adr= (SOAPAddress) supposedSOAPAddress;
				return adr.getLocationURI();
			}
		}
		throw new SpecificationException("I could not find a target URL for operation " + this);
	}

	/**
	 * Returns the namespace URI that should be used for the elements of the
	 * soap:Body of the SOAP Message. Only useful with the RPC style: if the
	 * style is "document", WS-I forbids using the namespace attribute in the
	 * soap:body child of the binding of the port type. Conversely, when using
	 * the RPC style, WS-I compliant WSDL files should use the "namespace"
	 * attribute.
	 */
	public String getBodyNamespace() {
		SOAPBody soapBody;
		if (SOAPOperationDirectionIdentifier.INPUT.equals(getDirection())) {
			soapBody = getSoapBody(fOperation.getBindingInput());
		} else {
			soapBody = getSoapBody(fOperation.getBindingOutput());
		}
		if (soapBody != null && soapBody.getNamespaceURI() != null) {
			return soapBody.getNamespaceURI();
		}
		else {
			/*
			 * ODE (at least) expects the wrapper *not* to be in any namespace
			 * if the "namespace" attribute is not used in the soap:body child
			 * of the <input> of the binding (violating the WS-I spec).
			 *
			 * See: https://github.com/bpelunit/bpelunit/issues/8
			 */
			return null;
		}
	}

	// ********************** Private Helpers ****************

	private SOAPOperation getSOAPOperation() {
		List<?> extensibilityElements2= fOperation.getExtensibilityElements();
		for (Object supposedSOAPOperation : extensibilityElements2) {
			if (supposedSOAPOperation instanceof SOAPOperation) {
				return ((SOAPOperation) supposedSOAPOperation);
			}
		}
		return null;
	}


	private String getEncoding(BindingOperation wsdlOperation) {
		// We assume that only one encoding is in effect, i.e. the same for
		// input and output.
		// According to WS-I Basic profile, only literal is accepted anyway.

		String use= null;
		// Has input?
		BindingInput bindingInput= wsdlOperation.getBindingInput();
		if (bindingInput != null) {
			use= getUse(bindingInput);
			if (use != null) {
				return use;
			}
		}
		// Has output?
		BindingOutput bindingOutput= wsdlOperation.getBindingOutput();
		if (bindingOutput != null) {
			use= getUse(bindingOutput);
			if (use != null) {
				return use;
			}
		}

		// May have faults, but not without input or output.

		return null;
	}

	private String getUse(ElementExtensible bindingInput) {
		SOAPBody body= getSoapBody(bindingInput);
		if (body != null) {
			return body.getUse();
		} else {
			return null;
		}
	}

	private SOAPBody getSoapBody(ElementExtensible bindingInput) {
		List<?> extensibilityElements= bindingInput.getExtensibilityElements();
		for (Object supposedSOAPBody : extensibilityElements) {
			if (supposedSOAPBody instanceof SOAPBody) {
				return ((SOAPBody) supposedSOAPBody);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "SOAP Operation " + fDefinition.getTargetNamespace() + " -> " + fPort.getName() + " -> " + fOperation.getName();
	}

}
