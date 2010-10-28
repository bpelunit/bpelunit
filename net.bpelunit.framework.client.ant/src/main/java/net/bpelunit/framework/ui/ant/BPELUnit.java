/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.ui.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;

/**
 * <p>
 * BPELUnit Ant Task
 * </p>
 * 
 * <p>
 * Invocation:
 * 
 * <pre>
 *     &lt;bpelunit testsuite=&quot;testsuite.bpts&quot; haltOnError=&quot;true&quot; haltOnFailure=&quot;true&quot; bpelunitdir=&quot;c:\location&quot;&gt; 
 *     &lt;output type=&quot;plain&quot; file=&quot;false&quot; /&gt;
 *     &lt;logging level=&quot;plain&quot; file=&quot;false&quot; /&gt;
 *     &lt;/bpelunit&gt;
 * </pre>
 * 
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnit extends Task {

	/**
	 * The test suite path and file name
	 */
	private String fTestSuite;

	/**
	 * The BPELUnit home directory
	 */
	private String fBPELUnitDir;

	/**
	 * If true, the framework halts on errors
	 */
	private boolean fHaltOnError;

	/**
	 * if true, the framework halts on failures
	 */
	private boolean fHaltOnFailure;

	/**
	 * List of "outputters"
	 */
	private List<Output> fOutputList= new ArrayList<Output>();

	/**
	 * List of "loggers"
	 */
	private List<Logging> fLoggingList= new ArrayList<Logging>();

	@Override
	public void execute() {

		// Check attributes

		if (fTestSuite == null || fTestSuite.equals(""))
			throw new BuildException("The testsuite argument is required.");

		File testSuite= new File(fTestSuite);
		if (!testSuite.exists())
			throw new BuildException("The testsuite file does not exist.");

		if (fBPELUnitDir == null || fBPELUnitDir.equals(""))
			fBPELUnitDir= System.getenv("BPELUNIT_HOME");

		if (fBPELUnitDir == null || fBPELUnitDir.equals(""))
			throw new BuildException("The BPELUnit directory is required - either specify an argument or set environment variable BPELUNIT_HOME.");

		for (Output output : fOutputList) {
			if (!output.hasValidStyle())
				throw new BuildException("An output specification is missing the style attribute, or wrong style specified.");
		}

		for (Logging logging : fLoggingList) {
			if (!logging.hasValidLevel())
				throw new BuildException("A logging specification is missing the level attribute, or wrong level specified.");
		}

		HashMap<String, String> options= new HashMap<String, String>();
		options.put(BPELUnitRunner.HALT_ON_ERROR, Boolean.toString(fHaltOnError));
		options.put(BPELUnitRunner.HALT_ON_FAILURE, Boolean.toString(fHaltOnFailure));

		BPELUnitAntRunner runner= new BPELUnitAntRunner(fBPELUnitDir, fLoggingList, fOutputList);
		try {
			runner.initialize(options);
			runner.run(testSuite);
		} catch (ConfigurationException e) {
			throw new BuildException(e.getMessage(), e);
		} catch (SpecificationException e) {
			throw new BuildException(e.getMessage(), e);
		} catch (DeploymentException e) {
			throw new BuildException(e.getMessage(), e);
		}

	}

	public void setTestSuite(String testSuite) {
		fTestSuite= testSuite;
	}

	public void setBPELUnitDir(String bpelUnitDir) {
		fBPELUnitDir= bpelUnitDir;
	}

	public void setHaltOnError(boolean haltOnError) {
		fHaltOnError= haltOnError;
	}

	public void setHaltOnFailure(boolean haltOnFailure) {
		fHaltOnFailure= haltOnFailure;
	}

	public void addConfiguredOutput(Output output) {
		fOutputList.add(output);
	}

	public void addConfiguredLogging(Logging logging) {
		fLoggingList.add(logging);
	}


	public static class Output extends Extension {

		public static final String STYLE_PLAIN= "PLAIN";
		public static final String STYLE_XML= "XML";

		private String fStyle;

		public String getStyle() {
			return fStyle;
		}

		public void setStyle(String style) {
			fStyle= style;
		}


		public boolean hasValidStyle() {
			return STYLE_PLAIN.equals(fStyle) || STYLE_XML.equals(fStyle);
		}
	}

	public static class Extension {

		private String fFile;
		private OutputStream fOutput;

		public OutputStream getOutput() {
			return fOutput;
		}

		public void initialize() throws ConfigurationException {
			fOutput= createOutputStream();
		}

		public void dispose() {
			if (fOutput != null) {
				try {
					fOutput.flush();
					fOutput.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		private OutputStream createOutputStream() throws ConfigurationException {
			if (fFile != null) {
				String dir= FilenameUtils.getPath(fFile);
				if (!"".equals(dir))
					new File(dir).mkdirs();

				try {
					return new FileOutputStream(fFile);
				} catch (FileNotFoundException e) {
					throw new ConfigurationException("Cannot create file with name " + fFile + " for output.");
				}
			} else
				return System.out;
		}

		public void write(String line) throws IOException {
			fOutput.write(line.getBytes());
		}

		public void setFile(String file) {
			fFile= file;
		}

		public String getFile() {
			return fFile;
		}
	}

	public static class Logging extends Extension {

		private String fLevel;

		public boolean hasValidLevel() {
			if (fLevel == null)
				return false;
			try {
				Level.parse(fLevel);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}

		public void setLevel(String type) {
			fLevel= type;
		}

		public String getLevel() {
			return fLevel;
		}

		public OutputStream getOutputStream() {
			return getOutput();
		}
	}

}
