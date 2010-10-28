/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package net.bpelunit.framework.control.ext;

import javax.xml.namespace.QName;

/**
 * This class encapsulates the mapping between corresponding partner service
 * with the partnerlink. PartnerLink contains the service and additionally the
 * port within the service to which the partnerlink maps to.
 * 
 * @author chamith
 * 
 */
public class PartnerLink {

	private String name;

	private QName service;

	private String port;

	public PartnerLink(String name, QName service, String port) {
		this.name = name;
		this.service = service;
		this.port = port;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	void setService(QName service) {
		this.service = service;
	}

	public QName getService() {
		return service;
	}

	void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return port;
	}

}
