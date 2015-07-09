/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.util.XMLPrinterTool;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.data.extraction.DataExtraction;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.rits.cloning.Cloner;

/**
 * Abstract superclass of the two data specification packages Send and Receive.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class DataSpecification implements ITestArtefact {

	private static final Cloner CLONER = new Cloner();

	/**
	 * Next parent activity this data specification belongs to.
	 */
	private Activity fActivity;

	/**
	 * Namespace Context
	 */
	private NamespaceContext fNamespaceContext;

	/**
	 * Status of this object.
	 */
	private ArtefactStatus fStatus;


	// ********************** Initialization ***************************

	public DataSpecification(Activity parent, NamespaceContext nsContext) throws SpecificationException {
		fActivity= parent;
		fNamespaceContext = nsContext;
		setStatus(ArtefactStatus.createInitialStatus());
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
		return getStatus().hasProblems();
	}

	public NamespaceContext getNamespaceContext() {
		return fNamespaceContext;
	}

	public final void setStatus(ArtefactStatus fStatus) {
		this.fStatus = fStatus;
	}

	protected String expandTemplateToString(VelocityContextProvider context, String template) throws DataSourceException {
		WrappedContext velocityCtx = CLONER.deepClone(context.createVelocityContext(this));
		velocityCtx.putReadOnly("xpath", new XPathTool(getNamespaceContext()));
		velocityCtx.putReadOnly("printer", new XMLPrinterTool());

		// Expand the template as a regular string
		StringWriter writer = new StringWriter();
		try {
			Velocity.evaluate(velocityCtx, writer, "expandTemplate", template);
		} catch (Exception e) {
			throw new DataSourceException(e);
		}
		return writer.toString();
	}

	protected Element generateLiteralDataFromTemplate(ActivityContext context, String dataTemplate) {
		try {
			String expandedTemplate = expandTemplateToString(context, dataTemplate);
	
			// Parse back to a DOM XML element
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document docExpanded = dbf.newDocumentBuilder().parse(
					new InputSource(new StringReader(expandedTemplate)));
	
			Element literalData = docExpanded.getDocumentElement();
			context.saveSentMessage(literalData);
	
			return literalData;
		} catch (Exception ex) {
			setStatus(ArtefactStatus.createErrorStatus("Template expansion fault: "
					+ ex.getLocalizedMessage(), ex));
			return null;
		}
	}

	protected void extractData(ActivityContext context, List<DataExtraction> dataExtractions, Element literalData) {
		// Create Velocity context for the data extraction request
		Context conditionContext;
		try {
			conditionContext = context.createVelocityContext(this);
		} catch (Exception e) {
			setStatus(ArtefactStatus.createErrorStatus(String.format(
				"Could not create the Velocity context for this data extraction request: %s",
				e.getLocalizedMessage()), e));
			return;
		}
		ContextXPathVariableResolver variableResolver = new ContextXPathVariableResolver(conditionContext);
	
		for (DataExtraction de : dataExtractions) {
			de.evaluate(context, literalData, getNamespaceContext(), variableResolver);
			if (de.getStatus().isError()) {
				setStatus(ArtefactStatus.createErrorStatus(String.format(
						"Data extraction for %s failed: %s",
						de.getVariable(), de.getStatus().getMessage())));
				return;
			}
		}
	}

}
