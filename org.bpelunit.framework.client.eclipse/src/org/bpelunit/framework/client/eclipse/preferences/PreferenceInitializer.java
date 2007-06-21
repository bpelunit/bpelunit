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
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
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
		store.setDefault(BasicActivities.RECEIVE_ACTIVITY, false);
		store.setDefault(BasicActivities.REPLY_ACTIVITY, false);
		store.setDefault(BasicActivities.INVOKE_ACTIVITY, false);
		store.setDefault(BasicActivities.ASSIGN_ACTIVITY, false);
		store.setDefault(BasicActivities.THROW_ACTIVITY, false);
		store.setDefault(BasicActivities.EXIT_ACTIVITY, false);
		store.setDefault(BasicActivities.WAIT_ACTIVITY, false);
		store.setDefault(BasicActivities.EMPTY_ACTIVITY, false);
		store.setDefault(BasicActivities.COMPENSATE_ACTIVITY, false);
		store.setDefault(BasicActivities.COMPENSATESCOPE_ACTIVITY, false);
		store.setDefault(BasicActivities.RETHROW_ACTIVITY, false);
		store.setDefault(BasicActivities.VALIDATE_ACTIVITY, false);
		store.setDefault(BranchMetric.METRIC_NAME, false);
		store.setDefault(LinkMetric.METRIC_NAME, false);
		store.setDefault(CompensationMetric.METRIC_NAME, false);
		store.setDefault(FaultMetric.METRIC_NAME, false);
		store.setDefault(BasicActivities.TERMINATE_ACTIVITY, false);
		store.setDefault(PreferenceConstants.P_COVERAGE_WAIT_TIME, 1500);
	}

}
