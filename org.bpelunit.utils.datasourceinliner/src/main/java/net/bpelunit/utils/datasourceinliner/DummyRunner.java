package net.bpelunit.utils.datasourceinliner;

import java.util.HashMap;

import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.exception.ConfigurationException;

/**
 * Dummy runner which does not set up any kind of logging, and has no special
 * logic. Useful for creating data sources in the inliner. Should
 * <emph>NOT</emph> be used to run real tests: if you want a Runner for testing
 * purposes, you should use {@link net.bpelunit.test.util.TestTestRunner} from
 * the core project, instead.
 *
 * @author Antonio García-Domínguez
 */
public class DummyRunner extends BPELUnitBaseRunner {

    private String fHomeDirectory;

    public DummyRunner() throws ConfigurationException {
        this(System.getenv(BPELUNIT_HOME_ENV));
    }

    public DummyRunner(String homeDirectory) throws ConfigurationException {
        fHomeDirectory = homeDirectory;
        initialize(new HashMap<String, String>());
    }

    @Override
    public void configureInit() throws ConfigurationException {
        setHomeDirectory(fHomeDirectory);
    }

    @Override
    public void configureLogging() throws ConfigurationException {}
}
