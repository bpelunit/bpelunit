/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.wizards.pages.NamespaceWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * A wizard for editing namespaces
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class NamespaceWizard extends Wizard {

	private NamespaceWizardPage fPage;
	private XMLTestSuite fPutInfo;

	public NamespaceWizard(XMLTestSuite suite) {
		super();
		setWindowTitle("Configure namespaces");
		setHelpAvailable(false);
		fPutInfo= suite;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void addPages() {
		fPage= new NamespaceWizardPage("Namespace Editing");
		addPage(fPage);
		fPage.init(fPutInfo);
	}

}
