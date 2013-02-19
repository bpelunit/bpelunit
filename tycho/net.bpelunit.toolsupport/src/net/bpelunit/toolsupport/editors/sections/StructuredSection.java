/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import java.util.HashMap;
import java.util.Map;

import net.bpelunit.toolsupport.editors.TestSuitePage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A structured section contains a structured viewer like a List, Table, or
 * Tree, and buttons for adding, editing, removing, and (optionally) moving the
 * elements inside the viewer.
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke
 * 
 */
public abstract class StructuredSection extends BPELUnitSection {

	public static String BUTTON_ADD = "ADD";
	private static final String BUTTON_ADD2 = "ADD2";
	public static String BUTTON_REMOVE = "REMOVE";
	public static String BUTTON_EDIT = "EDIT";
	public static String BUTTON_DUPLICATE = "DUPLICATE";
	public static final String BUTTON_UP = "UP";
	public static final String BUTTON_DOWN = "DOWN";

	private Map<String, Button> fButtons;
	private boolean fEnableUpDownButtons;
	private boolean fEnableDuplicateButton;
	private Composite buttonsContainer;
	
	/**
	 * if null no second add button shall be displayed
	 */
	private String fAdd2ndButtonLabel;

	public Composite getButtonsContainer() {
		return buttonsContainer;
	}

	public StructuredSection(Composite parent, FormToolkit toolkit,
			TestSuitePage page) {
		this(parent, toolkit, page, false);
	}

	public StructuredSection(Composite parent, FormToolkit toolkit,
			TestSuitePage page, boolean enableUpDownButtons) {
		this(parent, toolkit, page, enableUpDownButtons, false, null);
	}
	
