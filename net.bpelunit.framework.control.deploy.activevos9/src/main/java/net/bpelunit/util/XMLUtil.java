package net.bpelunit.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLUtil {

	/**
	 * @param xmlAsString document in string form, encoding specified in XML should be UTF-8
	 * @return
	 * @throws SAXException
	 */
	public static Document parseXML(String xmlAsString)
			throws SAXException {
		try {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(new ByteArrayInputStream(xmlAsString
					.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected internal error: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected internal error: " + e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Unexpected internal error: " + e.getMessage(), e);
		}
	}
	
	public static Document parseXML(InputStream in)
	throws SAXException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(in);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected internal error: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected internal error: " + e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Unexpected internal error: " + e.getMessage(), e);
		}
	}

	public static void writeXML(Document xml, File file) throws IOException {
		writeXML(xml, new FileOutputStream(file));
	}

	public static void writeXML(Document xml, OutputStream fileOutputStream) {
		System.out.println(xml.toString());
		// TODO Auto-generated method stub
	}

}
