package test.coverage.deployarchivetools.impl;

import static org.junit.Assert.*;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.junit.Before;
import org.junit.Test;

import com.ibm.wsdl.Constants;

import coverage.deploy.archivetools.impl.ActiveBPELDeploymentArchiveHandler;

public class ActiveBPELDeploymentArchiveHandlerTest {

	private Object wsdlFile;
	private Definition fWSDLDefinition;

	@Before
	public void setUp() throws Exception {
		WSDLFactory factory= WSDLFactory.newInstance();
		WSDLReader reader= factory.newWSDLReader();
		reader.setFeature(Constants.FEATURE_VERBOSE, false);
		fWSDLDefinition= reader.readWSDL("C:/bpelunit/conf/_LogService_.wsdl");
	}

	@Test
	public void testGetService() {
//		Service service=new ActiveBPELDeploymentArchiveHandler().getService(fWSDLDefinition);
//		System.out.println(service.getQName());
	}

}
