package net.bpelunit.util.bpel;

import org.w3c.dom.Element;

public class PartnerLink {

	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_PARTNERROLE = "partnerRole";
	
	private Element partnerLinkElement;
	
	public PartnerLink(Element partnerLink) {
		this.partnerLinkElement = partnerLink;
	}
	
	public String getName() {
		return partnerLinkElement.getAttribute(ATTRIBUTE_NAME);
	}
	
	public String getPartnerRole() {
		return partnerLinkElement.getAttribute(ATTRIBUTE_PARTNERROLE);
	}
}
