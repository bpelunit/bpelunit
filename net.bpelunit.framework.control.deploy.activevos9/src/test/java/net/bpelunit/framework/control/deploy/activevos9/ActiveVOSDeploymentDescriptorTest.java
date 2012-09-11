package net.bpelunit.framework.control.deploy.activevos9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.FileUtil;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ActiveVOSDeploymentDescriptorTest {

	private File pddFile = new File(
			"src/test/resources/net/bpelunit/framework/control/deploy/activevos9/some.pdd");
	private File pddActualTemplateFile = new File(
			"target/test/ActiveVOSDeploymentDescriptorTest");

	@Before
	public void setup() {
		pddActualTemplateFile.mkdirs();
	}

	@Test
	public void testReadPdd() throws Exception {
		ActiveVOSDeploymentDescriptor pdd = new ActiveVOSDeploymentDescriptor(
				pddFile);

		assertNotNull(pdd.pdd);
	}

	@Test
	public void testAddPartnerlink() throws Exception {
		String testName = "testAddPartnerlink";
		File f = copyPddToTestCaseFile(testName);
		ActiveVOSDeploymentDescriptor pdd = new ActiveVOSDeploymentDescriptor(f);
		pdd.addPartnerLink("NewPL", "http://www.example.org/partner",
				"partnerPort", new QName("ns", "partnerService"), null);
		pdd.save();

		String actual = normalize(FileUtil.readFile(f));
		String expected = normalize(getExpected(testName));
		assertEquals(expected, actual);
	}

	@Test
	public void testAddWsdlImport() throws Exception {
		String testName = "testAddWsdlImport";
		File f = copyPddToTestCaseFile(testName);
		ActiveVOSDeploymentDescriptor pdd = new ActiveVOSDeploymentDescriptor(f);
		pdd.addWsdlImport("mylocation", "ns");
		pdd.save();

		String actual = normalize(FileUtil.readFile(f));
		String expected = normalize(getExpected(testName));
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAddXsdImport() throws Exception {
		String testName = "testAddXsdImport";
		File f = copyPddToTestCaseFile(testName);
		ActiveVOSDeploymentDescriptor pdd = new ActiveVOSDeploymentDescriptor(f);
		pdd.addXsdImport("mylocation.xsd", "ns");
		pdd.save();

		String actual = normalize(FileUtil.readFile(f));
		String expected = normalize(getExpected(testName));
		assertEquals(expected, actual);
	}
	
	private byte[] getExpected(String testCaseName) throws IOException {
		InputStream stream = getClass().getResourceAsStream(
				getClass().getSimpleName() + "." + testCaseName + ".pdd");
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(stream, out);
			return out.toByteArray();
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	private String normalize(byte[] bytes) throws UnsupportedEncodingException {
		return normalize(new String(bytes, "UTF-8"));
	}

	private String normalize(String string) {
		return string.trim().replaceAll("\r", "");
	}

	private File copyPddToTestCaseFile(String testName)
			throws FileNotFoundException, IOException, DeploymentException {
		FileInputStream fileInputStream = new FileInputStream(pddFile);
		FileOutputStream fileOutputStream = null;
		try {
			File testFile = new File(pddActualTemplateFile, testName + ".pdd");
			fileOutputStream = new FileOutputStream(testFile);
			IOUtils.copy(fileInputStream, fileOutputStream);

			return testFile;
		} finally {
			IOUtils.closeQuietly(fileInputStream);
			IOUtils.closeQuietly(fileOutputStream);
		}
	}
}
