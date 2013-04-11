/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;



import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.ActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.SendComponent;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.util.ToolUtil;

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
	 * Creates a new send wizard page showing an operation and send control. The
	 * operation information will be initialized from the first given activity,
	 * the send information will be initialized from the second given activity.
	 * 
	 * @param operationActivity
	 * @param sendActivity
	 * @param pageName
	 */
	public SendCompleteWizardPage(XMLSoapActivity operationActivity, XMLSendActivity sendActivity,
			ActivityEditMode mode, String pageName) {
		super(operationActivity, mode, pageName);
		this.fSendActivity = sendActivity;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		super.createFieldControls(composite, nColumns);
		
		this.fSendComponent = new SendComponent(this, this.getFontMetrics());
		this.fSendComponent.init(this.getSendActivity());
		this.fSendComponent.createControls(composite, nColumns);
		this.fSendComponent.addComponentListener(this);
		
		
		
		this.valueChanged(null);
	}

	private XMLSendActivity getSendActivity() {
		return this.fSendActivity;
	}

	@Override
	public void valueChanged(DialogField field) {
		super.valueChanged(field);
		
		if (this.isPageComplete()) { // TODO this is because of errors from
			// "above"
			// (operation!) - but what about updating the
			// message?!
			if (this.fSendComponent != null) {
				String xmlText = this.fSendComponent.getXmlText();
				try {
					ToolUtil.parseSendBlockWithException(this.getTestSuite(), xmlText);
				} catch (Exception e) {
					this.fail("Not valid XML: " + e.getMessage());
					return;
				}

				String delaySequence = this.fSendComponent.getDelaySequence().trim();
				/*
				 * If the delay sequence is completely empty, no delay will be
				 * introduced
				 */
				if (!"".equals(delaySequence)) {
					String[] sequence = delaySequence.split(",");
					for (String element : sequence) {
						try {
							Integer.parseInt(element.trim());
						} catch (NumberFormatException e) {
							this.fail("Delay Sequence must be a comma-separate integer list.");
							return;
						}
					}
				}
				
			}
			if(this.fSendComponent!=null && fSendComponent.fieldTemplate!=null)
			{
			  
				if(fSendComponent.fieldTemplate.getError()){
					this.fail(fSendComponent.fieldTemplate.getMsgError());
					return;
				}
			}
			
			
			
			this.setErrorMessage(null);
			this.setPageComplete(true);
		}
	}

	private XMLTestSuite getTestSuite() {
		return ((ActivityWizard) this.getWizard()).getEditor().getTestSuite();
	}

	private void fail(String string) {
		this.setErrorMessage(string);
		this.setPageComplete(false);
	}

	public String getSendXML() {
		return this.fSendComponent.getXmlText();
	}

	public void setSendXML(String xml) {
		this.fSendComponent.setXmlText(xml);
	}
	public SendComponent getSendComponent()
	{
		return fSendComponent;
	}

	public String getDelaySequence() {
		return this.fSendComponent.getDelaySequence();
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.SEND;
	}

}
