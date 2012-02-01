package net.bpelunit.framework.control.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.jdom.input.DOMBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Element;

/**
 * Helper tool to pretty print DOM elements back into XML. Useful
 * when we need to repeat part of a request in the response we have
 * to generate: it can significantly shorten templates in some
 * cases.
 * 
 * @author Antonio García-Domínguez
 */
public class XMLPrinterTool {

	public XMLPrinterTool() {}

	public String print(Element e) throws IOException {
		final org.jdom.Element jdomElem = new DOMBuilder().build(e);
		final Writer sw = new StringWriter();
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(jdomElem, sw);
		return sw.toString();
	}
}
