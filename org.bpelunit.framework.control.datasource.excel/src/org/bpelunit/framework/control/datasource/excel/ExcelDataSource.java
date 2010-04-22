package org.bpelunit.framework.control.datasource.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bpelunit.framework.control.ext.IDataSource;
import org.bpelunit.framework.control.ext.IDataSource.DataSource;
import org.bpelunit.framework.exception.DataSourceException;

/**
 * A data source that can read Excel files (incl .xlsx from Excel 2007).
 * 
 * Following restrictions apply to the Excel files:
 * <ul>
 * <li>The first used row must contain the header (that will become the variable
 * name in the context)
 * <li>No merged cells</li>
 * </ul>
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
@DataSource(name = "Excel Data Source", shortName = "Excel", contentTypes = {
		"application/excel", "application/vnd.ms-excel", "application/x-excel",
		"application/x-msexcel",
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
public class ExcelDataSource implements IDataSource {

	private static final int DEFAULT_SHEET_INDEX = 0;

	private Sheet sheet;
	private List<String> headings;
	private final int headingRowIndex = 0;
	private final int startDataRowIndex = 1;
	private int currentDataRow = -1;
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
		Row row = sheet.getRow(startDataRowIndex + currentDataRow);
		int cellIndex = firstCellIndex + headings.indexOf(fieldName);

		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return "";
		} else {
			return cell.getStringCellValue();
		}
	}

	@Override
	public void setRow(int index) throws DataSourceException {
		if (index < getNumberOfRows()
				&& sheet.getRow(startDataRowIndex + index) != null) {
			currentDataRow = index;
		} else {
			throw new DataSourceException(String.format(
					"Index %d out of bounds [0, %d]", index, getNumberOfRows()));
		}
	}

	@Override
	public void loadFromStream(InputStream data) throws DataSourceException {
		Workbook workbook = readWorkbook(data);
		sheet = getSheetWithTestData(workbook);
		extractFieldNames();
	}

	protected Workbook readWorkbook(InputStream data)
			throws DataSourceException {
		try {
			Workbook wb = WorkbookFactory.create(data);
			return wb;
		} catch (InvalidFormatException e) {
			throw new DataSourceException(
					"The data source is not a valid MS Excel File!", e);
		} catch (FileNotFoundException e) {
			throw new DataSourceException("The data source does not exist!", e);
		} catch (IOException e) {
			throw new DataSourceException("The data source could not be read!",
					e);
		}
	}

	private Sheet getSheetWithTestData(Workbook workbook)
			throws DataSourceException {
		try {
			return workbook.getSheetAt(sheetIndex);
		} catch (IllegalArgumentException e) {
			throw new DataSourceException("Could not find sheet with index "
					+ sheetIndex);
		}
	}

	private void extractFieldNames() throws DataSourceException {
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
			throws DataSourceException {
		if (o == null) {
			throw new DataSourceException(message);
		}
	}

	@ConfigurationOption(defaultValue = "" + DEFAULT_SHEET_INDEX, description = "The number of the sheet in which the test data reside. Counting starts with 1 for the first sheet.")
	public void setSheet(String index) {
		checkIfMayAlterConfiguration();
		try {
			this.sheetIndex = Integer.parseInt(index) - 1;
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Sheet Index must be a positive number");
		}
	}

	private void checkIfMayAlterConfiguration() {
		if (sheet != null) {
			throw new IllegalStateException(
					"Data has already been loaded; must not alter settings anymore!");
		}
	}

	@Override
	public int getNumberOfRows() {
		return this.sheet.getLastRowNum();
	}
}