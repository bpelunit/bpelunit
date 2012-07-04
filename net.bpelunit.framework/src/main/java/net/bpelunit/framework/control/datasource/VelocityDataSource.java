package net.bpelunit.framework.control.datasource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.DataSource;
import net.bpelunit.framework.exception.DataSourceException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

@DataSource(name="Velocity Data Source", shortName="velocity", contentTypes={})
public class VelocityDataSource implements IDataSource {

	static final String PROPERTY_ITERATED_VARS = "iteratedVars";
	private List<String> fIteratedVars;
	private VelocityContext fContext;
	private int fRowCount;
	private int fCurrentRow = -1;

	public int getNumberOfRows() {
		return fRowCount;
	}

	@Override
	public void setRow(int index) throws DataSourceException {
		if (index < fRowCount) {
			fCurrentRow = index;
		}
		else {
			throw new DataSourceException(
				String.format("Index %d is out of bounds [0, %d]",
					index, fRowCount - 1));
		}
	}
	
	@Override
	public String[] getFieldNames() {
		Object[] objKeys = fContext.getKeys();
		String[] keys = new String[objKeys.length];
		           
		for(int i = 0; i < objKeys.length; i++) {
			keys[i] = objKeys[i].toString();
		}
		
		return keys;
	}
	
	@Override
	public Object getValueFor(String fieldName) {
		if (fIteratedVars.contains(fieldName)) {
			return ((List<?>)(fContext.get(fieldName))).get(fCurrentRow);
		} else {
			return fContext.get(fieldName);
		}
	}
	
	@Override
	public void close() {
		fContext = null;
	}
	
	public void loadFromStream(InputStream is) throws DataSourceException {
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new DataSourceException("Error while initializing Velocity",
					e);
		}

		String script = readContentsAsString(is);
		fContext = new VelocityContext();
		try {
			StringWriter sW = new StringWriter();
			Velocity.evaluate(fContext, sW, "datasource", script);
		} catch (Exception e) {
			throw new DataSourceException(
					"Error while evaluating the Velocity template:\n" + script,
					e);
		}

		// Validate all iterated vars and compute the row count
		validateIteratedVars();
	}

	/**
	 * Space-separated list of one or more variables which will be iterated over
	 * each row. These variables must all be lists with the same number of
	 * elements. The initialized context for the n-th row will have their n-th
	 * elements in variables with the same names as the lists. The rest of the
	 * variables will be available as-is.
	 * 
	 * For instance, suppose we had these contents and this property was set to
	 * "v w":
	 * 
	 * <code>
	 * #set($v = [1, 2, 3])
	 * #set($w = [2, 4, 6])
	 * #set($z = 3)
	 * </code>
	 * 
	 * This data source would have 3 rows: the first would have v = 1 and w = 2,
	 * the second would have v = 2 and w = 4, and the last one would have v = 3
	 * and w = 6. All three rows would have z = 3.
	 * 
	 * <emph>This property is required</emph>.
	 */
	@ConfigurationOption(description = "The names of the variables that should be used as test data. The list is space separated.", defaultValue = "")
	public void setIteratedVars(String value) throws DataSourceException {
		StringTokenizer tok = new StringTokenizer(value);
		List<String> iterVars = new ArrayList<String>();
		while (tok.hasMoreTokens()) {
			iterVars.add(tok.nextToken());
		}
		if (iterVars.isEmpty()) {
			throw new DataSourceException(
					"Iterated variable list cannot be empty");
		}
		fIteratedVars = iterVars;
	}

	/* PRIVATE METHODS */

	private String readContentsAsString(InputStream is)
			throws DataSourceException {
		String script;
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuffer sbuf = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sbuf.append(line);
				sbuf.append('\n');
			}
			script = sbuf.toString();
		} catch (Exception e) {
			throw new DataSourceException(
					"Error while reading the Velocity script", e);
		}
		return script;
	}

	private void validateIteratedVars() throws DataSourceException {
		if (fIteratedVars == null) {
			throw new DataSourceException(PROPERTY_ITERATED_VARS
					+ " property is required.");
		} else if (fIteratedVars.isEmpty()) {
			throw new DataSourceException(PROPERTY_ITERATED_VARS
					+ " must refer to at least one variable.");
		}

		// Check for missing itervars, itervars which are not list literals
		// and mismatching lengths
		fRowCount = -1;
		for (String var : fIteratedVars) {
			Object value = fContext.get(var);

			if (value == null) {
				throw new DataSourceException("Iterated variable " + var
						+ " is not set in the Velocity script of "
						+ "this data source.");
			}

			if (!(value instanceof List)) {
				throw new DataSourceException("Iterated variable " + var
						+ " does not contain a list literal.");
			}

			List<?> arrList = (List<?>) value;
			if (fRowCount != -1) {
				if (arrList.size() != fRowCount) {
					throw new DataSourceException("Iterated variable " + var
							+ " has mismatching length: should have "
							+ fRowCount + " element"
							+ (fRowCount > 1 ? "s" : ""));
				}
			} else {
				if (arrList.size() == 0) {
					throw new DataSourceException("Iterated variable " + var
							+ " should have at least one element");
				}
				fRowCount = arrList.size();
			}
		}
	}
}
