/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IHeaderProcessor;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.util.ExtensionRegistry;
import org.bpelunit.framework.coverage.CoverageConstants;
import org.bpelunit.framework.coverage.CoverageMeasurementTool;
import org.bpelunit.framework.coverage.ICoverageMeasurementTool;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;

/**
 * Basic test runner which uses XML configuration files for configuring extensions and configuring
 * them. 
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class BPELUnitBaseRunner extends BPELUnitRunner {

	public static final String CONFIG_DIR= "conf";
	public static final String BPELUNIT_HOME_ENV= "BPELUNIT_HOME";

	public static final String EXTENSIONS_FILE_NAME= "extensions.xml";
	public static final String DEPLOYER_CONFIG_FILE_NAME= "configuration.xml";

	public static final String COVERAGETOOL_CONFIG_FILE_NAME= "coverageMetricsConfiguration.xml";
	private String fHomeDirectory;

	public BPELUnitBaseRunner() {
		super();
	}


	@Override
	public void configureInit() throws ConfigurationException {
		setHomeDirectory(System.getenv(BPELUNIT_HOME_ENV));
	}

	@Override
	public void configureExtensions() throws ConfigurationException {
		String extensionsFile= FilenameUtils.concat(fHomeDirectory, FilenameUtils.concat(CONFIG_DIR, EXTENSIONS_FILE_NAME));

		if ( (extensionsFile == null) || ! (new File(extensionsFile).exists()))
			throw new ConfigurationException("BPELUnit was expecting a extension XML file at location " + extensionsFile);

		ExtensionRegistry.loadRegistry(new File(extensionsFile), isSkipUnknownExtensions());

	}

	@Override
	public void configureDeployers() throws ConfigurationException {
		String deploymentConfigFile= FilenameUtils.concat(fHomeDirectory, FilenameUtils.concat(CONFIG_DIR, DEPLOYER_CONFIG_FILE_NAME));

		if ( (deploymentConfigFile == null) || ! (new File(deploymentConfigFile).exists()))
			throw new ConfigurationException("BPELUnit was expecting a extension XML file at location " + deploymentConfigFile);

		ExtensionRegistry.loadDeploymentConfiguration(new File(deploymentConfigFile));

	}

	protected void setHomeDirectory(String homeDirectory) throws ConfigurationException {

		if (homeDirectory == null)
			throw new ConfigurationException(
					"BPELUnit cannot run without a set home directory. Either set environment variable BPELUNIT_HOME or instantiate BPELUnitCore with a home directory URL.");

		if (!new File(homeDirectory).exists())
			throw new ConfigurationException("The specified home directory \"" + homeDirectory
					+ "\" does not exist - cannot run without a home directory.");

		fHomeDirectory= homeDirectory;
//		ABSOLUT_CONFIG_DIR=FilenameUtils.concat(fHomeDirectory, CONFIG_DIR);
	}
	
	@Override
	public abstract void configureLogging() throws ConfigurationException;

	@Override
	public IBPELDeployer createNewDeployer(String type) throws SpecificationException {
		IBPELDeployer deployer= ExtensionRegistry.createNewDeployerForType(type);
		ExtensionRegistry.configure(type, deployer);
		return deployer;
	}

	@Override
	public IHeaderProcessor createNewHeaderProcessor(String name) throws SpecificationException {
		return ExtensionRegistry.createNewHeaderProcessorForType(name);
	}

	@Override
	public ISOAPEncoder createNewSOAPEncoder(String styleEncoding) throws SpecificationException {
		return ExtensionRegistry.createNewEncoderForType(styleEncoding);
	}
	
	@Override
	public void configureCoverageTool() throws ConfigurationException {
		ICoverageMeasurementTool coverageTool=new CoverageMeasurementTool();
		setCoverageMeasurmentTool(coverageTool);
		getCoverageMeasurmentTool().setPathToWSDL(FilenameUtils.concat(FilenameUtils.concat(fHomeDirectory,CONFIG_DIR),CoverageConstants.COVERAGE_SERVICE_WSDL));
		String coverageFile= FilenameUtils.concat(fHomeDirectory, FilenameUtils.concat(CONFIG_DIR, COVERAGETOOL_CONFIG_FILE_NAME));
		if ( (coverageFile == null) || ! (new File(coverageFile).exists())){
			coverageTool.setErrorStatus("BPELUnit was expecting a coverage tool configuration file.");
			throw new ConfigurationException("BPELUnit was expecting a coverage tool configuration file.");
		}
		
		Map<String, List<String>> configMap = ExtensionRegistry.loadCoverageToolConfiguration(new File(coverageFile));
		List<String> directory=new ArrayList<String>();
		directory.add(FilenameUtils.concat(fHomeDirectory,CONFIG_DIR));
		System.out.println("BPELUnitBaseRunner: configuration for Coverage Measurment loaded");
		coverageTool.configureMetrics(configMap);
		
	}


}
