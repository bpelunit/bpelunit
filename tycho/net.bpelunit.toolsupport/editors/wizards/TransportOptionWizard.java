/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.toolsupport.editors.wizards.pages.TransportOptionWizardPage;

import org.eclipse.jface.wizard.Wizard;

/**
 * A wizard for editing deployment options.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TransportOptionWizard extends Wizard {

	private TransportOptionWizardPage page;
	private XMLSendActivity sendActivity;

	public TransportOptionWizard(XMLSendActivity information) {
		super();
		setWindowTitle("Configure the deployment");
		setHelpAvailable(false);
		sendActivity = information;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void addPages() {
		page = new TransportOptionWizardPage("Deployment Options");
		addPage(page);
		page.init(sendActivity);
	}

}
