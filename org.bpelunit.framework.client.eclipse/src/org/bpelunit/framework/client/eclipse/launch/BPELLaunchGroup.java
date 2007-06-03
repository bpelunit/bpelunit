/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.client.eclipse.launch;

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
public class BPELLaunchGroup extends AbstractLaunchConfigurationTabGroup {

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {

		ILaunchConfigurationTab[] tabs= new ILaunchConfigurationTab[] { new BPELLaunchMainTab() };
		setTabs(tabs);
	}

}
