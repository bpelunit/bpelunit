/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.transform.TransformerException;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.src.XMLEditor;
import net.bpelunit.toolsupport.util.WSDLReadingException;
import net.bpelunit.toolsupport.util.schema.WSDLParser;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.xml.sax.SAXException;

/**
 * The BPELUnit Editor.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitEditor extends FormEditor {

	private static final int VISUAL_PAGE = 0;
	private static final int SOURCE_PAGE = 1;

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
	 * Cached WSDL Parser
	 */
	private Map<Definition, WSDLParser> fWSDLParser;

	/**
	 * The page to be opened first (may change if parsing errors occur)
	 */
	private int fFirstPage;

	// ***************** General editor methods *******************

	public BPELUnitEditor() {
		super();
		this.fListeners = new ArrayList<IModelChangedListener>();
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {

		if (!(editorInput instanceof IFileEditorInput)) {
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		}

		IFile baseFile = ((IFileEditorInput) editorInput).getFile();

		this.fCurrentProject = baseFile.getProject();
		this.fCurrentDirectory = baseFile.getParent();

		this.fWSDLDefinitions = new HashMap<IFile, Definition>();
		this.fWSDLParser = new HashMap<Definition, WSDLParser>();

		this.setPartName(baseFile.getName());

		if (!baseFile.exists()) {
			throw new PartInitException("Invalid Input: File " + baseFile + " does not exist.");
		}

		try {
			// Try to parse from a file whenever possible, so we may have access to the path
			// where it is stored. This could be important for editing fields which contain
			// relative paths from the .bpts file.
			final IPath rawLocation = baseFile.getRawLocation();
			if (rawLocation != null) {
				final File file = rawLocation.makeAbsolute().toFile();
				this.fDocument = XMLTestSuiteDocument.Factory.parse(file);
			}
			else {
				this.fDocument = XMLTestSuiteDocument.Factory.parse(new InputStreamReader(baseFile
					.getContents(), baseFile.getCharset()));
			}
			if (this.needsBasicRestructuring(this.fDocument)) {
				this.fTestSuitePage.markDirty();
			}
			this.fFirstPage = VISUAL_PAGE;
		} catch (Exception e) {
			// Parsing not possible - switch to source view
			this.fFirstPage = SOURCE_PAGE;
		}

		super.init(site, editorInput);
	}

	@Override
	protected void addPages() {

		try {
			this.fTestSuitePage = new TestSuitePage(this, "testSuitePage", "Test Suite");
			this.addPage(this.fTestSuitePage);
			this.fXmlEditorPage = new XMLEditor();
			int index = this.addPage(this.fXmlEditorPage, this.getEditorInput());
			this.setPageText(index, "Source");

			/*
			 * Set the first page (must be source if non-parseable)
			 */
			this.setActivePage(this.fFirstPage);

		} catch (PartInitException e) {
			ToolSupportActivator.log(e);
		}
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		this.preSave();
		// use the editor to save it
		this.fXmlEditorPage.doSave(monitor);
		this.postSave();

		super.editorDirtyStateChanged();
	}

	@Override
	public void doSaveAs() {
		this.preSave();

		this.fXmlEditorPage.doSaveAs();
		this.setInput(this.fXmlEditorPage.getEditorInput());

		this.postSave();
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

		boolean canChange = true;

		if (newPageIndex == SOURCE_PAGE) {
			this.model2src(false);
		}
		if (newPageIndex == VISUAL_PAGE) {
			canChange = this.src2model(false);
		}
		if (canChange) {
			super.pageChange(newPageIndex);
		} else {
			this.setActivePage(SOURCE_PAGE);
		}
	}

	private void postSave() {
		if (this.getCurrentPage() == SOURCE_PAGE) {
			// refresh model (forced)
			this.src2model(true);
		}
	}

	private void preSave() {
		if (this.getCurrentPage() == VISUAL_PAGE) {
			// flushes data back into the model
			this.fTestSuitePage.doSave(new NullProgressMonitor());
			// copy data from model to text editor
			this.model2src(true);
		}
	}

	public XMLTestSuite getTestSuite() {
		XMLTestSuite testSuite = this.fDocument.getTestSuite();
		if (testSuite == null) {
			testSuite = this.fDocument.addNewTestSuite();
		}
		return testSuite;
	}

	private boolean src2model(boolean force) {
		/*
		 * Grab if forced, if dirty, or if called for the first time
		 */
		if (force || this.isDirty() || this.fDocument == null) {

			IDocument document = this.fXmlEditorPage.getDocumentProvider().getDocument(
					this.getEditorInput());
			String string = document.get();
			try {
				this.fDocument = XMLTestSuiteDocument.Factory.parse(string);
				if (this.needsBasicRestructuring(this.fDocument)) {
					this.fTestSuitePage.markDirty();
				}

				for (IModelChangedListener listener : this.fListeners) {
					listener.modelChanged();
				}
			} catch (Exception e) {
				ErrorDialog.openError(this.getShell(), "Trouble parsing XML", e.getMessage(),
						ToolSupportActivator.getErrorStatus(e));
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
		boolean changed = false;

		if (document.getTestSuite() == null) {
			document.addNewTestSuite();
			changed = true;
		}

		XMLTestSuite suite = document.getTestSuite();

		if (suite.getDeployment() == null) {
			suite.addNewDeployment();
			changed = true;
		}
		if (suite.getTestCases() == null) {
			suite.addNewTestCases();
			changed = true;
		}

		XMLDeploymentSection deployment = suite.getDeployment();
		if (deployment.getPut() == null) {
			deployment.addNewPut();
			changed = true;
		}

		return changed;
	}

	private Shell getShell() {
		return this.fXmlEditorPage.getSite().getShell();
	}

	private void model2src(boolean force) {
		if ((force || this.isDirty()) && (this.fDocument != null)) {
			IDocument document = this.fXmlEditorPage.getDocumentProvider().getDocument(
					this.getEditorInput());
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
				this.fDocument.save(output, BPELUnitUtil.getDefaultXMLOptions());
				document.set(output.toString());
			} catch (IOException e) {
				ErrorDialog.openError(this.getShell(), "Error", "Error writing XML model to file",
						ToolSupportActivator.getErrorStatus(e));
			}
		}
	}

	public IProject getCurrentProject() {
		return this.fCurrentProject;
	}

	public IContainer getCurrentDirectory() {
		return this.fCurrentDirectory;
	}

	private String getPartnerWSDLString(XMLTrack track) {

		if (track instanceof XMLPartnerTrack) {
			XMLPartnerTrack partner = (XMLPartnerTrack) track;
			List<XMLPartnerDeploymentInformation> partnerList = this.getTestSuite().getDeployment()
					.getPartnerList();
			for (XMLPartnerDeploymentInformation information : partnerList) {
				if (information.getName().equals(partner.getName())) {
					return information.getPartnerWsdl();
				}
			}
		} else {
			return this.getTestSuite().getDeployment().getPut().getPartnerWSDL();
		}

		return null;
	}

	private String getWSDLString(XMLTrack track) {

		if (track instanceof XMLPartnerTrack) {
			XMLPartnerTrack partner = (XMLPartnerTrack) track;
			List<XMLPartnerDeploymentInformation> partnerList = this.getTestSuite().getDeployment()
					.getPartnerList();
			for (XMLPartnerDeploymentInformation information : partnerList) {
				if (information.getName().equals(partner.getName())) {
					return information.getWsdl();
				}
			}
		} else {
			return this.getTestSuite().getDeployment().getPut().getWsdl();
		}

		return null;
	}

	public Definition getWsdlForPartner(XMLTrack track) throws WSDLReadingException {

		String wsdl = this.getWSDLString(track);
		if (wsdl == null || "".equals(wsdl)) {
			return null;
		}

		return this.getWsdlForFile(wsdl);
	}

	public Definition getPartnerWsdlForPartner(XMLTrack track) throws WSDLReadingException {

		String wsdl = this.getPartnerWSDLString(track);
		if (wsdl == null || "".equals(wsdl)) {
			return null;
		}

		return this.getWsdlForFile(wsdl);
	}

	public Definition getWsdlForFile(String wsdl) throws WSDLReadingException {

		IPath path = new Path(wsdl);
		IResource resource = null;

		// try to find from current dir:
		resource = this.getCurrentDirectory().findMember(wsdl);

		// try to find from project dir:
		if (this.notFound(resource)) {
			resource = this.getCurrentProject().findMember(path);
		}

		if (this.notFound(resource)) {
			resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		}

		// all hope failed...
		if (this.notFound(resource)) {
			throw new WSDLReadingException("Cannot find WSDL file with file path " + wsdl);
		}

		IFile file = (IFile) resource;

		// TODO caching probably NOT a good idea at all.
		Definition definition = this.fWSDLDefinitions.get(file);

		if (definition == null) {
			String fileName = file.getRawLocation().toFile().toString();
			// load WSDL
			try {
				WSDLFactory factory = WSDLFactory.newInstance();
				WSDLReader reader = factory.newWSDLReader();
				// reader.setFeature(Constants.FEATURE_VERBOSE, false);

				definition = reader.readWSDL(null, fileName);
				this.fWSDLDefinitions.put(file, definition);

				WSDLParser parser = new WSDLParser(definition);
				this.fWSDLParser.put(definition, parser);

			} catch (WSDLException e) {
				throw new WSDLReadingException("Error loading WSDL file for partner", e);
			} catch (SAXException e) {
				MessageDialog dialog = new MessageDialog(this.getShell(), "Invalid Schema", null,
						e.getMessage(), MessageDialog.ERROR, new String[] { "OK" }, 0);
				dialog.open();
				throw new WSDLReadingException("Error reading Schemata in WSDL: " + e.getMessage(),
						e);
			} catch (TransformerException e) {
				MessageDialog dialog = new MessageDialog(this.getShell(), "Invalid Schema", null,
						e.getMessage(), MessageDialog.ERROR, new String[] { "OK" }, 0);
				dialog.open();
				throw new WSDLReadingException("Error reading Schemata in WSDL: " + e.getMessage(),
						e);
			}
		}
		return definition;

	}

	public WSDLParser getWSDLParserForDefinition(Definition definition) {
		return this.fWSDLParser.get(definition);
	}

	private boolean notFound(IResource resource) {
		return (resource == null || !resource.exists() || !(resource instanceof IFile));
	}

	public void addModelChangedListener(IModelChangedListener listener) {
		this.fListeners.add(listener);
	}

	public void removeModelChangedListener(IModelChangedListener listener) {
		this.fListeners.remove(listener);
	}

	public void refresh() {
		if (getActivePage() == VISUAL_PAGE) {
			for (IFormPart part : fTestSuitePage.getManagedForm().getParts()) {
				part.refresh();
			}
		}

	}
}
