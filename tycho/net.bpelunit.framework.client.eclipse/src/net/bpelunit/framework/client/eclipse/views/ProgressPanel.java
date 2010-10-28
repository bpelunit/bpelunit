/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Progress Panel for displaying a progress bar with a title.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ProgressPanel extends Composite {

	private Label fInfoLabel;
	private BPELUnitProgressBar fProgressBar;

	private String fLabelText;
	private int fCurrentStep;
	private int fMaxStep;
	private boolean fHasError;

	public ProgressPanel(Composite parent, String labeltext) {
		super(parent, SWT.WRAP);
		GridLayout gridLayout= new GridLayout();
		gridLayout.numColumns= 1;
		gridLayout.marginWidth= 0;
		setLayout(gridLayout);

		fLabelText= labeltext;

		fInfoLabel= new Label(parent, SWT.None);
		fInfoLabel.setText(fLabelText + ": 0/0");
		fInfoLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		fProgressBar= new BPELUnitProgressBar(parent);
		fProgressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
	}

	public void reset() {
		reset(0);
	}

	public void reset(int max) {
		fCurrentStep= 0;
		fMaxStep= max;
		fHasError= false;
		fProgressBar.reset(false, false, 0, max);
		updateLabel();
	}

	public void stepForward(boolean hasError) {
		fCurrentStep++;
		if (hasError)
			fHasError= true;
		fProgressBar.step(fHasError ? 1 : 0);
		fProgressBar.refresh();
		updateLabel();
	}

	public void abort() {
		fProgressBar.stopped();
	}

	private void updateLabel() {
		fInfoLabel.setText(fLabelText + ": " + Integer.toString(fCurrentStep) + "/" + Integer.toString(fMaxStep));
	}

}
