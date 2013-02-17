/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.bpelunit.framework.client.model.DataSourceExtension;
import net.bpelunit.framework.client.model.DeployerExtension;
import net.bpelunit.framework.client.model.Extension;
import net.bpelunit.framework.client.model.HeaderProcessorExtension;
import net.bpelunit.framework.client.model.SOAPEncoderExtension;
import net.bpelunit.framework.control.datasource.DataSourceHelper;
import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.exception.SpecificationException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * This class is responsible for loading BPELUnit extensions out of client
 * plugins.
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke <bpelunit@daniel-luebke.de>
 * 
 */
public class ExtensionControl {

	public static final String DEPLOYER_EXTENSION_POINT_ID = "net.bpelunit.framework.client.eclipse.bpelDeployer";
	public static final String ENCODER_EXTENSION_POINT_ID = "net.bpelunit.framework.client.eclipse.soapEncoder";
	public static final String HEADER_EXTENSION_POINT_ID = "net.bpelunit.framework.client.eclipse.headerProcessor";
	public static final String DATASOURCE_EXTENSION_POINT_ID = "net.bpelunit.framework.client.eclipse.dataSource";

	private static Map<String, DeployerExtension> fDeployers = new HashMap<String, DeployerExtension>();
	public static Map<String, DeployerExtension> getDeployers() {
		return fDeployers;
	}

	public static Map<String, HeaderProcessorExtension> getHeaderProcessors() {
		return new HashMap<String, HeaderProcessorExtension>(fHeaderProcessors);
	}

	public static Map<String, SOAPEncoderExtension> getSOAPEncoders() {
		return new HashMap<String, SOAPEncoderExtension>(fSOAPEncoders);
	}

	public static Map<String, DataSourceExtension> getDataSources() {
		return new HashMap<String, DataSourceExtension>(fDataSources);
	}

	private static Map<String, HeaderProcessorExtension> fHeaderProcessors = new HashMap<String, HeaderProcessorExtension>();
	private static Map<String, SOAPEncoderExtension> fSOAPEncoders = new HashMap<String, SOAPEncoderExtension>();
	private static Map<String, DataSourceExtension> fDataSources = new HashMap<String, DataSourceExtension>();

	private static boolean isInitialized = false;

	public static void initialize() {

		if (!isInitialized) {
			loadExtensions(DEPLOYER_EXTENSION_POINT_ID,
					new BPELDeployerLoader());
			loadExtensions(ENCODER_EXTENSION_POINT_ID, new SOAPEncoderLoader());
			loadExtensions(HEADER_EXTENSION_POINT_ID,
					new HeaderProcessorLoader());
			loadExtensions(DATASOURCE_EXTENSION_POINT_ID,
					new DataSourceLoader());

			isInitialized = true;
		}
	}

	private static void loadExtensions(String extensionPointID, ELoader loader) {

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(extensionPointID);
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension element : extensions) {
			IConfigurationElement[] elements = element
					.getConfigurationElements();
			for (IConfigurationElement element0 : elements) {
				loader.load(element0);
			}
		}
	}

	static class ELoader {

		protected String id;
		protected String name;
		protected String icon;

		public void load(IConfigurationElement e) {

			id = e.getAttribute("id");
			name = e.getAttribute("name");
		}
	}

	static class BPELDeployerLoader extends ELoader {

		@Override
		public void load(IConfigurationElement e) {

			super.load(e);

			if (id == null) {
				BPELUnitActivator
						.logErrorMessage("Error reading bpel deployer list from plugin.xml files - required attributes are missing.");
			} else {
				fDeployers.put(id, new DeployerExtension(id, name, e));
			}
		}
	}

	static class HeaderProcessorLoader extends ELoader {

		@Override
		public void load(IConfigurationElement e) {

			super.load(e);

			if (id == null) {
				BPELUnitActivator
						.logErrorMessage("Error reading header processor list from plugin.xml files - required attributes are missing.");
			} else {
				fHeaderProcessors.put(id, new HeaderProcessorExtension(id,
						name, e));
			}
		}
	}

	static class SOAPEncoderLoader extends ELoader {

		@Override
		public void load(IConfigurationElement e) {

			super.load(e);

			if (id == null) {
				BPELUnitActivator
						.logErrorMessage("Error reading soap encoder list from plugin.xml files - required attributes are missing.");
			} else {
				fSOAPEncoders.put(id, new SOAPEncoderExtension(id, name, e));
			}
		}
	}
	
	static class DataSourceLoader extends ELoader {
		@Override
		public void load(IConfigurationElement e) {
			super.load(e);
			
			try {
				IDataSource ds = (IDataSource)e.createExecutableExtension("class");
				String shortName = DataSourceHelper.getShortName(ds);
				name = DataSourceHelper.getName(ds);
				
				fDataSources.put(id, new DataSourceExtension(shortName, name, e));
			} catch (CoreException e1) {
				BPELUnitActivator
				.logErrorMessage("Error reading data source list from plugin.xml files - data source class cannot be instantiated.");
				e1.printStackTrace();
			}
		}
	}

	public static DeployerExtension findDeployerExtension(String type) {
		return fDeployers.get(type);
	}

	public static HeaderProcessorExtension findHeaderProcessorExtension(
			String name) {
		return fHeaderProcessors.get(name);
	}

	public static SOAPEncoderExtension findSOAPEncoderExtension(
			String styleEncoding) {
		return fSOAPEncoders.get(styleEncoding);
	}

	public static DataSourceExtension findDataSource(String type) {
		return fDataSources.get(type);
	}

	public static String[][] getDeployerMetaInformation() {
		initialize();
		String[][] str = new String[fDeployers.size()][2];
		int i = 0;
		for (Extension ext : fDeployers.values()) {
			str[i][0] = ext.getName();
			str[i][1] = ext.getId();
			i++;
		}
		return str;
	}

	public static String[][] getHeaderProcessorMetaInformation() {
		initialize();
		String[][] str = new String[fHeaderProcessors.size()][2];
		int i = 0;
		for (Extension ext : fHeaderProcessors.values()) {
			str[i][0] = ext.getName();
			str[i][1] = ext.getId();
			i++;
		}
		return str;
	}

	public static String chooseDefaultDeployerId() {
		initialize();
		if (fDeployers.isEmpty())
			return "";
		else
			return fDeployers.values().iterator().next().getId();
	}

	public static String getDeployerType(IBPELDeployer deployer) {
		Set<String> keys = fDeployers.keySet();

		for (String key : keys) {
			try {
				if (fDeployers.get(key).createNew().getClass() == deployer
						.getClass()) {
					return fDeployers.get(key).getId();
				}
			} catch (SpecificationException e) {
				// e.printStackTrace();
				return null;
			}
		}

		return null;
	}
}
