package net.bpelunit.model.bpel.wsdl;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.bpelunit.model.bpel.BPELConstants;
import net.bpelunit.util.XMLUtil;

/**
 * WSDL4J extension element deserializer for BPEL4WS 1.1 partner link types.
 *
 * @author Antonio García-Domínguez
 */
class BPEL4WSPartnerLinkTypeDeserializer implements ExtensionDeserializer {

	@SuppressWarnings("rawtypes")
	@Override
	public ExtensibilityElement unmarshall(Class parentType, QName elementType,
			Element el, Definition def, ExtensionRegistry extReg)
			throws WSDLException
	{
		final String pltName = el.getAttribute("name");
		final QName pltQName = XMLUtil.resolveQNameTargetNS(pltName, el, def.getTargetNamespace());

		final BPELPartnerLinkType plt = new BPELPartnerLinkType(pltQName);
		final NodeList roles = el.getElementsByTagNameNS(BPELConstants.PLINK_NAMESPACE_BPEL4WS, "role");
		for (int iRole = 0; iRole < roles.getLength(); ++iRole) {
			final Element eRole = (Element)roles.item(iRole);
			final String name = eRole.getAttribute("name");

			final Element ePortType = (Element)eRole.getElementsByTagNameNS(BPELConstants.PLINK_NAMESPACE_BPEL4WS, "portType").item(0);
			final QName portTypeName = XMLUtil.resolveQNameTargetNS(ePortType.getAttribute("name"), ePortType, def.getTargetNamespace());
			plt.addRoleWithPortType(name, portTypeName);
		}

		return plt;
	}

}
