package net.bpelunit.utils.bpelstats.hertis;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Gatherer for calulating the Nested Depth (NP)
 * 
 * @author dluebke
 * 
 */
public class NDGatherer extends DefaultHandler implements BPELMetric {

	private int np = 0;
	private int currentLevel = 0;
	
	@Override
	public void startDocument() throws SAXException {
		np = 0;
		currentLevel = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		QName elementName = new QName(uri, localName);

		if(BPELConstants.STRUCTURED_ACTIVITIES.contains(elementName)) {
			currentLevel++;
		}
		
		if (BPELConstants.BASIC_ACTIVITIES.contains(elementName)) {
			np = Math.max(np, currentLevel);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		QName elementName = new QName(uri, localName);

		if(BPELConstants.STRUCTURED_ACTIVITIES.contains(elementName)) {
			currentLevel--;
		}
	}
	
	public String getName() {
		return "ND";
	}

	public double getValue() {
		return np;
	}

}