	public StructuredSection(Composite parent, FormToolkit toolkit,
			TestSuitePage page, boolean enableUpDownButtons, boolean enableDuplicateButton, String add2ButtonLabel) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION);
		fEnableUpDownButtons = enableUpDownButtons;
		fEnableDuplicateButton = enableDuplicateButton;
		fAdd2ndButtonLabel = add2ButtonLabel;
		createClient(getSection(), toolkit);
	}

	// ************************ to be implemented *************************

	protected abstract String getName();

	protected abstract String getDescription();

	protected abstract void createStructuredViewer(Composite container);

	public abstract StructuredViewer getViewer();

	protected abstract void itemSelected(Object item);

	protected abstract void fillContextMenu(IMenuManager mng);

	protected abstract void addPressed();
	
	protected void add2Pressed() {}

	protected abstract void editPressed();

	protected abstract void removePressed();

	protected void downPressed() { /* dummy */
	}

	protected void upPressed() { /* dummy */
	}
	
	protected void duplicatePressed() { /* dummy */
	}

	// ******************************** helpers ****************************

	public void createClient(final Section section, FormToolkit toolkit) {

		Composite container = createClientContainer(section, 2, toolkit);

		fButtons = new HashMap<String, Button>();

		section.setText(getName());
		section.setDescription(getDescription());

		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createStructuredViewer(container);

		getViewer().getControl().addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent event) {
				handleKeyPressed(event);
			}

			public void keyReleased(KeyEvent event) {
				handleKeyReleased(event);
			}
		});

		buttonsContainer = toolkit.createComposite(container);
		buttonsContainer.setLayout(createButtonsLayout());

		GridData gd = new GridData(GridData.FILL_VERTICAL);
		buttonsContainer.setLayoutData(gd);

		Button addButton = createButton(buttonsContainer, "&Add...", toolkit);
		fButtons.put(BUTTON_ADD, addButton);

		if(fAdd2ndButtonLabel != null) {
			Button add2Button = createButton(buttonsContainer, fAdd2ndButtonLabel, toolkit);
			fButtons.put(BUTTON_ADD2, add2Button);
			
			add2Button.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					add2Pressed();
				}

				public void widgetSelected(SelectionEvent e) {
					add2Pressed();
				}
			});
		}
		
		Button editButton = createButton(buttonsContainer, "&Edit...", toolkit);
		fButtons.put(BUTTON_EDIT, editButton);

		Button removeButton = createButton(buttonsContainer, "Remove", toolkit);
		fButtons.put(BUTTON_REMOVE, removeButton);

		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				addPressed();
			}

			public void widgetSelected(SelectionEvent e) {
				addPressed();
			}
		});

		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				removePressed();
			}

			public void widgetSelected(SelectionEvent e) {
				removePressed();
			}
		});

		editButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				editPressed();
			}

			public void widgetSelected(SelectionEvent e) {
				editPressed();
			}
		});

		// gd= new GridData(GridData.VERTICAL_ALIGN_CENTER);
		addButton.setLayoutData(createButtonLayoutData());
		editButton.setLayoutData(createButtonLayoutData());
		removeButton.setLayoutData(createButtonLayoutData());

		if(fEnableDuplicateButton) {
			Button duplicateButton = createButton(buttonsContainer, "Duplicate", toolkit);
			fButtons.put(BUTTON_DUPLICATE, duplicateButton);
			duplicateButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					duplicatePressed();
				}
			});
			duplicateButton.setLayoutData(createButtonLayoutData());

		}
		
		if (fEnableUpDownButtons) {

			Label spacer = new Label(buttonsContainer, SWT.NULL);
			spacer.setLayoutData(createButtonLayoutData());

			Button upButton = createButton(buttonsContainer, "Up", toolkit);
			fButtons.put(BUTTON_UP, upButton);
			upButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					upPressed();
				}
			});
			upButton.setLayoutData(createButtonLayoutData());

			Button downButton = createButton(buttonsContainer, "Down", toolkit);
			fButtons.put(BUTTON_DOWN, downButton);
			downButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					downPressed();
				}
			});
			downButton.setLayoutData(createButtonLayoutData());
		}
		
		toolkit.paintBordersFor(container);
		section.setClient(container);
		setEditRemoveDuplicateEnabled(false);
	}

	private GridData createButtonLayoutData() {
		return new GridData(SWT.FILL, SWT.BEGINNING, false, false);
	}

	protected void handleKeyPressed(KeyEvent event) {
		if (event.keyCode == SWT.DEL)
			removePressed();
	}

	protected void handleKeyReleased(KeyEvent event) {
		// do nothing. Subclasses may override.
	}

	protected Button createButton(Composite parent, String label,
			FormToolkit toolkit) {
		Button button;
		if (toolkit != null)
			button = toolkit.createButton(parent, label, SWT.PUSH);
		else {
			button = new Button(parent, SWT.PUSH);
			button.setText(label);
		}
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		return button;
	}

	protected GridLayout createButtonsLayout() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		return layout;
	}

	protected Composite createClientContainer(Composite parent, int span,
			FormToolkit toolkit) {

		Composite container = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 2;
		layout.numColumns = span;
		container.setLayout(layout);
		return container;
	}

	public void setViewerInput(Object input) {
		setEditRemoveDuplicateEnabled(false);
		getViewer().setInput(input);
	}

	protected void setEnabled(String button, boolean enabled) {
		fButtons.get(button).setEnabled(enabled);
	}

	protected void setEditRemoveDuplicateEnabled(boolean enabled) {
		fButtons.get(BUTTON_EDIT).setEnabled(enabled);
		fButtons.get(BUTTON_REMOVE).setEnabled(enabled);
		if(fEnableDuplicateButton) {
			fButtons.get(BUTTON_DUPLICATE).setEnabled(enabled);
		}
		if (fEnableUpDownButtons) {
			fButtons.get(BUTTON_UP).setEnabled(enabled);
			fButtons.get(BUTTON_DOWN).setEnabled(enabled);
		}
	}

	protected Object getViewerSelection() {
		ISelection selection = getViewer().getSelection();
		if (selection instanceof IStructuredSelection)
			return ((IStructuredSelection) selection).getFirstElement();
		return null;
	}
	
	protected void setSelection(Object o) {
		getViewer().setSelection(new StructuredSelection(o), true);
	}

	protected void hookMenu() {
		MenuManager popupMenuManager = new MenuManager();
		IMenuListener listener = new IMenuListener() {
			public void menuAboutToShow(IMenuManager mng) {
				fillContextMenu(mng);
			}
		};
		popupMenuManager.addMenuListener(listener);
		popupMenuManager.setRemoveAllWhenShown(true);
		Control control = getViewer().getControl();
		Menu menu = popupMenuManager.createContextMenu(control);
		control.setMenu(menu);
	}

	protected Action createAction(IMenuManager newMenu, String text,
			Action action) {
		action.setText(text);
		newMenu.add(action);
		return action;
	}

}
