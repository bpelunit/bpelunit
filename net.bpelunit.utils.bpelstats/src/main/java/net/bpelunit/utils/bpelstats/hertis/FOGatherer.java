package net.bpelunit.utils.bpelstats.hertis;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Gatherer for calulating the Fan Out (FO)
 * 
 * @author dluebke
 * 
 */
public class FOGatherer extends DefaultHandler implements BPELMetric {

	private int fo = 0;
	
	@Override
	public void startDocument() throws SAXException {
		fo = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(BPELConstants.BPEL_NAMESPACE.equals(uri) && localName.equals("invoke")) {
			String operationName = attributes.getValue("operation").toLowerCase();
			if(!operationName.endsWith("callback") || !operationName.endsWith("failure")) {
				fo++;
			}
		}
	}

	public String getName() {
		return "FO";
	}

	public double getValue() {
		return fo;
	}

}
