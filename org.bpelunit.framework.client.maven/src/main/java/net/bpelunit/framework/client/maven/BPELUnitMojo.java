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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.base.BPELUnitBaseRunner;
import org.bpelunit.framework.control.result.ITestResultListener;
import org.bpelunit.framework.control.result.XMLResultProducer;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.TestSuite;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.codehaus.plexus.util.DirectoryScanner;

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

	private File[] getTestSuiteFiles() {
		final List<File> suites = new ArrayList<File>();

		getLog().debug("The tests directory is " + testsDirectory);

		// add all schemas from xsdJars that match filter
		if (testsDirectory.exists()) {

			DirectoryScanner scanner = new DirectoryScanner();
			scanner.setBasedir(testsDirectory);

			if (includes != null) {
				scanner.setIncludes((String[]) includes.toArray(new String[] {}));
			}
			
			if (excludes != null) {
				scanner.setExcludes((String[]) excludes.toArray(new String[] {}));
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
					getLog().warn(f.getName() + " does not exist or is not a file.");
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

	private class MavenBPELUnitRunner extends BPELUnitBaseRunner implements ITestResultListener {

		private int runs;
		private int errors;
		private int failures;
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
			//why do I have to implement that?
		}

		public void run(File testSuite) throws SpecificationException,
				DeploymentException {

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

			logger.info("Running BPELUnit test suite: " + testSuite.getName());
			suite.run();
			suite.removeResultListener(this);

			String result = "Test Run Completed. " + runs + " "
					+ getPluralOf(runs, "run") + " (" + failures + " "
					+ getPluralOf(failures, "failure") + ", " + errors + " "
					+ getPluralOf(errors, "error") + ") \n";

			logger.info(result);

			FileOutputStream fos = null;
			try {
				String filename = "BPTEST-" + suite.getSafeName() + ".xml";
				fos = new FileOutputStream(new File(reportsDirectory, filename));
				XMLResultProducer.writeXML(fos, suite);
			} catch (IOException e) {
				logger.warn("Could not write test results.", e);
			} finally {
				IOUtils.closeQuietly(fos);
			}

			suite.shutDown();
		}

		private String getPluralOf(int no, String name) {
			return no == 1 ? name : name + "s";
		}

		public void testCaseStarted(TestCase testCase) {
			BPELUnitMojo.this.getLog().info("START: " + testCase.getName() + ": " + testCase.getStatus().toString() + "\n");
		}

		public void testCaseEnded(TestCase testCase) {
			if (testCase.getStatus().isFailure())
				failures++;
			if (testCase.getStatus().isError())
				errors++;
			runs++;
			BPELUnitMojo.this.getLog().info("END: " + testCase.getName() + ": " + testCase.getStatus().toString() + "\n");
		}

		public void progress(ITestArtefact testArtefact) {
			// TODO: What to do here?
			BPELUnitMojo.this.getLog().info("[----]: " + testArtefact.getName() + ": " + testArtefact.getStatus().toString() + "\n");
		}
	}
}
