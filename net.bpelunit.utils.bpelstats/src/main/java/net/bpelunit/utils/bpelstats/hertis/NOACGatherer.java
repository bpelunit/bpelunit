package net.bpelunit.utils.bpelstats.hertis;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Gatherer for calulating the Number of Activities incl Control-Flow (NOAC),
 * i.e. the count of all basic and structured activities in a process
 * 
 * @author dluebke
 * 
 */
public class NOACGatherer extends DefaultHandler implements BPELMetric {

	private int noac = 0;

	@Override
	public void startDocument() throws SAXException {
		noac = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		QName elementName = new QName(uri, localName);

		if (BPELConstants.BASIC_ACTIVITIES.contains(elementName)
				|| BPELConstants.STRUCTURED_ACTIVITIES.contains(elementName)) {
			noac++;
		}
	}

	public String getName() {
		return "NOAC";
	}

	public double getValue() {
		return noac;
	}

}
