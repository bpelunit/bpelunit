package org.bpelunit.framework.client.eclipse.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.client.eclipse.BPELUnitActivator;
import org.bpelunit.framework.client.eclipse.ExtensionControl;
import org.bpelunit.framework.client.model.DeployerExtension;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivity;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class BPELUnitCoveragePreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private BooleanFieldEditor branchMetricFlag;

	private BooleanFieldEditor linkMetricFlag;

	private BooleanFieldEditor receiveFlag;

	private BooleanFieldEditor replyFlag;

	private BooleanFieldEditor invokeFlag;

	private BooleanFieldEditor assignFlag;

	private BooleanFieldEditor throwFlag;

	private BooleanFieldEditor exitFlag;

	private BooleanFieldEditor waitFlag;

	private BooleanFieldEditor emptyFlag;

	private BooleanFieldEditor compensateFlag;

	private BooleanFieldEditor compensateScopeFlag;

	private BooleanFieldEditor rethrowFlag;

	private BooleanFieldEditor validateFlag;

	private BooleanFieldEditor compensationHandlerFlag;

	private BooleanFieldEditor faultHandlerFlag;

	private BooleanFieldEditor terminateFlag;

	private BooleanFieldEditor coverageFlag;

	private CheckbuttonGroup checkboxGroup;

	public BPELUnitCoveragePreferencePage() {

		super(GRID);
		setPreferenceStore(BPELUnitActivator.getDefault().getPreferenceStore());
		setDescription("BPELUnit Coverage Tool");
		checkboxGroup = new CheckbuttonGroup();
	}

	@Override
	protected void createFieldEditors() {
		createSpacer(getFieldEditorParent(), 4);
		createFields();
		setListner();
		addFields();
	}

	private void createFields() {
		coverageFlag = new BooleanFieldEditor(
				PreferenceConstants.P_COVERAGE_MEASURMENT,
				"Coverage Measurment", getFieldEditorParent());
		receiveFlag = new BooleanFieldEditor(BasicActivity.RECEIVE_ACTIVITY,
				"receive", getFieldEditorParent());
		replyFlag = new BooleanFieldEditor(BasicActivity.REPLY_ACTIVITY,
				"reply", getFieldEditorParent());
		invokeFlag = new BooleanFieldEditor(BasicActivity.INVOKE_ACTIVITY,
				"invoke", getFieldEditorParent());
		assignFlag = new BooleanFieldEditor(BasicActivity.ASSIGN_ACTIVITY,
				"assign", getFieldEditorParent());
		throwFlag = new BooleanFieldEditor(BasicActivity.THROW_ACTIVITY,
				"throw", getFieldEditorParent());
		exitFlag = new BooleanFieldEditor(BasicActivity.EXIT_ACTIVITY, "exit",
				getFieldEditorParent());
		checkboxGroup.addField(exitFlag);
		waitFlag = new BooleanFieldEditor(BasicActivity.WAIT_ACTIVITY, "wait",
				getFieldEditorParent());
		emptyFlag = new BooleanFieldEditor(BasicActivity.EMPTY_ACTIVITY,
				"empty", getFieldEditorParent());
		compensateFlag = new BooleanFieldEditor(
				BasicActivity.COMPENSATE_ACTIVITY, "compensate",
				getFieldEditorParent());
		compensateScopeFlag = new BooleanFieldEditor(
				BasicActivity.COMPENSATESCOPE_ACTIVITY, "compensateScope",
				getFieldEditorParent());
		checkboxGroup.addField(compensateScopeFlag);
		rethrowFlag = new BooleanFieldEditor(BasicActivity.RETHROW_ACTIVITY,
				"rethrow", getFieldEditorParent());
		checkboxGroup.addField(rethrowFlag);
		validateFlag = new BooleanFieldEditor(BasicActivity.VALIDATE_ACTIVITY,
				"validate", getFieldEditorParent());
		checkboxGroup.addField(validateFlag);
		branchMetricFlag = new BooleanFieldEditor(BranchMetric.METRIC_NAME,
				"Branchmetric", getFieldEditorParent());
		linkMetricFlag = new BooleanFieldEditor(LinkMetric.METRIC_NAME,
				"Linkmetric", getFieldEditorParent());
		compensationHandlerFlag = new BooleanFieldEditor(
				CompensationMetric.METRIC_NAME, "Compensation Handler",
				getFieldEditorParent());
		faultHandlerFlag = new BooleanFieldEditor(FaultMetric.METRIC_NAME,
				"Fault Handler", getFieldEditorParent());
		terminateFlag = new BooleanFieldEditor(
				BasicActivity.TERMINATE_ACTIVITY, "terminate",
				getFieldEditorParent());
	}

	private void addFields() {

		addField(coverageFlag);
		addField(terminateFlag);
		addField(faultHandlerFlag);
		addField(compensationHandlerFlag);
		addField(linkMetricFlag);
		addField(branchMetricFlag);
		addField(validateFlag);
		addField(emptyFlag);
		addField(rethrowFlag);
		addField(compensateScopeFlag);
		addField(compensateFlag);
		addField(waitFlag);
		addField(exitFlag);
		addField(throwFlag);
		addField(assignFlag);
		addField(invokeFlag);
		addField(replyFlag);
		addField(receiveFlag);
		addField(coverageFlag);

	}

	private void setListner() {
		terminateFlag.setPropertyChangeListener(this);
		compensationHandlerFlag.setPropertyChangeListener(this);
		faultHandlerFlag.setPropertyChangeListener(this);
		linkMetricFlag.setPropertyChangeListener(this);
		branchMetricFlag.setPropertyChangeListener(this);
		validateFlag.setPropertyChangeListener(this);
		emptyFlag.setPropertyChangeListener(this);
		rethrowFlag.setPropertyChangeListener(this);
		compensateScopeFlag.setPropertyChangeListener(this);
		compensateFlag.setPropertyChangeListener(this);
		waitFlag.setPropertyChangeListener(this);
		exitFlag.setPropertyChangeListener(this);
		throwFlag.setPropertyChangeListener(this);
		assignFlag.setPropertyChangeListener(this);
		invokeFlag.setPropertyChangeListener(this);
		replyFlag.setPropertyChangeListener(this);
		receiveFlag.setPropertyChangeListener(this);
		coverageFlag.setPropertyChangeListener(this);
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		enableFields();
		System.out.println("Property Changed");
	}

	protected void createSpacer(Composite composite, int columnSpan) {
		Label label = new Label(composite, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = columnSpan;
		label.setLayoutData(gd);
	}

	@Override
	protected void initialize() {
		super.initialize();
		enableFields();
		System.out.println("initialize");
	}

	private void enableFields() {
		if (coverageFlag.getBooleanValue()) {
			if (terminateFlag.getBooleanValue()) {
				terminateFlag.setEnabled(true, getFieldEditorParent());
				checkboxGroup.setAllEnabled(false);
			} else if (checkboxGroup.isSelected()) {
				checkboxGroup.setAllEnabled(true);
				terminateFlag.setEnabled(false, getFieldEditorParent());
			} else {
				terminateFlag.setEnabled(true, getFieldEditorParent());
				checkboxGroup.setAllEnabled(true);
			}
			compensationHandlerFlag.setEnabled(true, getFieldEditorParent());
			faultHandlerFlag.setEnabled(true, getFieldEditorParent());
			linkMetricFlag.setEnabled(true, getFieldEditorParent());
			branchMetricFlag.setEnabled(true, getFieldEditorParent());
			emptyFlag.setEnabled(true, getFieldEditorParent());
			compensateFlag.setEnabled(true, getFieldEditorParent());
			waitFlag.setEnabled(true, getFieldEditorParent());
			throwFlag.setEnabled(true, getFieldEditorParent());
			assignFlag.setEnabled(true, getFieldEditorParent());
			invokeFlag.setEnabled(true, getFieldEditorParent());
			replyFlag.setEnabled(true, getFieldEditorParent());
			receiveFlag.setEnabled(true, getFieldEditorParent());
		} else {
			terminateFlag.setEnabled(false, getFieldEditorParent());
			compensationHandlerFlag.setEnabled(false, getFieldEditorParent());
			faultHandlerFlag.setEnabled(false, getFieldEditorParent());
			linkMetricFlag.setEnabled(false, getFieldEditorParent());
			branchMetricFlag.setEnabled(false, getFieldEditorParent());
			validateFlag.setEnabled(false, getFieldEditorParent());
			emptyFlag.setEnabled(false, getFieldEditorParent());
			rethrowFlag.setEnabled(false, getFieldEditorParent());
			compensateScopeFlag.setEnabled(false, getFieldEditorParent());
			compensateFlag.setEnabled(false, getFieldEditorParent());
			waitFlag.setEnabled(false, getFieldEditorParent());
			exitFlag.setEnabled(false, getFieldEditorParent());
			throwFlag.setEnabled(false, getFieldEditorParent());
			assignFlag.setEnabled(false, getFieldEditorParent());
			invokeFlag.setEnabled(false, getFieldEditorParent());
			replyFlag.setEnabled(false, getFieldEditorParent());
			receiveFlag.setEnabled(false, getFieldEditorParent());
		}
	}

	class CheckbuttonGroup {

		private java.util.List<BooleanFieldEditor> buttons = new ArrayList<BooleanFieldEditor>();

		public void addField(BooleanFieldEditor field) {
			buttons.add(field);
		}

		public boolean isSelected() {
			for (Iterator<BooleanFieldEditor> iter = buttons.iterator(); iter
					.hasNext();) {
				if (iter.next().getBooleanValue()) {
					return true;
				}
			}
			return false;
		}

		public void setAllEnabled(boolean enabled) {

			for (Iterator<BooleanFieldEditor> iter = buttons.iterator(); iter
					.hasNext();) {
				iter.next().setEnabled(enabled, getFieldEditorParent());
			}

		}
	}

}
