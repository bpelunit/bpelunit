package net.bpelunit.framework.control.datasource.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.DataSource;
import net.bpelunit.framework.exception.DataSourceException;

/**
 * This data source can be used to read in CSV (comma separated value) files.
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
@DataSource(name = "CSV Data Source", shortName = "csv", contentTypes = {
		"text/cvs", "text/plain" })
public class CSVDataSource implements IDataSource {

	private static final String DEFAULT_SEPARATOR = "\t";

	private List<String> headers = null;
	private List<String> lines = null;
	private String[] currentRecord = null;
	private String separator = DEFAULT_SEPARATOR;
	private int currentLineNumber = -1;

	@Override
	public String[] getFieldNames() {
		return headers.toArray(new String[headers.size()]);
	}

	@Override
	public String getValueFor(String fieldName) {
		int index = headers.indexOf(fieldName);
		return currentRecord[index];
	}

	@Override
	public void setRow(int index) throws DataSourceException {
		if (index < lines.size()) {
			currentLineNumber = index;
			String line = lines.get(currentLineNumber);
			currentRecord = parseLine(line);
		} else {
			throw new DataSourceException(String.format(
					"Index %d out of bounds [0, %d]", index, lines.size() - 1));
		}
	}

	private String[] parseLine(String currentLine) {
		return currentLine.split(separator);
	}

	@Override
	public void loadFromStream(InputStream data) throws DataSourceException {
		setSource(new InputStreamReader(data));
	}

	private void setSource(Reader reader) throws DataSourceException {
		try {
			this.lines = new ArrayList<String>();

			BufferedReader in = new BufferedReader(reader);
			readColumnHeadersIfNecessary(in);

			while (in.ready()) {
				String line = in.readLine();
				if (!line.trim().equals("")) {
					this.lines.add(line);
				}
			}

		} catch (IOException e) {
			throw new DataSourceException("Invalid data source", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// Do nothing because it is only a reader
			}
		}
	}

	private void readColumnHeadersIfNecessary(BufferedReader reader)
			throws IOException, DataSourceException {
		if (headersAreAlreadySet()) {
			return;
		}

		if (reader.ready()) {
			String headerLine = reader.readLine();
			headers = Arrays.asList(parseLine(headerLine));
		} else {
			throw new DataSourceException("CSV File contains no header row");
		}
	}

	private boolean headersAreAlreadySet() {
		return headers != null;
	}

	@Override
	public void close() {
		this.lines = null;
	}

	@ConfigurationOption(defaultValue = DEFAULT_SEPARATOR, description = "The separator used to divide two values within a row.")
	public void setSeparator(String separator) {
		checkIfMayAlterConfiguration();

		this.separator = separator;
	}

	private void checkIfMayAlterConfiguration() {
		if (lines != null) {
			throw new IllegalStateException(
					"Data Source is already open - must not set new parse options!");
		}
	}

	@ConfigurationOption(defaultValue = "", description = "The column names, separated by commas, which shall be used for naming the data columns of the CSV file. Use this option, if the CSV file itself does not have the headers in its first row.")
	public void setHeaders(String headerNames) {
		checkIfMayAlterConfiguration();

		if (headerNames != null && !headerNames.equals("")) {
			String[] headerArray = headerNames.split(",");
			trim(headerArray);

			this.headers = Arrays.asList(headerArray);
		}
	}

	private void trim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
	}

	@Override
	public int getNumberOfRows() {
		return this.lines.size();
	}
}