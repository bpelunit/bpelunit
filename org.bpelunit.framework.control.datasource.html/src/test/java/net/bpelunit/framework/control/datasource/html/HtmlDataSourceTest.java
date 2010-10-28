package net.bpelunit.framework.control.datasource.html;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import net.bpelunit.framework.control.datasource.DataSourceHelper;
import net.bpelunit.framework.exception.DataSourceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HtmlDataSourceTest {

	private HtmlDataSource ds;

	@Before
	public void setUp() {
		this.ds = new HtmlDataSource();
	}

	@After
	public void tearDown() {
		this.ds.close();
		this.ds = null;
	}

	@Test
	public void testMetaData() throws Exception {
		List<String> validationMessages = DataSourceHelper.validateDataSourceAnnotation(ds.getClass());
		assertEquals(validationMessages.toString(), 0, validationMessages.size());
		
		validationMessages = DataSourceHelper.validateMethodAnnotations(ds.getClass());
		assertEquals(validationMessages.toString(), 0, validationMessages.size());
		
		assertTrue(DataSourceHelper.isValidConfigurationOption(ds, "table"));
	}
	
	@Test(expected = DataSourceException.class)
	public void testTableNotFound() throws Exception {
		this.ds.loadFromStream(getStream("no-table"));
	}

	@Test(expected = DataSourceException.class)
	public void testTableWithNoRows() throws Exception {
		this.ds.loadFromStream(getStream("table-with-no-rows"));
	}

	@Test(expected = DataSourceException.class)
	public void testTableWithColSpan() throws Exception {
		this.ds.loadFromStream(getStream("table-with-colspan"));
	}

	@Test(expected = DataSourceException.class)
	public void testTableWithRowSpan() throws Exception {
		this.ds.loadFromStream(getStream("table-with-rowspan"));
	}

	@Test
	public void testTableWithHeader() throws Exception {
		this.ds.loadFromStream(getStream("table-with-header"));
		assertEquals(0, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);
	}

	@Test
	public void testTableWithHeaderAndOneRow() throws Exception {
		this.ds.loadFromStream(getStream("table-with-header-and-one-row"));
		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.setRow(0);
		assertEquals("1", ds.getValueFor("A"));
		assertEquals("2", ds.getValueFor("B"));
		assertEquals("3", ds.getValueFor("C"));
	}

	@Test
	public void testTableWithHeaderAndTwoRows() throws Exception {
		this.ds.loadFromStream(getStream("table-with-header-and-two-rows"));
		assertEquals(2, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.setRow(0);
		assertEquals("1", ds.getValueFor("A"));
		assertEquals("2", ds.getValueFor("B"));
		assertEquals("3", ds.getValueFor("C"));

		ds.setRow(1);
		assertEquals("4", ds.getValueFor("A"));
		assertEquals("5", ds.getValueFor("B"));
		assertEquals("6", ds.getValueFor("C"));
	}

	@Test
	public void testTableWithTdWithoutTr() throws Exception {
		this.ds.loadFromStream(getStream("table-with-td-without-tr"));

		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.setRow(0);
		assertEquals("1", ds.getValueFor("A"));
		assertEquals("2", ds.getValueFor("B"));
		assertEquals("3", ds.getValueFor("C"));
	}

	@Test
	public void testTableWithoutManyTags() throws Exception {
		this.ds.loadFromStream(getStream("table-without-endtags"));

		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.setRow(0);
		assertEquals("1", ds.getValueFor("A"));
		assertEquals("2", ds.getValueFor("B"));
		assertEquals("3", ds.getValueFor("C"));
	}

	@Test
	public void testSecondTableWithHeaderAndOneRow() throws Exception {
		DataSourceHelper.setConfigurationOption(ds, "table", "2");
		this.ds.loadFromStream(getStream("two-tables"));
		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.setRow(0);
		assertEquals("1", ds.getValueFor("A"));
		assertEquals("2", ds.getValueFor("B"));
		assertEquals("3", ds.getValueFor("C"));
	}

	@Test
	public void testUnbalancedTable() throws Exception {
		this.ds.loadFromStream(getStream("unbalanced-table"));
		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.setRow(0);
		assertEquals("1", ds.getValueFor("A"));
		assertEquals("2", ds.getValueFor("B"));
		assertEquals("", ds.getValueFor("C"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFieldName() throws Exception {
		this.ds.loadFromStream(getStream("table-with-header-and-one-row"));
		assertEquals(1, ds.getNumberOfRows());

		String[] fieldNames = ds.getFieldNames();
		assertEquals(3, fieldNames.length);
		assertEquals("A", fieldNames[0]);
		assertEquals("B", fieldNames[1]);
		assertEquals("C", fieldNames[2]);

		ds.getValueFor("Non Existing Field");
	}

	@Test(expected = IllegalStateException.class)
	public void testMayNotAlterTableNoAfterRead() throws Exception {
		this.ds.loadFromStream(getStream("two-tables"));
		this.ds.setTable("1");
	}

	private InputStream getStream(String string) {
		InputStream is = this.getClass().getResourceAsStream("/" + string + ".html");
		return is;
	}
}