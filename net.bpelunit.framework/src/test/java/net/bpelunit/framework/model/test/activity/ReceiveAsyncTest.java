package net.bpelunit.framework.model.test.activity;

import static org.junit.Assert.*;

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
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.soap.DocumentLiteralEncoder;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import net.bpelunit.framework.model.test.data.extraction.DataExtraction;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

public class ReceiveAsyncTest {

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
		
		ReceiveAsync r1 = new ReceiveAsync((PartnerTrack)null);
		ReceiveDataSpecification spec = new ReceiveDataSpecification(r1, null) {
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
		spec.initialize(soapOperationcallIdentifier , "literal", soapEncoder, new ArrayList<ReceiveCondition>(), new ArrayList<DataExtraction>(), null, null);
		r1.initialize(spec);
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy") {
			@Override
			public void postAnswer(PartnerTrack track, OutgoingMessage msg)
					throws TimeoutException, InterruptedException {
			}
			
			@Override
			public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException {
				return null;
			}
		};
		IncomingMessage message = new IncomingMessage();
		message.setMessage("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><x /></s:Body></s:Envelope>".getBytes());
		
		assertTrue(r1.canExecute(ctx, message));
		r1.run(ctx, message);
		
		assertTrue(r1.getStatus().toString(), r1.getStatus().isPassed());
	}
	
	@Test
	public void test_run_WithoutMessage() throws Exception {
		
		ReceiveAsync r1 = new ReceiveAsync((PartnerTrack)null);
		ReceiveDataSpecification spec = new ReceiveDataSpecification(r1, null) {
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
		spec.initialize(soapOperationcallIdentifier , "literal", soapEncoder, new ArrayList<ReceiveCondition>(), new ArrayList<DataExtraction>(), null, null);
		r1.initialize(spec);
		
		final IncomingMessage message = new IncomingMessage();
		
		ActivityContext ctx = new ActivityContext("http://localhost:7777/ws/Dummy") {
			@Override
			public void postAnswer(PartnerTrack track, OutgoingMessage msg)
					throws TimeoutException, InterruptedException {
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
		r1.run(ctx, null);
		
		assertTrue(hasBeenExecuted);
		assertTrue(r1.getStatus().toString(), r1.getStatus().isPassed());
	}
}
