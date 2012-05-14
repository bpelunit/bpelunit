package net.bpelunit.framework.control.deploy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DeploymentMock implements IDeployment {

	public static class BPELProcessMock implements IBPELProcess {
	
		private QName name;
		private Document bpel;
		private Map<String, String> changedEndpoints = new HashMap<String, String>();
	
		public BPELProcessMock(Document bpelXml) {
			this.bpel = bpelXml;
	
			Element processElement = bpelXml.getDocumentElement();
			this.name = new QName(
					processElement.getAttribute("targetNamespace"),
					processElement.getAttribute("name"));
		}
	
		@Override
		public void addPartnerlink(String name, QName partnerlinkType,
				String processRole, String partnerRole, QName service,
				String port, String endpointURL) {
		}
	
		@Override
		public void addWSDLImport(String wsdlFileName, InputStream contents) {
			// not used in test
		}
	
		@Override
		public QName getName() {
			return this.name;
		}
	
		@Override
		public Document getBpelXml() {
			return this.bpel;
		}
	
		@Override
		public void changePartnerEndpoint(String partnerLinkName,
				String newEndpoint) throws DeploymentException {
			this.getChangedEndpoints() .put(partnerLinkName, newEndpoint);
		}

		public Map<String, String> getChangedEndpoints() {
			return changedEndpoints;
		}
	}

	private List<BPELProcessMock> processes = new ArrayList<BPELProcessMock>();

	public DeploymentMock(String... resourceNames) throws SAXException,
			IOException, ParserConfigurationException {
		if (resourceNames == null) {
			return;
		}

		for (String resourceName : resourceNames) {
			InputStream r = getClass().getResourceAsStream(resourceName);
			Document bpelXml = XMLUtil.parseXML(r);

			processes.add(new DeploymentMock.BPELProcessMock(bpelXml));
		}
	}

	@Override
	public List<? extends BPELProcessMock> getBPELProcesses()
			throws DeploymentException {
		return processes;
	}
}