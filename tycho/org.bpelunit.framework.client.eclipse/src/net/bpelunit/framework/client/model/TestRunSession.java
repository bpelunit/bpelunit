/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.model.test.TestSuite;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

/**
 * A test session encapsulates execution of one test suite. It also allows stopping and restarting
 * the suite. A test session stays active even after the launch has completed.
 * 
 * A test session is only valid for one launch. Restarting the session will create a new launch and
 * thus a new session.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TestRunSession {

	private ILaunch fLaunch;
	private TestSuite fSuite;
	private boolean fAborted;

	public TestRunSession(TestSuite suite, ILaunch launch) {
		fSuite= suite;
		fLaunch= launch;
		fAborted= false;
	}

	public TestSuite getSuite() {
		return fSuite;
	}

	/**
	 * Stops the test. Can be called from any thread. Note, however, that the framework may still be
	 * running (cleanup) when this method returns.
	 * 
	 */
	public void stopTest() {
		// Do not stop again if already stopped - User just needs to wait. :)
		if (!fAborted)
			fSuite.abortTest();
		fAborted= true;
	}

	public void relaunchTest() {

		stopTest();
		// Do not wait here - launch configuration will wait for
		// run to end.

		// Run in different thread to avoid lock-up in UI.
		new Thread(new Runnable() {
			public void run() {
				ILaunchConfiguration launchConfiguration= fLaunch.getLaunchConfiguration();
				if (launchConfiguration != null) {
					try {
						ILaunchConfigurationWorkingCopy tmp= launchConfiguration.copy(launchConfiguration.getName() + "_copy");
						tmp.launch(ILaunchManager.RUN_MODE, null);
					} catch (CoreException e) {
						BPELUnitActivator.log(e);
					}
				}
			}

		}).start();
	}

	public boolean isAbortedByUser() {
		return fAborted;
	}
}
