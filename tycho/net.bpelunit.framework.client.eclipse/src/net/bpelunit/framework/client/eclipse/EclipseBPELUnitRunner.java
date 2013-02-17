/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse;

import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.client.model.DataSourceExtension;
import net.bpelunit.framework.client.model.DeployerExtension;
import net.bpelunit.framework.client.model.ExtensionUtil;
import net.bpelunit.framework.client.model.HeaderProcessorExtension;
import net.bpelunit.framework.client.model.SOAPEncoderExtension;
import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.SpecificationException;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Implementation of the BPELUnitRunner for use in Eclipse. Uses an
 * Eclipse-based implementation of extension loading and preference storage.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class EclipseBPELUnitRunner extends BPELUnitRunner {

	public EclipseBPELUnitRunner() {
		super();
	}

	@Override
	public void configureDeployers() throws ConfigurationException {
		// done by Eclipse
	}

	@Override
	public void configureExtensions() throws ConfigurationException {
		ExtensionControl.initialize();
	}

	@Override
	public void configureInit() throws ConfigurationException {
		// done by Eclipse
	}

	@Override
	public void configureLogging() {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.removeAllAppenders();
	}

	public void configureLogging(Level level, Appender appender) {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.removeAllAppenders();
		rootLogger.addAppender(appender);
		rootLogger.setLevel(level);
	}

	@Override
	public IBPELDeployer createNewDeployer(String type)
			throws SpecificationException {
		DeployerExtension deployer = ExtensionControl
				.findDeployerExtension(type);
		if (deployer != null) {
			IBPELDeployer newDeployer = deployer.createNew();
			return newDeployer;
		} else
			throw new SpecificationException(
					"Could not find a deployer for type " + type);
	}

	@Override
	public IHeaderProcessor createNewHeaderProcessor(String name)
			throws SpecificationException {
		HeaderProcessorExtension hproc = ExtensionControl
				.findHeaderProcessorExtension(name);
		if (hproc != null) {
			return hproc.createNew();
		} else
			throw new SpecificationException(
					"Could not find a header processor for type " + name);
	}

	@Override
	public ISOAPEncoder createNewSOAPEncoder(String styleEncoding)
			throws SpecificationException {
		SOAPEncoderExtension soapEnc = ExtensionControl
				.findSOAPEncoderExtension(styleEncoding);
		if (soapEnc != null) {
			return soapEnc.createNew();
		} else
			throw new SpecificationException(
					"Could not find a SOAP Encoder for type " + styleEncoding);
	}

	@Override
	public IDataSource createNewDataSource(String type)
			throws SpecificationException {
		DataSourceExtension dataSource = ExtensionControl.findDataSource(type);
		if (dataSource != null) {
			return dataSource.createNew();
		} else
			throw new SpecificationException(
					"Could not find a Data Source for type " + type);
	}

	@Override
	public Map<String, String> getGlobalConfigurationForDeployer(
			IBPELDeployer deployer) {
		IPreferenceStore ps = BPELUnitActivator.getDefault()
				.getPreferenceStore();
		String type = ExtensionControl.getDeployerType(deployer);
		if(type == null) {
			return null;
		}
		
		String serializedMap = ps.getString(type);
		if(serializedMap == null) {
			return null;
		}
		
		return ExtensionUtil.deserializeMap(serializedMap);
	}
}
