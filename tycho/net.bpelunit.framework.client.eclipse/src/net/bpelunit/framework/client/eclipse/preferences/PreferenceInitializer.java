/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.ExtensionControl;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initializes the preferences of the BPELUnit Eclipse plugin
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = BPELUnitActivator.getDefault()
				.getPreferenceStore();

		store.setDefault(PreferenceConstants.P_LOGLEVEL, "OFF");
		store.setDefault(PreferenceConstants.P_LOGTOCONSOLE, false);
		store.setDefault(PreferenceConstants.P_TIMEOUT, 25000);

		store.setDefault(PreferenceConstants.P_CURRENT_DEPLOYER,
				ExtensionControl.chooseDefaultDeployerId());
	}

}
