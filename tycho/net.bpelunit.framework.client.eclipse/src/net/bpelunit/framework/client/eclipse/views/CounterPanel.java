/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.views;

import net.bpelunit.framework.model.test.report.ArtefactStatus.StatusCode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A panel with counters for the number of Runs, Errors and Failures.
 * 
 * @version $Id$
 * @author Philip Mayer
 */
public class CounterPanel extends Composite {

	private Text fNumberOfRuns;
	private Text fNumberOfErrors;
	private Text fNumberOfFailures;

	private int fTotalRuns;
	private int fRuns;
	private int fErrors;
	private int fFailures;

	private final Image fErrorIcon= BPELUnitView.createImage("icons/error_ovr.gif"); //$NON-NLS-1$
	private final Image fFailureIcon= BPELUnitView.createImage("icons/failed_ovr.gif"); //$NON-NLS-1$

	public CounterPanel(Composite parent) {
		super(parent, SWT.WRAP);
		GridLayout gridLayout= new GridLayout();
		gridLayout.numColumns= 9;
		gridLayout.makeColumnsEqualWidth= false;
		gridLayout.marginWidth= 0;
		setLayout(gridLayout);

		fNumberOfRuns= createLabel("Runs:", null, " 0/0  "); //$NON-NLS-1$
		fNumberOfErrors= createLabel("Errors:", fErrorIcon, " 0 "); //$NON-NLS-1$
		fNumberOfFailures= createLabel("Failures:", fFailureIcon, " 0 "); //$NON-NLS-1$

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposeIcons();
			}
		});
	}

	private void disposeIcons() {
		fErrorIcon.dispose();
		fFailureIcon.dispose();
	}

	private Text createLabel(String name, Image image, String init) {
		Label label= new Label(this, SWT.NONE);
		if (image != null) {
			image.setBackground(label.getBackground());
			label.setImage(image);
		}
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		label= new Label(this, SWT.NONE);
		label.setText(name);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Text value= new Text(this, SWT.READ_ONLY);
		value.setText(init);
		value.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
		return value;
	}

	public void reset(String name, int total) {
		fErrors= 0;
		fFailures= 0;
		fTotalRuns= total;
		fRuns= 0;
		updateLabels();
	}

	public void addRun(StatusCode code) {
		switch (code) {
			case ERROR:
				fErrors++;
				break;
			case FAILED:
				fFailures++;
				break;
			default:
				// ignore
				break;
		}
		fRuns++;
		updateLabels();
	}

	private void updateLabels() {
		fNumberOfErrors.setText(Integer.toString(fErrors));
		fNumberOfFailures.setText(Integer.toString(fFailures));
		fNumberOfRuns.setText(Integer.toString(fRuns) + "/" + Integer.toString(fTotalRuns));
		redraw();
	}

}
