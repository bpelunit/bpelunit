package net.bpelunit.framework.control.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.DataSource;
import net.bpelunit.framework.exception.DataSourceException;
import org.junit.Before;
import org.junit.Test;

public class DataSourceHelperTest {

	private MyDataSource dataSource;

	@Before
	public void setUp() {
		this.dataSource = new MyDataSource();
	}

	@Test
	public void testIsValidParameter() throws Exception {
		assertTrue(DataSourceHelper.isValidConfigurationOption(dataSource, "A"));
		assertTrue(DataSourceHelper.isValidConfigurationOption(dataSource, "a"));
		assertTrue(DataSourceHelper.isValidConfigurationOption(dataSource, "B"));
		assertTrue(DataSourceHelper.isValidConfigurationOption(dataSource, "b"));
		assertFalse(DataSourceHelper
				.isValidConfigurationOption(dataSource, "C"));
		assertFalse(DataSourceHelper.isValidConfigurationOption(dataSource,
				"AB"));
	}

	@Test
	public void testGetConfigurationOptions() throws Exception {
		List<DataSourceConfigurationOption> options = DataSourceHelper
				.getConfigurationOptionsFor(dataSource);

		assertEquals(2, options.size());

		DataSourceConfigurationOption a = options.get(0);
		assertEquals("A", a.getName());
		assertEquals("a", a.getDefaultValue());
		assertEquals("A description", a.getDescription());

		DataSourceConfigurationOption b = options.get(1);
		assertEquals("B", b.getName());
		assertEquals("b", b.getDefaultValue());
		assertEquals("Another description", b.getDescription());
	}

	@Test
	public void testSetConfigurationOption() throws Exception {
		DataSourceHelper.setConfigurationOption(dataSource, "A", "aa");
		assertEquals("aa", dataSource.a);

		DataSourceHelper.setConfigurationOption(dataSource, "B", "bb");
		assertEquals("bb", dataSource.b);
	}

	@Test
	public void testGetInputStreamFromInlineData() throws Exception {
		String data = "ABC";

		InputStream is = DataSourceHelper.getInputStreamFromInlineData(data);
		BufferedReader r = new BufferedReader(new InputStreamReader(is));

		String actual = r.readLine();

		assertEquals(data, actual);
	}

	private static class AbstractDataSource implements IDataSource {

		@Override
		public void close() {
		}

		@Override
		public String[] getFieldNames() {
			return null;
		}

		@Override
		public int getNumberOfRows() {
			return 0;
		}

		@Override
		public Object getValueFor(String fieldName) {
			return null;
		}

		@Override
		public void loadFromStream(InputStream is) throws DataSourceException {
		}

		@Override
		public void setRow(int rowIndex) throws DataSourceException {
		}

	}

	private static final class DataSourceWithoutAnnotation extends
			AbstractDataSource {
	}

	@DataSource(contentTypes = {}, name = "Name", shortName = "N")
	private static final class MethodAnnotationsWithNoDescriptionWrongNameWrongParameterList
			extends AbstractDataSource {

		@SuppressWarnings("unused")
		@ConfigurationOption(defaultValue="", description="")
		public boolean myConfigurationOption() {
			return false;
		}
	}

	@DataSource(name = "", shortName = "", contentTypes = {})
	private static final class DataSourceAnnotationWithMissingNames extends
			AbstractDataSource {
	}

	@DataSource(name = "Name", shortName = "N", contentTypes = { "", "A" })
	private static final class DataSourceAnnotationWithEmptyContentType extends
			AbstractDataSource {
	}

	@DataSource(name="Test", shortName="test", contentTypes="")
	public static final class MyDataSource extends AbstractDataSource {

		String b;
		String a;

		@ConfigurationOption(defaultValue = "a", description = "A description")
		public void setA(String a) {
			this.a = a;
		}

		@ConfigurationOption(defaultValue = "b", description = "Another description")
		public void setB(String b) {
			this.b = b;
		}
	}

	@Test
	public void testValidateDataSourceAnnotationWithMissingAnnotation()
			throws Exception {
		List<String> messages = DataSourceHelper
				.validateDataSourceAnnotation(DataSourceWithoutAnnotation.class);
		assertEquals(1, messages.size());
	}

	@Test
	public void testValidateDataSourceAnnotationWithMissingNames()
			throws Exception {
		List<String> messages = DataSourceHelper
				.validateDataSourceAnnotation(DataSourceAnnotationWithMissingNames.class);
		assertEquals(2, messages.size());
	}

	@Test
	public void testValidateDataSourceAnnotationWithEmptyContentType()
			throws Exception {
		List<String> messages = DataSourceHelper
				.validateDataSourceAnnotation(DataSourceAnnotationWithEmptyContentType.class);
		assertEquals(1, messages.size());
	}

	@Test
	public void testValidateMethodAnnotationsWithNoDescriptionWrongNameWrongParameterList()
			throws Exception {
		List<String> messages = DataSourceHelper
				.validateMethodAnnotations(MethodAnnotationsWithNoDescriptionWrongNameWrongParameterList.class);
		assertEquals(4, messages.size());
	}
	
	@Test
	public void testGetName() {
		MyDataSource ds = new MyDataSource();
		assertEquals("Test", DataSourceHelper.getName(ds));
		assertEquals("test", DataSourceHelper.getShortName(ds));
	}
}
