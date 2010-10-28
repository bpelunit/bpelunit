/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

import java.util.logging.Level;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 * BPELUnit preference page. Allows setting of the BPELUnitCore properties and some logging-related
 * properties.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public BPELUnitPreferencePage() {
		super(GRID);
		setPreferenceStore(BPELUnitActivator.getDefault().getPreferenceStore());
		setDescription("BPELUnit Preferences");
	}

	@Override
	public void createFieldEditors() {

		String[][] logLevels= new String[9][2];
		set(logLevels, 0, Level.OFF, "No logging");
		set(logLevels, 1, Level.SEVERE, "Log serious failures only");
		set(logLevels, 2, Level.WARNING, "Log warnings");
		set(logLevels, 3, Level.INFO, "Log informational messages");
		set(logLevels, 4, Level.CONFIG, "Log static configuration messages");
		set(logLevels, 5, Level.FINE, "Log fine trace messages");
		set(logLevels, 6, Level.FINER, "Log detailed trace messages");
		set(logLevels, 7, Level.FINEST, "Log highly detailed trace messages");
		set(logLevels, 8, Level.ALL, "Log everything");

		createSpacer(getFieldEditorParent(), 2);
		addField(new BooleanFieldEditor(PreferenceConstants.P_LOGTOCONSOLE, "Send log &messages to console", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_LOGLEVEL, "Log &level", logLevels, getFieldEditorParent()));

		createSpacer(getFieldEditorParent(), 2);
		addField(new IntegerFieldEditor(PreferenceConstants.P_TIMEOUT, "Global &timeout", getFieldEditorParent()));

	}

	private void set(String[][] logLevels, int i, Level level, String text) {
		logLevels[i][0]= level.getName() + " - " + text;
		logLevels[i][1]= level.getName();
	}

	public void init(IWorkbench workbench) {
	}

	protected void createSpacer(Composite composite, int columnSpan) {
		Label label= new Label(composite, SWT.NONE);
		GridData gd= new GridData();
		gd.horizontalSpan= columnSpan;
		label.setLayoutData(gd);
	}

}
