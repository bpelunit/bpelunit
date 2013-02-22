package net.bpelunit.framework.execution.endpointreplacement;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.execution.AbstractTestLifeCycleElement;
import net.bpelunit.framework.execution.IBPELUnitContext;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IProcess;

public class EndpointReplacer extends AbstractTestLifeCycleElement {

	@Override
	public void doPrepareProcesses(IBPELUnitContext context)
			throws DeploymentException {
		changeDeployment(context.getDeployment(), context.getTestSuite());
	}

	public void changeDeployment(IDeployment d, TestSuite testSuite)
			throws DeploymentException {
		List<String> partnerTrackNames = getPartnerTrackNames(testSuite);

		for (IBPELProcess p : d.getBPELProcesses()) {
			List<String> partnerLinkNames = getPartnerLinkNamesWithPartnerRole(p
					.getProcessModel());

			partnerLinkNames.retainAll(partnerTrackNames);

			for (String partnerLinkName : partnerLinkNames) {
				p.changePartnerEndpoint(partnerLinkName,
						getEndpointForPartnerTrack(testSuite, partnerLinkName));
			}
		}
	}

	private String getEndpointForPartnerTrack(TestSuite testSuite,
			String partnerLinkName) {
		return testSuite.getProcessUnderTest().getPartners()
				.get(partnerLinkName).getSimulatedURL();
	}

	private List<String> getPartnerLinkNamesWithPartnerRole(IProcess iProcess) {
		List<String> names = new ArrayList<String>();

		for (IPartnerLink pl : iProcess.getPartnerLinks()) {
			if (pl.getPartnerRole() != null && !pl.getPartnerRole().equals("")) {
				names.add(pl.getName());
			}
		}

		return names;
	}

	private List<String> getPartnerTrackNames(TestSuite testSuite) {
		List<String> partnerTrackNames = new ArrayList<String>();

		if (testSuite != null && testSuite.getProcessUnderTest() != null
				&& testSuite.getProcessUnderTest().getPartners() != null) {
			for (String name : testSuite.getProcessUnderTest().getPartners()
					.keySet()) {
				partnerTrackNames.add(name);
			}
		}

		return partnerTrackNames;
	}

}
