/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import org.bpelunit.framework.xml.suite.XMLSendActivity;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import org.bpelunit.toolsupport.editors.wizards.ActivityWizard;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import org.bpelunit.toolsupport.editors.wizards.components.SendComponent;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.bpelunit.toolsupport.util.ToolUtil;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a "simple" send, i.e. only the literal XML field.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendSimpleWizardPage extends ActivityWizardPage implements IComponentListener {

	private SendComponent fSendComponent;
	private XMLSendActivity fSendActivity;

	public SendSimpleWizardPage(XMLSendActivity sendActivity, ActivityEditMode mode, String pageName) {
		super(pageName, mode);
		setDescription("Enter the data to be sent.");
		fSendActivity= sendActivity;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		fSendComponent= new SendComponent(this, getFontMetrics());
		fSendComponent.init(fSendActivity);
		fSendComponent.createControls(composite, nColumns);
		fSendComponent.addComponentListener(this);

		valueChanged(null);
	}

	public void valueChanged(DialogField field) {

		if (fSendComponent != null) {
			String xmlText= fSendComponent.getXmlText();
			try {
				ToolUtil.parseSendBlockWithException(getTestSuite(), xmlText);
			} catch (Exception e) {
				setErrorMessage("Not valid XML: " + e.getMessage());
				setPageComplete(false);
				return;
			}

			String delaySequence= fSendComponent.getDelaySequence().trim();
			if (!"".equals(delaySequence)) {
				String[] sequence= delaySequence.split(",");
				for (String element : sequence) {
					try {
						Integer.parseInt(element.trim());
					} catch (NumberFormatException e) {
						fail("Delay Sequence must be a comma-separate integer list.");
						return;
					}
				}
			}
		}

		setErrorMessage(null);
		setPageComplete(true);
	}

	private void fail(String string) {
		setErrorMessage(string);
		setPageComplete(false);
	}

	private XMLTestSuite getTestSuite() {
		return ((ActivityWizard) getWizard()).getEditor().getTestSuite();
	}

	public String getSendXML() {
		return fSendComponent.getXmlText();
	}

	public String getDelaySequence() {
		return fSendComponent.getDelaySequence();
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.SEND;
	}
}
