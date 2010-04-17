package org.bpelunit.framework.control.datasource.excel;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.bpelunit.framework.exception.DataSourceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractExcelDataSourceTest {

	protected abstract String getFileSuffix();

	private InputStream getStream(String filenameBody) {
		return getClass().getResourceAsStream(filenameBody + "."
				+ getFileSuffix());
	}

	private ExcelDataSource ds;

	@Before
	public void setUp() {
		ds = new ExcelDataSource();
	}

	@After
	public void tearDown() {
		ds.close();
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

		assertFalse(ds.next());
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

		assertTrue(ds.next());
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));

		assertFalse(ds.next());
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

		assertTrue(ds.next());
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));
		assertTrue(ds.next());
		assertEquals("D", ds.getValueFor("AA"));
		assertEquals("E", ds.getValueFor("BB"));
		assertEquals("F", ds.getValueFor("CC"));
		assertTrue(ds.next());
		assertEquals("G", ds.getValueFor("AA"));
		assertEquals("H", ds.getValueFor("BB"));
		assertEquals("I", ds.getValueFor("CC"));

		assertFalse(ds.next());
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

		assertTrue(ds.next());
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));
		assertTrue(ds.next());
		assertEquals("D", ds.getValueFor("AA"));
		assertEquals("E", ds.getValueFor("BB"));
		assertEquals("F", ds.getValueFor("CC"));
		assertTrue(ds.next());
		assertEquals("G", ds.getValueFor("AA"));
		assertEquals("H", ds.getValueFor("BB"));
		assertEquals("I", ds.getValueFor("CC"));

		assertFalse(ds.next());
	}

	@Test
	public void testSheetWithHeadersAndThreeRowsAndTwoSheets() throws Exception {
		ds.setSheet("2");
		ds.loadFromStream(getStream("header_3rows_2sheets"));

		assertEquals(3, ds.getNumberOfRows());
		
		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);

		assertTrue(ds.next());
		assertEquals("A", ds.getValueFor("AA"));
		assertEquals("B", ds.getValueFor("BB"));
		assertEquals("C", ds.getValueFor("CC"));
		assertTrue(ds.next());
		assertEquals("D", ds.getValueFor("AA"));
		assertEquals("E", ds.getValueFor("BB"));
		assertEquals("F", ds.getValueFor("CC"));
		assertTrue(ds.next());
		assertEquals("G", ds.getValueFor("AA"));
		assertEquals("H", ds.getValueFor("BB"));
		assertEquals("I", ds.getValueFor("CC"));

		assertFalse(ds.next());
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
		ds.loadFromStream(getStream("header_3rowsshifted"));
	}
}
