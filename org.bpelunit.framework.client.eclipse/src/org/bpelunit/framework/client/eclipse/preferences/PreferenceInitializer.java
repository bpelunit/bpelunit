/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.client.eclipse.preferences;

import org.bpelunit.framework.client.eclipse.BPELUnitActivator;
import org.bpelunit.framework.client.eclipse.ExtensionControl;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivity;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.BooleanFieldEditor;
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
		store.setDefault(PreferenceConstants.P_COVERAGE_MEASURMENT, false);
		store.setDefault(BasicActivity.RECEIVE_ACTIVITY, false);
		store.setDefault(BasicActivity.REPLY_ACTIVITY, false);
		store.setDefault(BasicActivity.INVOKE_ACTIVITY, false);
		store.setDefault(BasicActivity.ASSIGN_ACTIVITY, false);
		store.setDefault(BasicActivity.THROW_ACTIVITY, false);
		store.setDefault(BasicActivity.EXIT_ACTIVITY, false);
		store.setDefault(BasicActivity.WAIT_ACTIVITY, false);
		store.setDefault(BasicActivity.EMPTY_ACTIVITY, false);
		store.setDefault(BasicActivity.COMPENSATE_ACTIVITY, false);
		store.setDefault(BasicActivity.COMPENSATESCOPE_ACTIVITY, false);
		store.setDefault(BasicActivity.RETHROW_ACTIVITY, false);
		store.setDefault(BasicActivity.VALIDATE_ACTIVITY, false);
		store.setDefault(BranchMetric.METRIC_NAME, false);
		store.setDefault(LinkMetric.METRIC_NAME, false);
		store.setDefault(CompensationMetric.METRIC_NAME, false);
		store.setDefault(FaultMetric.METRIC_NAME, false);
		store.setDefault(BasicActivity.TERMINATE_ACTIVITY, false);
	}

}
