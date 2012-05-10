package net.bpelunit.framework.control.deploy.activevos9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.framework.control.deploy.activevos9.ActiveVOS9Deployment.BPELInfo;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.exception.DeploymentException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;


public class ActiveVOS9DeploymentTest {

	private static final String TEST_RESOURCE_DIR = "src/test/resources/net/bpelunit/framework/control/deploy/activevos9/";
	private static final String NS_TEST = "http://test.bpelunit.net/activevos9";
	
	private ActiveVOS9Deployment deployment = null;

	
	@Before
	public void setUp() {
		deployment = null;
	}
	
	@After
	public void tearDown() throws DeploymentException {
		if(deployment != null) {
			deployment.cleanUp();
		}
	}
	
	@Test
	public void testDeploymentWithOneProcess() throws Exception {
		deployment = new ActiveVOS9Deployment(new File(TEST_RESOURCE_DIR 
				+ "bpelunit-tc1.bpr")) {
		};
		
		XPathTool xpath = deployment.createXPathToolForPdd();
		
		List<IBPELProcess> bpelProcesses = deployment.getBPELProcesses();
		assertEquals(1, bpelProcesses.size());
		
		BPELInfo bpelProcess = (BPELInfo)bpelProcesses.get(0);
		assertEquals(new QName(NS_TEST, "tc1"), bpelProcess.getName());
		
		Element bpelXml = bpelProcess.getXML().getDocumentElement();
		Element pddXml = bpelProcess.getPddXml().getDocumentElement();
		
		assertNotNull(bpelXml);
		assertEquals(BpelXMLTools.NAMESPACE_BPEL_2_0.getURI(), bpelXml.getNamespaceURI());
		assertEquals("process", bpelXml.getLocalName());

		assertNotNull(pddXml);
		assertEquals(ActiveVOS9Deployment.NAMESPACE_PDD, pddXml.getNamespaceURI());
		assertEquals("process", pddXml.getLocalName());
		
		assertEquals("http://www.example.org/", xpath.evaluateAsString("//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/wsa:EndpointReference/wsa:Address/text()", pddXml));
		assertEquals("static", xpath.evaluateAsString("//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@endpointReference", pddXml));
		assertEquals("default:Service", xpath.evaluateAsString("//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@invokeHandler", pddXml));
		
		bpelProcess.changePartnerEndpoint("Provider", "http://localhost:7777/ws/Provider");
		
		assertEquals("http://localhost:7777/ws/Provider", xpath.evaluateAsString("//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/wsa:EndpointReference/wsa:Address/text()", pddXml));
		assertEquals("static", xpath.evaluateAsString("//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@endpointReference", pddXml));
		assertEquals("default:Address", xpath.evaluateAsString("//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@invokeHandler", pddXml));
	}
}
