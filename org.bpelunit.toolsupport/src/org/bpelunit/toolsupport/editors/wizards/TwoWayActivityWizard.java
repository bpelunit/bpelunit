/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.xml.suite.XMLMapping;
import org.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import org.bpelunit.toolsupport.editors.TestSuitePage;
import org.bpelunit.toolsupport.editors.wizards.pages.DataCopyPage;
import org.bpelunit.toolsupport.editors.wizards.pages.HeaderProcessorPage;

/**
 * Abstract superclass for all wizards which deal with two-way activities.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class TwoWayActivityWizard extends ActivityWizard {

	private XMLTwoWayActivity fTwoWayActivity;
	private HeaderProcessorPage fHeaderPage;
	private DataCopyPage fDataCopyPage;

	public TwoWayActivityWizard(TestSuitePage page, ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode);
		fTwoWayActivity= twoWayActivity;
	}

	protected void addHeaderProcessorPage() {
		fHeaderPage= new HeaderProcessorPage(fTwoWayActivity, getMode(), getPageName());
		addPage(fHeaderPage);
	}

	protected void processHeaderPage() {
		if (!fHeaderPage.hasHeaderProcessorSelected()) {
			// No header was selected. In this case, we have to remove
			// the header processor.
			// If a processor was selected, all data already belongs to the
			// activity.
			fTwoWayActivity.unsetHeaderProcessor();
		}
	}

	protected void processDataCopyPage() {
		XMLMapping mapping= fTwoWayActivity.getMapping();
		if (mapping == null)
			return;
		else if (mapping.getCopyArray().length == 0)
			fTwoWayActivity.unsetMapping();

	}

	protected void addDataCopyPage() {
		fDataCopyPage= new DataCopyPage(fTwoWayActivity, getMode(), getPageName());
		addPage(fDataCopyPage);
	}

	protected XMLTwoWayActivity getTwoWayActivity() {
		return fTwoWayActivity;
	}

}
