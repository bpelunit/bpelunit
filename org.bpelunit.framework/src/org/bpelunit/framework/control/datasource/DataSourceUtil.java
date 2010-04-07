package org.bpelunit.framework.control.datasource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.bpelunit.framework.exception.DataSourceException;
import org.bpelunit.framework.xml.suite.XMLDataSource;

/**
 * Utility methods for working with data sources.
 * 
 * @author Antonio García Domínguez
 * @version 1.0
 */
public class DataSourceUtil {

	/**
	 * Returns an InputStream for the contents referenced in the data source. It
	 * is a convenience wrapper for
	 * {@link #getStreamForDataSource(String, String)}.
	 * 
	 * @param xmlDataSource
	 *            XMLBeans object for the <dataSource> element.
	 * @throws DataSourceException
	 *             Unknown content source type.
	 */
	public static InputStream getStreamForDataSource(XMLDataSource xmlDataSource)
			throws DataSourceException {
		return getStreamForDataSource(xmlDataSource.getContents(),
				xmlDataSource.getSrc());
	}

	/**
	 * Returns an InputStream for the contents referenced in the data source.
	 * Currently, only inline content through the nested <contents> element is
	 * supported.
	 * 
	 * TODO paths, file:// and http:// URLs.
	 * 
	 * @param contents
	 *            Inline contents of the data source. If <code>null</code>, the
	 *            data source has no inline contents.
	 * @param externalReference
	 *            Value of the external reference. If <code>null</code> the data
	 *            source has no external reference.
	 * @throws DataSourceException
	 *             Either the external reference is of an unknown type, or
	 *             neither an external reference nor inline contents are
	 *             available.
	 */
	public static InputStream getStreamForDataSource(String contents,
			String externalReference) throws DataSourceException {
		if (contents != null) {
			if (externalReference != null) {
				throw new DataSourceException("Inline content and external " +
						"references cannot be used at the same time in a " +
						"data source: it is ambiguous");
			}
			return new ByteArrayInputStream(contents.getBytes());
		} else if (externalReference != null) {
			throw new DataSourceException(String.format(
					"Unknown external reference '%s'", externalReference));
		} else {
			throw new DataSourceException(
					"No inline contents and no external reference are available");
		}
	}

}
