package net.bpelunit.framework.control.deploy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IProcess;

public class DeploymentMock implements IDeployment {

	public static class BPELProcessMock implements IBPELProcess {

		private QName name;
		private IProcess bpel;
		private Map<String, String> changedEndpoints = new HashMap<String, String>();

		public BPELProcessMock(IProcess bpelprocess) {
			this.bpel = bpelprocess;

			this.name = new QName(bpelprocess.getTargetNamespace(),
					bpelprocess.getName());
		}

		@Override
		public void addPartnerlink(String name, QName partnerlinkType,
				String processRole, String partnerRole, QName service,
				String port, String endpointURL) {
		}

		@Override
		public void addWSDLImport(String wsdlFileName, String namespace, InputStream contents) {
			// not used in test
		}

		@Override
		public QName getName() {
			return this.name;
		}

		@Override
		public IProcess getProcessModel() {
			return this.bpel;
		}

		@Override
		public void changePartnerEndpoint(String partnerLinkName,
				String newEndpoint) throws DeploymentException {
			this.getChangedEndpoints().put(partnerLinkName, newEndpoint);
		}

		public Map<String, String> getChangedEndpoints() {
			return changedEndpoints;
		}

		@Override
		public void addXSDImport(String wsdlFileName, String namespace, InputStream contents) {
			// not used in test
		}
	}

	private List<BPELProcessMock> processes = new ArrayList<BPELProcessMock>();

	public DeploymentMock(String... resourceNames) throws IOException,
			JAXBException {
		if (resourceNames == null) {
			return;
		}

		for (String resourceName : resourceNames) {
			InputStream r = getClass().getResourceAsStream(resourceName);
			IProcess process = BpelFactory.loadProcess(r);

			processes.add(new DeploymentMock.BPELProcessMock(process));
		}
	}

	@Override
	public List<? extends BPELProcessMock> getBPELProcesses()
			throws DeploymentException {
		return processes;
	}
}