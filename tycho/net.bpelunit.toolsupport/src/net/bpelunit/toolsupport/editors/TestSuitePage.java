/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors;

import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.toolsupport.editors.sections.ActivitySection;
import net.bpelunit.toolsupport.editors.sections.PUTSection;
import net.bpelunit.toolsupport.editors.sections.PartnerSection;
import net.bpelunit.toolsupport.editors.sections.TestCaseAndTrackSection;
import net.bpelunit.toolsupport.editors.sections.TestSuiteSection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 
 * The first page in the BPELUnit editor.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TestSuitePage extends FormPage {

	// Sections
	private TestSuiteSection fGeneralSection;
	private PUTSection fPUTSection;
	private PartnerSection fPartnerSection;
	private ActivitySection fTrackSection;
	private TestCaseAndTrackSection fTestCaseAndTrackSection;

	public TestSuitePage(BPELUnitEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {

		super.createFormContent(managedForm);

		ScrolledForm form= managedForm.getForm();
		FormToolkit toolkit= managedForm.getToolkit();
		form.setText("Test Suite");
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {

		GridLayout layout= new GridLayout();
		Composite body= managedForm.getForm().getBody();
		layout.numColumns= 2;
		layout.marginWidth= 10;
		layout.horizontalSpacing= 15;
		layout.verticalSpacing= 10;
		layout.makeColumnsEqualWidth= true;
		body.setLayout(layout);
		// body.setBackground(new Color(body.getDisplay(), 255, 0,0));

		// Upper left
		createTestSuiteSection(managedForm, toolkit, body);
		// Upper right
		createPartnerSection(managedForm, toolkit, body);

		createPUTSection(managedForm, toolkit, body);

		// Lower left
		createTestCaseSection(managedForm, toolkit, body);
		// Lower right
		createActivitySection(managedForm, toolkit, body);
	}

	private void createTestSuiteSection(IManagedForm managedForm, FormToolkit toolkit, Composite body) {

		fGeneralSection= new TestSuiteSection(body, this, toolkit, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		managedForm.addPart(fGeneralSection);
	}

	private void createPUTSection(IManagedForm managedForm, FormToolkit toolkit, Composite body) {

		fPUTSection= new PUTSection(body, this, toolkit, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		managedForm.addPart(fPUTSection);
	}

	private void createPartnerSection(IManagedForm managedForm, FormToolkit toolkit, Composite body) {

		fPartnerSection= new PartnerSection(body, this, toolkit);
		managedForm.addPart(fPartnerSection);

		GridData gridData= new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.verticalSpan= 2;
		fPartnerSection.getSection().setLayoutData(gridData);
	}

	private void createTestCaseSection(IManagedForm managedForm, FormToolkit toolkit, Composite parent) {

		fTestCaseAndTrackSection= new TestCaseAndTrackSection(parent, this, toolkit);
		managedForm.addPart(fTestCaseAndTrackSection);
	}

	private void createActivitySection(IManagedForm managedForm, FormToolkit toolkit, Composite parent) {

		fTrackSection= new ActivitySection(parent, this, toolkit);
		managedForm.addPart(fTrackSection);
	}

	public BPELUnitEditor getSuiteEditor() {
		return (BPELUnitEditor) getEditor();
	}

	public void postTrackSelected(XMLTrack selection) {
		fTrackSection.handleTrackSelection(selection);
	}
	
	public void postTrackSelected(XMLHumanPartnerTrack selection) {
		fTrackSection.handleTrackSelection(selection);
	}

	public void markDirty() {
		fGeneralSection.markDirty();
	}

}
