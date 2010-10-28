package net.bpelunit.framework.control.datasource.ods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import net.bpelunit.framework.control.datasource.DataSourceHelper;
import net.bpelunit.framework.exception.DataSourceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OdsDataSourceTest {
	private InputStream getStream(String filenameBody) {
		return getClass().getResourceAsStream("/" + filenameBody + ".ods");
	}

	private OdsDataSource ds;

	@Before
	public void setUp() {
		ds = new OdsDataSource();
	}

	@After
	public void tearDown() {
		ds.close();
	}

	@Test
	public void testMetaData() throws Exception {
		List<String> validationMessages = DataSourceHelper
				.validateDataSourceAnnotation(ds.getClass());
		assertEquals(validationMessages.toString(), 0, validationMessages
				.size());

		validationMessages = DataSourceHelper.validateMethodAnnotations(ds
				.getClass());
		assertEquals(validationMessages.toString(), 0, validationMessages
				.size());

		assertTrue(DataSourceHelper.isValidConfigurationOption(ds, "sheet"));
	}

	@Test(expected = DataSourceException.class)
	public void testEmptySheet() throws Exception {
		ds.loadFromStream(getStream("empty"));
	}

	@Test
	public void testSheetWithHeadersOnly() throws Exception {
		ds.loadFromStream(getStream("header_0rows"));

		assertEquals(0, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);
	}

	@Test
	public void testSheetWithHeadersAndOneRow() throws Exception {
		ds.loadFromStream(getStream("header_1row"));

		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);

		ds.setRow(0);
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));
	}

	@Test
	public void testSheetWithHeadersAndThreeRows() throws Exception {
		ds.loadFromStream(getStream("header_3rows"));

		assertEquals(3, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);

		ds.setRow(0);
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));

		ds.setRow(1);
		assertEquals("D", ds.getValueFor("AA"));
		assertEquals("E", ds.getValueFor("BB"));
		assertEquals("F", ds.getValueFor("CC"));

		ds.setRow(2);
		assertEquals("G", ds.getValueFor("AA"));
		assertEquals("H", ds.getValueFor("BB"));
		assertEquals("I", ds.getValueFor("CC"));
	}

	@Test
	public void testSheetWithHeadersAndThreeRowsShifted() throws Exception {
		ds.loadFromStream(getStream("header_3rowsshifted"));

		assertEquals(3, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);

		ds.setRow(0);
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));

		ds.setRow(1);
		assertEquals("D", ds.getValueFor("AA"));
		assertEquals("E", ds.getValueFor("BB"));
		assertEquals("F", ds.getValueFor("CC"));

		ds.setRow(2);
		assertEquals("G", ds.getValueFor("AA"));
		assertEquals("H", ds.getValueFor("BB"));
		assertEquals("I", ds.getValueFor("CC"));
	}

	@Test
	public void testSheetWithHeadersAndThreeRowsAndTwoSheets() throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "sheet", "2");
		ds.loadFromStream(getStream("header_3rows_2sheets"));

		assertEquals(3, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);

		ds.setRow(0);
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));

		ds.setRow(1);
		assertEquals("D", ds.getValueFor("AA"));
		assertEquals("E", ds.getValueFor("BB"));
		assertEquals("F", ds.getValueFor("CC"));

		ds.setRow(2);
		assertEquals("G", ds.getValueFor("AA"));
		assertEquals("H", ds.getValueFor("BB"));
		assertEquals("I", ds.getValueFor("CC"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSheetNumber() throws Exception {
		ds.setSheet("A");
	}

	@Test(expected = IllegalStateException.class)
	public void testSettingSheetNumberAfterAccessingDataSource()
			throws Exception {
		ds.loadFromStream(getStream("header_3rowsshifted"));
		ds.setSheet("1");
	}

	@Test(expected = DataSourceException.class)
	public void testInvalidSheetIndex() throws Exception {
		ds.setSheet("2");
		ds.loadFromStream(getStream("header_3rowsshifted.xls"));
	}
	
	// TODO StartRow for data for ODS & Excel incl. tests
	// TODO Property setting with DataSourceHelper
}