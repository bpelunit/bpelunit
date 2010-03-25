package org.bpelunit.framework.datasource;

import static org.junit.Assert.*;

import org.bpelunit.framework.datasource.CSVDataSource;
import org.bpelunit.framework.datasource.InvalidDataSourceException;
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

	@Test(expected = InvalidDataSourceException.class)
	public void testEmptyCSVFile() throws Exception {
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "empty.csv");
	}

	@Test
	public void testCSVFileWithHeaderAndOneRowAndDefaultSeparator()
			throws Exception {
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_1row_tab.csv");

		assertEquals(1, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);
		assertTrue(ds.next());
		assertEquals("A1", ds.getValueFor("A"));
		assertFalse(ds.next());
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndTabSeparator()
			throws Exception {
		ds.setSeparator("\t");
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_3rows_tab.csv");

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);
		assertEquals("B", ds.getFieldNames()[1]);
		assertEquals("C", ds.getFieldNames()[2]);

		assertTrue(ds.next());
		assertEquals("A1", ds.getValueFor("A"));
		assertEquals("B1", ds.getValueFor("B"));
		assertEquals("C1", ds.getValueFor("C"));
		assertTrue(ds.next());
		assertEquals("A2", ds.getValueFor("A"));
		assertEquals("B2", ds.getValueFor("B"));
		assertEquals("C2", ds.getValueFor("C"));
		assertTrue(ds.next());
		assertEquals("A3", ds.getValueFor("A"));
		assertEquals("B3", ds.getValueFor("B"));
		assertEquals("C3", ds.getValueFor("C"));
		assertFalse(ds.next());
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndCommaSeparator()
			throws Exception {
		ds.setSeparator(",");
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_3rows_comma.csv");

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);
		assertEquals("B", ds.getFieldNames()[1]);
		assertEquals("C", ds.getFieldNames()[2]);

		assertTrue(ds.next());
		assertEquals("A1", ds.getValueFor("A"));
		assertEquals("B1", ds.getValueFor("B"));
		assertEquals("C1", ds.getValueFor("C"));
		assertTrue(ds.next());
		assertEquals("A2", ds.getValueFor("A"));
		assertEquals("B2", ds.getValueFor("B"));
		assertEquals("C2", ds.getValueFor("C"));
		assertTrue(ds.next());
		assertEquals("A3", ds.getValueFor("A"));
		assertEquals("B3", ds.getValueFor("B"));
		assertEquals("C3", ds.getValueFor("C"));
		assertFalse(ds.next());
	}

	@Test
	public void testEmptyCSVFileWithDefinedHeaders() throws Exception {
		ds.setHeaders(" D, E , F ");
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "empty.csv");

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("D", ds.getFieldNames()[0]);
		assertEquals("E", ds.getFieldNames()[1]);
		assertEquals("F", ds.getFieldNames()[2]);
		assertFalse(ds.next());
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndDefaultSeparatorWithDefinedHeaders()
			throws Exception {
		ds.setHeaders(" D, E , F ");
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_3rows_tab.csv");

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("D", ds.getFieldNames()[0]);
		assertEquals("E", ds.getFieldNames()[1]);
		assertEquals("F", ds.getFieldNames()[2]);

		assertTrue(ds.next());
		assertEquals("A", ds.getValueFor("D"));
		assertEquals("B", ds.getValueFor("E"));
		assertEquals("C", ds.getValueFor("F"));
		assertTrue(ds.next());
		assertEquals("A1", ds.getValueFor("D"));
		assertEquals("B1", ds.getValueFor("E"));
		assertEquals("C1", ds.getValueFor("F"));
		assertTrue(ds.next());
		assertEquals("A2", ds.getValueFor("D"));
		assertEquals("B2", ds.getValueFor("E"));
		assertEquals("C2", ds.getValueFor("F"));
		assertTrue(ds.next());
		assertEquals("A3", ds.getValueFor("D"));
		assertEquals("B3", ds.getValueFor("E"));
		assertEquals("C3", ds.getValueFor("F"));
		assertFalse(ds.next());
	}

	@Test
	public void testCSVFileWithHeaderAndThreeRowsAndCommaSeparatorWithDefinedHeaders()
			throws Exception {
		ds.setSeparator(",");
		ds.setHeaders(" D, E , F ");
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_3rows_comma.csv");

		assertEquals(3, ds.getFieldNames().length);
		assertEquals("D", ds.getFieldNames()[0]);
		assertEquals("E", ds.getFieldNames()[1]);
		assertEquals("F", ds.getFieldNames()[2]);

		assertTrue(ds.next());
		assertEquals("A", ds.getValueFor("D"));
		assertEquals("B", ds.getValueFor("E"));
		assertEquals("C", ds.getValueFor("F"));
		assertTrue(ds.next());
		assertEquals("A1", ds.getValueFor("D"));
		assertEquals("B1", ds.getValueFor("E"));
		assertEquals("C1", ds.getValueFor("F"));
		assertTrue(ds.next());
		assertEquals("A2", ds.getValueFor("D"));
		assertEquals("B2", ds.getValueFor("E"));
		assertEquals("C2", ds.getValueFor("F"));
		assertTrue(ds.next());
		assertEquals("A3", ds.getValueFor("D"));
		assertEquals("B3", ds.getValueFor("E"));
		assertEquals("C3", ds.getValueFor("F"));
		assertFalse(ds.next());
	}

	@Test(expected = IllegalStateException.class)
	public void testMayNotChangeSeparatorLater() throws Exception {
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_3rows_comma.csv");
		ds.setSeparator("something");
	}

	@Test(expected = IllegalStateException.class)
	public void testMayNotChangeHeadersLater() throws Exception {
		ds.setSource(TestConfig.TEST_FILE_DIRECTORY + "header_3rows_comma.csv");
		ds.setHeaders("A, B, C");
	}
	
	@Test
	public void testInlineData() throws Exception {
		ds.setData("A\tB\tC\nA1\tB1\tC1\nA2\tB2\tC2\n");
		assertEquals(3, ds.getFieldNames().length);
		assertEquals("A", ds.getFieldNames()[0]);
		assertEquals("B", ds.getFieldNames()[1]);
		assertEquals("C", ds.getFieldNames()[2]);

		assertTrue(ds.next());
		assertEquals("A1", ds.getValueFor("A"));
		assertEquals("B1", ds.getValueFor("B"));
		assertEquals("C1", ds.getValueFor("C"));
		assertTrue(ds.next());
		assertEquals("A2", ds.getValueFor("A"));
		assertEquals("B2", ds.getValueFor("B"));
		assertEquals("C2", ds.getValueFor("C"));
		assertFalse(ds.next());
	}
}
