/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.field;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

/**
 * Provides methods for selecting a file in the workspace.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class FileSelector {

	/**
	 * Opens a dialog and lets the user select a file from the current project. The code attempts to
	 * find the file "currentFile" which is assumed to be relative to the current directory and
	 * select it.
	 * 
	 * The returned file name is relative to the current directory.
	 * 
	 * @param shell shell
	 * @param preSelectFile a file path and name to preselect
	 * @param validator file validator for the file dialog
	 * @param filter filters objects in the selection dialog
	 * @param project project to be displayed in selection dialog
	 * @param directory directory to be pre-selected in the selection dialog
	 * @return
	 */
	public static String getFile(Shell shell, String preSelectFile, ISelectionStatusValidator validator, ViewerFilter filter, IProject project,
			IContainer directory) {

		String path= null;

		IFile file= null;
		if (preSelectFile != null) {
			IResource res= directory.findMember(preSelectFile);
			if (res != null && res.exists() && res instanceof IFile)
				file= (IFile) res;
		}

		IFile newFile= selectFile(shell, "File selection", "Select a file.", filter, project, file);

		if (newFile != null) {
			IPath projPath= newFile.getProjectRelativePath();
			if (directory.getProjectRelativePath().isPrefixOf(projPath))
				projPath= projPath.removeFirstSegments(directory.getProjectRelativePath().segmentCount());

			path= projPath.toString();
		}
		return path;
	}

	private static IFile selectFile(Shell shell, String title, String message, ViewerFilter filter, IContainer rootElement, IFile selectedFile) {

		ISelectionStatusValidator validator= new ISelectionStatusValidator() {
			public IStatus validate(Object[] selection) {
				if (selection.length == 0) {
					return new Status(IStatus.ERROR, BPELUnitActivator.getUniqueIdentifier(), 0, "", null); //$NON-NLS-1$
				}
				for (Object element : selection) {
					if (! (element instanceof IFile)) {
						return new Status(IStatus.ERROR, BPELUnitActivator.getUniqueIdentifier(), 0, "", null); //$NON-NLS-1$
					}
				}
				return new Status(IStatus.OK, BPELUnitActivator.getUniqueIdentifier(), 0, "", null); //$NON-NLS-1$
			}
		};

		ILabelProvider lp= new WorkbenchLabelProvider();
		ITreeContentProvider cp= new WorkbenchContentProvider();

		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(shell, lp, cp);
		dialog.setValidator(validator);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.addFilter(filter);
		dialog.setInput(rootElement);
		if (selectedFile != null)
			dialog.setInitialSelection(selectedFile);
		dialog.setComparator(new ResourceComparator(ResourceComparator.NAME));

		if (dialog.open() == Window.OK) {
			return (IFile) dialog.getFirstResult();
		}

		return null;
	}

}
