/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.xml.suite.XMLDeploymentSection;
import org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import org.bpelunit.framework.xml.suite.XMLPartnerTrack;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import org.bpelunit.framework.xml.suite.XMLTrack;
import org.bpelunit.toolsupport.ToolSupportActivator;
import org.bpelunit.toolsupport.editors.src.XMLEditor;
import org.bpelunit.toolsupport.util.WSDLReadingException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import com.ibm.wsdl.Constants;

/**
 * The BPELUnit Editor.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitEditor extends FormEditor {

	private static final int VISUAL_PAGE= 0;
	private static final int SOURCE_PAGE= 1;

	/**
	 * The base model document for the test suite
	 */
	private XMLTestSuiteDocument fDocument;

	/**
	 * The first page of the editor (visual page)
	 */
	private TestSuitePage fTestSuitePage;
	/**
	 * The second page of the editor (source page)
	 */
	private XMLEditor fXmlEditorPage;

	/**
	 * Listeners on model changes
	 */
	private List<IModelChangedListener> fListeners;

	/**
	 * The project we're in
	 */
	private IProject fCurrentProject;

	/**
	 * The directory the .bpts is in
	 */
	private IContainer fCurrentDirectory;

	/**
	 * Cached WSDL definitions
	 */
	private Map<IFile, Definition> fWSDLDefinitions;

	/**
	 * The page to be opened first (may change if parsing errors occur)
	 */
	private int fFirstPage;


	// ***************** General editor methods *******************

	public BPELUnitEditor() {
		super();
		fListeners= new ArrayList<IModelChangedListener>();
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {

		if (! (editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");

		IFile baseFile= ((IFileEditorInput) editorInput).getFile();

		fCurrentProject= baseFile.getProject();
		fCurrentDirectory= baseFile.getParent();

		fWSDLDefinitions= new HashMap<IFile, Definition>();

		setPartName(baseFile.getName());

		if (!baseFile.exists()) {
			throw new PartInitException("Invalid Input: File " + baseFile + " does not exist.");
		}

		try {
			fDocument= XMLTestSuiteDocument.Factory.parse(baseFile.getContents());
			if (needsBasicRestructuring(fDocument))
				fTestSuitePage.markDirty();
			fFirstPage= VISUAL_PAGE;
		} catch (Exception e) {
			// Parsing not possible - switch to source view
			fFirstPage= SOURCE_PAGE;
		}


		super.init(site, editorInput);
	}

	@Override
	protected void addPages() {

		try {
			fTestSuitePage= new TestSuitePage(this, "testSuitePage", "Test Suite");
			addPage(fTestSuitePage);
			fXmlEditorPage= new XMLEditor();
			int index= addPage(fXmlEditorPage, getEditorInput());
			setPageText(index, "Source");

			/*
			 * Set the first page (must be source if non-parseable)
			 */
			setActivePage(fFirstPage);

		} catch (PartInitException e) {
			ToolSupportActivator.log(e);
		}
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code>
	 * method disposes all nested editors. Subclasses may extend.
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		preSave();
		// use the editor to save it
		fXmlEditorPage.doSave(monitor);
		postSave();

		super.editorDirtyStateChanged();
	}

	@Override
	public void doSaveAs() {
		preSave();

		fXmlEditorPage.doSaveAs();
		setInput(fXmlEditorPage.getEditorInput());

		postSave();
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void pageChange(int newPageIndex) {

		boolean canChange= true;

		if (newPageIndex == SOURCE_PAGE) {
			model2src(false);
		}
		if (newPageIndex == VISUAL_PAGE) {
			canChange= src2model(false);
		}
		if (canChange)
			super.pageChange(newPageIndex);
		else
			setActivePage(SOURCE_PAGE);
	}

	private void postSave() {
		if (getCurrentPage() == SOURCE_PAGE) {
			// refresh model (forced)
			src2model(true);
		}
	}

	private void preSave() {
		if (getCurrentPage() == VISUAL_PAGE) {
			// flushes data back into the model
			fTestSuitePage.doSave(new NullProgressMonitor());
			// copy data from model to text editor
			model2src(true);
		}
	}


	public XMLTestSuite getTestSuite() {
		XMLTestSuite testSuite= fDocument.getTestSuite();
		if (testSuite == null)
			testSuite= fDocument.addNewTestSuite();
		return testSuite;
	}



	private boolean src2model(boolean force) {
		/*
		 * Grab if forced, if dirty, or if called for the first time
		 */
		if (force || isDirty() || fDocument == null) {

			IDocument document= fXmlEditorPage.getDocumentProvider().getDocument(getEditorInput());
			String string= document.get();
			try {
				fDocument= XMLTestSuiteDocument.Factory.parse(string);
				if (needsBasicRestructuring(fDocument))
					fTestSuitePage.markDirty();

				for (IModelChangedListener listener : fListeners) {
					listener.modelChanged();
				}
			} catch (Exception e) {
				ErrorDialog.openError(getShell(), "Trouble parsing XML", e.getMessage(), ToolSupportActivator.getErrorStatus(e));
				return false;
			}
		}

		return true;
	}

	/**
	 * Creates the basic elements if not already present.
	 * 
	 * @param document
	 */
	private boolean needsBasicRestructuring(XMLTestSuiteDocument document) {
		boolean changed= false;

		if (document.getTestSuite() == null) {
			document.addNewTestSuite();
			changed= true;
		}

		XMLTestSuite suite= document.getTestSuite();

		if (suite.getDeployment() == null) {
			suite.addNewDeployment();
			changed= true;
		}
		if (suite.getTestCases() == null) {
			suite.addNewTestCases();
			changed= true;
		}

		XMLDeploymentSection deployment= suite.getDeployment();
		if (deployment.getPut() == null) {
			deployment.addNewPut();
			changed= true;
		}

		return changed;
	}

	private Shell getShell() {
		return fXmlEditorPage.getSite().getShell();
	}

	private void model2src(boolean force) {
		if ( (force || isDirty()) && (fDocument != null)) {
			IDocument document= fXmlEditorPage.getDocumentProvider().getDocument(getEditorInput());
			ByteArrayOutputStream output= new ByteArrayOutputStream();
			try {
				fDocument.save(output, BPELUnitUtil.getDefaultXMLOptions());
				document.set(output.toString());
			} catch (IOException e) {
				ErrorDialog.openError(getShell(), "Error", "Error writing XML model to file", ToolSupportActivator.getErrorStatus(e));
			}
		}
	}

	public IProject getCurrentProject() {
		return fCurrentProject;
	}


	public IContainer getCurrentDirectory() {
		return fCurrentDirectory;
	}

	private String getWSDLString(XMLTrack track) {

		if (track instanceof XMLPartnerTrack) {
			XMLPartnerTrack partner= (XMLPartnerTrack) track;
			List<XMLPartnerDeploymentInformation> partnerList= getTestSuite().getDeployment().getPartnerList();
			for (XMLPartnerDeploymentInformation information : partnerList) {
				if (information.getName().equals(partner.getName()))
					return information.getWsdl();
			}
		} else {
			return getTestSuite().getDeployment().getPut().getWsdl();
		}
		return null;

	}

	public Definition getWsdlForPartner(XMLTrack track) throws WSDLReadingException {

		String wsdl= getWSDLString(track);
		if (wsdl == null || "".equals(wsdl))
			return null;

		return getWsdlForFile(wsdl);
	}

	public Definition getWsdlForFile(String wsdl) throws WSDLReadingException {

		IPath path= new Path(wsdl);
		IResource resource= null;

		// try to find from current dir:
		resource= getCurrentDirectory().findMember(wsdl);

		// try to find from project dir:
		if (notFound(resource))
			resource= getCurrentProject().findMember(path);

		if (notFound(resource))
			resource= ResourcesPlugin.getWorkspace().getRoot().findMember(path);

		// all hope failed...
		if (notFound(resource))
			throw new WSDLReadingException("Cannot find WSDL file with file path " + wsdl);

		IFile file= (IFile) resource;

		// TODO caching probably NOT a good idea at all.
		Definition definition= fWSDLDefinitions.get(file);

		if (definition == null) {
			String fileName= file.getRawLocation().toFile().toString();
			// load WSDL
			try {
				WSDLFactory factory= WSDLFactory.newInstance();
				WSDLReader reader= factory.newWSDLReader();
				reader.setFeature(Constants.FEATURE_VERBOSE, false);

				definition= reader.readWSDL(null, fileName);
				fWSDLDefinitions.put(file, definition);
				return definition;

			} catch (WSDLException e) {
				throw new WSDLReadingException("Error loading WSDL file for partner", e);
			}
		} else
			return definition;

	}

	private boolean notFound(IResource resource) {
		return (resource == null || !resource.exists() || ! (resource instanceof IFile));
	}

	public void addModelChangedListener(IModelChangedListener listener) {
		fListeners.add(listener);
	}

	public void removeModelChangedListener(IModelChangedListener listener) {
		fListeners.remove(listener);
	}
}
