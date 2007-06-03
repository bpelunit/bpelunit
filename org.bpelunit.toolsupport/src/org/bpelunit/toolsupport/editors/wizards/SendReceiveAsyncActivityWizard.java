/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import org.bpelunit.toolsupport.editors.TestSuitePage;

/**
 * A wizard for editing a send/receive asynchronous activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendReceiveAsyncActivityWizard extends TwoWayAsyncActivityWizard {

	public SendReceiveAsyncActivityWizard(TestSuitePage page, ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode, twoWayActivity);
	}

	@Override
	public void addPages() {
		super.addPages();

		addPage(createSendPage());
		addPage(createReceivePage());

		addHeaderProcessorPage();
		addDataCopyPage();
	}

	@Override
	protected String getPageName() {
		return "Send/Receive Synchronous";
	}

}
