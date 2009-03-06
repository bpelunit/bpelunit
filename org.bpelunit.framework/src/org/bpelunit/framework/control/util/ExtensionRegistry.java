/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IHeaderProcessor;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.coverage.CoverageConstants;
import org.bpelunit.framework.coverage.annotation.metrics.activitycoverage.ActivityMetric;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.xml.config.XMLConfiguration;
import org.bpelunit.framework.xml.config.XMLProperty;
import org.bpelunit.framework.xml.config.XMLTestConfiguration;
import org.bpelunit.framework.xml.config.XMLTestConfigurationDocument;
import org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions;
import org.bpelunit.framework.xml.extension.XMLExtension;
import org.bpelunit.framework.xml.extension.XMLExtensionRegistryDocument;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * The BPELUnit Extension Registry handles reading the extensions.xml file and
 * instantiates the extensions.
 * 
 * @version $Id: ExtensionRegistry.java,v 1.3 2007/06/21 06:34:46 asalnikowAlex Salnikow
 * 
 */
public class ExtensionRegistry {

	private static Logger fsLogger = Logger
			.getLogger("org.bpelunit.framework.ExtensionRegistry");

	private static Map<String, Class> fsDeployerRegistry;

	private static Map<String, Map<String, String>> fsDeployerOptions;

	private static Map<String, Class> fsEncoderRegistry;

	private static Map<String, Class> fsHeaderRegistry;

	private static boolean fsIgnoreOnNotFound;

	/**
	 * Initializes the extension registry. Must be called before all other
	 * methods.
	 * 
	 * @param configurationFile
	 *            absolute path to configuration file (extensions.xml)
	 * @param ignoreOnNotFound
	 *            if true, the registry will not fail if a class is not found
	 * 
	 * @throws ConfigurationException
	 *             error reading the configuration file
	 */
	public static void loadRegistry(File configurationFile,
			boolean ignoreOnNotFound) throws ConfigurationException {

		fsIgnoreOnNotFound = ignoreOnNotFound;

		fsDeployerRegistry = new HashMap<String, Class>();
		fsEncoderRegistry = new HashMap<String, Class>();
		fsHeaderRegistry = new HashMap<String, Class>();
		fsDeployerOptions = new HashMap<String, Map<String, String>>();

		XMLExtensionRegistryDocument document;

		try {
			document = XMLExtensionRegistryDocument.Factory
					.parse(configurationFile);

			XMLBPELUnitCoreExtensions testExtensions = document
					.getExtensionRegistry();

			for (XMLExtension deployer : testExtensions.getDeployerList())
				load(deployer, IBPELDeployer.class, "Deployer",
						fsDeployerRegistry);

			for (XMLExtension encoder : testExtensions.getEncoderList()) {
				load(encoder, ISOAPEncoder.class, "Encoder", fsEncoderRegistry);

				for (XMLExtension hproc : testExtensions
						.getHeaderProcessorList())
					load(hproc, IHeaderProcessor.class, "Header Processor",
							fsHeaderRegistry);

			}

		} catch (XmlException e) {
			throw new ConfigurationException(
					"An XML reading error occurred reading the deployment plug-ins from file "
							+ configurationFile.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"An I/O error occurred reading the deployment plug-ins from file "
							+ configurationFile.getAbsolutePath(), e);
		}
	}

	/**
	 * Creates a new instance of the deployment class for the given type.
	 * 
	 * @param type
	 *            type, as specified in the extensions.xml file.
	 * @return new deployer instance
	 * @throws SpecificationException
	 */
	public static IBPELDeployer createNewDeployerForType(String type)
			throws SpecificationException {
		return (IBPELDeployer) createObject(fsDeployerRegistry.get(type), type);
	}

	/**
	 * Creates a new instance of the encoder class for the given style/encoding
	 * 
	 * @param styleEncoding
	 *            style and encoding in the form "style/encoding", as specified
	 *            in the extensions.xml file
	 * @return new encoder instance
	 * @throws SpecificationException
	 */
	public static ISOAPEncoder createNewEncoderForType(String styleEncoding)
			throws SpecificationException {
		return (ISOAPEncoder) createObject(
				fsEncoderRegistry.get(styleEncoding), styleEncoding);
	}

	/**
	 * Creates a new instance of the header processor for the given type
	 * 
	 * @param type
	 *            processor type as specified in the extensions.xml file
	 * @return new header processor instance
	 * @throws SpecificationException
	 */
	public static IHeaderProcessor createNewHeaderProcessorForType(String type)
			throws SpecificationException {
		return (IHeaderProcessor) createObject(fsHeaderRegistry.get(type), type);
	}

	/**
	 * Returns the number of extensions loaded.
	 * 
	 * @return
	 */
	protected static int getExtensionCount() {
		return fsDeployerRegistry.size() + fsEncoderRegistry.size()
				+ fsHeaderRegistry.size();
	}

	// ********************* Internals ********************

	private static Object createObject(Class clazz, String type)
			throws SpecificationException {

		if (clazz == null)
			throw new SpecificationException(
					"No deployer class registered for type " + type);
		Object deployer = null;
		try {
			deployer = clazz.newInstance();
			return deployer;
		} catch (InstantiationException e) {
			throw new SpecificationException(
					"The deployment class for type "
							+ type
							+ " from the configuration could not be instantiated. Is it a non-abstract class?",
					e);
		} catch (IllegalAccessException e) {
			throw new SpecificationException(
					"The deployment class for type "
							+ type
							+ " from the configuration is not accessible. Does it have a nullary constructor?",
					e);
		}
	}

