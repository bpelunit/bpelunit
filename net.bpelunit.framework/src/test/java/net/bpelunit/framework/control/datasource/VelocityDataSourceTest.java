package net.bpelunit.framework.control.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.control.ext.ExtensionRegistry;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.SpecificationException;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Velocity data source type.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class VelocityDataSourceTest {

	private static final String SOURCE_TYPE = "velocity";

	@Before
	public void setUp() throws Exception {
		ExtensionRegistry.loadRegistry(new File("src/main/resources/" + BPELUnitBaseRunner.CONFIG_DIR,
				BPELUnitBaseRunner.EXTENSIONS_FILE_NAME).toURI().toURL(), false);
	}

	@Test
	public void testMetaData() throws Exception {
		List<String> messages = DataSourceHelper.validateDataSourceAnnotation(VelocityDataSource.class);
		assertEquals(messages.toString(), 0, messages.size());
		
		messages = DataSourceHelper.validateMethodAnnotations(VelocityDataSource.class);
		assertEquals(messages.toString(), 0, messages.size());
		
		assertTrue(DataSourceHelper.isValidConfigurationOption(new VelocityDataSource(), "iteratedVars"));
	}
	
	/**
	 * Checks that the data source can be created. This requires that it is
	 * properly registered in <code>conf/extensions.xml</code>.
	 */
	@Test
	public void canBeCreated() throws Exception {
		ExtensionRegistry.createNewDataSourceForType(SOURCE_TYPE);
	}

	/**
	 * Checks that the
	 * {@link net.bpelunit.framework.control.datasource.VelocityDataSource#PROPERTY_ITERATED_VARS}
	 * property is required.
	 */
	@Test
	public void iteratedVarsPropertyIsRequired() throws SpecificationException {
		try {
			createDataSourceFromString(null, "#set($x=[1,2,3])");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {
			assertTrue(
					"The exception message should indicate that the required "
							+ "iterated_vars property is missing", ex
							.getMessage().contains(
									VelocityDataSource.PROPERTY_ITERATED_VARS));
		}
	}

	/**
	 * Checks that empty
	 * {@link net.bpelunit.framework.control.datasource.VelocityDataSource#PROPERTY_ITERATED_VARS}
	 * properties are reported.
	 */
	@Test
	public void iteratedVarsPropertyCannotBeEmpty() throws SpecificationException {
		try {
			createDataSourceFromString("   ", "#set($x=[1,2,3])");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {
			assertTrue(
					"The exception message should indicate that the required "
							+ "iterated_vars property cannot be empty", ex
							.getMessage().contains("empty"));
		}
	}

	/**
	 * Checks that missing iterated variables are reported.
	 */
	@Test
	public void missingIteratedVariablesAreReported() throws SpecificationException {
		final String variableName = "myvar";
		try {
			createDataSourceFromString(variableName, "");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {
			assertTrue(
					"The exception message should indicate that the iterated "
							+ "variable x is not set in its contents", ex
							.getMessage().contains(variableName));
		}
	}

	/**
	 * Checks that mismatching lengths in the iterated variables are reported.
	 */
	@Test
	public void mismatchingLengthsOfIteratedVariablesAreReported() throws SpecificationException {
		try {
			createDataSourceFromString("x y",
					"#set($x=[1,2,3])\n#set($y=[1,2])");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {
			assertTrue("The exception message should indicate that the "
					+ "iterated variables have mismatching lengths", ex
					.getMessage().contains("length"));
		}
	}

	/**
	 * Checks that empty iterated variables are reported.
	 */
	@Test
	public void emptyIteratedVariablesAreReported() throws SpecificationException {
		try {
			createDataSourceFromString("x y", "#set($x=[])\n#set($y=[])");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {
			assertTrue("The exception message should indicate that iterated "
					+ "variables cannot have 0 elements", ex.getMessage()
					.contains("at least one element"));
		}
	}

	/**
	 * Checks that iterated variables which are not list literals are reported.
	 */
	@Test
	public void iteratedVariablesWithWrongTypesAreReported() throws SpecificationException {
		try {
			createDataSourceFromString("x myvar",
					"#set($x=[1,2,3])\n#set($myvar={'foo':1,'bar':2})");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {
			assertTrue("The exception message should indicate that one of the "
					+ "iterated variables has a wrong type", ex.getMessage()
					.contains("does not contain a list literal"));
		}
	}

	/**
	 * Checks that unknown properties are reported.
	 */
	@Test
	public void unknownPropertiesAreReported() throws SpecificationException {
		try {
			HashMap<String, String> props = new HashMap<String, String>();
			props.put(VelocityDataSource.PROPERTY_ITERATED_VARS, "x");
			props.put("badprop", "nobodyexpectsthespanishinquisition");
			IDataSource ds = ExtensionRegistry.createNewDataSourceForType(SOURCE_TYPE);
			DataSourceUtil.initializeDataSource(ds, DataSourceUtil
					.getStreamForDataSource("#set($x=[1,2,3])", null, null), props);
			fail("A DataSourceException was expected.");
		} catch (DataSourceException ex) {
			assertTrue("The exception message should indicate that an "
					+ "unknown property was used.", ex.getMessage().contains(
					"Property"));
		}
	}

	/**
	 * Checks that initializing contexts with invalid rows throws an exception.
	 */
	@Test
	public void invalidRowsAreReported() throws Exception {
		IDataSource ds = createDataSourceFromString("x", "#set($x=[1,2])");
		try {
			ds.setRow(5);
			fail("Invalid row indexes should be reported");
		} catch (DataSourceException ex) {}
	}

	/**
	 * Checks that valid contents and calls produce the expected results.
	 */
	@Test
	public void validContentsWorkAsExpected() throws Exception {
		IDataSource ds = createDataSourceFromString("x y",
				"#set($x=[1,2,3])\n#set($y=[2,4,6])\n#set($z=3)");

		assertEquals(3, ds.getNumberOfRows());
		
		List<String> fieldNames = Arrays.asList(ds.getFieldNames());
		assertEquals(3, fieldNames.size());
		assertTrue(fieldNames.contains("x"));
		assertTrue(fieldNames.contains("y"));
		assertTrue(fieldNames.contains("z"));
		
		final int[] x = new int[] { 1, 2, 3 };
		final int[] y = new int[] { 2, 4, 6 };
		final int z = 3;

		for (int iRow = 0; iRow < ds.getNumberOfRows(); ++iRow) {
			ds.setRow(iRow);
			assertEquals(x[iRow], ds.getValueFor("x"));
			assertEquals(y[iRow], ds.getValueFor("y"));
			assertEquals(z, ds.getValueFor("z"));
			
			/*VelocityContext ctx = new VelocityContext();
			ds.initializeContext(ctx, iRow);
			assertEquals(x[iRow], ctx.get("x"));
			assertEquals(y[iRow], ctx.get("y"));
			assertEquals(z, ctx.get("z"));
			*/ // TODO Remove block
		}
	}

	/**
	 * Checks that nested list literals work.
	 */
	@Test
	public void nestedListLiteralsWork() throws Exception {
		IDataSource ds = createDataSourceFromString("lines",
				"#set($lines=[[],['A'],['A','B'],['A','B','C']])");

		String[] fieldNames = ds.getFieldNames();
		assertEquals(1, fieldNames.length);
		assertEquals("lines", fieldNames[0]);
		
		final String[][] expectedRows = new String[][] { new String[] {},
				new String[] { "A" }, new String[] { "A", "B" },
				new String[] { "A", "B", "C" } };
		assertEquals(expectedRows.length, ds.getNumberOfRows());

		for (int iRow = 0; iRow < ds.getNumberOfRows(); ++iRow) {
			ds.setRow(iRow);
			List<?> actualLines = (List<?>) ds.getValueFor("lines");
			final String[] expectedLines = expectedRows[iRow];
			assertEquals(expectedLines.length, actualLines.size());
			for (int iLine = 0; iLine < expectedLines.length; ++iLine) {
				assertEquals(expectedLines[iLine], actualLines.get(iLine));
			}
		}
	}

	/* UTILITY METHODS */

	private IDataSource createDataSourceFromString(String variables,
			String contents) throws DataSourceException, SpecificationException {
		HashMap<String, String> props = new HashMap<String, String>();
		if (variables != null) {
			props.put(VelocityDataSource.PROPERTY_ITERATED_VARS, variables);
		}
		IDataSource ds = ExtensionRegistry.createNewDataSourceForType(SOURCE_TYPE);
		return DataSourceUtil.initializeDataSource(ds, DataSourceUtil
				.getStreamForDataSource(contents, null, null), props);
	}
}
