/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class XMLUtil {

	private XMLUtil() {
		// utility class
	}
	
	/**
	 * @param xmlAsString document in string form, encoding specified in XML should be UTF-8
	 * @return
	 * @throws SAXException
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParserConfigurationException 
	 */
	public static Document parseXML(String xmlAsString)
			throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(new ByteArrayInputStream(xmlAsString
					.getBytes("UTF-8")));
	}
	
	public static Document parseXML(InputStream in) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(in);
	}

	public static void writeXML(Document xml, File file) throws IOException, TransformerException {
		writeXML(xml, new FileOutputStream(file));
	}

	public static void writeXML(Document xml, OutputStream outputStream) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.transform(new DOMSource(xml), new StreamResult(outputStream));
	}

}
