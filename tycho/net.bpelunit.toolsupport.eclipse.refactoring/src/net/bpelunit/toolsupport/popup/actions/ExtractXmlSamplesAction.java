package net.bpelunit.toolsupport.popup.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.utils.testdataexternalizer.TestDataExternalizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ExtractXmlSamplesAction implements IObjectActionDelegate {

	private Shell shell;
	private IFile bptsFile;
	
	/**
	 * Constructor for Action1.
	 */
	public ExtractXmlSamplesAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		boolean openConfirm = MessageDialog.openQuestion(shell, "Extract XML Samples", "Do you really want to extract the XML samples out of the test suite?");
		
		if(!openConfirm) {
			return;
		}
		
		InputStream bptsStream = null;
		IContainer path = bptsFile.getParent();
		EclipseFileWriter fw = new EclipseFileWriter(path);
		try {
			TestDataExternalizer tde = new TestDataExternalizer();
			bptsStream = bptsFile.getContents();
			XMLTestSuiteDocument ts = XMLTestSuiteDocument.Factory.parse(bptsStream);
			bptsStream.close();
			
			tde.replaceContentsWithSrc(ts);
			tde.externalize(fw);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ts.save(os);
			bptsFile.delete(true, null);
			bptsFile.create(new ByteArrayInputStream(os.toByteArray()), true, null);
		} catch (Exception e) {
			MessageDialog.openError(shell, "Error extracting messages", e.getMessage());
			fw.rollback();
		} finally {
			try {
				bptsStream.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof StructuredSelection) {
			Object selectedItem = ((StructuredSelection)selection).getFirstElement();
			if(selectedItem instanceof IFile) {
				bptsFile = (IFile)selectedItem;
			}
		}
	}

}
