package net.bpelunit.utils.bpelstats.hertis;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Gatherer for calulating the Fan-In (FI)
 * 
 * @author dluebke
 * 
 */
public class FIGatherer extends DefaultHandler implements BPELMetric {

	private int fi = 0;
	private int pickLevel = 0;
	private boolean searchForOnMessageInCreateInstancePick = false;
	
	@Override
	public void startDocument() throws SAXException {
		fi = 0;
		pickLevel = 0;
		searchForOnMessageInCreateInstancePick = false;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(searchForOnMessageInCreateInstancePick) {
			pickLevel++;
		}
		
		if(BPELConstants.BPEL_NAMESPACE.equals(uri)) {
			if("receive".equals(localName) && "yes".equals(attributes.getValue("createInstance"))) {
				fi = 1;
			}
			
			if("pick".equals(localName) && "yes".equals(attributes.getValue("createInstance"))) {
				searchForOnMessageInCreateInstancePick = true;
				pickLevel = 1;
				fi = 0;
			}
			
			if(searchForOnMessageInCreateInstancePick && pickLevel == 2 && "onMessage".equals(localName)) {
				fi++;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(searchForOnMessageInCreateInstancePick) {
			pickLevel--;
		}
		
		if(pickLevel == 0) {
			searchForOnMessageInCreateInstancePick = false;
		}
	}
	
	public String getName() {
		return "FI";
	}

	public double getValue() {
		return fi;
	}

}
