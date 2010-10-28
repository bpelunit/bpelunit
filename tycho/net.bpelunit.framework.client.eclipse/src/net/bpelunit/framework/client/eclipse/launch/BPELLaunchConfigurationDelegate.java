/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.launch;

import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.EclipseBPELUnitRunner;
import net.bpelunit.framework.client.eclipse.preferences.PreferenceConstants;
import net.bpelunit.framework.client.model.TestRunSession;
import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.ICoverageMeasurementTool;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.TestSuite;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
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
import org.osgi.framework.Bundle;

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

			BPELUnitActivator plugin = BPELUnitActivator.getDefault();
			EclipseBPELUnitRunner unitCore = plugin.getBPELUnitCore();

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
			// HIER
			setUpCoverageTool(plugin, unitCore);

			// Bundle bundle = Platform
			// .getBundle(BPELUnitActivator.FRAMEWORK_BUNDLE_SYMBOLICNAME);
			if (BPELUnitRunner.getCoverageMeasurmentTool() != null) {
				Bundle bundle = plugin.getBundle();
				URL url = bundle
						.getResource(CoverageConstants.COVERAGE_SERVICE_WSDL);
				if (url != null) {
					System.out.println("URL " + url.getPath());
					url = FileLocator.toFileURL(url);
					BPELUnitRunner.getCoverageMeasurmentTool().setPathToWSDL(url.getPath());
				} else
					BPELUnitRunner.getCoverageMeasurmentTool().setErrorStatus("File " + CoverageConstants.COVERAGE_SERVICE_WSDL
									+ " not found");
			}

			// FileLocator.openStream(bundle, file, substituteArgs);

			// Bundle bundle = Platform
			// .getBundle(BPELUnitActivator.FRAMEWORK_BUNDLE_SYMBOLICNAME);
			// if (bundle != null) {
			// Enumeration enumer=bundle.findEntries("/",
			// CoverageConstants.COVERAGE_SERVICE_WSDL,true);
			// while(enumer.hasMoreElements()){
			// Logger logger=Logger.getLogger(getClass());
			// logger.info("HIER!! "+((URL)enumer.nextElement()).getPath());
			// logger.info("HIER!! "+bundle.getLocation());
			// // URL url = (URL)enumer.nextElement();
			// // String path=bundle.getLocation().substring(7);
			// //
			// CoverageLabelsReceiver.ABSOLUT_CONFIG_PATH=FilenameUtils.concat(path,url.getPath());
			// }
			//
			// }
			// System.out.println("BUNDLE "+ bundle.getLocation());

			TestSuite suite = unitCore.loadTestSuite(suiteFile.getRawLocation()
					.toFile());

			/*
			 * Register the new suite. Note that this might wait for an old
			 * session to close.
			 */

			plugin.initializeCoverageResultView();
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
			if (BPELUnitRunner.measureTestCoverage()) {
				ICoverageMeasurementTool tool=BPELUnitRunner.getCoverageMeasurmentTool();
				plugin.showCoverageResult(suite.getTestCases(), tool.getStatistics(), tool.getErrorStatus());
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
		finally{
			BPELUnitRunner.setCoverageMeasurmentTool(null);
		}
	}

	private void setUpCoverageTool(BPELUnitActivator plugin,
			EclipseBPELUnitRunner unitCore) {
		boolean coverageMeasure = plugin.getPreferenceStore().getBoolean(
				PreferenceConstants.P_COVERAGE_MEASURMENT);
		if (coverageMeasure) {
			unitCore.configureCoverageTool(plugin);
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

	private boolean showError(final IStatus status) {
		final boolean[] success = new boolean[] { false };
		BPELUnitActivator.getDisplay().syncExec(new Runnable() {
			public void run() {
				Shell shell = BPELUnitActivator.getActiveWorkbenchWindow()
						.getShell();
				if (shell == null)
					shell = BPELUnitActivator.getDisplay().getActiveShell();
				if (shell != null) {
					ErrorDialog.openError(shell, "BPELUnit Launcher",
							"An error occurred during the launch.", status);
					success[0] = true;
				}
			}
		});
		return success[0];
	}

}
