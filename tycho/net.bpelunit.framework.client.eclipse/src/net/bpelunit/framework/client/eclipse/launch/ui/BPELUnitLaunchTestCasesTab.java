package net.bpelunit.framework.client.eclipse.launch.ui;

import java.util.Arrays;
import java.util.Collections;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.launch.LaunchConstants;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

public class BPELUnitLaunchTestCasesTab extends AbstractLaunchConfigurationTab
		implements IBPELUnitLaunchMainTabListener {

	private List testCasesList;
	private Button runAllTestCasesButton;
	private String projectName;
	private String testSuiteFileName;

	@Override
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.verticalSpacing = 0;
		comp.setLayout(topLayout);
		comp.setFont(font);

		createTestCaseSection(comp);
	}

	private void createTestCaseSection(Composite parent) {
		Group mainGroup = new Group(parent, SWT.NONE);
		mainGroup.setText("Test Cases to run");
		GridData gd = new GridData(GridData.FILL_BOTH);
		mainGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		mainGroup.setLayout(layout);
		mainGroup.setFont(parent.getFont());

		runAllTestCasesButton = new Button(mainGroup, SWT.CHECK);
		runAllTestCasesButton.setText("Run all Test Cases");
		runAllTestCasesButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testCasesList.setEnabled(!runAllTestCasesButton.getSelection());
				updateLaunchConfigurationDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				testCasesList.setEnabled(!runAllTestCasesButton.getSelection());
				updateLaunchConfigurationDialog();
			}
		});

		testCasesList = new List(mainGroup, SWT.BORDER | SWT.V_SCROLL
				| SWT.MULTI);
		gd = new GridData(GridData.FILL_BOTH);
		testCasesList.setLayoutData(gd);
		testCasesList.setFont(parent.getFont());
		testCasesList.setEnabled(!runAllTestCasesButton.getSelection());
		testCasesList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateLaunchConfigurationDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void updateTestCasesList() {
		if (StringUtils.isEmpty(testSuiteFileName)
				|| StringUtils.isEmpty(projectName)) {
			testCasesList.removeAll();
			return;
		}

		try {
			String previouslySelectedTestCases[] = new String[0];
			previouslySelectedTestCases = testCasesList.getSelection();
			testCasesList.removeAll();
			Path path = new Path(testSuiteFileName);
			IFile file = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projectName).getFile(path);
			// TODO Check whether factory closes input stream
			XMLTestSuiteDocument testSuiteDoc = XMLTestSuiteDocument.Factory
					.parse(file.getContents());
			XMLTestCase[] xmlTestCases = testSuiteDoc.getTestSuite()
					.getTestCases().getTestCaseList()
					.toArray(new XMLTestCase[0]);
			for (XMLTestCase ts : xmlTestCases) {
				testCasesList.add(ts.getName());
			}
			testCasesList.setSelection(previouslySelectedTestCases);
		} catch (Exception ex) {
			// error reading test suite or test suite has no test cases, so
			// simply show empty list
		}
	}

	@Override
	public String getName() {
		return "Test Cases";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		updateTestCasesFromConfig(configuration);
	}

	/**
	 * updates the project text field form the configuration
	 * 
	 * @param config
	 *            the configuration we are editing
	 */
	@SuppressWarnings("unchecked")
	private void updateTestCasesFromConfig(ILaunchConfiguration config) {
		String[] testCaseNames = new String[0];
		try {
			testCaseNames = ((java.util.List<String>) config.getAttribute(
					LaunchConstants.ATTR_TEST_CASES_NAMES,
					LaunchConstants.EMPTY_LIST)).toArray(testCaseNames);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}

		testCasesList.deselectAll();
		if (testCaseNames != null && testCaseNames.length > 0) {
			runAllTestCasesButton.setSelection(false);
			testCasesList.setEnabled(true);
			for (String testCaseName : testCaseNames) {
				testCasesList.select(testCasesList.indexOf(testCaseName));
			}
		} else {
			runAllTestCasesButton.setSelection(true);
			testCasesList.setEnabled(false);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if (runAllTestCasesButton.getSelection()) {
			configuration.setAttribute(LaunchConstants.ATTR_TEST_CASES_NAMES,
					Collections.EMPTY_LIST);
		} else {
			configuration.setAttribute(LaunchConstants.ATTR_TEST_CASES_NAMES,
					Arrays.asList(testCasesList.getSelection()));
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(LaunchConstants.ATTR_TEST_CASES_NAMES,
				LaunchConstants.EMPTY_LIST);
	}

	@Override
	public void updated(String project, String path) {
		this.projectName = project;
		this.testSuiteFileName = path;
		updateTestCasesList();
	}
}
