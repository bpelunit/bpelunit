package net.bpelunit.framework.control.datasource.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.DataSource;
import net.bpelunit.framework.exception.DataSourceException;

/**
 * <ul>
 * <li>First row must be heading
 * <li>No closing tag must be there that has not an opening tag
 * <li>No rowspan/colspan
 * </ul>
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
@DataSource(name="HTML Data Source", shortName="html", contentTypes={"text/html", "application/xhtml+xml", "application/xhtml+xml"})
public class HtmlDataSource implements IDataSource {

	private final class DataSourceHtmlParser extends ParserCallback {
		private int tablesToWaitFor = tableToUse;
		private List<String> currentRow = null;
		private StringBuilder cellValue;
		private boolean isInCell = false;

		@Override
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			if (t == Tag.TABLE) {
				tablesToWaitFor--;
			}

			if (tablesToWaitFor == 0) {
				if (t == Tag.TR) {
					currentRow = new ArrayList<String>();
					data.add(currentRow);
				}

				if (t == Tag.TD || t == Tag.TH) {
					if (a.getAttribute(HTML.Attribute.COLSPAN) != null
							|| a.getAttribute(HTML.Attribute.ROWSPAN) != null) {
						hadRowOrColSpan = true;
					}
					cellValue = new StringBuilder();
					isInCell = true;
				}
			}
		}

		@Override
		public void handleEndTag(Tag t, int pos) {
			if (isInCell && (t == Tag.TD || t == Tag.TH)) {
				isInCell = false;
				currentRow.add(cellValue.toString());
				cellValue = null;
			}
		}

		@Override
		public void handleText(char[] data, int pos) {
			if (isInCell) {
				cellValue.append(data);
			}
		}
	}

	private static class HTMLParse extends HTMLEditorKit {

		private static final long serialVersionUID = 1L;

		public HTMLEditorKit.Parser getParserPublic() {
			return super.getParser();
		}
	}

	private int tableToUse = 1;
	private List<String> headings;
	private List<List<String>> data;
	private List<String> currentRow;
	private boolean hadRowOrColSpan = false;

	@Override
	public void close() {
		this.data = null;
		this.headings = null;
		this.currentRow = null;
	}

	@Override
	public String[] getFieldNames() {
		return headings.toArray(new String[0]);
	}

	@Override
	public int getNumberOfRows() {
		return data.size();
	}

	@Override
	public Object getValueFor(String fieldName) {
		int fieldIndex = this.headings.indexOf(fieldName);

		if (fieldIndex < 0) {
			throw new IllegalArgumentException("No row named \"" + fieldName
					+ "\" found in HTML table");
		}

		try {
			return currentRow.get(fieldIndex);
		} catch (IndexOutOfBoundsException e) {
			// Unbalanced row. There should be such a field but isn't
			return "";
		}
	}

	@Override
	public void loadFromStream(InputStream is) throws DataSourceException {
		Parser parser = new HTMLParse().getParserPublic();

		data = new ArrayList<List<String>>();

		ParserCallback callback = new DataSourceHtmlParser();
		try {
			parser.parse(new InputStreamReader(is), callback, true);
			is.close();
		} catch (IOException e) {
			throw new DataSourceException("Error while reading HTML: "
					+ e.getMessage(), e);
		}

		if (hadRowOrColSpan) {
			throw new DataSourceException(
					"The input table in the HTML file had rowspan or colspan attributes. These are not allowed for a data source!");
		}

		if (data.size() == 0) {
			throw new DataSourceException("No table data found in HTML file!");
		}

		this.headings = data.remove(0);
	}

	@Override
	public void setRow(int index) throws DataSourceException {
		if (index >= getNumberOfRows()) {
			throw new DataSourceException(String.format(
					"Index %d out of bounds [0, %d]", index,
					getNumberOfRows() - 1));
		}
		currentRow = data.get(index);
	}

	@ConfigurationOption(defaultValue = "1", description = "The index of the table in the HTML file in which the data is contained")
	public void setTable(String value) {
		checkIfConfigurationMayBeChanged();

		int index = Integer.parseInt(value);
		tableToUse = index;
	}

	private void checkIfConfigurationMayBeChanged() {
		if (headings != null) {
			throw new IllegalStateException(
					"Data has already been read. Configuration must be changed anymore!");
		}
	}

}