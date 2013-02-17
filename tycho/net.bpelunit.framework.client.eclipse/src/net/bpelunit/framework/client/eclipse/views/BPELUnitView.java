/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.model.TestRunSession;
import net.bpelunit.framework.model.test.ITestResultListener;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.model.test.report.ArtefactStatus.StatusCode;
import net.bpelunit.framework.model.test.report.ITestArtefact;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

/**
 * The BPELUnit Test Runner View.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitView extends ViewPart implements ITestResultListener {

	// Some images
	private Map<StatusCode, Image> fTestCaseIcons;
	private Map<StatusCode, Image> fPartnerTrackIcons;
	private Map<StatusCode, Image> fActivityIcons;
	private Map<StatusCode, Image> fDataPackageIcons;
	private Image fDetailViewIcon;
	private Image fStandardStatusIcon;

	// Actions
	private Action fStopTestAction;
	private Action fReRunAction;

	// UI elements
	private CounterPanel fCounterPanel;
	private Composite fCounterComposite;
	private Label fTestInfoLabel;
	private TreeViewer fTreeViewer;
	private SashForm fSashForm;
	private DetailPane fDetailPane;
	private ProgressPanel fTestCaseProgress;
	private ProgressPanel fActivityProgress;
	private DrillDownAdapter drillDownAdapter;

	// Current session
	private TestRunSession fTestRunSession;

	class TestSessionChangedListener implements ISelectionChangedListener {

		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof IStructuredSelection) {
				Object o = ((IStructuredSelection) selection).getFirstElement();
				if (o instanceof ITestArtefact)
					handleSelected((ITestArtefact) o);
			}
		}
	}

	/**
	 * The constructor.
	 */
	public BPELUnitView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		fTestInfoLabel = new Label(parent, SWT.LEFT);
		fTestInfoLabel.setText(" BPELUnit Eclipse Runner");
		fTestInfoLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		fCounterComposite = createProgressCountPanel(parent);
		fCounterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		SashForm sashForm = createSashForm(parent);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		drillDownAdapter = new DrillDownAdapter(fTreeViewer);
		makeActions();
		contributeToActionBars();

	}

	protected Composite createProgressCountPanel(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 0;

		createImages();

		fCounterPanel = new CounterPanel(composite);
		fCounterPanel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		fTestCaseProgress = new ProgressPanel(composite, "Test Cases");
		fActivityProgress = new ProgressPanel(composite, "Activities");

		return composite;
	}

	private void createImages() {
		fTestCaseIcons = create("testCase");
		fPartnerTrackIcons = create("partnerTrack");
		fActivityIcons = create("activity");
		fDataPackageIcons = create("dataPackage");

		fDetailViewIcon = createImage("icons/bpel.gif");
		fStandardStatusIcon = createImage("icons/plainData.gif");
	}

	private Map<StatusCode, Image> create(String name) {
		Map<StatusCode, Image> map = new HashMap<StatusCode, Image>();
		map.put(StatusCode.PASSED, createImage("icons/" + name + "_pass.gif"));

		if ("activity".equals(name)) {
			// we need inprogress only for wait activity
			map.put(StatusCode.INPROGRESS, createImage("icons/" + name
					+ "_inprogress.gif"));
		}

		map.put(StatusCode.ERROR, createImage("icons/" + name + "_err.gif"));
		map.put(StatusCode.FAILED, createImage("icons/" + name + "_fail.gif"));
		map.put(StatusCode.NOTYETSPECIFIED, createImage("icons/" + name
				+ "_notyet.gif"));
		map.put(StatusCode.ABORTED, createImage("icons/" + name
				+ "_aborted.gif"));
		return map;
	}

	public static Image createImage(String path) {
		return BPELUnitActivator.getImageDescriptor(path).createImage();
	}

	private void disposeImages() {
		fDetailViewIcon.dispose();
		fStandardStatusIcon.dispose();
		diposeImages(fTestCaseIcons);
		diposeImages(fPartnerTrackIcons);
		diposeImages(fActivityIcons);
		diposeImages(fDataPackageIcons);
	}

	private void diposeImages(Map<StatusCode, Image> testCaseIcons) {
		for (Image image : testCaseIcons.values()) {
			image.dispose();
		}
	}

	@Override
	public void dispose() {
		disposeImages();
	}

	private SashForm createSashForm(Composite parent) {
		fSashForm = new SashForm(parent, SWT.VERTICAL);

		ViewForm top = new ViewForm(fSashForm, SWT.NONE);

		Composite empty = new Composite(top, SWT.NONE);
		empty.setLayout(new Layout() {
			@Override
			protected Point computeSize(Composite composite, int wHint,
					int hHint, boolean flushCache) {
				return new Point(1, 1); // (0, 0) does not work with
				// super-intelligent ViewForm
			}

			@Override
			protected void layout(Composite composite, boolean flushCache) {
			}
		});

		// makes ViewForm draw the horizontal separator line:
		top.setTopLeft(empty);

		fTreeViewer = new TreeViewer(top, SWT.V_SCROLL | SWT.SINGLE);
		fTreeViewer.setUseHashlookup(true);
		fTreeViewer.setContentProvider(new TestSessionTreeContentProvider());
		fTreeViewer.setLabelProvider(new TestSessionLabelProvider(this));
		fTreeViewer
				.addSelectionChangedListener(new TestSessionChangedListener());

		top.setContent(fTreeViewer.getTree());

		ViewForm bottom = new ViewForm(fSashForm, SWT.NONE);

		CLabel label = new CLabel(bottom, SWT.NONE);
		label.setText("Details:");
		label.setImage(fDetailViewIcon);
		bottom.setTopLeft(label);

		ToolBar failureToolBar = new ToolBar(bottom, SWT.FLAT | SWT.WRAP);
		bottom.setTopCenter(failureToolBar);

		fDetailPane = new DetailPane(bottom, failureToolBar);
		bottom.setContent(fDetailPane.getComposite());

		fSashForm.setWeights(new int[] { 50, 50 });
		return fSashForm;
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(fStopTestAction);
		manager.add(new Separator());
		manager.add(fReRunAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fStopTestAction);
		manager.add(fReRunAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		fStopTestAction = new Action() {
			@Override
			public void run() {
				fStopTestAction.setEnabled(false);
				fTestRunSession.stopTest();
			}
		};
		fStopTestAction.setText("Stop Test");
		fStopTestAction.setToolTipText("Stops the current test execution");
		fStopTestAction.setImageDescriptor(BPELUnitActivator
				.getImageDescriptor("icons/stop.gif"));
		fStopTestAction.setEnabled(false);

		fReRunAction = new Action() {
			@Override
			public void run() {
				fTestRunSession.relaunchTest();
			}
		};
		fReRunAction.setText("Re-Run Last Test Suite");
		fReRunAction.setToolTipText("Re-Runs the current test");
		fReRunAction.setImageDescriptor(BPELUnitActivator
				.getImageDescriptor("icons/relaunch.gif"));
		fReRunAction.setEnabled(false);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		fTreeViewer.getTree().setFocus();
	}

	/**
	 * Registers a new launch session. This updates the view and registers it to
	 * the new suite as a listener.
	 * 
	 */
	public void registerLaunchSession(TestRunSession session) {
		fTestRunSession = session;
		TestSuite suite = session.getSuite();
		suite.addResultListener(this);
		Collection<TestCase> testCasesToExecute = suite.getTestCasesToExecute();

		// Set tree input
		fTreeViewer.setInput(suite);

		// Set progress views
		fTestCaseProgress.reset(testCasesToExecute.size());
		fActivityProgress.reset();

		// Set counter panel
		fCounterPanel.reset(suite.getName(), testCasesToExecute.size());

		// Actions
		fStopTestAction.setEnabled(true);
		fReRunAction.setEnabled(true);

		// Info
		fTestInfoLabel.setText(" Now starting test: "
				+ suite.getName());
	}

	public void deregisterLaunchSession(TestRunSession session) {

		fTestRunSession.getSuite().removeResultListener(this);
		fStopTestAction.setEnabled(false);

		fTestInfoLabel.setText(" Test run completed: "
				+ session.getSuite().getName());
	}

	public void progress(final ITestArtefact testArtefact) {

		Display.getDefault().syncExec(new Runnable() {
			public void run() {

				if (testArtefact instanceof Activity) {
					fActivityProgress.stepForward(testArtefact.getStatus()
							.hasProblems());
					fTreeViewer.reveal(testArtefact);
					// Bug: Do not check for .isAborted() in activities; they
					// might only
					// be aborted due to some error in another activity. Don't
					// make the bar gray in those cases.
				}

				fTreeViewer.update(testArtefact, null);
			}
		});
	}

	public void testCaseEnded(final TestCase testCase) {

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				fTestCaseProgress.stepForward(testCase.getStatus()
						.hasProblems());
				// Normally, a complete test case is only aborted
				// if the user aborted it. In all other cases, it will have an
				// error or failure.
				if (testCase.getStatus().isAborted())
					abortTest();
				fCounterPanel.addRun(testCase.getStatus().getCode());
				fTreeViewer.update(testCase, null);
			}
		});
	}

	public void testCaseStarted(final TestCase testCase) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				fActivityProgress.reset(testCase.getActivityCount());
				fTreeViewer.update(testCase, null);
				fTestInfoLabel.setText(" Running tests of suite: "
						+ fTestRunSession.getSuite().getName());
			}
		});
	}

	public Image getImage(ITestArtefact element) {

		if (element instanceof TestCase) {
			return fTestCaseIcons.get(element.getStatus().getCode());
		} else if (element instanceof PartnerTrack) {
			return fPartnerTrackIcons.get(element.getStatus().getCode());
		} else if (element instanceof Activity) {
			return fActivityIcons.get(element.getStatus().getCode());
		} else if (element instanceof SendDataSpecification
				|| element instanceof ReceiveDataSpecification) {
			return fDataPackageIcons.get(element.getStatus().getCode());
		} else if (element instanceof ReceiveCondition) {
			return fDataPackageIcons.get(element.getStatus().getCode());
		} else {
			// Fall-through: StatusData, DataCopy
			return fStandardStatusIcon;
		}
	}

	public void handleSelected(ITestArtefact testElement) {
		// Show in detail pane
		fDetailPane.setArtefact(testElement);
	}

	private void abortTest() {
		fActivityProgress.abort();
		fTestCaseProgress.abort();
	}
}
