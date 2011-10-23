/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import javax.wsdl.Definition;

import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.BPELUnitEditor;
import net.bpelunit.toolsupport.editors.IModelChangedListener;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Abstract super-section for all sections in the TestSuitePage.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class BPELUnitSection extends SectionPart implements IModelChangedListener {

	private TestSuitePage fSuitePage;

	public BPELUnitSection(TestSuitePage page, Composite parent, FormToolkit toolkit, int style) {
		super(parent, toolkit, style);
		fSuitePage= page;
		getEditor().addModelChangedListener(this);
	}

	public TestSuitePage getPage() {
		return fSuitePage;
	}

	@Override
	public void dispose() {
		super.dispose();
		getEditor().removeModelChangedListener(this);
	}

	public void modelChanged() {
		/*
		 * Directly call refresh, without calling markStale(). markStale() triggers some other
		 * events we're not interested in.
		 * 
		 */
		refresh();
	}

	protected Shell getShell() {
		return fSuitePage.getSite().getShell();
	}

	protected XMLTestSuite getTestSuite() {
		XMLTestSuite model= getEditor().getTestSuite();
		return model;
	}

	protected void manageTargetNamespace(String file) {
		// Add namespace prefix to test suite
		try {
			Definition wsdlForFile= getEditor().getWsdlForFile(file);
			String targetNamespace= wsdlForFile.getTargetNamespace();
			getTestSuite().newCursor().prefixForNamespace(targetNamespace);
		} catch (Exception e) {
			// cannot happen, checked before
		}
	}

	protected FormText createText(Composite section, String content, FormToolkit toolkit, IHyperlinkListener listener) {

		FormText text= toolkit.createFormText(section, true);
		try {
			text.setText(content, true, false);
		} catch (SWTException e) {
			text.setText(e.getMessage(), false, false);
		}

		TableWrapData tableWrapData= new TableWrapData(TableWrapData.FILL_GRAB);
		tableWrapData.colspan= 2;
		text.setLayoutData(tableWrapData);
		text.addHyperlinkListener(listener);
		return text;
	}

	protected BPELUnitEditor getEditor() {
		return getPage().getSuiteEditor();
	}
}
