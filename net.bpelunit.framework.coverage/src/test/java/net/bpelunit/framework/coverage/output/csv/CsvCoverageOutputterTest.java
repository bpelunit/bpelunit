package net.bpelunit.framework.coverage.output.csv;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.bpelunit.framework.coverage.output.DummyCoverage;
import net.bpelunit.framework.coverage.result.CoverageDocument;
import net.bpelunit.util.FileUtil;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;


public class CsvCoverageOutputterTest {

	private CoverageDocument doc;
	private File outputDirectory;
	private CsvCoverageOutputter outputter = new CsvCoverageOutputter();
	
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
		assertEquals(2, files.length);
		assertEquals("testOutput.Metric1.csv", files[0].getName());
		assertEquals("testOutput.Metric2.csv", files[1].getName());
		
		String actualContents = new String(FileUtil.readFile(files[0]));
		String expectedContents = new String(FileUtil.readFile(new File("src/test/resources/csvoutputter/testOutput.Metric1.csv")));
		assertEquals(expectedContents, actualContents);
		
		actualContents = new String(FileUtil.readFile(files[1]));
		expectedContents = new String(FileUtil.readFile(new File("src/test/resources/csvoutputter/testOutput.Metric2.csv")));
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
	public void testOutputWithSeparator() throws IOException {
		initializeOutputDirectory("testOutputWithSeparator");
		DummyCoverage coverage = DummyCoverage.createDummyCoverage("testOutputWithSeparator");
		doc.getCoverageInformationForProcesses().add(coverage);
		
		outputter.setSeperator("|");
		outputter.exportCoverageInformation(doc);
		
		File[] files = outputDirectory.listFiles();
		assertEquals(2, files.length);
		assertEquals("testOutputWithSeparator.Metric1.csv", files[0].getName());
		assertEquals("testOutputWithSeparator.Metric2.csv", files[1].getName());
		
		String actualContents = new String(FileUtil.readFile(files[0]));
		String expectedContents = new String(FileUtil.readFile(new File("src/test/resources/csvoutputter/testOutputWithSeparator-Metric1.csv")));
		assertEquals(expectedContents, actualContents);
		
		actualContents = new String(FileUtil.readFile(files[1]));
		expectedContents = new String(FileUtil.readFile(new File("src/test/resources/csvoutputter/testOutputWithSeparator-Metric2.csv")));
		assertEquals(expectedContents, actualContents);
	}
	
}
