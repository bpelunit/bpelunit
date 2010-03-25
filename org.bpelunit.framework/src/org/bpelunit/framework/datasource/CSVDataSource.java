package org.bpelunit.framework.datasource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class CSVDataSource implements DataSource {

	private static final String DEFAULT_SEPARATOR = "\t";

	private BufferedReader reader;
	private List<String> headers = null;
	private String[] currentRecord = null;
	private String separator = DEFAULT_SEPARATOR;

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
	public boolean next() {
		try {
			if (reader.ready()) {
				String currentLine = reader.readLine();
				if(currentLine == null) {
					return false;
				}
				currentRecord = parseLine(currentLine);
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	private String[] parseLine(String currentLine) {
		return currentLine.split(separator);
	}

	@Override
	public void setSource(String uri) throws InvalidDataSourceException {
		try {
			setSource(new BufferedReader(new FileReader(uri)));
		} catch (IOException e) {
			throw new InvalidDataSourceException("Invalid data source", e);
		}
	}

	private void setSource(Reader reader) throws InvalidDataSourceException {
		try {
			this.reader = new BufferedReader(reader);
			readColumnHeadersIfNecessary();
		} catch (IOException e) {
			throw new InvalidDataSourceException("Invalid data source", e);
		}
	}

	@Override
	public void setData(String data) throws InvalidDataSourceException {
		setSource(new BufferedReader(new StringReader(data)));
	}

	private void readColumnHeadersIfNecessary() throws IOException,
			InvalidDataSourceException {
		if (headersAreAlreadySet()) {
			return;
		}

		if (reader.ready()) {
			String headerLine = reader.readLine();
			headers = Arrays.asList(parseLine(headerLine));
		} else {
			throw new InvalidDataSourceException(
					"CSV File contains no header row");
		}
	}

	private boolean headersAreAlreadySet() {
		return headers != null;
	}

	@Override
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// Ignore, close of reader not writer
			}
			reader = null;
		}
	}

	@ConfigurationOption(defaultValue = DEFAULT_SEPARATOR, description = "The separator used to divide two values within a row.")
	public void setSeparator(String separator) {
		checkIfMayAlterConfiguration();

		this.separator = separator;
	}

	private void checkIfMayAlterConfiguration() {
		if (reader != null) {
			throw new IllegalStateException(
					"Data Source is already open - must not set new parse options!");
		}
	}

	@ConfigurationOption(defaultValue = "", description = "The column names, separated by commas, which shall be used for naming the data columns of the CSV file. Use this option, if the CSV file itself does not have the headers in its first row.")
	public void setHeaders(String headerNames) {
		checkIfMayAlterConfiguration();

		if (headerNames != null && !headerNames.equals("")) {
			String[] headers = headerNames.split(",");
			trim(headers);

			this.headers = Arrays.asList(headers);
		}
	}

	private void trim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
	}

	@Override
	public String[] getSupportedContentTypes() {
		return new String[] { "text/cvs" };
	}
}
