/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.io.StringWriter;

import javax.xml.namespace.NamespaceContext;

import net.bpelunit.framework.control.util.XMLPrinterTool;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import com.rits.cloning.Cloner;

/**
 * Abstract superclass of the two data specification packages Send and Receive.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class DataSpecification implements ITestArtefact {

	private static final Cloner fCloner = new Cloner();

	/**
	 * Next parent activity this data specification belongs to.
	 */
	private Activity fActivity;

	/**
	 * Namespace Context
	 */
	protected NamespaceContext fNamespaceContext;

	/**
	 * Status of this object.
	 */
	protected ArtefactStatus fStatus;


	// ********************** Initialization ***************************

	public DataSpecification(Activity parent, NamespaceContext nsContext) throws SpecificationException {
		fActivity= parent;
		fNamespaceContext= nsContext;
		fStatus= ArtefactStatus.createInitialStatus();
	}

	// ************************** ITestArtefact ************************


	public ITestArtefact getParent() {
		return fActivity;
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public void reportProgress(ITestArtefact artefact) {
		fActivity.reportProgress(artefact);
	}

	// ***************************** Other ******************************

	public boolean hasProblems() {
		return fStatus.hasProblems();
	}

	protected String expandTemplateToString(VelocityContextProvider context, String template) throws Exception {
		Context velocityCtx = fCloner.deepClone(context.createVelocityContext());
		velocityCtx.put("xpath", new XPathTool(fNamespaceContext));
		velocityCtx.put("printer", new XMLPrinterTool());

		// Expand the template as a regular string
		StringWriter writer = new StringWriter();
		Velocity.evaluate(velocityCtx, writer, "expandTemplate", template);
		String expandedTemplate = writer.toString();
		return expandedTemplate;
	}
}
