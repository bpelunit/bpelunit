/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.field;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import net.bpelunit.framework.client.eclipse.dialog.Field;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * A field for the {@link FieldBasedInputDialog}. The SelectionField allows the user to either type
 * in a certain value, or select values from a list of values presented in a dialog.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SelectionField extends Field {

	class SimpleLabelProvider implements ILabelProvider {

		private Image fBpelImage;

		public Image getImage(Object element) {

			if (fBpelImage == null)
				fBpelImage= BPELUnitActivator.getImageDescriptor("icons/bpel.gif").createImage();

			return fBpelImage;
		}

		public String getText(Object element) {
			return ((String) element);
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
			if ( (fBpelImage != null) && (!fBpelImage.isDisposed()))
				fBpelImage.dispose();
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}

	private Text fText;
	private String fButtonTitle;

	private String fCurrentSelection;
	private String[] fChoices;
	private List<ModifyListener> fModifyListeners = new ArrayList<ModifyListener>();

	public SelectionField(FieldBasedInputDialog inputDialog, String labelText, String initialValue, String buttonTitle, final String[] choices) {
		super(inputDialog, labelText, initialValue);
		fChoices= choices;
		fButtonTitle= buttonTitle;
	}

	@Override
	public void createControl(Composite composite) {
		Label label= new Label(composite, SWT.NONE);
		label.setText(getLabelText());
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Composite comp= new Composite(composite, SWT.NONE);
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
				
				for(ModifyListener ml : fModifyListeners) {
					ml.modifyText(e);
				}
			}
		});


		Button button= createButton(comp, IDialogConstants.IGNORE_ID, fButtonTitle, false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), new SimpleLabelProvider());
				dialog.setElements(fChoices);

				dialog.setTitle("Options");
				dialog.setMessage("Select one of the options.");
				dialog.setMultipleSelection(false);
				int code= dialog.open();
				if (code == IDialogConstants.OK_ID) {
					Object[] res= dialog.getResult();
					if (res != null && res.length > 0) {
						String variable= (String) res[0];
						fCurrentSelection= variable;
						fText.setText(variable);
						getDialog().validateFields();
					}
				}
			}
		});
	}

	protected Shell getShell() {
		return getDialog().getShell();
	}

	@Override
	public String getSelection() {
		return fCurrentSelection;
	}

	public void addModifyListener(ModifyListener ml) {
		this.fModifyListeners.add(ml);
	}
}
