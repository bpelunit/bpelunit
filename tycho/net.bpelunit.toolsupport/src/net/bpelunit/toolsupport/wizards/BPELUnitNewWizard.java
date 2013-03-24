/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.wizards;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.toolsupport.ToolSupportActivator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * 
 * A wizard for creating a new, empty bpts Test Suite file.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitNewWizard extends BasicNewResourceWizard {
	private static final String DEFAULT_BASEURL = "http://localhost:7777/ws";
	private WizardNewFileCreationPage mainPage;

	public BPELUnitNewWizard() {
		super();
	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage= new WizardNewFileCreationPage("newBPTSPage", getSelection());//$NON-NLS-1$
		mainPage.setTitle("New BPELUnit Test Suite");
		mainPage.setDescription("Creates a new BPELUnit Test Suite (.bpts)");
		mainPage.setFileName("suite.bpts");

		addPage(mainPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle("New TestSuite");
		setNeedsProgressMonitor(true);
	}

	@Override
	protected void initializeDefaultPageImageDescriptor() {
		setDefaultPageImageDescriptor(ToolSupportActivator.getImageDescriptor("icons/new_wiz.png"));
	}

	@Override
	public boolean performFinish() {

		String fileName= mainPage.getFileName();
		if (!fileName.endsWith(".bpts")) {
			fileName+= ".bpts";
			mainPage.setFileName(fileName);
		}

		IFile file= mainPage.createNewFile();
		if (file == null) {
			return false;
		}
		XMLTestSuiteDocument document = null;
		try {
			document= XMLTestSuiteDocument.Factory.newInstance();
		} catch(Throwable e) {
			e.printStackTrace();
		}

		/*if (document == null) {
			return true;
		}*/
		
		// Create some initial stuff
		XMLTestSuite suite= document.addNewTestSuite();
		suite.setBaseURL(DEFAULT_BASEURL);
		XMLDeploymentSection deploymentSection= suite.addNewDeployment();
		deploymentSection.addNewPut();
		suite.addNewTestCases();

		suite.setName(file.getName());

		// Open editor on new file.
		IWorkbenchWindow dw= getWorkbench().getActiveWorkbenchWindow();

		try {
			file.setCharset("UTF-8", null);
			file.setContents(document.newInputStream( BPELUnitUtil.getDefaultXMLOptions() ), IResource.NONE, new NullProgressMonitor());
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), "Error writing initial data", "Could not create the .bpts file");
		}

		selectAndReveal(file);

		try {
			if (dw != null) {
				IWorkbenchPage page= dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (PartInitException e) {
			MessageDialog.openError(getShell(), "Problems Opening Editor", e.getMessage());
			ToolSupportActivator.log(e);
		}

		return true;
	}
}
