package org.bpelunit.framework.control.datasource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.bpelunit.framework.control.ext.IDataSource;
import org.bpelunit.framework.control.util.ExtensionRegistry;
import org.bpelunit.framework.exception.DataSourceException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.xml.suite.XMLDataSource;
import org.bpelunit.framework.xml.suite.XMLProperty;
import org.bpelunit.framework.xml.suite.XMLTestCase;
import org.bpelunit.framework.xml.suite.XMLTestSuite;

/**
 * Utility methods for working with data sources.
 * 
 * @author Antonio García Domínguez
 * @version 1.1
 */
public class DataSourceUtil {

	/**
	 * Creates the effective IDataSource for a test case inside a test suite. It
	 * is a convenience wrapper for
	 * {@link #createDataSource(String, InputStream, Map)}.
	 * 
	 * @param xmlTestSuite
	 *            XMLBeans object for the <testSuite> element.
	 * @param xmlTestCase
	 *            XMLBeans object for the <testCase> element.
	 * @param bptsDir
	 *            Directory where the BPTS resides.
	 * @return <code>null</code> if no data source is available. Otherwise, an
	 *         instance of the appropriate data source will be returned, with
	 *         its contents initialized from its source according to
	 *         {@link #getStreamForDataSource(XMLDataSource)} and all specified
	 *         properties set.
	 * @throws DataSourceException
	 *             The requested data source type is not known, or there was a
	 *             problem while initializing its contents from the XML element.
	 */
	public static IDataSource createDataSource(XMLTestSuite xmlTestSuite,
			XMLTestCase xmlTestCase, File bptsDir) throws DataSourceException {
		XMLDataSource xmlDataSource = null;
		if (xmlTestCase.isSetSetUp()
				&& xmlTestCase.getSetUp().isSetDataSource()) {
			xmlDataSource = xmlTestCase.getSetUp().getDataSource();
		} else if (xmlTestSuite.isSetSetUp()
				&& xmlTestSuite.getSetUp().isSetDataSource()) {
			xmlDataSource = xmlTestSuite.getSetUp().getDataSource();
		}

		if (xmlDataSource != null) {
			final String type = xmlDataSource.getType();
			final InputStream istream = getStreamForDataSource(xmlDataSource, bptsDir);
			Map<String, String> properties = createPropertyMap(xmlDataSource
					.getPropertyList());
			return createDataSource(type, istream, properties);
		} else {
			return null;
		}
	}

	/**
	 * Creates and initializes an IDataSource instance.
	 * 
	 * @param type
	 *            Type of the data source instance, according to the extension
	 *            registry in <code>conf/extensions.xml</code>.
	 * @param istream
	 *            InputStream with the contents to be loaded, as created by
	 *            {@link #getStreamForDataSource(String, String)} or its
	 *            wrappers.
	 * @param properties
	 *            Key-value map for the properties to be set on the data source.
	 * @return IDataSource instance with its contents initialized from the
	 *         InputStream and all properties set.
	 * @throws DataSourceException
	 *             There was a problem while creating the data source instance,
	 *             loading its contents or setting a property.
	 */
	public static IDataSource createDataSource(final String type,
			final InputStream istream, Map<String, String> properties)
			throws DataSourceException {
		IDataSource dataSource = null;

		try {
			dataSource = ExtensionRegistry.createNewDataSourceForType(type);
		} catch (SpecificationException e) {
			throw new DataSourceException(
					"Could not create data source instance for type " + type, e);
		}

		for (Entry<String, String> prop : properties.entrySet()) {
			setProperty(dataSource, prop.getKey(), prop.getValue());
		}

		// Contents should be loaded after setting the properties, as these might
		// have information that is required to correctly interpret the input
		// stream, such as text encoding, for instance.
		dataSource.loadFromStream(istream);

		return dataSource;
	}

