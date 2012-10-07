package net.bpelunit.framework.control.deploy.activevos9;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import net.bpelunit.framework.exception.DeploymentException;

import org.w3.x2005.x08.addressing.EndpointReferenceType;
import org.w3.x2005.x08.addressing.MetadataType;
import org.w3.x2005.x08.addressing.ServiceNameType;

import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.MyRoleType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.PartnerLinkType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.PartnerRoleType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.ProcessDocument;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.ProcessType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.SchemaType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.WsdlType;

public class ActiveVOSDeploymentDescriptor {

	private ProcessDocument pddDoc;
	private File pddFile;
	ProcessType pdd;

	public ActiveVOSDeploymentDescriptor(File file) throws DeploymentException {
		try {
			this.pddFile = file;
			this.pddDoc = ProcessDocument.Factory.parse(pddFile);
			this.pdd = pddDoc.getProcess(); 
		} catch (Exception e) {
			throw new DeploymentException("Cannot parse Deployment Descriptor "
					+ pddFile.getName(), e);
		}
	}

	public void addPartnerLink(String name, String partnerEndpoint,
			String partnerPortName, QName partnerService, String myEndPoint) {
		if (pdd.getPartnerLinks() == null) {
			pdd.addNewPartnerLinks();
		}

		PartnerLinkType newPl = pdd.getPartnerLinks().addNewPartnerLink();
		newPl.setName(name);

		if (partnerEndpoint != null && partnerService != null
				&& partnerPortName != null) {
			PartnerRoleType partnerRole = newPl.addNewPartnerRole();
			partnerRole.setInvokeHandler("default:Address");
			partnerRole.setEndpointReference2("static");
			EndpointReferenceType endPointReference = partnerRole
					.addNewEndpointReference();
			endPointReference.setAddress(partnerEndpoint);
			MetadataType metadata = endPointReference.addNewMetadata();
			ServiceNameType serviceName = metadata.addNewServiceName();
			serviceName.setPortName(partnerPortName);
			serviceName.setQNameValue(partnerService);
		}

		if (myEndPoint != null) {
			MyRoleType myRole = newPl.addNewMyRole();
			myRole.setBinding("MSG");
			myRole.setService(myEndPoint);
		}
	}

	public void addWsdlImport(String locationInDeployment, String wsdlNamespace) {
		if(pdd.getReferences() == null) {
			pdd.addNewReferences();
		}
		
		WsdlType wsdlRef = pdd.getReferences().addNewWsdl();
		wsdlRef.setLocation(locationInDeployment);
		wsdlRef.setNamespace(wsdlNamespace);
	}
	
	public void addXsdImport(String locationInDeployment, String xsdNamespace) {
		if(pdd.getReferences() == null) {
			pdd.addNewReferences();
		}
		
		SchemaType xsdRef = pdd.getReferences().addNewSchema();
		xsdRef.setLocation(locationInDeployment);
		xsdRef.setNamespace(xsdNamespace);
	}
	
	public void save() throws DeploymentException {
		try {
			pddDoc.save(pddFile, null);
		} catch (IOException e) {
			throw new DeploymentException("Error while saving changes to " + pddFile.getName(), e);
		}
	}
}
