/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.util;

import java.util.HashSet;
import java.util.Set;

import net.bpelunit.toolsupport.ToolSupportActivator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.custom.BusyIndicator;

/**
 * A filter for WSDL files.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class WSDLFileFilter extends ViewerFilter {

	private Set<Object> fArchives;

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return fArchives.contains(element);
	}

	public WSDLFileFilter() {
		init();
	}

	private void init() {
		BusyIndicator.showWhile(ToolSupportActivator.getDisplay(), new Runnable() {
			public void run() {
				fArchives= new HashSet<Object>();
				traverse(ResourcesPlugin.getWorkspace().getRoot(), fArchives);
			}
		});
	}

	private boolean traverse(IContainer container, Set<Object> set) {
		boolean added= false;
		try {
			IResource[] resources= container.members();
			for (IResource resource : resources) {
				if (resource instanceof IFile) {
					IFile file= (IFile) resource;
					String ext= file.getFileExtension();
					if (ext != null && (ext.equalsIgnoreCase("wsdl"))) {
						set.add(file);
						added= true;
					}
				} else if (resource instanceof IContainer) {
					if (traverse((IContainer) resource, set)) {
						set.add(resource);
						added= true;
					}
				}
			}
		} catch (CoreException e) {
		}
		return added;
	}
}
