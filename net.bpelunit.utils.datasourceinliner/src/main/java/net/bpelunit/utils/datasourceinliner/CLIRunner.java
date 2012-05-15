package net.bpelunit.utils.datasourceinliner;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

/**
 * Command-line interface for the data source inliner.
 * 
 * @author Antonio García-Domínguez
 */
public final class CLIRunner {

	private CLIRunner() {
	}
	
    /**
     * Runs the inliner from the command line.
     * 
     * @param args
     *            Command line arguments.
     * @throws IOException
     *             Could not create the temporary file.
     * @throws XmlException
     *             There was a problem during XML parsing.
     * @throws DataSourceException
     *             There was a problem while reading the contents of the data
     *             sources in the BPTS file.
     * @throws ConfigurationException
     *             BPELUnit was not configured properly.
     */
    public static void main(String[] args) throws IOException,
            DataSourceException, XmlException, ConfigurationException {
        if (args.length != 1) {
            System.err.println(String.format(
                    "Usage: dsexpand.sh (path to bpts)\n\nExpands the "
                    + "specified BPTS and dumps it to the standard output."));
            System.exit(1);
        }

        System.out.println(expandBPTS(args[0]));
    }

    static String expandBPTS(String sPath) throws IOException,
            DataSourceException, XmlException, ConfigurationException {
        File fBpts = new File(sPath);
        XMLTestSuiteDocument bptsDoc = XMLTestSuiteDocument.Factory.parse(fBpts);
        final XMLTestSuiteDocument inlinedDoc = new Inliner().inlineDataSources(
                bptsDoc, fBpts.getParentFile(), new DummyRunner());

        XmlOptions opts = new XmlOptions();
        opts.setSavePrettyPrint();
        opts.setSaveAggressiveNamespaces();
        return inlinedDoc.xmlText(opts);
    }

}
