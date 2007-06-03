/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import org.bpelunit.framework.xml.suite.XMLActivity;
import org.bpelunit.framework.xml.suite.XMLSendActivity;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import org.bpelunit.toolsupport.editors.wizards.ActivityWizard;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.components.SendComponent;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.bpelunit.toolsupport.util.ToolUtil;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a "complete" send, i.e. operation and literal XML field.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendCompleteWizardPage extends OperationWizardPage {

	private SendComponent fSendComponent;
	private XMLSendActivity fSendActivity;

	/**
	 * Creates a new send wizard page showing an operation and send control. The operation
	 * information will be initialized from the first given activity, the send information will be
	 * initialized from the second given activity.
	 * 
	 * @param operationActivity
	 * @param sendActivity
	 * @param pageName
	 */
	public SendCompleteWizardPage(XMLActivity operationActivity, XMLSendActivity sendActivity, ActivityEditMode mode, String pageName) {
		super(operationActivity, mode, pageName);
		fSendActivity= sendActivity;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		super.createFieldControls(composite, nColumns);

		fSendComponent= new SendComponent(this, getFontMetrics());
		fSendComponent.init(getSendActivity());
		fSendComponent.createControls(composite, nColumns);
		fSendComponent.addComponentListener(this);

		valueChanged(null);
	}

	private XMLSendActivity getSendActivity() {
		return fSendActivity;
	}

	@Override
	public void valueChanged(DialogField field) {
		super.valueChanged(field);

		if (isPageComplete()) { // TODO this is because of errors from "above"
			// (operation!) - but what about updating the
			// message?!
			if (fSendComponent != null) {
				String xmlText= fSendComponent.getXmlText();
				try {
					ToolUtil.parseSendBlockWithException(getTestSuite(), xmlText);
				} catch (Exception e) {
					fail("Not valid XML: " + e.getMessage());
					return;
				}

				String delaySequence= fSendComponent.getDelaySequence().trim();
				/*
				 * If the delay sequence is completely empty, no delay will be introduced
				 */
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
	}

	private XMLTestSuite getTestSuite() {
		return ((ActivityWizard) getWizard()).getEditor().getTestSuite();
	}

	private void fail(String string) {
		setErrorMessage(string);
		setPageComplete(false);
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
