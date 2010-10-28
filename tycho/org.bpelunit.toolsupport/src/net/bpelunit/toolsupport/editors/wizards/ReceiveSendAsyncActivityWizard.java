/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.TestSuitePage;

/**
 * A wizard for editing a receive/send asynchronous activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveSendAsyncActivityWizard extends TwoWayAsyncActivityWizard {

	public ReceiveSendAsyncActivityWizard(TestSuitePage page, ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode, twoWayActivity);
	}

	@Override
	public void addPages() {
		super.addPages();

		addPage(createReceivePage());
		addPage(createSendPage());

		addHeaderProcessorPage();
		addDataCopyPage();

	}

	@Override
	protected String getPageName() {
		return "Receive/Send Asynchronous";
	}

}
