package net.bpelunit.framework.coverage.output.html;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.bpelunit.framework.coverage.result.CoverageDocument;
import net.bpelunit.util.FileUtil;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;


public class HtmlCoverageOutputterTest {

	private CoverageDocument doc;
	private File outputDirectory;
	private HtmlCoverageOutputter outputter = new HtmlCoverageOutputter();
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void testOutput() throws IOException {
		initializeOutputDirectory("testOutput");
		
		DummyCoverage coverage = DummyCoverage.createDummyCoverage("testOutput");
		doc.getCoverageInformationForProcesses().add(coverage);
		outputter.exportCoverageInformation(doc);
		
		File[] files = outputDirectory.listFiles();
		assertEquals(1, files.length);
		assertEquals("testOutput.html", files[0].getName());
		
		String actualContents = new String(FileUtil.readFile(files[0]));
		String expectedContents = new String(FileUtil.readFile(new File("src/test/resources/htmloutputter/testOutput.html")));
		
		assertEquals(expectedContents, actualContents);
	}
	
	private void initializeOutputDirectory(String testName) throws IOException {
		outputDirectory = new File("target/test/" + testName);
		
		if(outputDirectory.exists()) {
			FileUtils.deleteDirectory(outputDirectory);
		}
		outputDirectory.mkdirs();
		doc = new CoverageDocument(new File(outputDirectory, "suite.bpts"));		
	}

	@Test
	public void testOutputWithStylesheet() throws IOException {
		initializeOutputDirectory("testOutputWithStylesheet");
		DummyCoverage coverage = DummyCoverage.createDummyCoverage("testOutputWithStylesheet");
		doc.getCoverageInformationForProcesses().add(coverage);
		
		outputter.setStylesheet("../../../src/main/resources/bpelunit-report.css");
		outputter.exportCoverageInformation(doc);
		
		File[] files = outputDirectory.listFiles();
		assertEquals(1, files.length);
		assertEquals("testOutputWithStylesheet.html", files[0].getName());
		
		String actualContents = new String(FileUtil.readFile(files[0]));
		String expectedContents = new String(FileUtil.readFile(new File("src/test/resources/htmloutputter/testOutputWithStylesheet.html")));
		
		assertEquals(expectedContents, actualContents);
	}
	
}
