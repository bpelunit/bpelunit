/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.client.eclipse;

import java.util.HashMap;
import java.util.Map;

import org.bpelunit.framework.client.model.DeployerExtension;
import org.bpelunit.framework.client.model.Extension;
import org.bpelunit.framework.client.model.HeaderProcessorExtension;
import org.bpelunit.framework.client.model.SOAPEncoderExtension;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * This class is responsible for loading BPELUnit extensions out of client plugins.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ExtensionControl {

	public static final String DEPLOYER_EXTENSION_POINT_ID= "org.bpelunit.framework.client.eclipse.bpelDeployer";
	public static final String ENCODER_EXTENSION_POINT_ID= "org.bpelunit.framework.client.eclipse.soapEncoder";
	public static final String HEADER_EXTENSION_POINT_ID= "org.bpelunit.framework.client.eclipse.headerProcessor";

	private static Map<String, DeployerExtension> fDeployers= new HashMap<String, DeployerExtension>();
	private static Map<String, HeaderProcessorExtension> fHeaderProcessors= new HashMap<String, HeaderProcessorExtension>();
	private static Map<String, SOAPEncoderExtension> fSOAPEncoders= new HashMap<String, SOAPEncoderExtension>();

	private static boolean isInitialized= false;

	public static void initialize() {

		if (!isInitialized) {
			loadExtensions(DEPLOYER_EXTENSION_POINT_ID, new BPELDeployerLoader());
			loadExtensions(ENCODER_EXTENSION_POINT_ID, new SOAPEncoderLoader());
			loadExtensions(HEADER_EXTENSION_POINT_ID, new HeaderProcessorLoader());

			isInitialized= true;
		}
	}

	private static void loadExtensions(String extensionPointID, ELoader loader) {

		IExtensionRegistry registry= Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint= registry.getExtensionPoint(extensionPointID);
		IExtension[] extensions= extensionPoint.getExtensions();
		for (IExtension element : extensions) {
			IConfigurationElement[] elements= element.getConfigurationElements();
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

			id= e.getAttribute("id");
			name= e.getAttribute("name");
		}
	}

	static class BPELDeployerLoader extends ELoader {

		@Override
		public void load(IConfigurationElement e) {

			super.load(e);

			if (id == null) {
				BPELUnitActivator.logErrorMessage("Error reading bpel deployer list from plugin.xml files - required attributes are missing.");
			} else {

				String[] general_options= getOptions(e, "general_options");
				String[] suite_options= getOptions(e, "suite_options");

				fDeployers.put(id, new DeployerExtension(id, name, general_options, suite_options, e));
			}
		}

		private String[] getOptions(IConfigurationElement e, String name) {
			String options= e.getAttribute(name);
			String[] optionsx= new String[0];
			if (options != null) {
				optionsx= options.split(",");
				for (int i= 0; i < optionsx.length; i++) {
					optionsx[i]= optionsx[i].trim();
				}
			} else
				optionsx= new String[0];
			return optionsx;
		}
	}

	static class HeaderProcessorLoader extends ELoader {

		@Override
		public void load(IConfigurationElement e) {

			super.load(e);

			if (id == null) {
				BPELUnitActivator.logErrorMessage("Error reading header processor list from plugin.xml files - required attributes are missing.");
			} else {
				fHeaderProcessors.put(id, new HeaderProcessorExtension(id, name, e));
			}
		}
	}

	static class SOAPEncoderLoader extends ELoader {

		@Override
		public void load(IConfigurationElement e) {

			super.load(e);

			if (id == null) {
				BPELUnitActivator.logErrorMessage("Error reading soap encoder list from plugin.xml files - required attributes are missing.");
			} else {
				fSOAPEncoders.put(id, new SOAPEncoderExtension(id, name, e));
			}
		}
	}

	public static DeployerExtension findDeployerExtension(String type) {
		return fDeployers.get(type);
	}

	public static HeaderProcessorExtension findHeaderProcessorExtension(String name) {
		return fHeaderProcessors.get(name);
	}

	public static SOAPEncoderExtension findSOAPEncoderExtension(String styleEncoding) {
		return fSOAPEncoders.get(styleEncoding);
	}

	public static String[][] getDeployerMetaInformation() {
		initialize();
		String[][] str= new String[fDeployers.size()][2];
		int i= 0;
		for (Extension ext : fDeployers.values()) {
			str[i][0]= ext.getName();
			str[i][1]= ext.getId();
			i++;
		}
		return str;
	}

	public static String[][] getHeaderProcessorMetaInformation() {
		initialize();
		String[][] str= new String[fHeaderProcessors.size()][2];
		int i= 0;
		for (Extension ext : fHeaderProcessors.values()) {
			str[i][0]= ext.getName();
			str[i][1]= ext.getId();
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

	public static String[] getSuiteOptionsForDeployer(String type) {
		initialize();
		DeployerExtension extension= findDeployerExtension(type);
		if (extension != null)
			return extension.getSuiteOptions();
		else
			return new String[0];

	}
}
