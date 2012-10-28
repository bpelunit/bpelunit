package net.bpelunit.model.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IPartnerLink;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;

public class PartnerLink extends AbstractBpelObject implements IPartnerLink {

	private TPartnerLink partnerLink;

	public PartnerLink(TPartnerLink wrappedPartnerLink) {
		super(wrappedPartnerLink);
		this.partnerLink = wrappedPartnerLink;
	}

	public String getPartnerRole() {
		return partnerLink.getPartnerRole();
	}

	public String getName() {
		return partnerLink.getName();
	}

	public void setName(String newName) {
		partnerLink.setName(newName);
	}

	public QName getPartnerLinkType() {
		return partnerLink.getPartnerLinkType();
	}

	public void setPartnerLinkType(QName value) {
		partnerLink.setPartnerLinkType(value);
	}

	public String getMyRole() {
		return partnerLink.getMyRole();
	}

	public void setMyRole(String value) {
		partnerLink.setMyRole(value);
	}

	public void setPartnerRole(String newPartnerRole) {
		partnerLink.setPartnerRole(newPartnerRole);
	}
}
