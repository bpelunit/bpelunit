/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package org.bpelunit.framework.control.deploy.helpers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.bpelunit.framework.control.util.ParseUtil;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.Partner;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import de.schlichtherle.io.File;

/**
 * @author chamith
 * 
 */
public class ODEDeployment extends GenericDeployment {

	private static final String DESCRIPTOR = "deploy.xml";
	private static final String PROCESS_ELEMENT = "process";
	private static final String INVOKE_ATTR = "invoke";
	private static final String NAME_ATTR = "name";
	private static final String PARTNERLINK_ATTR = "partnerLink";
	private static final String PORT_ATTR = "port";

	private Document fDescriptorDocument;

	public ODEDeployment(Partner[] partners, String archive)
			throws DeploymentException {
		super(partners, archive);
		this.fDescriptorDocument = getDescriptorDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bpelunit.framework.control.deploy.ode.IDeployment#addLoggingService()
	 */
	public void addLoggingService() {
		// TODO Auto-generated method stub

	}

	// ************* IDeployment Implementation method *********************

	/**
	 * This method gets the details of the partnerlinks defined by the process.
	 * Details include the service to which a particular partnerlink map to and
	 * additionally the port within the service, if enough details are present
	 * to determine it by parsing the ODE deployment descriptor deploy.xml.
	 */
	public PartnerLink[] getPartnerLinks() {
		ArrayList<PartnerLink> links = new ArrayList<PartnerLink>();
		Element envelope = fDescriptorDocument.getRootElement();

		Iterator<Element> processes = envelope
				.getDescendants(new ElementFilter(PROCESS_ELEMENT));

		while (processes.hasNext()) {
			Element process = processes.next();
			String processName = process.getAttributeValue(NAME_ATTR);

			if (processName.contains(getProcessUnderTest().getName())) {
				Iterator<Element> invokes = process
						.getDescendants(new ElementFilter(INVOKE_ATTR));

				while (invokes.hasNext()) {
					Element invoke = invokes.next();
					Element serviceElement = (Element) invoke.getChildren()
							.iterator().next();
					String serviceName = serviceElement
							.getAttributeValue(NAME_ATTR);
					QName service = extractQName(serviceName, serviceElement);
					String port = serviceElement.getAttributeValue(PORT_ATTR);
					String partnerLink = invoke
							.getAttributeValue(PARTNERLINK_ATTR);

					PartnerLink pl = new PartnerLink(partnerLink, service, port);
					links.add(pl);
				}

				return links.toArray(new PartnerLink[] {});
			}
		}

		return null;
	}

	// ******************** Private helper methods ****************************

	private Document getDescriptorDocument() throws DeploymentException {
		File archive = new File(getArchive());
		Document document = null;

		for (File file : (File[]) archive.listFiles()) {
			if (file.getName().equals(DESCRIPTOR)) {
				try {
					document = ParseUtil
							.getJDOMDocument(file.getAbsolutePath());
				} catch (IOException e) {
					throw new DeploymentException(
							"Error while reading deployment descriptor from file \""
									+ file.getAbsolutePath() + "\".", e);
				}

				break;
			}
		}

		return document;
	}

	private QName extractQName(String serviceName, Element service) {
		final int NS_URI = 0;
		final int LOCALNAME = 1;

		String tokens[];

		if (serviceName.contains(":")) {
			tokens = serviceName.split(":");

			if (isUri(tokens[NS_URI]) && !isPrefix(tokens[NS_URI], service)) {
				return new QName(tokens[NS_URI], tokens[LOCALNAME]);
			} else {
				String namespace = getPrefixValue(tokens[NS_URI], service);
				return new QName(namespace, tokens[LOCALNAME]);
			}
		}

		return new QName(null, serviceName);
	}

	private boolean isUri(String uriStr) {
		try {
			new URI(uriStr);
		} catch (URISyntaxException e) {
			return false;
		}

		return true;
	}

	private boolean isPrefix(String uriStr, Element service) {

		if (service != null && service.getNamespace(uriStr) != null) {
			return true;
		}
		return false;
	}

	private String getPrefixValue(String prefix, Element service) {

		if (service != null && service.getNamespace(prefix) != null) {
			return service.getNamespace(prefix).getURI();
		}
		return null;
	}

}
