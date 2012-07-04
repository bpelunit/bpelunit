package net.bpelunit.framework.client.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.ITestResultListener;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.report.ITestArtefact;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomWriter;

/**
 * Runs BPELUnit tests.
 * 
 * @author Tammo van Lessen
 * 
 * @goal test
 * @phase test
 */
public class BPELUnitMojo extends AbstractMojo {

	/**
	 * Set this to 'true' to bypass BPELunit tests entirely. Its use is NOT
	 * RECOMMENDED, especially if you enable it using the "maven.test.skip"
	 * property, because maven.test.skip disables both running the tests and
	 * compiling the tests. Consider using the skipTests parameter instead.
	 * 
	 * @parameter default-value="false" expression="${maven.test.skip}"
	 */
	private boolean skip;

	/**
	 * If true, the framework halts on errors
	 * 
	 * @parameter default-value="false"
	 */
	private boolean haltOnError;

	/**
	 * If true, the framework halts on failures
	 * 
	 * @parameter default-value="false"
	 */
	private boolean haltOnFailure;

	/**
	 * The base directory of the project being tested. This can be obtained in
	 * your unit test by System.getProperty("basedir").
	 * 
	 * @parameter default-value="${basedir}"
	 */
	private File basedir;

	/**
	 * Base directory where all reports are written to.
	 * 
	 * @parameter default-value="${project.build.directory}/bpelunit-reports"
	 */
	private File reportsDirectory;

	/**
	 * The test source directory containing test class sources.
	 * 
	 * @parameter default-value="${basedir}/src/test/bpelunit"
	 * @required
	 */
	private File testsDirectory;

	/**
	 * List of patterns (separated by commas) used to specify the tests that
	 * should be included in testing. When not specified and when the
	 * <code>test</code> parameter is not specified, the default includes will
	 * be
	 * <code>**&#47;Test*.java   **&#47;*Test.java   **&#47;*TestCase.java</code>
	 * . This parameter is ignored if TestNG suiteXmlFiles are specified.
	 * 
	 * @parameter
	 */
	private List<String> includes;

	/**
	 * List of patterns (separated by commas) used to specify the tests that
	 * should be excluded in testing. When not specified and when the
	 * <code>test</code> parameter is not specified, the default excludes will
	 * be <code>**&#47;*$*</code> (which excludes all inner classes). This
	 * parameter is ignored if TestNG suiteXmlFiles are specified.
	 * 
	 * @parameter
	 */
	private List<String> excludes;
	
	private static final String LS = System.getProperty( "line.separator" );

