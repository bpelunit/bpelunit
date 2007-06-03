/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.editors.wizards.pages.NamespaceWizardPage;
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
