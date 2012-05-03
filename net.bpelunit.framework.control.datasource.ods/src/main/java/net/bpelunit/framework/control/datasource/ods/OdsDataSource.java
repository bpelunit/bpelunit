package net.bpelunit.framework.control.datasource.ods;

import java.util.ArrayList;
import java.io.InputStream;
import java.util.List;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.DataSource;
import net.bpelunit.framework.exception.DataSourceException;

import org.apache.commons.lang.StringUtils;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

/**
 * A data source for accessing Open Document Spreadsheet (ODS) compliant files
 * (OpenOffice.org, KOffice, etc.). ODS is an official ISO standard, developed
 * in an open manner and implemented in several office productivity products.
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
@DataSource(name = "ODS Data Source", shortName = "ods", contentTypes = {
		"application/vnd.oasis.opendocument.spreadsheet",
		"application/x-vnd.oasis.opendocument.spreadsheet" })
public class OdsDataSource implements IDataSource {

	private static final int DEFAULT_SHEET_INDEX = 0;
	private static final int MAX_CELL_SEARCH_DEPTH = 10;
	private OdfTable table;
	private int headingRowIndex;
	private List<String> headings;
	private int sheetIndex;
	private int firstCellIndex;
	private int currentRowIndex = headingRowIndex;
	private OdfTableRow currentRow;
	private int lastRow;

	@ConfigurationOption(defaultValue = "" + DEFAULT_SHEET_INDEX, description = "The number of the sheet in which the test data reside. Counting starts with 1 for the first sheet.")
	public void setSheet(String index) {
		checkIfMayAlterConfiguration();
		try {
			int currentSheetIndex = Integer.parseInt(index) - 1;
			if (currentSheetIndex < 0) {
				throw new IllegalArgumentException(
						"Sheet Index must be a positive number but was " + currentSheetIndex);
			}
			this.sheetIndex = currentSheetIndex;
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Sheet Index must be a positive number", e);
		}
	}

	@Override
	public void close() {
		// nothing to do
	}

	@Override
	public String[] getFieldNames() {
		return headings.toArray(new String[headings.size()]);
	}

	@Override
	public String getValueFor(String fieldName) {
		int cellIndex = headings.indexOf(fieldName);
		cellIndex += firstCellIndex;
		return currentRow.getCellByIndex(cellIndex).getStringValue();
	}

	@Override
	public void setRow(int index) throws DataSourceException {
		if (index < getNumberOfRows()) {
			currentRowIndex = headingRowIndex + 1 + index;
			currentRow = table.getRowByIndex(currentRowIndex);
		} else {
			throw new DataSourceException(String.format(
					"Index %d out of bounds [0, %d]", index, lastRow - 1));
		}
	}

	@Override
	public void loadFromStream(InputStream data) throws DataSourceException {
		OdfDocument ods;
		try {
			ods = OdfSpreadsheetDocument.loadDocument(data);
		} catch (Exception e) {
			throw new DataSourceException(
					"Error while reading the ODS data source", e);
		}
		table = ods.getTableList().get(sheetIndex);
		extractFieldNames();
		calculateRowCount();
	}

	private void calculateRowCount() {
		this.lastRow = this.headingRowIndex;

		while (isDataRow(this.lastRow + 1)) {
			this.lastRow++;
		}
	}

	private boolean isDataRow(int lastRow) {
		return !StringUtils.isEmpty(this.table.getCellByPosition(
				this.firstCellIndex, lastRow).getStringValue());
	}

	private void extractFieldNames() throws DataSourceException {
		headings = new ArrayList<String>();
		OdfTableRow headingRow = table.getRowByIndex(headingRowIndex);

		findFirstCellIndex(headingRow);

		if (firstCellIndex < 0) {
			throw new DataSourceException("No headings found at row "
					+ headingRowIndex);
		}

		int i = firstCellIndex;
		do {
			String heading = headingRow.getCellByIndex(i).getStringValue();
			headings.add(heading);
			i++;
		} while (!StringUtils.isEmpty(headingRow.getCellByIndex(i)
				.getStringValue()));

		this.currentRowIndex = this.headingRowIndex;
	}

	private void findFirstCellIndex(OdfTableRow headingRow) {
		firstCellIndex = 0;
		while (StringUtils.isEmpty(headingRow.getCellByIndex(firstCellIndex)
				.getStringValue())) {
			if (firstCellIndex > MAX_CELL_SEARCH_DEPTH) {
				firstCellIndex = -1;
				return;
			}

			firstCellIndex++;
		}
	}

	private void checkIfMayAlterConfiguration() {
		if (table != null) {
			throw new IllegalStateException(
					"Data has already been loaded; must not alter settings anymore!");
		}
	}

	@Override
	public int getNumberOfRows() {
		return (this.lastRow - this.headingRowIndex);
	}
}