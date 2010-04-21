package org.bpelunit.framework.control.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.bpelunit.framework.control.ext.IDataSource;
import org.bpelunit.framework.exception.DataSourceException;
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
		assertEquals("A", a.name);
		assertEquals("a", a.defaultValue);
		assertEquals("A description", a.description);

		DataSourceConfigurationOption b = options.get(1);
		assertEquals("B", b.name);
		assertEquals("b", b.defaultValue);
		assertEquals("Another description", b.description);
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

	public static final class MyDataSource implements IDataSource {

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

		@Override
		public void close() {
		}

		@Override
		public String[] getFieldNames() {
			return null;
		}

		@Override
		public String getValueFor(String fieldName) {
			return null;
		}

		@Override
		public void setRow(int index) {
		}

		@Override
		public void loadFromStream(InputStream is) throws DataSourceException {
		}

		@Override
		public int getNumberOfRows() {
			return 0;
		}
	}
}
