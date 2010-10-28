package net.bpelunit.utils.datasourceinliner;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.control.datasource.DataSourceUtil;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

/**
 * Data source inliner for BPELUnit test suite specifications in BPTS format.
 * Test case templates are expanded: each is repeated as many times as there are
 * rows in the effective data source. All data sources and the test suite setup
 * script are removed. Each of the resulting test cases get a setup script which
 * concatenates the old test suite setup script, the old test case setup script,
 * and a set of variable assignments which match the variables contained in the
 * corresponding row of the data source. The rest of the BPTS remains unchanged,
 * in order to be forwards compatible. In particular, delaySequences are *NOT*
 * modified.
 * 
 * @author Antonio García-Domínguez
 */
public class Inliner {

    /**
     * Expands the data sources in the BPTS so test cases are independent from
     * each other. See the class Javadoc for more details.
     *
     * @param xmlOldSuiteDoc
     *             XMLTestSuiteDocument produced by parsing the original BPTS
     *             using {@link XMLTestSuiteDocument.Factory#parse}.
     * @param bptsDir
     *             File of the directory which contains the BPTS, from which
     *             relative paths in the original BPTS will be interpreted.
     * @param runner
     *             Runner which will be used to create the data source.
     * @throws XmlException
     *             Could not clone the original XML tree.
     * @throws DataSourceException
     *             Could not access one of the rows of the data source.
     */
    public XMLTestSuiteDocument inlineDataSources(
            XMLTestSuiteDocument xmlOldSuiteDoc, File bptsDir,
            BPELUnitRunner runner) throws XmlException, DataSourceException {

        // Clone the original document, removing all test cases
        XMLTestSuiteDocument newDoc = (XMLTestSuiteDocument) xmlOldSuiteDoc
                .copy();
        final XMLTestSuite xmlNewSuite = newDoc.getTestSuite();
        final XMLTestCasesSection xmlNewCases = removeAllTestCases(xmlNewSuite);
        if (xmlNewSuite.isSetSetUp()) {
            xmlNewSuite.unsetSetUp();
        }

        // Expand the original test cases into the new document
        final XMLTestSuite xmlOldSuite = xmlOldSuiteDoc.getTestSuite();
        final XMLTestCasesSection xmlOldCases = xmlOldSuite.getTestCases();
        final String globalSetup = getGlobalSetUp(xmlOldSuite);

        for (XMLTestCase xmlOldCase : xmlOldCases.getTestCaseList()) {
            IDataSource dataSource = DataSourceUtil.createDataSource(
                    xmlOldSuite, xmlOldCase, bptsDir, runner);
            String localSetup = getLocalSetUp(xmlOldCase);

            final int nRows = dataSource != null ? dataSource.getNumberOfRows()
                    : 1;
            for (int i = 0; i < nRows; ++i) {
                String expandedSetup = computeExpandedSetUp(globalSetup,
                        localSetup, dataSource, i);
                final XMLTestCase xmlNewCase = createExpandedTestCase(
                        xmlOldCase, dataSource != null ? i : -1, expandedSetup);

                // Add a new test case to the new document and replace it
                // with the test case created above
                xmlNewCases.addNewTestCase().set(xmlNewCase);
            }
        }

        return newDoc;
    }

    /**
     * Inlines the data sources in a .bpts file and saves the resulting test
     * suite to another file.
     * 
     * @param fSource
     *            Original .bpts file to be modified.
     * @param fDestination
     *            Destination file for the modified test suite specification.
     * @throws XmlException
     *             Could not parse the original .bpts or dump the modified .bpts
     *             back as XML.
     * @throws IOException
     *             Could not open fOriginal or fTemp or there was an I/O error
     *             while manipulating them.
     * @throws DataSourceException
     *             Could not read one of the rows of one of the data sources in
     *             the BPTS file.
     */
    public void inlineFile(final File fSource, final File fDestination,
            final BPELUnitRunner runner) throws XmlException, IOException,
            DataSourceException {
        final XMLTestSuiteDocument originalBpts = XMLTestSuiteDocument.Factory
                .parse(fSource);
        final XMLTestSuiteDocument inlinedDoc = inlineDataSources(originalBpts,
                fSource.getParentFile(), runner);
        inlinedDoc.save(fDestination);
    }

    /*** PRIVATE METHODS ***/

    private XMLTestCase createExpandedTestCase(XMLTestCase original, int row,
            String setup) {
        final XMLTestCase xmlNewCase = (XMLTestCase) original.copy();
        if (row >= 0) {
            xmlNewCase.setName(String.format("%s (Row %d)", original.getName(),
                    row + 1));
        }
        if (xmlNewCase.isSetSetUp()) {
            xmlNewCase.unsetSetUp();
        }
        if (setup.trim().length() > 0) {
            final XMLSetUp xmlNewSetUp = xmlNewCase.addNewSetUp();
            xmlNewSetUp.setScript(setup);
        }
        return xmlNewCase;
    }

    private String computeExpandedSetUp(final String globalSetup,
            String localSetup, IDataSource dataSource, int row)
            throws DataSourceException {
        return globalSetup + "\n" + localSetup + "\n"
                + renderRowAsVelocity(dataSource, row);
    }

    private XMLTestCasesSection removeAllTestCases(
            final XMLTestSuite xmlNewSuite) {
        final XMLTestCasesSection xmlNewTestCases = xmlNewSuite.getTestCases();
        xmlNewTestCases.setTestCaseArray(new XMLTestCase[] {});
        return xmlNewTestCases;
    }

    private String getLocalSetUp(XMLTestCase xmlOldCase) {
        if (xmlOldCase.isSetSetUp() && xmlOldCase.getSetUp().isSetScript()) {
            return xmlOldCase.getSetUp().getScript();
        }
        return "";
    }

    private String getGlobalSetUp(final XMLTestSuite xmlOldSuite) {
        if (xmlOldSuite.isSetSetUp() && xmlOldSuite.getSetUp().isSetScript()) {
            return xmlOldSuite.getSetUp().getScript();
        }
        return "";
    }

    private String renderRowAsVelocity(IDataSource dataSource, int row)
            throws DataSourceException {
        if (dataSource == null) {
            return "";
        }
        dataSource.setRow(row);
        final StringBuffer sbuf = new StringBuffer();
        for (String fieldName : dataSource.getFieldNames()) {
            sbuf.append(String.format("#set( $%s = ", fieldName));
            renderValueAsVelocity(sbuf, dataSource.getValueFor(fieldName));
            sbuf.append(" )\n");
        }
        return sbuf.toString();
    }

    private void renderValueAsVelocity(StringBuffer sbuf, Object value) {
        if (value instanceof String) {
            sbuf.append("'" + (String) value + "'");
        }
        else if (value instanceof Iterable<?>) {
            sbuf.append("[");
            boolean bFirst = true;
            for (Object elem : (Iterable<?>) value) {
                if (!bFirst) {
                    sbuf.append(", ");
                }
                renderValueAsVelocity(sbuf, elem);
                bFirst = false;
            }
            sbuf.append("]");
        }
        else {
            sbuf.append(value.toString());
        }
    }

}
