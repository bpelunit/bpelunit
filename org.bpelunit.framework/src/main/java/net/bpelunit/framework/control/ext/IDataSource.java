package net.bpelunit.framework.control.ext;

import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.bpelunit.framework.exception.DataSourceException;

/**
 * Represents a data source for Apache Velocity templates. Data sources
 * initialize the test case context with one or more variables.
 *
 * A single data source can produce several test cases from a single test case
 * template (<testCase> element with one or more <template>s), by giving these
 * variables different values for each <emph>row</emph>. Data sources are
 * responsible for reporting the correct number of rows that they contain.
 *
 * Data sources do not need to know where the data comes from: all they receive
 * is an InputStream. The BPELUnit core is responsible for creating the stream
 * from a file, inline text content or a remote resource, according to the
 * attributes and nested elements of the <dataSource> element in the BPTS.
 *
 * However, the data sources are responsible for interpreting the byte sequence
 * from the InputStream as necessary. For instance, they should know whether to
 * expect text in some encoding, or binary data.
 *
 * The behavior of a data source may be customized through <emph>options</emph>.
 * BPELUnit does not provide any conversion or validation facilities: these will
 * have to be implemented in the concrete data source class itself.
 *
 * @see net.bpelunit.framework.model.test.TestCase#createVelocityContext()
 * @see net.bpelunit.framework.control.util.ExtensionRegistry
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public interface IDataSource {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface DataSource {
		String name();
		String shortName();
		String[] contentTypes();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ConfigurationOption { String defaultValue(); String description(); }
	
	/**
	 * Reports the number of rows in this data source. BPELUnit will produce
	 * as many test cases from a template as there are rows in its data source.
	 * @return Number of rows in the contents of the data source.
	 * */
	int getNumberOfRows();

	/**
	 * Replaces the current contents of the data source with those present in
	 * the byte sequence in <code>is</code>. If the byte sequence is known to
	 * be text, it should be wrapped in an InputStreamReader.
	 * 
	 * @param is Byte sequence whose contents should be loaded.
	 * @throws DataSourceException There was a problem while interpreting the
	 * byte sequence in <code>is</code>.
	 */
	void loadFromStream(InputStream is) throws DataSourceException;

	String[] getFieldNames();
	
	Object getValueFor(String fieldName);
	
	void close();

	void setRow(int rowIndex) throws DataSourceException;
}
