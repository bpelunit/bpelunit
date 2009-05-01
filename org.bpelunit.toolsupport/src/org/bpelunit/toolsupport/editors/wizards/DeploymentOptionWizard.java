/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.client.eclipse.ExtensionControl;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import org.bpelunit.toolsupport.editors.wizards.pages.DeploymentOptionWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * A wizard for editing deployment options.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DeploymentOptionWizard extends Wizard {

	private DeploymentOptionWizardPage fPage;
	private XMLPUTDeploymentInformation fPutInfo;

	public DeploymentOptionWizard(XMLPUTDeploymentInformation information) {
		super();
		setWindowTitle("Configure the deployment");
		setHelpAvailable(false);
		fPutInfo = information;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void addPages() {
		fPage = new DeploymentOptionWizardPage("Deployment Options");
		addPage(fPage);
		fPage.init(fPutInfo);
	}

}