	private static void setProperty(IDataSource ds, String key, String value)
			throws DataSourceException {
		try {
			Method method = ds.getClass().getMethod("set" + toFirstUpper(key),
					String.class);
			method.invoke(ds, value);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if (targetException instanceof DataSourceException) {
				throw (DataSourceException) targetException;
			} else {
				throw new DataSourceException("Property " + key
						+ " is invalid for data source", targetException);
			}
		} catch (Exception e) {
			throw new DataSourceException("Property " + key
					+ " is invalid for data source", e);
		}
	}

	/**
	 * Package protected for tests
	 */
	static String toFirstUpper(String s) {
		if (s == null || "".equals(s)) {
			return "";
		}

		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * Returns an InputStream for the contents referenced in the data source. It
	 * is a convenience wrapper for
	 * {@link #getStreamForDataSource(String, String)}.
	 * 
	 * @param xmlDataSource
	 *            XMLBeans object for the <dataSource> element.
	 * @param basedir
	 *            Base directory from which relative paths should be interpreted.
	 * @throws DataSourceException
	 *             Unknown content source type.
	 */
	public static InputStream getStreamForDataSource(XMLDataSource xmlDataSource, File basedir)
			throws DataSourceException {
		return getStreamForDataSource(xmlDataSource.getContents(),
				xmlDataSource.getSrc(), basedir);
	}

	/**
	 * Returns an InputStream for the contents referenced in the data source.
	 * These reference types are available:
	 * 
	 * <ul>
	 *   <li>Inline content through the nested <contents> element.</li>
	 *   <li>Absolute file paths.</li>
	 *   <li>Relative file paths, evaluated from the BPTS' directory.</li>
	 *   <li>Any URL accepted by java.net.URL (such as http:// or file://).</li>
	 * </ul>
	 * 
	 * @see java.net.URL#URL(String)
	 * @param contents
	 *            Inline contents of the data source. If <code>null</code>, the
	 *            data source has no inline contents.
	 * @param externalReference
	 *            Value of the external reference. If <code>null</code> the data
	 *            source has no external reference.
	 * @param basedir
	 *            Base directory from which relative paths should be interpreted.
	 * @throws DataSourceException
	 *             Either the external reference is of an unknown type, or
	 *             neither an external reference nor inline contents are
	 *             available.
	 */
	public static InputStream getStreamForDataSource(String contents,
			String externalReference, File basedir) throws DataSourceException {
		if (contents != null) {
			if (externalReference != null) {
				throw new DataSourceException("Inline content and external "
						+ "references cannot be used at the same time in a "
						+ "data source: it is ambiguous");
			}
			return new ByteArrayInputStream(contents.getBytes());
		} else if (externalReference != null) {
			// Try to parse it as an URL (http://, file://)
			try {
				return new URL(externalReference).openStream();
			} catch (MalformedURLException e) {
				// Malformed URL... might be a file path
			} catch (IllegalArgumentException e) {
				// Not a valid URL either
			} catch (IOException e) {
				throw new DataSourceException("Error while opening URL '"
					+ externalReference + "'", e);
			}

			// Try opening it as a file - absolute path
			File f = new File(externalReference);
			try {
				return new FileInputStream(f);
			}
			catch (FileNotFoundException e){
				// let's try again as a relative path
			}

			// Last chance - relative path
			f = new File(basedir, externalReference);
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new DataSourceException("File not found: '"
						+ externalReference + "'", e);
			}
		} else {
			throw new DataSourceException(
					"No inline contents and no external reference are available");
		}
	}

	public static void initializeContext(VelocityContext ctx, IDataSource ds) {
		if(ds.next()) {
			String[] fieldNames = ds.getFieldNames();
			
			for(String fieldName : fieldNames) {
				System.out.println("Put " + fieldName + " -> " + ds.getValueFor(fieldName));
				ctx.put(fieldName, ds.getValueFor(fieldName));
			}
		}
	}
	
	/************* PRIVATE METHODS ****************/

	private static Map<String, String> createPropertyMap(
			List<XMLProperty> propertyList) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (XMLProperty prop : propertyList) {
			map.put(prop.getName(), prop.getStringValue());
		}
		return map;
	}
}
