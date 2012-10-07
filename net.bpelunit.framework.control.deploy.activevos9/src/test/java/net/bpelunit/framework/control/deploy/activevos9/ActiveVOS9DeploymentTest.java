package net.bpelunit.framework.control.deploy.activevos9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.activevos9.ActiveVOS9Deployment.BPELInfo;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.util.FileUtil;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

import com.activeEndpoints.schemas.catalog.x2006.x07.catalog.WsdlEntryType;

public class ActiveVOS9DeploymentTest {

	private static final String TEST_RESOURCE_DIR = "src/test/resources/net/bpelunit/framework/control/deploy/activevos9/";
	private static final String NS_TEST = "http://test.bpelunit.net/activevos9";

	private ActiveVOS9Deployment deployment = null;
	private File tempDeploymentFile = null;
	private File tempDeploymentDir = null;

	@Before
	public void setUp() {
		deployment = null;
		tempDeploymentFile = null;
		tempDeploymentDir = null;
	}

	@After
	public void tearDown() {
		if (deployment != null) {
			try {
				deployment.cleanUp();
			} catch (DeploymentException e) {
			}
		}
		
		if (tempDeploymentDir != null) {
			try {
				FileUtils.deleteDirectory(tempDeploymentDir);
			} catch (IOException e) {
			}
		}
		
		if (tempDeploymentFile != null) {
			tempDeploymentDir.delete();
		}
	}

	@Test
	public void testDeploymentWithOneProcess() throws Exception {
		deployment = new ActiveVOS9Deployment(new File(TEST_RESOURCE_DIR
				+ "bpelunit-tc1.bpr"));

		XPathTool xpath = deployment.createXPathToolForPdd();

		List<IBPELProcess> bpelProcesses = deployment.getBPELProcesses();
		assertEquals(1, bpelProcesses.size());

		BPELInfo bpelProcess = (BPELInfo) bpelProcesses.get(0);
		assertEquals(new QName(NS_TEST, "tc1"), bpelProcess.getName());

		IProcess process = bpelProcess.getProcessModel();
		Element pddXml = bpelProcess.getPddXml().getDocumentElement();

		assertNotNull(process);

		assertNotNull(pddXml);
		assertEquals(ActiveVOS9Deployment.NAMESPACE_PDD,
				pddXml.getNamespaceURI());
		assertEquals("process", pddXml.getLocalName());

		assertEquals(
				"http://www.example.org/",
				xpath.evaluateAsString(
						"//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/wsa:EndpointReference/wsa:Address/text()",
						pddXml));
		assertEquals(
				"static",
				xpath.evaluateAsString(
						"//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@endpointReference",
						pddXml));
		assertEquals(
				"default:Service",
				xpath.evaluateAsString(
						"//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@invokeHandler",
						pddXml));

		bpelProcess.changePartnerEndpoint("Provider",
				"http://localhost:7777/ws/Provider");

		assertEquals(
				"http://localhost:7777/ws/Provider",
				xpath.evaluateAsString(
						"//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/wsa:EndpointReference/wsa:Address/text()",
						pddXml));
		assertEquals(
				"static",
				xpath.evaluateAsString(
						"//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@endpointReference",
						pddXml));
		assertEquals(
				"default:Address",
				xpath.evaluateAsString(
						"//pdd:partnerLinks/pdd:partnerLink[@name='Provider']/pdd:partnerRole/@invokeHandler",
						pddXml));

		tempDeploymentFile = saveNewDeployment();

		tempDeploymentDir = FileUtil.createTempDirectory();
		ZipUtil.unzipFile(tempDeploymentFile, tempDeploymentDir);

		byte[] actualPddContents = FileUtil.readFile(new File(
				tempDeploymentDir, "META-INF/pdd/bpelunit-tc1/deploy/tc1.pdd"));
		ByteArrayOutputStream filePddContents = new ByteArrayOutputStream();
		XMLUtil.writeXML(bpelProcess.getPddXml(), filePddContents);

		String actualPddString = new String(actualPddContents);
		String filePddString = new String(filePddContents.toByteArray());
		assertEquals(actualPddString, filePddString);
	}

	@Test
	@Ignore
	public void testAddFilesToProcess() throws Exception {
		deployment = new ActiveVOS9Deployment(new File("src/test/resources/simplebpel.bpr"));
		
		List<IBPELProcess> processes = deployment.getBPELProcesses();
		assertEquals(1, processes.size());
		BPELInfo firstProcess = (BPELInfo)processes.get(0);
		
		assertEquals("../../../wsdl/bpelunit/coverage.wsdl", firstProcess.calculateRelativePathTo("coverage.wsdl"));

		File newBPR = saveNewDeployment();
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(newBPR);
			out = new FileOutputStream(new File("target/simplebpel_updated.bpr"));
			IOUtils.copy(in, out);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		
		firstProcess.addWSDLImport("coverage.wsdl", "http://www.example.org/coverage/", getClass().getResourceAsStream("coverage.wsdl"));
		
		IProcess bpelModel = firstProcess.getProcessModel();
		IImport lastImport = bpelModel.getImports().get(bpelModel.getImports().size()-1);
		assertEquals("http://www.example.org/coverage/", lastImport.getNamespace());
		assertTrue(lastImport.getLocation().endsWith("coverage.wsdl"));
		assertEquals(IImport.IMPORTTYPE_WSDL, lastImport.getImportType());
		
		WsdlEntryType[] wsdlsInCatalog = deployment.catalogDoc.getCatalog().getWsdlEntryArray();
		WsdlEntryType lastWsdl = wsdlsInCatalog[wsdlsInCatalog.length - 1];
		assertEquals("project:/com.innoq.simplebpel/wsdl/bpelunit/coverage.wsdl", lastWsdl.getLocation());
		assertTrue(lastWsdl.getClasspath().endsWith("coverage.wsdl"));
	}
	
	private File saveNewDeployment() throws IOException, DeploymentException,
			FileNotFoundException {
		File tempDeploymentFile = File.createTempFile("bpelunit-test", ".zip");

		InputStream updatedDeployment = deployment.getUpdatedDeployment();
		OutputStream updatedDeploymentOut = new FileOutputStream(
				tempDeploymentFile);

		try {
			IOUtils.copy(updatedDeployment, updatedDeploymentOut);
		} finally {
			IOUtils.closeQuietly(updatedDeployment);
			IOUtils.closeQuietly(updatedDeploymentOut);
		}

		return tempDeploymentFile;
	}
}
