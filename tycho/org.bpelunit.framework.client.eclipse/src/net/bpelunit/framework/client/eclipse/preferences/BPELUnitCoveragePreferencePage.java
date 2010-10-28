package net.bpelunit.framework.client.eclipse.preferences;

import java.util.ArrayList;
import java.util.Iterator;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import net.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import net.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import net.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 * @author Alex Salnikow
 *
 */
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

	private StringFieldEditor waitTimeInput;

	public BPELUnitCoveragePreferencePage() {

		super(GRID);

		setPreferenceStore(BPELUnitActivator.getDefault().getPreferenceStore());
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
		createSpacer(getFieldEditorParent(), 1);

		createSpacer(getFieldEditorParent(), 2,"    ACTIVITY COVERAGE");
		receiveFlag = new BooleanFieldEditor(BasicActivities.RECEIVE_ACTIVITY,
				"receive", getFieldEditorParent());
		replyFlag = new BooleanFieldEditor(BasicActivities.REPLY_ACTIVITY,
				"reply", getFieldEditorParent());
		invokeFlag = new BooleanFieldEditor(BasicActivities.INVOKE_ACTIVITY,
				"invoke", getFieldEditorParent());
		assignFlag = new BooleanFieldEditor(BasicActivities.ASSIGN_ACTIVITY,
				"assign", getFieldEditorParent());
		throwFlag = new BooleanFieldEditor(BasicActivities.THROW_ACTIVITY,
				"throw", getFieldEditorParent());

		waitFlag = new BooleanFieldEditor(BasicActivities.WAIT_ACTIVITY, "wait",
				getFieldEditorParent());
		emptyFlag = new BooleanFieldEditor(BasicActivities.EMPTY_ACTIVITY,
				"empty", getFieldEditorParent());
		compensateFlag = new BooleanFieldEditor(
				BasicActivities.COMPENSATE_ACTIVITY, "compensate",
				getFieldEditorParent());
		compensateScopeFlag = new BooleanFieldEditor(
				BasicActivities.COMPENSATESCOPE_ACTIVITY, "compensateScope (only BPEL 2.0)",
				getFieldEditorParent());
		checkboxGroup.addField(compensateScopeFlag);
		rethrowFlag = new BooleanFieldEditor(BasicActivities.RETHROW_ACTIVITY,
				"rethrow (only BPEL 2.0)", getFieldEditorParent());
		checkboxGroup.addField(rethrowFlag);
		validateFlag = new BooleanFieldEditor(BasicActivities.VALIDATE_ACTIVITY,
				"validate (only BPEL 2.0)", getFieldEditorParent());
		checkboxGroup.addField(validateFlag);
		exitFlag = new BooleanFieldEditor(BasicActivities.EXIT_ACTIVITY, "exit (only BPEL 2.0)",
				getFieldEditorParent());
		checkboxGroup.addField(exitFlag);
		terminateFlag = new BooleanFieldEditor(
				BasicActivities.TERMINATE_ACTIVITY, "terminate (only BPEL 1.1)",
				getFieldEditorParent());

		createSpacer(getFieldEditorParent(), 1);

		branchMetricFlag = new BooleanFieldEditor(BranchMetric.METRIC_NAME,
				"BRANCH COVERAGE", getFieldEditorParent());

		createSpacer(getFieldEditorParent(), 1);
		linkMetricFlag = new BooleanFieldEditor(LinkMetric.METRIC_NAME,
				"LINK COVERAGE", getFieldEditorParent());
		createSpacer(getFieldEditorParent(), 2);
		compensationHandlerFlag = new BooleanFieldEditor(
				CompensationMetric.METRIC_NAME, "COMPENSATION HANDLER COVERAGE",
				getFieldEditorParent());
		createSpacer(getFieldEditorParent(), 1);
		faultHandlerFlag = new BooleanFieldEditor(FaultMetric.METRIC_NAME,
				"FAULT HANDLER COVERAGE", getFieldEditorParent());
		createSpacer(getFieldEditorParent(), 1);
		createSpacer(getFieldEditorParent(), 2,"DELAY PER TESTCASE");
		waitTimeInput=new IntegerFieldEditor(PreferenceConstants.P_COVERAGE_WAIT_TIME,"",5,StringFieldEditor.VALIDATE_ON_KEY_STROKE,getFieldEditorParent());
		waitTimeInput.setTextLimit(5);
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
		addField(waitTimeInput);

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
	}

	protected void createSpacer(Composite composite, int columnSpan) {
		Label label = new Label(composite, SWT.LEFT);
		GridData gd = new GridData();
		gd.horizontalSpan = columnSpan;
		label.setLayoutData(gd);
	}
	
	protected void createSpacer(Composite composite, int columnSpan,String text) {
		Label label = new Label(composite, SWT.LEFT);
		label.setText(text);
		GridData gd = new GridData();
		gd.horizontalSpan = columnSpan;
		label.setLayoutData(gd);
	}

	@Override
	protected void initialize() {
		super.initialize();
		enableFields();
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
			waitTimeInput.setEnabled(true, getFieldEditorParent());
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
			waitTimeInput.setEnabled(false, getFieldEditorParent());
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
