/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.launch;

import java.util.List;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.EclipseBPELUnitRunner;
import net.bpelunit.framework.client.eclipse.preferences.PreferenceConstants;
import net.bpelunit.framework.client.model.TestRunSession;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.exception.TestCaseNotFoundException;
import net.bpelunit.framework.model.test.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * The BPELUnit launch delegate. This delegate is instantiated and run each time
 * the user runs a test suite.
 * 
 * @version $Id: BPELLaunchConfigurationDelegate.java,v 1.1 2006/10/14 14:50:30
 *          pmayer Exp $
 * @author Philip Mayer
 * 
 */
public class BPELLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		try {
			String projectName = configuration.getAttribute(
					LaunchConstants.ATTR_PROJECT_NAME, "");
			String fileName = configuration.getAttribute(
					LaunchConstants.ATTR_SUITE_FILE_NAME, "");
			@SuppressWarnings("unchecked")
			List<String> testCasesToRun = configuration.getAttribute(
					LaunchConstants.ATTR_TEST_CASES_NAMES,
					LaunchConstants.EMPTY_LIST);

			boolean haltOnError;
			try {
				haltOnError = configuration
						.getAttribute(LaunchConstants.ATTR_HALT_ON_ERROR, "")
						.toLowerCase().equals("true");
			} catch (NullPointerException e) {
				haltOnError = false;
			}

			boolean haltOnFailure;
			try {
				haltOnFailure = configuration
						.getAttribute(LaunchConstants.ATTR_HALT_ON_FAILURE, "")
						.toLowerCase().equals("true");
			} catch (NullPointerException e) {
				haltOnFailure = false;
			}

			BPELUnitActivator plugin = BPELUnitActivator.getDefault();
			EclipseBPELUnitRunner unitCore = plugin.getBPELUnitCore();
			unitCore.setHaltOnError(haltOnError);
			unitCore.setHaltOnFailure(haltOnFailure);

			IViewDescriptor[] views = plugin.getWorkbench().getViewRegistry()
					.getViews();
			for (int i = 0; i < views.length; i++) {
				if (views[i].getId().equals(
						BPELUnitActivator.BPELUNIT_COVERAGE_VIEW_ID)) {
					views[i].createView();
				}
			}

			IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projectName);
			IFile suiteFile = project.getFile(fileName);

			setUpLogging(plugin, unitCore);

			TestSuite suite = unitCore.loadTestSuite(suiteFile.getRawLocation()
					.toFile());

			if (testCasesToRun != null && testCasesToRun.size() > 0) {
				try {
					suite.setFilter(testCasesToRun);
				} catch (TestCaseNotFoundException e) {
					showError(e);
					return;
				}
			}

			/*
			 * Register the new suite. Note that this might wait for an old
			 * session to close.
			 */
			TestRunSession testRunSession = new TestRunSession(suite, launch);
			plugin.registerLaunchSession(testRunSession);
			try {
				suite.setUp();
			} catch (DeploymentException e) {
				e.printStackTrace();
				try {
					suite.shutDown();
				} catch (DeploymentException unused) {
					// do nothing (can't do anything;))
				}
				plugin.deregisterLaunchSession(testRunSession);
				showError(e);
				return;
			}

			/*
			 * User may have already canceled the session...
			 */
			if (!testRunSession.isAbortedByUser())
				suite.run();

			try {
				suite.shutDown();
			} catch (DeploymentException e) {
				showError(e);
				return;
			} finally {
				plugin.deregisterLaunchSession(testRunSession);
			}
		} catch (SpecificationException e) {
			e.printStackTrace();
			showError(e);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			showError(e);
		} catch (Exception e) {
			showError(e);
			BPELUnitActivator.log(e);
		}
	}

	// **************** Console ********************

	private void setUpLogging(BPELUnitActivator plugin,
			EclipseBPELUnitRunner unitCore) throws PartInitException {
		boolean logToConsole = plugin.getPreferenceStore().getBoolean(
				PreferenceConstants.P_LOGTOCONSOLE);
		if (logToConsole) {
			MessageConsoleStream out = retrieveConsoleStream();
			String logLevel = plugin.getPreferenceStore().getString(
					PreferenceConstants.P_LOGLEVEL);
			unitCore.configureLogging(Level.toLevel(logLevel),
					new WriterAppender(new PatternLayout(
							PatternLayout.TTCC_CONVERSION_PATTERN), out));
		} else
			unitCore.configureLogging();
	}

	private MessageConsoleStream retrieveConsoleStream()
			throws PartInitException {
		MessageConsole myConsole = findConsole(BPELUnitActivator.BPELUNIT_CONSOLE_ID);
		myConsole.clearConsole();
		MessageConsoleStream out = myConsole.newMessageStream();
		openConsole(myConsole);
		return out;
	}

	private void openConsole(final MessageConsole bpelConsole)
			throws PartInitException {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = BPELUnitActivator
						.getActiveWorkbenchWindow().getActivePage();
				String id = IConsoleConstants.ID_CONSOLE_VIEW;
				IConsoleView view;
				try {
					view = (IConsoleView) page.showView(id);
					view.display(bpelConsole);
				} catch (PartInitException e) {
					// ignore
				}
			}
		});
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (IConsole element : existing)
			if (name.equals(element.getName()))
				return (MessageConsole) element;
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	// ************* Error reporting ***************

	private void showError(Exception exception) {
		String message = exception.getMessage() != null ? exception
				.getMessage() : "An error occurred during the launch.";
		showError(new Status(IStatus.ERROR, BPELUnitActivator.PLUGIN_ID, 500,
				message, exception));
	}

	private void showError(final IStatus status) {
		BPELUnitActivator.getDisplay().syncExec(new Runnable() {
			public void run() {
				Shell shell = BPELUnitActivator.getActiveWorkbenchWindow()
						.getShell();
				if (shell == null)
					shell = BPELUnitActivator.getDisplay().getActiveShell();
				if (shell != null) {
					ErrorDialog.openError(shell, "BPELUnit Launcher",
							"An error occurred during the launch.", status);
				}
			}
		});
	}
}
