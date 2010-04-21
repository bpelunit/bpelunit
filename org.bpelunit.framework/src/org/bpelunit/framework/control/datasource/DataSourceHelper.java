package org.bpelunit.framework.control.datasource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bpelunit.framework.control.ext.IDataSource;
import org.bpelunit.framework.control.ext.IDataSource.ConfigurationOption;

/**
* This class offers static helper methods for extracting and dealing with meta
* data from data sources and using this data for setting configuration options
* on data sources.
*
* @author Daniel Luebke <bpelunit@daniel-luebke.de>
*/
public class DataSourceHelper {

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

optionsForDataSource.put(dataSource.getClass(),
configurationOptions);
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

propertyName = toFirstUpper(propertyName);

for (DataSourceConfigurationOption p : properties) {
if (p.name.equals(propertyName)) {
return true;
}
}

return false;
}

/**
* Sets a configuration option on a specific data source instance.
*
* @param dataSource
* the data source on which the configuration option should be
* set
* @param name
* the name of the configuration option
* @param value
* the new value of the configuration option
* @throws IllegalArgumentException
* if the given configuration option does not exist or is not
* accessible
*/
public static void setConfigurationOption(IDataSource dataSource,
String name, String value) {
name = toFirstUpper(name);

try {
Method m = dataSource.getClass().getMethod("set" + name,
String.class);
m.invoke(dataSource, value);
} catch (SecurityException e) {
throw new IllegalArgumentException("Cannot access set" + name
+ ". Setting configuration option for data source failed!",
e);
} catch (NoSuchMethodException e) {
throw new IllegalArgumentException(
"Data Source does not have any configuration option "
+ name + "!");
} catch (IllegalAccessException e) {
throw new IllegalArgumentException("Cannot access set" + name
+ ". Setting configuration option for data source failed!",
e);
} catch (InvocationTargetException e) {
throw new IllegalArgumentException("Error while setting option "
+ name + " on data source: " + e.getMessage(), e.getCause());
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

if (name.startsWith("set")) {
return toFirstUpper(name.substring("set".length()));
}

return null;
}

private static List<DataSourceConfigurationOption> extractConfigurationOptionsForClass(
Class<? extends IDataSource> clazz) {
List<DataSourceConfigurationOption> configurationOptions = new ArrayList<DataSourceConfigurationOption>();

for (Method m : clazz.getMethods()) {
extractConfigurationOptionForMethod(m, configurationOptions);
}

configurationOptions = Collections
.unmodifiableList(configurationOptions);

return configurationOptions;
}

/**
* Convenience method. Should be in some StringHelper XXX Refactor out and
* move somewhere else
*/
private static String toFirstUpper(String optionName) {
if (optionName == null || optionName.length() == 0) {
return "";
}

return optionName.substring(0, 1).toUpperCase()
+ optionName.substring(1);
}

/**
* Convenience method for passing an InputStream to a data source when the
* user specified the test data in the test suite file.
*
* This method is probably included in some Apache util lib but we don't
* need more dependencies, especially for such small functions
*
* @param data
* the data that the user entered in the .bpts file
* @return an InputStream that can be used by the data source
*/
static InputStream getInputStreamFromInlineData(String data) {
return new ByteArrayInputStream(data.getBytes());
}

}