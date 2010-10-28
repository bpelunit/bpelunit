/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.field;

import net.bpelunit.framework.client.eclipse.dialog.Field;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A field for the {@link FieldBasedInputDialog}. The FileField allows the user to either type in
 * the name of a file, or use the browse button to select a file from the workbench.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class FileField extends Field {

	private Text fText;
	private ViewerFilter fFilter;
	private ISelectionStatusValidator fFileValidator;

	private IProject fRoot;
	private IContainer fCurrentDir;
	protected String fCurrentSelection;


	public FileField(FieldBasedInputDialog inputDialog, String labelText, String initialValue, ISelectionStatusValidator validator,
			ViewerFilter filter, IProject root, IContainer dir) {
		super(inputDialog, labelText, initialValue);
		fRoot= root;
		fFileValidator= validator;
		fFilter= filter;
		fCurrentDir= dir;
	}

	@Override
	protected void createControl(Composite parent) {

		Label label= new Label(parent, SWT.NONE);
		label.setText(getLabelText());
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Composite comp= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		fText= new Text(comp, SWT.SINGLE | SWT.BORDER);
		GridData data= new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint= 200;
		fText.setLayoutData(data);

		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, fText.getSize().y);

		if (getInitialValue() != null) {
			fText.setText(getInitialValue());
		}

		fCurrentSelection= fText.getText();

		fText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fCurrentSelection= fText.getText();
				getDialog().validateFields();
			}
		});

		Button button= createButton(comp, IDialogConstants.IGNORE_ID, "&Browse...", false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selected= FileSelector.getFile(getDialog().getShell(), fText.getText(), fFileValidator, fFilter, fRoot, fCurrentDir);

				if (selected != null) {
					fCurrentSelection= fText.getText();
					fText.setText(selected);
					getDialog().validateFields();
				}
			}
		});

	}

	@Override
	public String getSelection() {
		return fCurrentSelection;
	}

}
