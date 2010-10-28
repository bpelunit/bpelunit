package net.bpelunit.utils.datasourceinliner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import net.bpelunit.framework.control.datasource.DataSourceUtil;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.xml.suite.XMLDataSource;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.test.util.TestTestRunner;
import net.bpelunit.test.util.TestUtil;
import org.junit.Test;

/**
 * Tests for the data source inliner.
 * 
 * @author Antonio García-Domínguez
 */
public class DataSourceInlinerTest {

    private static final File BPTS_BASEDIR       = new File("target/test-classes");

    private static final File LINKED_BPTS_FILE   = getBPTSFile("templates",
                                                         "tacService-suitesrc-t.bpts");
    private static final File EMBEDDED_BPTS_FILE = getBPTSFile("templates",
                                                         "tacService-casesrc-t.bpts");
    private static final File DELAYSEQ_BPTS_FILE = getBPTSFile("end2end/03_SendReceiveAsync",
                                                         "LoanServiceTestSuite.bpts");
    private static final File PLAIN_BPTS_FILE    = getBPTSFile("end2end/01_SendReceiveOnly",
                                                         "WastePaperBasketTestSuite.bpts");

    @Test
    public void inlinedEmbeddedDSIsEquivalent() throws Exception {
        assertInlinedProducesSameResult(EMBEDDED_BPTS_FILE);
    }

    @Test
    public void inlinedEmbeddedDSIsStandalone() throws Exception {
        assertInlinedIsStandalone(EMBEDDED_BPTS_FILE);
    }

    @Test
    public void inlinedLinkedDSIsEquivalent() throws Exception {
        assertInlinedProducesSameResult(LINKED_BPTS_FILE);
    }

    @Test
    public void inlinedLinkedDSIsStandalone() throws Exception {
        assertInlinedIsStandalone(LINKED_BPTS_FILE);
    }

    @Test
    public void inlinedPlainBPTSIsEquivalent() throws Exception {
        assertInlinedProducesSameResult(PLAIN_BPTS_FILE);
    }

    @Test
    public void inlinedDelaySequenceIsEquivalent() throws Exception {
        assertInlinedProducesSameResult(DELAYSEQ_BPTS_FILE);
    }

    private static File getBPTSFile(final String dirBasename,
            final String bptsBasename) {
        return new File(new File(BPTS_BASEDIR, dirBasename), bptsBasename);
    }

    private void assertInlinedProducesSameResult(final File fOriginal) throws Exception {
        final File fInlined = inlineToTemp(fOriginal);
        TestUtil.assertSameAndSuccessfulResults(
                "BPTS with no templates should work just like before",
                fOriginal, fInlined);
    }

    private void assertInlinedIsStandalone(final File fOriginal) throws Exception {
        final TestTestRunner runner = new TestTestRunner(fOriginal);
        final XMLTestSuiteDocument xmlOriginal
            = XMLTestSuiteDocument.Factory.parse(fOriginal);
        final XMLTestSuiteDocument xmlInlined
            = new Inliner().inlineDataSources(xmlOriginal, fOriginal.getParentFile(), runner);

        final XMLTestSuite xmlTestSuite = xmlInlined.getTestSuite();
        if (xmlTestSuite.isSetSetUp()) {
            assertFalse("Inlined BPTS should not have a global data source",
                    xmlTestSuite.getSetUp().isSetDataSource());
        }

        for (XMLTestCase xmlTestCase : xmlTestSuite.getTestCases().getTestCaseList()) {
            if (xmlTestCase.isSetSetUp() && xmlTestCase.getSetUp().isSetDataSource()) {
                final XMLDataSource xmlDataSrc = xmlTestCase.getSetUp().getDataSource();
                assertFalse("Inlined BPTS should only have embedded data sources",
                            xmlDataSrc.isSetSrc());

                IDataSource dataSource
                    = DataSourceUtil.createDataSource(
                            xmlTestSuite, xmlTestCase,
                            fOriginal.getParentFile(), runner);
                assertEquals(
                        "Data sources in each test case of an inlined BPTS should only have one row",
                        1, dataSource.getNumberOfRows());
            }
        }
    }

    private File inlineToTemp(final File fOriginal) throws Exception {
        final File fInlined = File.createTempFile("inlined", ".bpts",
                fOriginal.getParentFile());
        fInlined.deleteOnExit();
        new Inliner().inlineFile(fOriginal, fInlined, new DummyRunner("."));
        return fInlined;
    }
}
