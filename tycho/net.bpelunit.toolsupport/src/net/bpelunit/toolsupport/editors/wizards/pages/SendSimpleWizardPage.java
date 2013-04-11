/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;


import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.ActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import net.bpelunit.toolsupport.editors.wizards.components.MessageChangeListener;
import net.bpelunit.toolsupport.editors.wizards.components.SendComponent;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.util.ToolUtil;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a "simple" send, i.e. only the literal XML field.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendSimpleWizardPage extends ActivityWizardPage implements
		IComponentListener {

	private SendComponent fSendComponent;
	private XMLSendActivity fSendActivity;

	public SendSimpleWizardPage(XMLSendActivity sendActivity,
			ActivityEditMode mode, String pageName) {
		super(pageName, mode);
		this.setDescription("Enter the data to be sent.");
		this.fSendActivity = sendActivity;
				
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		
		this.fSendComponent = new SendComponent(this, this.getFontMetrics());
		this.fSendComponent.init(this.fSendActivity);
		this.fSendComponent.createControls(composite, nColumns);
		this.fSendComponent.addComponentListener(this);
		this.valueChanged(null);

	}

	public void valueChanged(DialogField field) {

		if (this.fSendComponent != null) {
			String xmlText = this.fSendComponent.getXmlText();
			try {
				ToolUtil.parseSendBlockWithException(this.getTestSuite(),
						xmlText);
			} catch (Exception e) {
				this.setErrorMessage("Not valid XML: " + e.getMessage());
				this.setPageComplete(false);
				return;
			}

			String delaySequence = this.fSendComponent.getDelaySequence()
					.trim();
			if (!"".equals(delaySequence)) {
				String[] sequence = delaySequence.split(",");
				for (String element : sequence) {
					try {
						Integer.parseInt(element.trim());
					} catch (NumberFormatException e) {
						this.fail("Delay Sequence must be a comma-separate integer list");
						return;
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
		}

		this.setErrorMessage(null);
		this.setPageComplete(true);
	}

	private void fail(String string) {
		this.setErrorMessage(string);
		this.setPageComplete(false);
	}

	private XMLTestSuite getTestSuite() {
		return ((ActivityWizard) this.getWizard()).getEditor().getTestSuite();
	}

	public String getSendXML() {
		return this.fSendComponent.getXmlText();
	}

	public String getDelaySequence() {
		return this.fSendComponent.getDelaySequence();
	}
	public SendComponent getSendComponent()
	{
		return fSendComponent;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.SEND;
	}

	public MessageChangeListener getOperationChangeListener() {
		return this.fSendComponent;
	}
}
