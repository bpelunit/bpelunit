package net.bpelunit.utils.bpelstats.hertis;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Gatherer for calulating the Number of Activities (NOA), i.e. the count of all
 * basic activities in a process
 * 
 * @author dluebke
 * 
 */
public class NOAGatherer extends DefaultHandler implements BPELMetric {

	private int noa = 0;

	@Override
	public void startDocument() throws SAXException {
		noa = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		QName elementName = new QName(uri, localName);

		if (BPELConstants.BASIC_ACTIVITIES.contains(elementName)) {
			noa++;
		}
	}

	public String getName() {
		return "NOA";
	}

	public double getValue() {
		return noa;
	}

}
