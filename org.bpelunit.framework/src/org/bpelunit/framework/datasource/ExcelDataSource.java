package org.bpelunit.framework.datasource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelDataSource implements DataSource {

	private static final int DEFAULT_SHEET_INDEX = 0;

	private Sheet sheet;
	private List<String> headings;
	private int headingRowIndex = 0;
	private int startDataRowIndex = 1;
	private int currentDataRow = startDataRowIndex - 1;
	private short firstCellIndex = 0;
	private int sheetIndex = DEFAULT_SHEET_INDEX;

	@Override
	public void close() {
		sheet = null;
	}

	@Override
	public String[] getFieldNames() {
		return headings.toArray(new String[headings.size()]);
	}

	@Override
	public String getValueFor(String fieldName) {
		Row row = sheet.getRow(currentDataRow);
		int cellIndex = firstCellIndex + headings.indexOf(fieldName);

		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return "";
		} else {
			return cell.getStringCellValue();
		}
	}

	@Override
	public boolean next() {
		Row rowToCheck = sheet.getRow(currentDataRow + 1);

		if (rowToCheck == null) {
			return false;
		} else {
			currentDataRow++;
			return true;
		}
	}

	@Override
	public void setSource(String uri) throws InvalidDataSourceException {
		Workbook workbook = readWorkbook(uri);
		sheet = getSheetWithTestData(workbook);
		extractFieldNames();
	}

	protected Workbook readWorkbook(String uri)
			throws InvalidDataSourceException {
		try {
			Workbook wb = WorkbookFactory.create(new FileInputStream(uri));
			return wb;
		} catch (InvalidFormatException e) {
			throw new InvalidDataSourceException("The file '" + uri
					+ "' is not a valid MS Excel File!", e);
		} catch (FileNotFoundException e) {
			throw new InvalidDataSourceException("The file '" + uri
					+ "' does not exist!", e);
		} catch (IOException e) {
			throw new InvalidDataSourceException("The file '" + uri
					+ "' could not be read!", e);
		}
	}

	private Sheet getSheetWithTestData(Workbook workbook)
			throws InvalidDataSourceException {
		try {
			return workbook.getSheetAt(sheetIndex);
		} catch (IllegalArgumentException e) {
			throw new InvalidDataSourceException(
					"Could not find sheet with index " + sheetIndex);
		}
	}

	private void extractFieldNames() throws InvalidDataSourceException {
		List<String> headings = new ArrayList<String>();
		Row headingRow = sheet.getRow(headingRowIndex);

		checkForNull(headingRow, "No headings found at row " + headingRowIndex);

		firstCellIndex = headingRow.getFirstCellNum();
		for (short i = firstCellIndex; i < headingRow.getLastCellNum(); i++) {
			String heading = headingRow.getCell(i).getStringCellValue();
			headings.add(heading);
		}

		this.headings = headings;
	}

	private void checkForNull(Object o, String message)
			throws InvalidDataSourceException {
		if (o == null) {
			throw new InvalidDataSourceException(message);
		}
	}

	@Override
	public void setData(String data) throws InvalidDataSourceException {
		throw new InvalidDataSourceException(
				"Inline sources are not allowed for MS Excel files");
	}

	@Override
	public String[] getSupportedContentTypes() {
		return new String[] { "application/excel", "application/vnd.ms-excel",
				"application/x-excel", "application/x-msexcel",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };
	}

	@ConfigurationOption(defaultValue = "" + DEFAULT_SHEET_INDEX, description = "The number of the sheet in which the test data reside. Counting starts with 1 for the first sheet.")
	public void setSheet(String index) {
		checkIfMayAlterConfiguration();
		try {
			this.sheetIndex = Integer.parseInt(index) - 1;
		} catch(Exception e) {
			throw new IllegalArgumentException("Sheet Index must be a positive number");
		}
	}

	private void checkIfMayAlterConfiguration() {
		if(sheet != null) {
			throw new IllegalStateException("Data has already been loaded; must not alter settings anymore!");
		}
	}
}
