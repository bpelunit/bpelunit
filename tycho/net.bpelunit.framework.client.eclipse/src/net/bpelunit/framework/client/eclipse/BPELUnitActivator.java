/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.client.eclipse.preferences.PreferenceConstants;
import net.bpelunit.framework.client.eclipse.views.BPELUnitView;
import net.bpelunit.framework.client.model.TestRunSession;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.exception.ConfigurationException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plug-in class for the BPELUnit Eclipse client.
 * 
 * @version $Id$
 * @author Philip Mayer
 */
public class BPELUnitActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "net.bpelunit.framework.client.eclipse";

	public static final String FRAMEWORK_BUNDLE_SYMBOLICNAME = "net.bpelunit.framework";

	public static final String BPELUNIT_VIEW_ID = "net.bpelunit.framework.client.eclipse.views.BPELUnitView";

	public static final String BPELUNIT_COVERAGE_VIEW_ID = "net.bpelunit.framework.client.eclipse.views.BPELUnitCoverageResultView";

	public static final String BPELUNIT_CONSOLE_ID = "BPELUnitConsole";

	public static final int INTERNAL_ERROR = 150;

	public static final String INTERNAL_ERROR_MESSAGE = "Internal Error";

	public static final String IMAGE_INFO = "info_obj";

	public static final String IMAGE_WARNING = "warning_obj";

	public static final String IMAGE_ERROR = "error_obj";

	public static final String IMAGE_BPEL = "bpel_obj";

	// The shared instance
	private static BPELUnitActivator fgPlugin;

	/**
	 * Denotes the current test session. Note that only one session may be
	 * active at any given time (most suites will use the default port 7777, for
	 * example).
	 */
	private TestRunSession fCurrentSession = null;

	/**
	 * The BPELUnit runner
	 */
	private EclipseBPELUnitRunner fUnitCore;

	public BPELUnitActivator() {
		fgPlugin = this;
	}

	/**
	 * Convenience method which returns the unique identifier of this plugin.
	 */
	public static String getUniqueIdentifier() {
		return PLUGIN_ID;
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
	public static BPELUnitActivator getDefault() {
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

	/**
	 * Logs the specified status with this plug-in's log.
	 * 
	 * @param status
	 *            status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/**
	 * Logs an internal error with the specified message.
	 * 
	 * @param message
	 *            the error message to log
	 */
	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getUniqueIdentifier(), INTERNAL_ERROR,
				message, null));
	}

	/**
	 * Logs an internal error with the specified throwable
	 * 
	 * @param e
	 *            the exception to be logged
	 */
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getUniqueIdentifier(), INTERNAL_ERROR,
				INTERNAL_ERROR_MESSAGE, e));
	}

	/**
	 * Returns the active workbench window
	 * 
	 * @return the active workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * Registers a new launch session with the UI.
	 * 
	 * @param session
	 */
	public void registerLaunchSession(final TestRunSession session) {

		if (fCurrentSession != null) {
			/*
			 * A session is still active. It might correspond to a running
			 * suite, although most of the time a suite has already been
			 * completed and is still being displayed.
			 * 
			 * Attempt to stop the session.
			 */
			fCurrentSession.stopTest();

			/*
			 * Wait until session is stopped. This waits for the launch to
			 * complete. See BPELLaunchConfigurationDelegate.
			 */
			while (fCurrentSession != null) {
				try {
					Thread.sleep(BPELUnitConstants.TIMEOUT_SLEEP_TIME);
				} catch (InterruptedException e) {
				}
			}
		}

		// Set new current session
		fCurrentSession = session;

		Display.getDefault().syncExec(new Runnable() {
			// Add to UI
			public void run() {
				try {
					getBPELUnitView().registerLaunchSession(session);
				} catch (PartInitException e) {
					BPELUnitActivator.log(e);
				}
			}
		});
	}

	/**
	 * Deregisters the a launch session from the UI.
	 * 
	 * @param session
	 */
	public void deregisterLaunchSession(final TestRunSession session) {
		// Remove from UI
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					getBPELUnitView().deregisterLaunchSession(session);
				} catch (PartInitException e) {
					BPELUnitActivator.log(e);
				}
			}
		});
		fCurrentSession = null;
	}

	/**
	 * Grab the BPEL Unit Core. This lazily creates the core the first time this
	 * method is called.
	 * 
	 * The core is configured on any call to this method, as the configuration
	 * depends on values set in the preference pages, which may change at any
	 * given time.
	 * 
	 * @return BPELUnit core
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public EclipseBPELUnitRunner getBPELUnitCore() throws IOException,
			URISyntaxException, ConfigurationException {

		if (fUnitCore == null)
			fUnitCore = new EclipseBPELUnitRunner();

		int globalTimeout = getPreferenceStore().getInt(
				PreferenceConstants.P_TIMEOUT);

		Map<String, String> options = new HashMap<String, String>();
		options.put(BPELUnitRunner.GLOBAL_TIMEOUT, Integer
				.toString(globalTimeout));
		if (getPreferenceStore().getBoolean(
				PreferenceConstants.P_COVERAGE_MEASURMENT))
			options.put(BPELUnitRunner.MEASURE_COVERAGE, "true");

		if (getPreferenceStore().getBoolean(
				PreferenceConstants.P_ENDPOINT_MODIFICATION))
			options.put(BPELUnitRunner.CHANGE_ENDPOINTS, "true");

		fUnitCore.initialize(options);

		return fUnitCore;
	}

	/**
	 * Returns the current, or default display
	 * 
	 * @return
	 */
	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	/**
	 * Finds and returns the BPELUnitView.
	 * 
	 * @return
	 * @throws PartInitException
	 */
	private BPELUnitView getBPELUnitView() throws PartInitException {
		return (BPELUnitView) getActiveWorkbenchWindow().getActivePage()
				.showView(BPELUNIT_VIEW_ID);
	}

	/**
	 * Initializes the image registry
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_INFO, getImageDescriptor("icons/info_obj.gif"));
		reg.put(IMAGE_WARNING, getImageDescriptor("icons/warning_obj.gif"));
		reg.put(IMAGE_ERROR, getImageDescriptor("icons/error_obj.gif"));
		reg.put(IMAGE_BPEL, getImageDescriptor("icons/bpel.gif"));
	}

	/**
	 * Returns a managed image for the given key.
	 * 
	 * @param key
	 * @return
	 */
	public static Image getImage(String key) {
		return getDefault().getImageRegistry().get(key);
	}
}
