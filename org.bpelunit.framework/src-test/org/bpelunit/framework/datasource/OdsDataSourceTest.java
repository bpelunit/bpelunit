package org.bpelunit.framework.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class OdsDataSourceTest {
	private String getFullPath(String filenameBody) {
		return TestConfig.TEST_FILE_DIRECTORY + filenameBody + ".ods";
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

	@Test(expected = InvalidDataSourceException.class)
	public void testNoInlineData() throws Exception {
		ds.setData("abc");
	}

	@Test(expected = InvalidDataSourceException.class)
	public void testEmptySheet() throws Exception {
		ds.setSource(getFullPath("empty"));
	}

	@Test
	public void testSheetWithHeadersOnly() throws Exception {
		ds.setSource(getFullPath("header_0rows"));

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("AA", fieldNames[0]);
		assertEquals("BB", fieldNames[1]);
		assertEquals("CC", fieldNames[2]);

		assertFalse(ds.next());
	}

	@Test
	public void testSheetWithHeadersAndOneRow() throws Exception {
		ds.setSource(getFullPath("header_1row"));

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
		ds.setSource(getFullPath("header_3rows"));

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
		ds.setSource(getFullPath("header_3rowsshifted"));

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
		ds.setSource(getFullPath("header_3rows_2sheets"));

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
		ds.setSource(getFullPath("header_3rowsshifted"));
		ds.setSheet("1");
	}

	@Test(expected = InvalidDataSourceException.class)
	public void testInvalidSheetIndex() throws Exception {
		ds.setSheet("2");
		ds.setSource(getFullPath("header_3rowsshifted.xls"));
	}
}
