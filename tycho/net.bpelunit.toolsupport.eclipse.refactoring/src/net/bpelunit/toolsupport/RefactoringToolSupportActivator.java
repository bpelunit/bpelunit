/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RefactoringToolSupportActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.bpelunit.toolsupport";

	public static final String IMAGE_DEPLOYER = "img_deployer";
	public static final String IMAGE_HUMANTASK = "img_deployer"; 
	public static final String IMAGE_TESTCASE = "img_testcase";
	public static final String IMAGE_ACTIVITY = "img_activity";

	public static final String IMAGE_LOCK = "img_lock";
	public static final String IMAGE_EDITABLE = "img_editable";
	public static final String IMAGE_CLONEABLE = "img_cloneable";
	public static final String IMAGE_ADD = "img_add";
	public static final String IMAGE_DELETE = "img_delete";
	public static final String IMAGE_EDITABLE_CLONEABLE = "img_editable_cloneable";

	// The shared instance
	private static RefactoringToolSupportActivator fgPlugin;

	/**
	 * The constructor
	 */
	public RefactoringToolSupportActivator() {
		fgPlugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		fgPlugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static RefactoringToolSupportActivator getDefault() {
		return fgPlugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static Image getImage(String key) {
		return getDefault().getImageRegistry().get(key);
	}

	public static String getPluginId() {
		return PLUGIN_ID;
	}

	public static void log(Throwable e) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			status = getErrorStatus(e);
		}
		log(status);
	}

	public static Status getErrorStatus(Throwable e) {
		return new Status(IStatus.ERROR, getPluginId(), IStatus.OK, "Error", e);
	}

	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, null));
	}

	public static void log(IStatus status) {
		ResourcesPlugin.getPlugin().getLog().log(status);
	}

	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_DEPLOYER, getImageDescriptor("icons/deployer.gif"));
		reg.put(IMAGE_HUMANTASK, getImageDescriptor("icons/deployer.gif"));
		reg.put(IMAGE_TESTCASE, getImageDescriptor("icons/testCase.gif"));
		reg.put(IMAGE_ACTIVITY, getImageDescriptor("icons/activity.gif"));
		reg.put(IMAGE_LOCK, getImageDescriptor("icons/lock.gif"));
		reg.put(IMAGE_EDITABLE, getImageDescriptor("icons/editable.gif"));
		reg.put(IMAGE_CLONEABLE, getImageDescriptor("icons/add_delete.gif"));
		reg.put(IMAGE_ADD, getImageDescriptor("icons/add_border.gif"));
		reg.put(IMAGE_DELETE, getImageDescriptor("icons/delete.gif"));
		reg.put(IMAGE_EDITABLE_CLONEABLE, getImageDescriptor("icons/add_delete_edit.gif"));
	}
}
