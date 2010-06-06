package org.bpelunit.utils.datasourceinliner;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

/**
 * Data source inliner for BPELUnit test suite specifications in BPTS format.
 * Test case templates are expanded: each is repeated as many times as there are
 * rows in the effective data source, with an embedded Velocity data source with
 * the proper values. Existing test suite and test case data sources are
 * removed. The rest of the BPTS remains unchanged, in order to be forwards
 * compatible. In particular, delaySequences and test suite/test case setUp
 * blocks are *NOT* modified.
 * 
 * @author Antonio García-Domínguez
 */
public class Inliner {

    /**
     * Expands the data sources in the BPTS so test cases do not depend on each
     * other or on the test suite data source. Repeats each test case template
     * as many times as there are rows in its effective data source, adding an
     * embedded Velocity data source with the right contents.
     */
    public XMLTestSuiteDocument inlineDataSources(XMLTestSuiteDocument source) {
        return source;
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
     */
    public void inlineFile(final File fSource, final File fDestination)
            throws XmlException, IOException {
        final XMLTestSuiteDocument originalBpts
            = XMLTestSuiteDocument.Factory.parse(fSource);
        final XMLTestSuiteDocument inlinedDoc
            = inlineDataSources(originalBpts);
        inlinedDoc.save(fDestination);
    }

}
