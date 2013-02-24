/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.launch.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * The launch group, containing the BPELLaunchMainTab.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitLaunchGroup extends AbstractLaunchConfigurationTabGroup {

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {

		BPELUnitLaunchTestCasesTab testCasesTab = new BPELUnitLaunchTestCasesTab();
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new BPELUnitLaunchMainTab(testCasesTab), 
				testCasesTab };
		setTabs(tabs);
	}

}
