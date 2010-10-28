/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.formwidgets.ContextPart;
import net.bpelunit.toolsupport.editors.formwidgets.EntryAdapter;
import net.bpelunit.toolsupport.editors.formwidgets.TextEntry;
import net.bpelunit.toolsupport.editors.wizards.NamespaceWizard;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * The test suite section allows the user to edit the test suite name and base url and provides a
 * link to the namespace editor.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TestSuiteSection extends BPELUnitSection implements ContextPart, IPropertyListener, IHyperlinkListener {

	private TextEntry nameEntry;
	private TextEntry baseURLEntry;

	public TestSuiteSection(Composite parent, TestSuitePage page, FormToolkit toolkit, int style) {

		super(page, parent, toolkit, style);
		createClient(getSection(), toolkit);
		page.getEditor().addPropertyListener(this);
	}

	protected void createClient(Section section, FormToolkit toolkit) {

		section.setText("Test Suite");
		section.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		section.setDescription("Enter a name and a base URL for this suite.");

		Composite content= toolkit.createComposite(section);

		TableWrapLayout layout= new TableWrapLayout();
		layout.leftMargin= layout.rightMargin= toolkit.getBorderStyle() != SWT.NULL ? 0 : 2;
		layout.numColumns= 2;
		content.setLayout(layout);

		section.setClient(content);

		createNameEntry(content, toolkit);
		createDescriptionEntry(content, toolkit);

		createText(content, "<form><p><a href=\"namespaceOptions\">Configure Namespace Prefixes...</a></p></form>", toolkit, this);

		toolkit.paintBordersFor(content);
	}

	private void createNameEntry(Composite content, FormToolkit toolkit) {

		nameEntry= new TextEntry(content, toolkit, "Suite Name", SWT.SINGLE);
		nameEntry.setFormEntryListener(new EntryAdapter(this) {
			@Override
			public void textValueChanged(TextEntry entry) {
				getTestSuite().setName(entry.getValue());
				markDirty();
			}
		});

	}

	private void createDescriptionEntry(Composite content, FormToolkit toolkit) {

		baseURLEntry= new TextEntry(content, toolkit, "Base URL", SWT.SINGLE);

		baseURLEntry.setFormEntryListener(new EntryAdapter(this) {
			@Override
			public void textValueChanged(TextEntry entry) {
				getTestSuite().setBaseURL(entry.getValue());
				markDirty();
			}
		});

	}

	@Override
	public void refresh() {
		XMLTestSuite suite= getTestSuite();
		nameEntry.setValue(suite.getName());
		baseURLEntry.setValue(suite.getBaseURL());
		super.refresh();
	}

	public void fireSaveNeeded() {
		markDirty();
	}

	public void propertyChanged(Object source, int propId) {
	}

	public void linkActivated(HyperlinkEvent e) {
		WizardDialog dialog= new WizardDialog(getShell(), new NamespaceWizard(getTestSuite()));
		if (dialog.open() == Window.OK)
			markDirty();
	}

	public void linkEntered(HyperlinkEvent e) {
		// do nothing
	}

	public void linkExited(HyperlinkEvent e) {
		// do nothing
	}

}
