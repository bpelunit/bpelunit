package net.bpelunit.bpel._2_0;

import javax.xml.namespace.QName;

import net.bpelunit.bpel.IPartnerLink;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TPartnerLink;

public class PartnerLink implements IPartnerLink {

	private TPartnerLink partnerLink;

	public PartnerLink(TPartnerLink wrappedPartnerLink) {
		this.partnerLink = wrappedPartnerLink;
	}

	@Override
	public String getPartnerRole() {
		return partnerLink.getPartnerRole();
	}

	@Override
	public String getName() {
		return partnerLink.getName();
	}

	@Override
	public void setName(String newName) {
		partnerLink.setName(newName);
	}

	@Override
	public QName getPartnerLinkType() {
		return partnerLink.getPartnerLinkType();
	}

	@Override
	public void setPartnerLinkType(QName value) {
		partnerLink.setPartnerLinkType(value);
	}

	@Override
	public String getMyRole() {
		return partnerLink.getMyRole();
	}

	@Override
	public void setMyRole(String value) {
		partnerLink.setMyRole(value);
	}

	@Override
	public void setPartnerRole(String newPartnerRole) {
		partnerLink.setPartnerRole(newPartnerRole);
	}
}
