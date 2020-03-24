package net.bpelunit.framework.model.test.activity;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.soap.DocumentLiteralEncoder;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.SynchronousSendException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.model.test.data.extraction.DataExtraction;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

public class ReceiveSendSyncTest {

	private boolean hasBeenExecuted;
	
	@Before
	public void setup() {
		hasBeenExecuted = false;
	}
	
	@BeforeClass
	public static void setupClass() throws ParserConfigurationException {
		BPELUnitUtil.initializeParsing();
	}
	
	@Test
	public void test_run_WithMessage() throws Exception {
		ReceiveSendSync receiveSendActivity = new ReceiveSendSync((PartnerTrack)null);
		ReceiveDataSpecification receiveSpec = new ReceiveDataSpecification(receiveSendActivity, null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
			}
		};
		Definition definition = WSDLFactory.newInstance().newDefinition();
		definition.setQName(new QName("MyTarget"));
		String targetNamespace = "urn:mytarget";
		definition.setTargetNamespace(targetNamespace);
		Service service = definition.createService();
		service.setQName(new QName(definition.getTargetNamespace(), "MyService"));
		Port port = definition.createPort();
		port.setName("myPort");
		Binding binding = definition.createBinding();
		binding.setQName(new QName(definition.getTargetNamespace(), "MyBinding"));
		definition.addBinding(binding);
		PortType portType = definition.createPortType();
		Operation operation = definition.createOperation();
		operation.setName("MyOperation");
		portType.addOperation(operation);
		binding.setPortType(portType);
		BindingOperation bindingOperation = definition.createBindingOperation();
		bindingOperation.setName(operation.getName());
		binding.addBindingOperation(bindingOperation);
		port.setBinding(binding);
		service.addPort(port);
		definition.addService(service);
		
		SOAPOperationCallIdentifier soapOperationcallIdentifier = new SOAPOperationCallIdentifier(
				definition, 
				new QName(targetNamespace, service.getQName().getLocalPart()), 
				port.getName(), 
				operation.getName(),
				SOAPOperationDirectionIdentifier.INPUT);
		ISOAPEncoder soapEncoder = new DocumentLiteralEncoder();
		receiveSpec.initialize(soapOperationcallIdentifier , "literal", soapEncoder, new ArrayList<ReceiveCondition>(), new ArrayList<DataExtraction>(), null, null);
		SendDataSpecification sendSpec = new SendDataSpecification(receiveSendActivity, new NamespaceContextImpl());
		Element rawDataRoot = createDummyElement();
		sendSpec.initialize(soapOperationcallIdentifier, 0, null, "", "", "literal", soapEncoder, rawDataRoot, null, null, null);
		receiveSendActivity.initialize(sendSpec , receiveSpec, null, new ArrayList<DataCopyOperation>());
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy") {
			@Override
			public void postAnswer(PartnerTrack track, OutgoingMessage msg)
					throws TimeoutException, InterruptedException {
			}
			
			@Override
			public IncomingMessage sendMessage(OutgoingMessage msg)
					throws SynchronousSendException, InterruptedException {
				
				IncomingMessage message = new IncomingMessage();
				message.setMessage("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><response /></s:Body></s:Envelope>".getBytes());
				return message;
			}
			
			@Override
			public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException {
				return null;
			}
		};
		IncomingMessage message = new IncomingMessage();
		message.setMessage("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><x /></s:Body></s:Envelope>".getBytes());
		receiveSendActivity.run(ctx, message);
		
		assertTrue(receiveSendActivity.getStatus().toString(), receiveSendActivity.getStatus().isPassed());
	}

	private Element createDummyElement() throws ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("testElement");
	}
	
	@Test
	public void test_run_WithoutMessage() throws Exception {
		ReceiveSendSync receiveSendActivity = new ReceiveSendSync((PartnerTrack)null);
		ReceiveDataSpecification receiveSpec = new ReceiveDataSpecification(receiveSendActivity, null) {
			@Override
			public void reportProgress(ITestArtefact artefact) {
			}
		};
		Definition definition = WSDLFactory.newInstance().newDefinition();
		definition.setQName(new QName("MyTarget"));
		String targetNamespace = "urn:mytarget";
		definition.setTargetNamespace(targetNamespace);
		Service service = definition.createService();
		service.setQName(new QName(definition.getTargetNamespace(), "MyService"));
		Port port = definition.createPort();
		port.setName("myPort");
		Binding binding = definition.createBinding();
		binding.setQName(new QName(definition.getTargetNamespace(), "MyBinding"));
		definition.addBinding(binding);
		PortType portType = definition.createPortType();
		Operation operation = definition.createOperation();
		operation.setName("MyOperation");
		portType.addOperation(operation);
		binding.setPortType(portType);
		BindingOperation bindingOperation = definition.createBindingOperation();
		bindingOperation.setName(operation.getName());
		binding.addBindingOperation(bindingOperation);
		port.setBinding(binding);
		service.addPort(port);
		definition.addService(service);
		
		SOAPOperationCallIdentifier soapOperationcallIdentifier = new SOAPOperationCallIdentifier(
				definition, 
				new QName(targetNamespace, service.getQName().getLocalPart()), 
				port.getName(), 
				operation.getName(),
				SOAPOperationDirectionIdentifier.INPUT);
		ISOAPEncoder soapEncoder = new DocumentLiteralEncoder();
		receiveSpec.initialize(soapOperationcallIdentifier , "literal", soapEncoder, new ArrayList<ReceiveCondition>(), new ArrayList<DataExtraction>(), null, null);
		SendDataSpecification sendSpec = new SendDataSpecification(receiveSendActivity, new NamespaceContextImpl());
		Element rawDataRoot = createDummyElement();
		sendSpec.initialize(soapOperationcallIdentifier, 0, null, "", "", "literal", soapEncoder, rawDataRoot, null, null, null);
		receiveSendActivity.initialize(sendSpec , receiveSpec, null, new ArrayList<DataCopyOperation>());
		
		final IncomingMessage message = new IncomingMessage();
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy") {
			@Override
			public void postAnswer(PartnerTrack track, OutgoingMessage msg)
					throws TimeoutException, InterruptedException {
			}
			
			@Override
			public IncomingMessage sendMessage(OutgoingMessage msg)
					throws SynchronousSendException, InterruptedException {
				
				IncomingMessage message = new IncomingMessage();
				message.setMessage("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><response /></s:Body></s:Envelope>".getBytes());
				return message;
			}
			
			@Override
			public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException {
				return null;
			}
			
			@Override
			public IncomingMessage receiveMessage(PartnerTrack track) throws TimeoutException, InterruptedException {
				hasBeenExecuted = true;
				return message;
			}
		};
		
		message.setMessage("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><x /></s:Body></s:Envelope>".getBytes());
		receiveSendActivity.run(ctx, null);
		
		assertTrue(hasBeenExecuted);
		assertTrue(receiveSendActivity.getStatus().toString(), receiveSendActivity.getStatus().isPassed());
	}

}