	private File[] getTestSuiteFiles() {
		final List<File> suites = new ArrayList<File>();

		getLog().debug("The tests directory is " + testsDirectory);

		// add all schemas from xsdJars that match filter
		if (testsDirectory.exists()) {

			DirectoryScanner scanner = new DirectoryScanner();
			scanner.setBasedir(testsDirectory);

			if (includes != null) {
				scanner.setIncludes((String[]) includes
						.toArray(new String[] {}));
			}

			if (excludes != null) {
				scanner.setExcludes((String[]) excludes
						.toArray(new String[] {}));
			}

			scanner.addDefaultExcludes();

			scanner.setCaseSensitive(false);
			scanner.scan();

			String[] files = scanner.getIncludedFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					getLog().debug("Adding " + files[i]);
					suites.add(new File(testsDirectory, files[i]));
				}
			}
		}

		return (File[]) suites.toArray(new File[suites.size()]);
	}

	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info("BPELUnit test runs skipped.");
			return;
		}

		File f = reportsDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		Map<String, String> options = new HashMap<String, String>();
		options.put(BPELUnitRunner.HALT_ON_ERROR, Boolean.toString(haltOnError));
		options.put(BPELUnitRunner.HALT_ON_FAILURE,
				Boolean.toString(haltOnFailure));

		MavenBPELUnitRunner runner = new MavenBPELUnitRunner();
		try {
			runner.initialize(options);

			for (File suite : getTestSuiteFiles()) {
				if (suite.exists() && suite.isFile()) {
					runner.run(suite);
				} else {
					getLog().warn(
							f.getName() + " does not exist or is not a file.");
				}
			}

		} catch (ConfigurationException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (SpecificationException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (DeploymentException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private class MavenBPELUnitRunner extends BPELUnitBaseRunner implements
			ITestResultListener {

		private int runs;
		private int errors;
		private int failures;
		private long suiteRunTime = 0;
		private long testStart = 0;
		private long testStop = 0;

		private NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		private Xpp3Dom xmlSuite;

		private Log logger;

		public MavenBPELUnitRunner() {
			logger = BPELUnitMojo.this.getLog();
		}

		@Override
		public void configureInit() throws ConfigurationException {
			setHomeDirectory(".");
		}

		@Override
		public void configureLogging() throws ConfigurationException {
			// why do I have to implement that?
		}

		public void run(File testSuite) throws SpecificationException,
				DeploymentException, MojoExecutionException {

			runs = 0;
			errors = 0;
			failures = 0;
			xmlSuite = new Xpp3Dom("testsuite");
			addProperties(xmlSuite);
			
			TestSuite suite = loadTestSuite(testSuite);
			suite.addResultListener(this);

			try {
				suite.setUp();
			} catch (DeploymentException e) {
				try {
					suite.shutDown();
				} catch (DeploymentException e1) {
				}
				throw e;
			}

			logger.info("Running BPELUnit: " + testSuite.getName());
			long started = System.currentTimeMillis();
			suite.run();
			long stopped = System.currentTimeMillis();
			suiteRunTime = stopped - started;

			suite.removeResultListener(this);

			String result = "Tests run: " + runs + ", Failures: " + failures
					+ ", Errors: " + errors + ", Time elapsed: "
					+ nf.format((double) suiteRunTime / 1000) + " sec";
			logger.info(result);

			xmlSuite.setAttribute("name", suite.getRawName());
			xmlSuite.setAttribute("time",
					nf.format((double) suiteRunTime / 1000));
			xmlSuite.setAttribute("tests", String.valueOf(runs));
			xmlSuite.setAttribute("errors", String.valueOf(errors));
			xmlSuite.setAttribute("failures", String.valueOf(failures));

			String filename = "TEST-" + suite.getSafeName() + ".xml";
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(new File(reportsDirectory, filename)), "UTF-8")));

				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + LS);

				Xpp3DomWriter.write(new PrettyPrintXMLWriter(writer), xmlSuite);
			} catch (UnsupportedEncodingException e) {
				throw new MojoExecutionException("Unable to use UTF-8 encoding", e);
			} catch (FileNotFoundException e) {
				throw new MojoExecutionException("Unable to create file: " + e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(writer);
			}

			suite.shutDown();
		}

		public void testCaseStarted(TestCase testCase) {
			logger.debug("START: " + testCase.getName() + ": "
					+ testCase.getStatus().toString() + "\n");
			testStart = System.currentTimeMillis();
		}

		public void testCaseEnded(TestCase testCase) {
			testStop = System.currentTimeMillis();

			Xpp3Dom xmlTestCase = new Xpp3Dom("testcase");
			xmlTestCase.setAttribute("group", "BPELUnit");
			xmlTestCase.setAttribute("name", testCase.getRawName());
			xmlTestCase.setAttribute("time",
					nf.format((double) (testStop - testStart) / 1000));

			if (testCase.getStatus().isFailure()) {
				failures++;
				Xpp3Dom element = new Xpp3Dom("failure");
				element.setAttribute("message", testCase.getStatus()
						.getMessage());
				xmlTestCase.addChild(element);
			}

			if (testCase.getStatus().isError()) {
				errors++;
				Xpp3Dom element = new Xpp3Dom("error");
				element.setAttribute("message", testCase.getStatus()
						.getMessage());
				xmlTestCase.addChild(element);
			}

			runs++;

			xmlSuite.addChild(xmlTestCase);

			logger.debug("END: " + testCase.getName() + ": "
					+ testCase.getStatus().toString() + "\n");
		}

		public void progress(ITestArtefact testArtefact) {
			// TODO: What to do here?
			logger.debug("[----]: " + testArtefact.getName() + ": "
					+ testArtefact.getStatus().toString() + "\n");
		}

		private void addProperties(Xpp3Dom testSuite) {
			Xpp3Dom properties = new Xpp3Dom("properties");
			testSuite.addChild(properties);

			Properties systemProperties = System.getProperties();
			if (systemProperties != null) {
				@SuppressWarnings("rawtypes")
				Enumeration propertyKeys = systemProperties.propertyNames();

				while (propertyKeys.hasMoreElements()) {
					String key = (String) propertyKeys.nextElement();
					String value = systemProperties.getProperty(key);
					if (value == null) {
						value = "null";
					}

					Xpp3Dom property = new Xpp3Dom("property");
					properties.addChild(property);
					property.setAttribute("name", key);
					property.setAttribute("value", value);
				}
			}
		}

	}
}
