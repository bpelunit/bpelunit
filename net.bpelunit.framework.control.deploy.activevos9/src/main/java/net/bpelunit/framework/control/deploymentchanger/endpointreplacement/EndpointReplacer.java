package net.bpelunit.framework.control.deploymentchanger.endpointreplacement;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.control.ext.IDeploymentChanger;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.util.bpel.BPELFacade;
import net.bpelunit.util.bpel.PartnerLink;

import org.w3c.dom.Element;

public class EndpointReplacer implements IDeploymentChanger {

	@Override
	public void changeDeployment(IDeployment d, TestSuite testSuite) throws DeploymentException {
		List<String> partnerTrackNames = getPartnerTrackNames(testSuite);
		
		for(IBPELProcess p : d.getBPELProcesses()) {
			List<String> partnerLinkNames = getPartnerLinkNamesWithPartnerRole(p.getBpelXml().getDocumentElement());
		
			partnerLinkNames.retainAll(partnerTrackNames);
			
			for(String partnerLinkName : partnerLinkNames) {
				p.changePartnerEndpoint(partnerLinkName, getEndpointForPartnerTrack(testSuite, partnerLinkName));
			}
		}
	}

	private String getEndpointForPartnerTrack(TestSuite testSuite,
			String partnerLinkName) {
		return testSuite.getProcessUnderTest().getPartners().get(partnerLinkName).getSimulatedURL();
	} 

	private List<String> getPartnerLinkNamesWithPartnerRole(Element processElement) {
		List<String> names = new ArrayList<String>();
		
		BPELFacade bpel = BPELFacade.getInstance(processElement.getNamespaceURI());
		List<PartnerLink> partnerLinks =  bpel.getPartnerLinks(processElement);
		
		for(PartnerLink pl : partnerLinks) {
			if(pl.getPartnerRole() != null && !pl.getPartnerRole().equals("")) {
				names.add(pl.getName());
			}
		}
		
		return names;
	}

	private List<String> getPartnerTrackNames(TestSuite testSuite) {
		List<String> partnerTrackNames = new ArrayList<String>();
		
		if(testSuite != null && testSuite.getProcessUnderTest() != null && testSuite.getProcessUnderTest().getPartners() != null) {
			for(String name : testSuite.getProcessUnderTest().getPartners().keySet()) {
				partnerTrackNames.add(name);
			}
		}
		
		return partnerTrackNames;
	}

}
