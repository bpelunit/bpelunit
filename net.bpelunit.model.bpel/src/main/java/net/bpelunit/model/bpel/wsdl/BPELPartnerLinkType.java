package net.bpelunit.model.bpel.wsdl;

import java.util.HashMap;
import java.util.Map;

import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.BPELConstants;

/**
 * This class holds the information about a partner link type.
 */
public class BPELPartnerLinkType implements ExtensibilityElement {
	public static final QName TAG = new QName(BPELConstants.PLINK_NAMESPACE_WSBPEL2, "partnerLinkType");
	public static final QName TAG_BPEL4WS = new QName(BPELConstants.PLINK_NAMESPACE_BPEL4WS, "partnerLinkType");

	private QName name;
	private Map<String, QName> portTypeByRole = new HashMap<String, QName>();

	public BPELPartnerLinkType(QName name) {
		this.name = name;
	}

	/**
	 * Returns the qualified name of the partner link type.
	 */
	public QName getName() {
		return name;
	}

	/**
	 * Returns a map from the roles associated with the partner to the names of the associated port types.
	 */
	public Map<String, QName> getPortTypesByRole() {
		return portTypeByRole;
	}

	/**
	 * Adds a certain role to this partner link type.
	 */
	public void addRoleWithPortType(String name, QName portTypeName) {
		portTypeByRole.put(name, portTypeName);
	}

	@Override
	public void setElementType(QName elementType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public QName getElementType() {
		return TAG;
	}

	@Override
	public void setRequired(Boolean required) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean getRequired() {
		return false;
	}
}
