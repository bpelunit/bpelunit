package net.bpelunit.framework.control.deploy.activebpel;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Test;

/**
 * Tests for the ActiveBPEL BPR packager.
 */
public class BPELPackagerTest {

	@Test
	public void testCreditRatingService() throws Exception {
		final File fBPR = File.createTempFile("bpelunit", ".bpr");
		fBPR.deleteOnExit();

		final File fBPEL = new File("src/test/resources/bprPackager/LoanApprovalProcess.bpel");
		final DeploymentArchivePackager packager = new DeploymentArchivePackager(fBPEL);
		packager.generateBPR(fBPR);

		assertTrue("The BPR file has been created", fBPR.exists());
		final ZipFile zf = new ZipFile(fBPR);

		final Set<String> zfFilenames = new HashSet<String>();
		Enumeration<? extends ZipEntry> zfEnum = zf.entries();
		while (zfEnum.hasMoreElements()) {
			final ZipEntry zfEntry = zfEnum.nextElement();
			zfFilenames.add(zfEntry.getName());
		}
		final String[] expectedNames = {
			"process.pdd", "wsdl/Loans.xsd", "META-INF/catalog.xml",
			"wsdl/LoanService.wsdl", "wsdl/ApprovalService.wsdl", "bpel/LoanApprovalProcess.bpel",
			"wsdl/AssessorService.wsdl"
		};
		for (String expectedName : expectedNames) {
			assertTrue("Zip file should contain " + expectedName, zfFilenames.contains(expectedName));
		}
	}
}
