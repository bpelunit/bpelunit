package net.bpelunit.utils.testdataexternalizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import net.bpelunit.framework.xml.suite.XMLAnyElement;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.util.FileUtil;
import net.bpelunit.utils.testdataexternalizer.io.FileSystemFileWriter;
import net.bpelunit.utils.testdataexternalizer.io.IFileWriter.FileAlreadyExistsException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDataExternalizerTest {

	TestDataExternalizer tde;
	private File tmpDir;
	
	@Before
	public void setUp() throws IOException {
		tde = new TestDataExternalizer();
		tmpDir = FileUtil.createTempDirectory();
	}
	
	@After
	public void tearDown() throws IOException {
		tde = null;
		FileUtils.deleteDirectory(tmpDir);
	}
	
	@Test
	public void testGetDataElementName() throws XmlException, IOException {
		XMLTestSuiteDocument testSuite = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("suite-to-externalize.bpts"));
		XMLAnyElement anyElement = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(0).getSend().getData();
		
		assertEquals("NewOperation", TestDataExternalizer.getDataElementName(anyElement));
	}
	
	@Test
	public void testRegisterXmlContent() throws XmlException, IOException {
		XMLTestSuiteDocument testSuite = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("suite-to-externalize.bpts"));
		
		XMLAnyElement anyElement = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(0).getSend().getData();
		assertEquals("NewOperation.xml", tde.register(anyElement));

		XMLAnyElement anyElement1 = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(1).getSend().getData();
		assertEquals("NewOperation.xml", tde.register(anyElement1));
		
		XMLAnyElement anyElement2 = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(2).getSend().getData();
		assertEquals("NewOperation-1.xml", tde.register(anyElement2));
	}
	
	@Test
	public void testExternalize() throws XmlException, IOException, FileAlreadyExistsException {
		XMLTestSuiteDocument testSuite = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("suite-to-externalize.bpts"));
		
		XMLAnyElement anyElement = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(0).getSend().getData();		
		tde.register(anyElement);
		
		XMLAnyElement anyElement2 = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(2).getSend().getData();
		tde.register(anyElement2);

		tde.externalize(new FileSystemFileWriter(tmpDir));
		
		List<String> fileNames = Arrays.asList(tmpDir.list());
		assertEquals(2, fileNames.size());
		assertTrue(fileNames.contains("NewOperation.xml"));
		assertTrue(fileNames.contains("NewOperation-1.xml"));
	}
	
	@Test
	public void testReplaceContentsWithSrc() throws XmlException, IOException {
		XMLTestSuiteDocument testSuite = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("suite-to-externalize.bpts"));
		
		XMLAnyElement anyElement = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(0).getSend().getData();		
		tde.replaceContentsWithSrc(anyElement);
		assertSrcIsSetContentsIsEmpty(anyElement, "NewOperation.xml");
		
		XMLAnyElement anyElement2 = testSuite.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(2).getSend().getData();
		tde.replaceContentsWithSrc(anyElement2);
		assertSrcIsSetContentsIsEmpty(anyElement2, "NewOperation-1.xml");
	}

	private void assertSrcIsSetContentsIsEmpty(XMLAnyElement anyElement,
			String srcValue) {
		assertNull(TestDataExternalizer.getDataElementName(anyElement));
		assertEquals(srcValue, anyElement.getSrc());
	}
	
	@Test
	public void testExternalizeTestSuite() throws XmlException, IOException, FileAlreadyExistsException {
		XMLTestSuiteDocument testSuite = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("suite-to-externalize.bpts"));
		
		tde.replaceContentsWithSrc(testSuite);
		
		tde.externalize(new FileSystemFileWriter(tmpDir));
		
		List<String> fileNames = Arrays.asList(tmpDir.list());
		assertEquals(2, fileNames.size());
		assertTrue(fileNames.contains("NewOperation.xml"));
		assertTrue(fileNames.contains("NewOperation-1.xml"));
		
		StringWriter actualTestSuiteWriter = new StringWriter();
		testSuite.save(actualTestSuiteWriter);
		
		StringWriter expectedTestSuiteWriter = new StringWriter();
		IOUtils.copy(getClass().getResourceAsStream("suite-after-externalize.bpts"), expectedTestSuiteWriter);
		
		assertEquals(
				normalizeForComparision(expectedTestSuiteWriter.toString()),
				normalizeForComparision(actualTestSuiteWriter.toString()));
	}
	
	@Test
	public void testExternalizeTestSuiteWithRelativePath() throws XmlException, IOException, FileAlreadyExistsException {
		tde = new TestDataExternalizer("myBPTS");
		XMLTestSuiteDocument testSuite = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("suite-to-externalize.bpts"));
		
		tde.replaceContentsWithSrc(testSuite);
		
		tde.externalize(new FileSystemFileWriter(tmpDir));
		
		File[] files = tmpDir.listFiles();
		assertEquals(1, files.length);
		assertEquals("myBPTS", files[0].getName());
		assertTrue(files[0].isDirectory());
		
		List<String> fileNames = Arrays.asList(files[0].list());
		assertEquals(2, fileNames.size());
		assertTrue(fileNames.contains("NewOperation.xml"));
		assertTrue(fileNames.contains("NewOperation-1.xml"));
	}
	
	String normalizeForComparision(String input) {
		return input.trim().replaceAll("\r\n", "\n").replaceAll("\n\r", "\n").replaceAll("\r", "\n");
	}
}
