package net.bpelunit.framework.model.bpel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BPELProcess extends BPELActivity {

	private String namespaceURI;
	private Map<Element, BPELActivity> activities = new HashMap<Element, BPELActivity>();
	private BPELActivityFactory factory = new BPELActivityFactory();
	
	public BPELProcess(Element element) {
		super(element, null);
		this.setProcess(this);
		this.namespaceURI = element.getNamespaceURI();
		this.activities.put(element, this);
	}
	
	public List<PartnerLink> getPartnerLinks() {
		List<PartnerLink> partnerLinks = new ArrayList<PartnerLink>();
		
		NodeList partnerLinkElements = getElement().getElementsByTagNameNS(namespaceURI, "partnerLink");
		
		for(int i = 0; i < partnerLinkElements.getLength(); i++) {
			partnerLinks.add(new PartnerLink((Element)partnerLinkElements.item(i)));
		}
		
		return partnerLinks;
	}

	BPELActivity getActivityForElement(Node node) {
		BPELActivity bpelActivity = activities.get(node);
		
		if(bpelActivity == null) {
			bpelActivity = factory.createActivityFor(node.getLocalName(), (Element)node, this);
		}
		
		return bpelActivity;
	}
}
