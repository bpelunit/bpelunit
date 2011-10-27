package net.bpelunit.toolsupport.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class AggregatedWSDLDefinitionFacade implements Definition {

	private Definition[] definitions;

	public AggregatedWSDLDefinitionFacade(Definition... definitions) {
		this.definitions = definitions;
	}

	@Override
	public void addExtensibilityElement(ExtensibilityElement arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public List<?> getExtensibilityElements() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void addBinding(Binding arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void addImport(Import arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void addMessage(Message arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void addNamespace(String arg0, String arg1) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void addPortType(PortType arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void addService(Service arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Binding createBinding() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public BindingFault createBindingFault() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public BindingInput createBindingInput() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public BindingOperation createBindingOperation() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public BindingOutput createBindingOutput() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Fault createFault() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Import createImport() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Input createInput() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Message createMessage() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Operation createOperation() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Output createOutput() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Part createPart() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Port createPort() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public PortType createPortType() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Service createService() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Types createTypes() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Binding getBinding(QName qname) {
		for (Definition d : definitions) {
			Binding binding = d.getBinding(qname);
			if (binding != null) {
				return binding;
			}
		}

		return null;
	}

	@Override
	public Map<?, ?> getBindings() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public String getDocumentBaseURI() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Element getDocumentationElement() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public ExtensionRegistry getExtensionRegistry() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Map<?, ?> getImports() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public List<?> getImports(String arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Message getMessage(QName qname) {
		for (Definition d : definitions) {
			Message m = d.getMessage(qname);
			if (m != null) {
				return m;
			}
		}

		return null;
	}

	@Override
	public Map<?, ?> getMessages() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public String getNamespace(String arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Map<?, ?> getNamespaces() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public PortType getPortType(QName qname) {
		for (Definition d : definitions) {
			PortType p = d.getPortType(qname);
			if (p != null) {
				return p;
			}
		}

		return null; 
	}

	@Override
	public Map<?, ?> getPortTypes() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public String getPrefix(String arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public QName getQName() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Service getService(QName qname) {
		for (Definition d : definitions) {
			Service s = d.getService(qname);
			if (s != null) {
				return s;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> getServices() {
		Map<Object, Object> services = new HashMap<Object, Object>();
		
		for(Definition d : definitions) {
			services.putAll(d.getServices());
		}
		
		return services;
	}

	@Override
	public String getTargetNamespace() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Types getTypes() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Binding removeBinding(QName arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Message removeMessage(QName arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public PortType removePortType(QName arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Service removeService(QName arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setDocumentBaseURI(String arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setDocumentationElement(Element arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setExtensionRegistry(ExtensionRegistry arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setQName(QName arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setTargetNamespace(String arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setTypes(Types arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	public Definition getDefinition(String namespace) {
		for(Definition d : definitions) {
			if(d.getTargetNamespace().equals(namespace)) {
				return d;
			}
		}
		
		return null;
	}

	@Override
	public Object getExtensionAttribute(QName arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Map<?, ?> getExtensionAttributes() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public List<?> getNativeAttributeNames() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public void setExtensionAttribute(QName arg0, Object arg1) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public ExtensibilityElement removeExtensibilityElement(ExtensibilityElement arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Map<?, ?> getAllBindings() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Map<?, ?> getAllPortTypes() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Map<?, ?> getAllServices() {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public Import removeImport(Import arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}

	@Override
	public String removeNamespace(String arg0) {
		throw new UnsupportedOperationException("Not supported by facade");
	}
}
