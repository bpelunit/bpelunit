package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLWaitActivity;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class WaitWizardPage extends WizardPage {

	private Text durationText;
	private XMLWaitActivity xmlWait;

	public WaitWizardPage(String pageName, XMLWaitActivity xmlWait) {
		super(pageName, "Edit a Wait Activity", null);
		this.setDescription("Configure the duration for waiting");
		this.xmlWait = xmlWait;
	}

	@Override
	public void createControl(Composite parent) {
		Composite topLevel = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		layout.marginWidth = layout.marginHeight = 5;
		topLevel.setLayout(layout);

		Label textLabel = new Label(topLevel, SWT.NONE);
		textLabel.setText("Wait Duration (ms): ");

		durationText = new Text(topLevel, SWT.SINGLE | SWT.BORDER);
		durationText.setText("" + xmlWait.getWaitForMilliseconds());
		durationText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = string.toCharArray();
				for (char c : chars) {
					if (!('0' <= c && c <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		durationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateInput();
			}
		});

		GC gc = new GC(durationText);
		FontMetrics fm = gc.getFontMetrics();
		int charWidth = fm.getAverageCharWidth();
		int width = durationText.computeSize(charWidth * 8, SWT.DEFAULT).x;
		gc.dispose();
		FormData data = new FormData(width, SWT.DEFAULT);
		durationText.setLayoutData(data);
		data.left = new FormAttachment(textLabel, 5);
		data.top = new FormAttachment(textLabel, 0, SWT.CENTER);

		setControl(topLevel);
		validateInput();
	}

	public void validateInput() {
		String duration = durationText.getText();

		if (duration == null || duration.equals("")) {
			this.setPageComplete(false);
			return;
		}

		try {
			Long.parseLong(duration);
		} catch (NumberFormatException e) {
			this.setPageComplete(false);
		}

		this.setPageComplete(true);
	}

	public long getDuration() {
		try {
			return Long.parseLong(durationText.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