	private static <T> void load(XMLExtension extension, Class<T> requiredType,
			String readableName, Map<String, Class> registry)
			throws ConfigurationException {
		try {
			Class theClazz = getClassFor(extension);
			if (!requiredType.isAssignableFrom(theClazz))
				throw new ConfigurationException("The " + readableName
						+ " class for type " + extension.getType()
						+ " from the configuration does not implement "
						+ requiredType.getSimpleName());

			registry.put(extension.getType(), theClazz);

		} catch (ConfigurationException e) {
			if (!fsIgnoreOnNotFound)
				throw e;
			else {
				fsLogger.debug("Configuration Error in extensions.xml; "
						+ e.getMessage());
			}

		}
	}

	private static Class getClassFor(XMLExtension deployer)
			throws ConfigurationException {
		String type = deployer.getType();
		String clazz = deployer.getExtensionClass();
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(
					"The extension class for type "
							+ type
							+ " from the configuration could not be found. Check the CLASSPATH.",
					e);
		} catch (NoClassDefFoundError e) {
			throw new ConfigurationException(
					"Requirements for extension class of type "
							+ type
							+ " from the configuration could not be found. Check the CLASSPATH.",
					e);
		}
	}

	public static void loadDeploymentConfiguration(File file)
			throws ConfigurationException {

		XMLTestConfigurationDocument document;

		try {
			document = XMLTestConfigurationDocument.Factory.parse(file);
			XMLTestConfiguration testConfig = document.getTestConfiguration();

			List<XMLConfiguration> configurationList = testConfig
					.getConfigurationList();
			for (XMLConfiguration configuration : configurationList) {
				String deployer = configuration.getDeployer();
				List<XMLProperty> propertyList = configuration
						.getPropertyList();
				for (XMLProperty property : propertyList) {
					Map<String, String> options = fsDeployerOptions
							.get(deployer);
					if (options == null) {
						options = new HashMap<String, String>();
						fsDeployerOptions.put(deployer, options);
					}
					options.put(property.getName(), property.getStringValue());
				}
			}

		} catch (XmlException e) {
			throw new ConfigurationException(
					"An XML reading error occurred reading the deployment configuration from file "
							+ file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"An I/O error occurred reading the deployment configuration from file "
							+ file.getAbsolutePath(), e);
		}
	}

	public static void configure(String deployerID, IBPELDeployer deployer) {
		Map<String, String> options = fsDeployerOptions.get(deployerID);
		if (options == null) {
			// can happen, no problem
			fsLogger.warn("No deployment configuration for type " + deployerID
					+ " was found.");
			options = new HashMap<String, String>();
		}
		deployer.setConfiguration(options);
	}

	/**
	 * Read configuration data from file.
	 * 
	 * @param file
	 * @throws ConfigurationException
	 */
	public static Map<String, List<String>> loadCoverageToolConfiguration(
			File file) throws ConfigurationException {
		SAXBuilder builder = new SAXBuilder();
		Map<String, List<String>> map = null;
		try {
			Document doc = builder.build(file);
			Element config = doc.getRootElement();
			String attribute=config.getAttributeValue(CoverageConstants.CONFIG_ATTRIBUTE_WAIT_TIME);
			if(attribute!=null){
				try{
					int i=Integer.parseInt(attribute);
					TestCaseRunner.wait_time_for_coverage_markers=i;
				}catch(Exception e){
					BPELUnitRunner.getCoverageMeasurmentTool().setErrorStatus("Configuration of wait time is not valid.");
				}
			}
			map = handleMetricElements(config);
		} catch (JDOMException e) {
			throw new ConfigurationException(
					"An XML reading error occurred reading the configuration of coverage tool from file "
							+ file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"An I/O error occurred reading the configuration of coverage tool from file "
							+ file.getAbsolutePath(), e);
		}
		return map;
	}

	private static Map<String, List<String>> handleMetricElements(Element config) {
		String attribute;
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List children = config.getChildren(
				CoverageConstants.CONFIG_METRIC_ELEMENT,
				CoverageConstants.NAMESPACE_CONFIGURATION);
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			attribute = element
					.getAttributeValue(CoverageConstants.CONFIG_ATTRIBUT_NAME);
			if (attribute != null) {
				if (attribute.equalsIgnoreCase(ActivityMetric.METRIC_NAME)) {
					map.put(ActivityMetric.METRIC_NAME, null);
					Element child2 = element.getChild(
							CoverageConstants.CONFIG_PROPERTY_ELEMENT,
							CoverageConstants.NAMESPACE_CONFIGURATION);
					String name = child2
							.getAttributeValue(CoverageConstants.CONFIG_ATTRIBUT_NAME);
					if (name != null
							&& name
									.equals(CoverageConstants.CONFIG_ELEMENT_BASIC_ACTIVITIES)) {
						List<String> list = new ArrayList<String>();
						list.addAll(analyzeString(child2.getText()));
						map.put(ActivityMetric.METRIC_NAME, list);
					}
				} else if (attribute.equalsIgnoreCase(BranchMetric.METRIC_NAME)) {
					map.put(BranchMetric.METRIC_NAME, null);
				} else if (attribute.equalsIgnoreCase(LinkMetric.METRIC_NAME)) {
					map.put(LinkMetric.METRIC_NAME, null);
				} else if (attribute.equalsIgnoreCase(FaultMetric.METRIC_NAME)) {
					map.put(FaultMetric.METRIC_NAME, null);
				} else if (attribute
						.equalsIgnoreCase(CompensationMetric.METRIC_NAME)) {
					map.put(CompensationMetric.METRIC_NAME, null);
				}
			}
		}
		return map;
	}

	private static List<String> analyzeString(String activities) {
		List<String> basicActivities = new ArrayList<String>();
		if (!activities.endsWith(","))
			activities = activities + ",";
		Scanner scanner = new Scanner(activities);
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			basicActivities.add(scanner.next().trim());
		}
		return basicActivities;
	}

}
