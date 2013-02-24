/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.launch.ui;

import java.util.Arrays;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.launch.LaunchConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

/**
 * The main tab for the BPELUnit test Suite Runner.
 * 
 * Most of this code has been blatantly ripped out of JavaMainTab (JDT).
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke (Test Case selection)
 */
public class BPELUnitLaunchMainTab extends AbstractLaunchConfigurationTab {

	private Text projText;
	private Button projButton;
	private Text suiteText;
	private Button searchTestSuiteButton;
	private Button haltOnErrorButton;
	private Button haltOnFailureButton;
	private java.util.List<IBPELUnitLaunchMainTabListener> listeners;
	
	public BPELUnitLaunchMainTab(IBPELUnitLaunchMainTabListener... listeners) {
		this.listeners = Arrays.asList(listeners);
	}
	
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.verticalSpacing = 0;
		comp.setLayout(topLayout);
		comp.setFont(font);

		createProjectSection(comp);
		createVerticalSpacer(comp, 1);
		createSuiteSection(comp, "&BPEL Test Suite File:");
		createHaltSection(comp);
	}

	public String getName() {
		return "BPEL Test Suite";
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		updateProjectFromConfig(configuration);
		updateSuiteFromConfig(configuration);
		updateHaltOnErrorFromConfiguration(configuration);
		updateHaltOnFailureFromConfiguration(configuration);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, projText
				.getText().trim());
		configuration.setAttribute(LaunchConstants.ATTR_SUITE_FILE_NAME,
				suiteText.getText().trim());
		
		configuration.setAttribute(LaunchConstants.ATTR_HALT_ON_ERROR, "" + haltOnErrorButton.getSelection());
		configuration.setAttribute(LaunchConstants.ATTR_HALT_ON_FAILURE, "" + haltOnFailureButton.getSelection());
		
		mapResources(configuration);
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IFile selectedFile = getContext();
		if (selectedFile != null) {
			initializeProject(selectedFile, configuration);
		} else {
			configuration.setAttribute(LaunchConstants.ATTR_PROJECT_NAME,
					LaunchConstants.EMPTY_STRING);
		}
		initializeSuiteAndName(selectedFile, configuration);
	}

	protected void createProjectSection(Composite parent) {
		Font font = parent.getFont();
		Group group = new Group(parent, SWT.NONE);
		group.setText("&Project:");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setFont(font);
		projText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projText.setLayoutData(gd);
		projText.setFont(font);
		projText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
		projButton = createPushButton(group, "&Browse...", null);
		projButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				handleProjectButtonSelected();
			}
		});
	}

	protected void handleProjectButtonSelected() {
		IProject project = chooseProject();
		if (project == null) {
			return;
		}
		String projectName = project.getName();
		projText.setText(projectName);
	}

	private IProject chooseProject() {
		ILabelProvider labelProvider = new WorkbenchLabelProvider();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				getShell(), labelProvider);
		dialog.setTitle("Project Selection");
		dialog.setMessage("Select a project to constrain your search.");

		dialog.setElements(getWorkspaceRoot().getProjects());

		IProject project = getProject();
		if (project != null) {
			dialog.setInitialSelections(new Object[] { project });
		}
		if (dialog.open() == Window.OK) {
			return (IProject) dialog.getFirstResult();
		}
		return null;
	}

	/**
	 * Return the IProject corresponding to the project name in the project name
	 * text field, or null if the text does not match a project name.
	 */
	protected IProject getProject() {
		String projectName = projText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getWorkspaceRoot().getProject(projectName);
	}

	/**
	 * Convenience method to get the workspace root.
	 */
	protected IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * Creates the widgets for specifying a main type.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	protected void createSuiteSection(Composite parent, String text) {
		Font font = parent.getFont();
		Group mainGroup = new Group(parent, SWT.NONE);
		mainGroup.setText(text);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		mainGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		mainGroup.setLayout(layout);
		mainGroup.setFont(font);
		suiteText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		suiteText.setLayoutData(gd);
		suiteText.setFont(font);
		suiteText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}

		});
		searchTestSuiteButton = createPushButton(mainGroup, "&Search...", null);
		searchTestSuiteButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				handleSearchButtonSelected();
			}
		});
	}

	
	private void createHaltSection(Composite parent) {
		Group mainGroup = new Group(parent, SWT.NONE);
		mainGroup.setText("Halt on Error/Failure");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		mainGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		mainGroup.setLayout(layout);
		mainGroup.setFont(parent.getFont());
		
		SelectionListener selectionListener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateLaunchConfigurationDialog();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				updateLaunchConfigurationDialog();
			}
		};
		
		haltOnErrorButton = new Button(mainGroup, SWT.CHECK);
		haltOnErrorButton.setText("Halt after Error");
		haltOnErrorButton.addSelectionListener(selectionListener);
		
		haltOnFailureButton = new Button(mainGroup, SWT.CHECK);
		haltOnFailureButton.setText("Halt after Failure");
		haltOnFailureButton.addSelectionListener(selectionListener);
	}
	
	/**
	 * Show a dialog that lists all main types
	 */
	protected void handleSearchButtonSelected() {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				getShell(), new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		dialog.setTitle("Select a .bpts file:");
		dialog.setMessage("Select the .bpts file to run:");
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setComparator(new ResourceComparator(ResourceComparator.NAME));
		dialog.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {

				if (element instanceof IFile) {
					IFile file = (IFile) element;
					if (!file.getFileExtension().equals("bpts"))
						return false;
				}
				return true;
			}
		});
		dialog.setAllowMultiple(false);
		dialog.setValidator(new ISelectionStatusValidator() {

			public IStatus validate(Object[] selection) {
				if ((selection != null) && (selection.length > 0)) {
					Object first = selection[0];
					if (first instanceof IFile) {
						IFile file = (IFile) first;
						if (file.getFileExtension().equals("bpts"))
							return Status.OK_STATUS;
					}
				}
				return new Status(IStatus.ERROR, BPELUnitActivator
						.getUniqueIdentifier(), -1, "Select a .bpts file.",
						null);
			}

		});

		if (dialog.open() == IDialogConstants.OK_ID) {
			IResource resource = (IResource) dialog.getFirstResult();
			if (!(resource instanceof IFile)
					|| !resource.getFileExtension().equals("bpts")) {
				MessageDialog.openError(this.getShell(), "BPELUnit Test Suite",
						"Must select a file with a .bpts ending");
			} else {
				suiteText
						.setText(resource.getProjectRelativePath().toString());
			}
		}
	}

	/**
	 * Loads the main type from the launch configuration's preference store
	 * 
	 * @param config
	 *            the config to load the main type from
	 */
	private void updateSuiteFromConfig(ILaunchConfiguration config) {
		String suiteName = LaunchConstants.EMPTY_STRING;
		
		try {
			suiteName = config.getAttribute(
					LaunchConstants.ATTR_SUITE_FILE_NAME,
					LaunchConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		suiteText.setText(suiteName);
	}
	
	private void updateHaltOnErrorFromConfiguration(ILaunchConfiguration config) {
		String haltOnError = LaunchConstants.EMPTY_STRING;
		
		try {
			haltOnError = config.getAttribute(
					LaunchConstants.ATTR_HALT_ON_ERROR,
					LaunchConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		haltOnErrorButton.setSelection(haltOnError != null && haltOnError.toLowerCase().equals("true"));
	}

	private void updateHaltOnFailureFromConfiguration(ILaunchConfiguration config) {
		String haltOnFailure = LaunchConstants.EMPTY_STRING;
		
		try {
			haltOnFailure = config.getAttribute(
					LaunchConstants.ATTR_HALT_ON_FAILURE,
					LaunchConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		haltOnFailureButton.setSelection(haltOnFailure != null && haltOnFailure.toLowerCase().equals("true"));
	}
	
	
	
	/**
	 * updates the project text field form the configuration
	 * 
	 * @param config
	 *            the configuration we are editing
	 */
	private void updateProjectFromConfig(ILaunchConfiguration config) {
		String projectName = LaunchConstants.EMPTY_STRING;
		try {
			projectName = config.getAttribute(
					LaunchConstants.ATTR_PROJECT_NAME,
					LaunchConstants.EMPTY_STRING);
		} catch (CoreException ce) {
			BPELUnitActivator.log(ce);
		}
		projText.setText(projectName);
	}

	/**
	 * Maps the config to associated IProject
	 * 
	 * @param config
	 */
	protected void mapResources(ILaunchConfigurationWorkingCopy config) {
		IProject project = getProject();
		IResource[] resources = null;
		if (project != null) {
			resources = new IResource[] { project.getProject() };
		}
		config.setMappedResources(resources);
	}

	/**
	 * Returns the current file context in the active workbench page or
	 * <code>null</code> if none.
	 * 
	 * @return current file in the active page or <code>null</code>
	 */
	protected IFile getContext() {
		IWorkbenchPage page = getActivePage();
		if (page != null) {
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				if (!ss.isEmpty()) {
					Object obj = ss.getFirstElement();
					if (obj instanceof IFile) {
						return (IFile) obj;
					}
				}
			}
			IEditorPart part = page.getActiveEditor();
			if (part != null) {
				IEditorInput input = part.getEditorInput();

				return (IFile) input.getAdapter(IFile.class);
			}
		}
		return null;
	}

	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow w = getActiveWorkbenchWindow();
		if (w != null) {
			return w.getActivePage();
		}
		return null;
	}

	/**
	 * Returns the active workbench window
	 * 
	 * @return the active workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return BPELUnitActivator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow();
	}

	/**
	 * Sets the project attribute on the given working copy to the project
	 * associated with the given file.
	 * 
	 * @param file
	 *            File this tab is associated with
	 * @param config
	 *            configuration on which to set the project attribute
	 */
	protected void initializeProject(IFile file,
			ILaunchConfigurationWorkingCopy config) {
		IProject project = file.getProject();
		String name = null;
		if (project != null && project.exists()) {
			name = project.getName();
		}
		config.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, name);
	}

	protected void initializeSuiteAndName(IFile file,
			ILaunchConfigurationWorkingCopy config) {
		String name = null;

		if (file != null && file.getFileExtension().equals("bpts")) {
			name = file.getProjectRelativePath().toString();

		}
		if (name == null) {
			name = LaunchConstants.EMPTY_STRING;
		}
		config.setAttribute(LaunchConstants.ATTR_SUITE_FILE_NAME, name);
		if (name.length() > 0) {
			// Remove .bpts
			int index = name.lastIndexOf('.');
			if (index > 0) {
				name = name.substring(0, index);
			}
			name = getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}
	}

	/*
	 * @see AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return BPELUnitActivator.getImage(BPELUnitActivator.IMAGE_BPEL);
	}

	protected static Image createImage(String path) {
		return BPELUnitActivator.getImageDescriptor(path).createImage();
	}

	@Override
	public boolean isValid(ILaunchConfiguration config) {

		setErrorMessage(null);
		setMessage(null);
		String fileName = projText.getText().trim();
		IProject project = null;
		if (fileName.length() > 0) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IStatus status = workspace
					.validateName(fileName, IResource.PROJECT);
			if (status.isOK()) {
				project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(fileName);
				if (!project.exists()) {
					setErrorMessage("Project " + fileName + " does not exist");
					return false;
				}
				if (!project.isOpen()) {
					setErrorMessage("Project " + fileName + " is closed");
					return false;
				}
			} else {
				setErrorMessage("Illegal project name: " + status.getMessage());
				return false;
			}
		}

		fileName = suiteText.getText().trim();
		if (fileName.length() == 0) {
			setErrorMessage("Test Suite file not specified.");
			return false;
		}
		if (project != null && !project.getFile(fileName).exists()) {
			setErrorMessage("Test Suite " + fileName + " does not exist.");
			return false;
		}
		return true;
	}
	
	@Override
	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
		for(IBPELUnitLaunchMainTabListener l : this.listeners) {
			l.updated(projText.getText(), suiteText.getText());
		}
	}
}
