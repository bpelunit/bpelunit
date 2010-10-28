package net.bpelunit.framework.control.datasource.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import net.bpelunit.framework.control.datasource.DataSourceHelper;
import net.bpelunit.framework.exception.DataSourceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVDataSourceTest {

	private CSVDataSource ds;

	@Before
	public void setUp() {
		ds = new CSVDataSource();
	}

	@After
	public void tearDown() {
		ds.close();
	}

	private InputStream getStream(String fileName) {
		return getClass().getResourceAsStream("/" + fileName);
	}

	@Test
	public void testMetaData() throws Exception {
		List<String> validationMessages = DataSourceHelper.validateDataSourceAnnotation(ds.getClass());
		assertEquals(validationMessages.toString(), 0, validationMessages.size());
		
		validationMessages = DataSourceHelper.validateMethodAnnotations(ds.getClass());
		assertEquals(validationMessages.toString(), 0, validationMessages.size());
		
		assertTrue(DataSourceHelper.isValidConfigurationOption(ds, "separator"));
		assertTrue(DataSourceHelper.isValidConfigurationOption(ds, "headers"));
	}
	
	@Test(expected = DataSourceException.class)
	public void testEmptyCSVFile() throws Exception {
		ds.loadFromStream(getStream("empty.csv"));
	}

	@Test
	public void testCSVFileWithHeaderAndOneRowAndDefaultSeparator()
			throws Exception {
		ds.loadFromStream(getStream("header_1row_tab.csv"));

		assertEquals(1, ds.getNumberOfRows());
		assertEquals(1, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);

		ds.setRow(0);
		assertEquals("A1", ds.getValueFor("A"));
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndTabSeparator()
			throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "separator", "\t");
		ds.loadFromStream(getStream("header_3rows_tab.csv"));
		assertEquals(3, ds.getNumberOfRows());

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);
		assertEquals("B", ds.getFieldNames()[1]);
		assertEquals("C", ds.getFieldNames()[2]);

		ds.setRow(0);
		assertEquals("A1", ds.getValueFor("A"));
		assertEquals("B1", ds.getValueFor("B"));
		assertEquals("C1", ds.getValueFor("C"));

		ds.setRow(1);
		assertEquals("A2", ds.getValueFor("A"));
		assertEquals("B2", ds.getValueFor("B"));
		assertEquals("C2", ds.getValueFor("C"));

		ds.setRow(2);
		assertEquals("A3", ds.getValueFor("A"));
		assertEquals("B3", ds.getValueFor("B"));
		assertEquals("C3", ds.getValueFor("C"));
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndCommaSeparator()
			throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "separator", ",");
		ds.loadFromStream(getStream("header_3rows_comma.csv"));
		assertEquals(3, ds.getNumberOfRows());

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);
		assertEquals("B", ds.getFieldNames()[1]);
		assertEquals("C", ds.getFieldNames()[2]);

		ds.setRow(0);
		assertEquals("A1", ds.getValueFor("A"));
		assertEquals("B1", ds.getValueFor("B"));
		assertEquals("C1", ds.getValueFor("C"));

		ds.setRow(1);
		assertEquals("A2", ds.getValueFor("A"));
		assertEquals("B2", ds.getValueFor("B"));
		assertEquals("C2", ds.getValueFor("C"));

		ds.setRow(2);
		assertEquals("A3", ds.getValueFor("A"));
		assertEquals("B3", ds.getValueFor("B"));
		assertEquals("C3", ds.getValueFor("C"));
	}

	@Test
	public void testEmptyCSVFileWithDefinedHeaders() throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "headers", " D, E , F ");
		ds.loadFromStream(getStream("empty.csv"));
		assertEquals(0, ds.getNumberOfRows());

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("D", ds.getFieldNames()[0]);
		assertEquals("E", ds.getFieldNames()[1]);
		assertEquals("F", ds.getFieldNames()[2]);
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndDefaultSeparatorWithDefinedHeaders()
			throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "headers", " D, E , F ");
		ds.loadFromStream(getStream("header_3rows_tab.csv"));
		assertEquals(4, ds.getNumberOfRows());

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("D", ds.getFieldNames()[0]);
		assertEquals("E", ds.getFieldNames()[1]);
		assertEquals("F", ds.getFieldNames()[2]);

		ds.setRow(0);
		assertEquals("A", ds.getValueFor("D"));
		assertEquals("B", ds.getValueFor("E"));
		assertEquals("C", ds.getValueFor("F"));

		ds.setRow(1);
		assertEquals("A1", ds.getValueFor("D"));
		assertEquals("B1", ds.getValueFor("E"));
		assertEquals("C1", ds.getValueFor("F"));

		ds.setRow(2);
		assertEquals("A2", ds.getValueFor("D"));
		assertEquals("B2", ds.getValueFor("E"));
		assertEquals("C2", ds.getValueFor("F"));

		ds.setRow(3);
		assertEquals("A3", ds.getValueFor("D"));
		assertEquals("B3", ds.getValueFor("E"));
		assertEquals("C3", ds.getValueFor("F"));
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndCommaSeparatorWithDefinedHeaders()
			throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "separator", ",");
		DataSourceHelper.setConfigurationOption(ds, "headers", " D, E , F ");
		ds.loadFromStream(getStream("header_3rows_comma.csv"));
		assertEquals(4, ds.getNumberOfRows());

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("D", ds.getFieldNames()[0]);
		assertEquals("E", ds.getFieldNames()[1]);
		assertEquals("F", ds.getFieldNames()[2]);

		ds.setRow(0);
		assertEquals("A", ds.getValueFor("D"));
		assertEquals("B", ds.getValueFor("E"));
		assertEquals("C", ds.getValueFor("F"));

		ds.setRow(1);
		assertEquals("A1", ds.getValueFor("D"));
		assertEquals("B1", ds.getValueFor("E"));
		assertEquals("C1", ds.getValueFor("F"));

		ds.setRow(2);
		assertEquals("A2", ds.getValueFor("D"));
		assertEquals("B2", ds.getValueFor("E"));
		assertEquals("C2", ds.getValueFor("F"));

		ds.setRow(3);
		assertEquals("A3", ds.getValueFor("D"));
		assertEquals("B3", ds.getValueFor("E"));
		assertEquals("C3", ds.getValueFor("F"));
	}

	@Test(expected = IllegalStateException.class)
	public void testMayNotChangeSeparatorLater() throws Exception {
		ds.loadFromStream(getStream("header_3rows_comma.csv"));
		ds.setSeparator("something");
	}

	@Test(expected = IllegalStateException.class)
	public void testMayNotChangeHeadersLater() throws Exception {
		ds.loadFromStream(getStream("header_3rows_comma.csv"));
		ds.setHeaders("A, B, C");
	}
}