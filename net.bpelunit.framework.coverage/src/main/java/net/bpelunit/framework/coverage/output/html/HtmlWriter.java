package net.bpelunit.framework.coverage.output.html;

import java.io.IOException;
import java.io.Writer;

public class HtmlWriter extends Writer {

	private Writer writer;
	
	public HtmlWriter(Writer w) throws IOException {
		writer = w;
		writer.write("<html>");
	}

	public HtmlWriter startHead() throws IOException {
		writer.write("<head>");
		return this;
	}
	
	public HtmlWriter endHead() throws IOException {
		writer.write("</head>");
		return this;
	}
	
	public HtmlWriter title(String title) throws IOException {
		return elementWithText("title", title);
	}
	
	public HtmlWriter startBody() throws IOException {
		writer.write("<body>");
		return this;
	}
	
	public HtmlWriter endBody() throws IOException {
		writer.write("</body>");
		return this;
	}
	
	@Override
	public void close() throws IOException {
		writer.write("</html>");
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		writer.write(arg0, arg1, arg2);
	}

	public HtmlWriter h1(String headingText) throws IOException {
		return elementWithText("h1", headingText);
	}
	
	public HtmlWriter h2(String headingText) throws IOException {
		return elementWithText("h2", headingText);
	}

	public HtmlWriter h3(String headingText) throws IOException {
		return elementWithText("h3", headingText);
	}
	
	public HtmlWriter h4(String headingText) throws IOException {
		return elementWithText("h4", headingText);
	}
	
	public HtmlWriter h5(String headingText) throws IOException {
		return elementWithText("h5", headingText);
	}
	
	public HtmlWriter h6(String headingText) throws IOException {
		return elementWithText("h6", headingText);
	}
	
	public HtmlWriter p(String text) throws IOException {
		return elementWithText("p", text);
	}
	
	public HtmlWriter th(String text) throws IOException {
		return elementWithText("th", text);
	}
	
	public HtmlWriter td(String text) throws IOException {
		return elementWithText("td", text);
	}

	private HtmlWriter elementWithText(String tagName, String enclosedText) throws IOException {
		writer.write(String.format("<%s>%s</%s>", tagName, enclosedText, tagName));
		return this;
	}

	public HtmlWriter startTable() throws IOException {
		writer.write("<table>");
		return this;
	}
	
	public HtmlWriter endTable() throws IOException {
		writer.write("</table>");
		return this;
	}

	public HtmlWriter startTr() throws IOException {
		writer.write("<tr>");
		return this;
	}
	
	public HtmlWriter endTr() throws IOException {
		writer.write("</tr>");
		return this;
	}

	public HtmlWriter startTd() throws IOException {
		writer.write("<td>");
		return this;
	}
	
	public HtmlWriter endTd() throws IOException {
		writer.write("</td>");
		return this;
	}

	public HtmlWriter td(double min) throws IOException {
		return td("" + min);
	}

	public HtmlWriter stylesheet(String stylesheet) throws IOException {
		if(stylesheet != null && !stylesheet.equals("")) {
			writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + stylesheet + "\" media=\"all\">");
		}
		return this;
	}
}
