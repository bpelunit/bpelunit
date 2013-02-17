/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.ExtensionControl;
import net.bpelunit.framework.client.model.DeployerExtension;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 * Deployment preference page. Allows setting of options for the registered
 * deployers.
 * 
 * @version $Id: BPELUnitDeploymentPreferencePage.java 81 2007-06-03 10:07:37Z
 *          asalnikow $
 * @author Philip Mayer
 * 
 */
public class BPELUnitDeploymentPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private ComboFieldEditor fComboFieldEditor;
	private OptionTableFieldEditor fOptionTableFieldEditor;
	private BooleanFieldEditor fEndpointModificationFlag;

	public BPELUnitDeploymentPreferencePage() {
		super(GRID);
		setPreferenceStore(BPELUnitActivator.getDefault().getPreferenceStore());
		setDescription("Deployment Preferences");
	}

	@Override
	public void createFieldEditors() {

		createSpacer(getFieldEditorParent(), 2);

		String[][] deployerIds = ExtensionControl.getDeployerMetaInformation();

		fComboFieldEditor = new ComboFieldEditor(
				PreferenceConstants.P_CURRENT_DEPLOYER, "&Deployer:",
				deployerIds, getFieldEditorParent());
		fComboFieldEditor.setPropertyChangeListener(this);
		addField(fComboFieldEditor);

		fOptionTableFieldEditor = new OptionTableFieldEditor("Options",
				"&Options for selected deployer:", getFieldEditorParent());
		addField(fOptionTableFieldEditor);

		createSpacer(getFieldEditorParent(), 2);

		fEndpointModificationFlag = new BooleanFieldEditor(
				PreferenceConstants.P_ENDPOINT_MODIFICATION,
				"Automatically modify endpoints to simulated URLs",
				getFieldEditorParent());
		fEndpointModificationFlag.setPropertyChangeListener(this);
		addField(fEndpointModificationFlag);

		String current = getPreferenceStore().getString(
				PreferenceConstants.P_CURRENT_DEPLOYER);
		if (current.equals(""))
			current = getPreferenceStore().getDefaultString(
					PreferenceConstants.P_CURRENT_DEPLOYER);

		DeployerExtension deployerExtension = ExtensionControl
				.findDeployerExtension(current);
		fOptionTableFieldEditor.setDeployer(deployerExtension);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		FieldEditor editor = (FieldEditor) event.getSource();
		if (editor == fComboFieldEditor) {
			DeployerExtension deployerExtension = ExtensionControl
					.findDeployerExtension(fComboFieldEditor
							.getCurrentSelectionString());
			fOptionTableFieldEditor.setDeployer(deployerExtension);
		}
	}

	public void init(IWorkbench workbench) {
	}

	protected void createSpacer(Composite composite, int columnSpan) {
		Label label = new Label(composite, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = columnSpan;
		label.setLayoutData(gd);
	}

}
