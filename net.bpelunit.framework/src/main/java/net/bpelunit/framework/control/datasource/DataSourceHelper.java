package net.bpelunit.framework.control.datasource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.ConfigurationOption;
import net.bpelunit.framework.control.ext.IDataSource.DataSource;
import net.bpelunit.util.StringUtil;

/**
 * This class offers static helper methods for extracting and dealing with meta
 * data from data sources and using this data for setting configuration options
 * on data sources.
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public final class DataSourceHelper {

	private static final String SETTER_PREFIX = "set";

	private DataSourceHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns an immutable list of possible configuration options for a given
	 * data source class
	 */
	public static List<DataSourceConfigurationOption> getConfigurationOptionsFor(
			Class<? extends IDataSource> dataSource) {
		List<DataSourceConfigurationOption> configurationOptions = optionsForDataSource
				.get(dataSource.getClass());

		if (configurationOptions == null) {
			configurationOptions = extractConfigurationOptionsForClass(dataSource);
			Collections.sort(configurationOptions, new DataSourceConfigurationOptionComparator());

			optionsForDataSource.put(dataSource, Collections.unmodifiableList(configurationOptions));
		}

		return configurationOptions;
	}

	/**
	 * Returns an immutable list of possible configuration options for a given
	 * instance of a data source
	 */
	public static List<DataSourceConfigurationOption> getConfigurationOptionsFor(
			IDataSource dataSource) {

		return getConfigurationOptionsFor(dataSource.getClass());
	}

	public static boolean isValidConfigurationOption(IDataSource dataSource,
			String propertyName) {
		List<DataSourceConfigurationOption> properties = getConfigurationOptionsFor(dataSource);

		String realPropertyName = StringUtil.toFirstUpper(propertyName);

		for (DataSourceConfigurationOption p : properties) {
			if (p.getName().equals(realPropertyName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets a configuration option on a specific data source instance.
	 * 
	 * @param dataSource
	 *            the data source on which the configuration option should be
	 *            set
	 * @param name
	 *            the name of the configuration option
	 * @param value
	 *            the new value of the configuration option
	 * @throws IllegalArgumentException
	 *             if the given configuration option does not exist or is not
	 *             accessible
	 */
	public static void setConfigurationOption(IDataSource dataSource,
			String name, String value) {
		String realName = StringUtil.toFirstUpper(name);

		try {
			Method m = dataSource.getClass().getMethod(SETTER_PREFIX + realName,
					String.class);
			m.invoke(dataSource, value);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Cannot access set" + name
					+ ". Setting configuration option for data source failed!",
					e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"Data Source does not have any configuration option "
							+ name + "!", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot access set" + name
					+ ". Setting configuration option for data source failed!",
					e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Error while setting option "
					+ name + " on data source: " + e.getMessage(), e);
		}
	}

	/**
	 * Internal cache for configuration options of a certain data source class
	 */
	private static Map<Class<?>, List<DataSourceConfigurationOption>> optionsForDataSource = new HashMap<Class<?>, List<DataSourceConfigurationOption>>();

	private static void extractConfigurationOptionForMethod(Method m,
			List<DataSourceConfigurationOption> configurationOptions) {
		ConfigurationOption annotation = m
				.getAnnotation(ConfigurationOption.class);
		if (annotation != null) {
			String configurationOptionName = extractConfigurationOptionName(m);

			if (configurationOptionName != null) {
				DataSourceConfigurationOption configurationOption = new DataSourceConfigurationOption(
						configurationOptionName, annotation.defaultValue(),
						annotation.description());
				configurationOptions.add(configurationOption);
			}
		}
	}

	private static String extractConfigurationOptionName(Method m) {
		String name = m.getName();

		if (name.startsWith(SETTER_PREFIX)) {
			return StringUtil.toFirstUpper(name.substring(SETTER_PREFIX.length()));
		}

		return null;
	}

	private static List<DataSourceConfigurationOption> extractConfigurationOptionsForClass(
			Class<? extends IDataSource> clazz) {
		List<DataSourceConfigurationOption> configurationOptions = new ArrayList<DataSourceConfigurationOption>();

		for (Method m : clazz.getMethods()) {
			extractConfigurationOptionForMethod(m, configurationOptions);
		}

		return configurationOptions;
	}

	

	/**
	 * Convenience method for passing an InputStream to a data source when the
	 * user specified the test data in the test suite file.
	 * 
	 * This method is probably included in some Apache util lib but we don't
	 * need more dependencies, especially for such small functions
	 * 
	 * @param data
	 *            the data that the user entered in the .bpts file
	 * @return an InputStream that can be used by the data source
	 */
	static InputStream getInputStreamFromInlineData(String data) {
		return new ByteArrayInputStream(data.getBytes());
	}

	public static List<String> validateDataSourceAnnotation(
			Class<? extends IDataSource> ds) {
		List<String> validationMessages = new ArrayList<String>();

		DataSource annotation = ds.getAnnotation(DataSource.class);

		if (annotation == null) {
			validationMessages
					.add("A class implementing IDataSource must be annotated with @DataSource");
			return validationMessages;
		}

		if (annotation.name().equals("") ) {
			validationMessages
					.add("DataSource.name() must not be null. This name is used in the UI.");
		}

		if (annotation.shortName().equals("")) {
			validationMessages
					.add("DataSource.shortName() must not be null. This name can be used in the test suite specification instead of a content type.");
		}

		for(String contentType : annotation.contentTypes()) {
			if(contentType.equals("")) {
				validationMessages.add("DataSource.contentTypes() must not include a null nor an empty (\"\") value.");
			}
		}
		
		return validationMessages;
	}

	public static List<String> validateMethodAnnotations(
			Class<? extends IDataSource> ds) {
		List<String> validationMessages = new ArrayList<String>();

		for (Method m : ds.getMethods()) {
			validateMethodAnnotation(m, validationMessages);
		}

		return validationMessages;
	}

	private static void validateMethodAnnotation(Method m,
			List<String> validationMessages) {

		ConfigurationOption methodAnnotation = m
				.getAnnotation(ConfigurationOption.class);

		if (methodAnnotation != null) {
			String name = m.getName();

			if (!name.startsWith(SETTER_PREFIX) | name.length() < 4) {
				validationMessages
						.add("Method "
								+ name
								+ " is annotated with @ConfigurationOption but must be named setXYZ.");
			}

			if (methodAnnotation.description() == null
					|| methodAnnotation.description().equals("")) {
				validationMessages.add("Description for method " + name
						+ " must not be empty.");
			}
			
			Class<?>[] parameterTypes = m.getParameterTypes();
			if(parameterTypes.length != 1 || parameterTypes[0] != String.class) {
				validationMessages.add("The method " + name + " must have the signature public void " + name + "(String) (wrong parameter list)");
			}
			
			if(m.getReturnType() != void.class) {
				validationMessages.add("The method " + name + " must have the signature public void " + name + "(String) (wrong return type)");
			}
		}
	}

	public static String getName(IDataSource ds) {
		DataSource annotation = ds.getClass().getAnnotation(DataSource.class);
		return annotation.name();
	}
	
	public static String getShortName(IDataSource ds) {
		DataSource annotation = ds.getClass().getAnnotation(DataSource.class);
		return annotation.shortName();
	}
}
