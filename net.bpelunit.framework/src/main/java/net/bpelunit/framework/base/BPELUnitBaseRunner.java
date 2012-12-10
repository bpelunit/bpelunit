/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.base;

import java.io.File;
import java.net.MalformedURLException;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.ext.ExtensionRegistry;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.SpecificationException;

import org.apache.commons.io.FilenameUtils;

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

		if ( (extensionsFile != null) && (new File(extensionsFile).exists())) {
			try {
				ExtensionRegistry.loadRegistry(new File(extensionsFile).toURI().toURL(), isSkipUnknownExtensions());
			} catch (MalformedURLException e) {
				throw new ConfigurationException("BPELUnit could not locate the extension file: " + extensionsFile, e);
			}
		} else {
			// if file could not be loaded, default to embedded settings
			ExtensionRegistry.loadRegistry(this.getClass().getResource('/' + CONFIG_DIR + '/' + EXTENSIONS_FILE_NAME), isSkipUnknownExtensions());
		}
	}

	@Override
	public void configureDeployers() throws ConfigurationException {
		String deploymentConfigFile= FilenameUtils.concat(fHomeDirectory, FilenameUtils.concat(CONFIG_DIR, DEPLOYER_CONFIG_FILE_NAME));

		if ( (deploymentConfigFile != null) && (new File(deploymentConfigFile).exists())) {
			try {
				ExtensionRegistry.loadDeploymentConfiguration(new File(deploymentConfigFile).toURI().toURL());
			} catch (MalformedURLException e) {
				throw new ConfigurationException("BPELUnit could not locate the deployer config file: " + deploymentConfigFile, e);
			}
		} else {
			// if file could not be loaded, default to embedded settings
			ExtensionRegistry.loadDeploymentConfiguration(this.getClass().getResource('/' + CONFIG_DIR + '/' + DEPLOYER_CONFIG_FILE_NAME));
		}
	}

	protected void setHomeDirectory(String homeDirectory) throws ConfigurationException {
		fHomeDirectory= homeDirectory;
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
	public IDataSource createNewDataSource(String type) throws SpecificationException {
		return ExtensionRegistry.createNewDataSourceForType(type);
	}
}
