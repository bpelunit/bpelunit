/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A data copy operation is responsible for transferring a single text element from input literal
 * XML data to output literal XML data.
 * 
 * Copying is a two-phase process:
 * 
 * After the DataCopyOperation has been initialized, the method
 * {@link #retrieveTextNodes(Element, NamespaceContext)} must be called, which extracts the text to
 * be copied from the given literal data.
 * 
 * Afterwards, the methods {@link #setTextNodes(Element, NamespaceContext)} may be called to insert
 * the previously copied value into a target literal data.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DataCopyOperation implements ITestArtefact {

	/**
	 * XPath expression which evaluates to the part to be copied (source)
	 */
	private String fFrom;

	/**
	 * XPath expression which evaluates to the target
	 */
	private String fTo;

	/**
	 * The copied value (when available)
	 */
	private String fCopiedValue;

	/**
	 * Parent activity
	 */
	private ITestArtefact fParent;

	/**
	 * Status of this object.
	 */
	private ArtefactStatus fStatus;


	// ******************** Initialization ************************

	public DataCopyOperation(ITestArtefact parent, String from, String to) {
		fFrom= from;
		fTo= to;
		fStatus= ArtefactStatus.createInitialStatus();
		fParent= parent;
	}


	// ******************** Implementation ***************************

	public void retrieveTextNodes(Element literalData, NamespaceContext namespaceContext) {

		XPath xpath= XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(namespaceContext);

		try {
			Node inputNode;
			inputNode= (Node) xpath.evaluate(getFromXPath(), literalData, XPathConstants.NODE);

			if (inputNode != null) {
				Node inputTextNode= null;
				// find the first Text Node underneath the selected Node
				NodeList children= inputNode.getChildNodes();
				for (int i= 0; i < children.getLength(); i++) {
					Node node= children.item(i);
					if (node.getNodeType() == Node.TEXT_NODE) {
						inputTextNode= node;
					}
				}
				if (inputTextNode == null) {
					fStatus= ArtefactStatus.createErrorStatus("Could not find a text node underneath node " + inputNode.getLocalName());
				} else {
					fStatus= ArtefactStatus.createInProgressStatus();
					setCopiedValue(inputTextNode.getNodeValue());
				}
			} else {
				fStatus= ArtefactStatus.createErrorStatus("XPath expression did not yield a result node.");
			}

		} catch (Exception e) {
			Throwable root= BPELUnitUtil.findRootThrowable(e);
			fStatus= ArtefactStatus.createErrorStatus("XPath Expression Exception when evaluating XPath: " + root.getMessage());
		}

	}

	public void setTextNodes(Element parent, NamespaceContext context) {

		XPath xpath= XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(context);

		try {

			Node outputNode= (Node) xpath.evaluate(getToXPath(), parent, XPathConstants.NODE);

			if (outputNode != null) {

				Node outTextNode= null;
				NodeList children= outputNode.getChildNodes();
				for (int i= 0; i < children.getLength(); i++) {
					Node node= children.item(i);
					if (node.getNodeType() == Node.TEXT_NODE) {
						outTextNode= node;
					}
				}
				if (outTextNode == null) {
					Node node= outputNode.getOwnerDocument().createTextNode(getCopiedValue());
					outputNode.appendChild(node);
				} else {
					outTextNode.setNodeValue(getCopiedValue());
				}
				fStatus= ArtefactStatus.createPassedStatus();

			} else {
				fStatus= ArtefactStatus.createErrorStatus("XPath expression did not yield a result node.");
			}
		} catch (Exception e) {
			Throwable root= BPELUnitUtil.findRootThrowable(e);
			fStatus= ArtefactStatus.createErrorStatus("XPath Expression Exception when evaluating XPath: " + root.getMessage());
		}
	}

	// ********************* Getters & Setters *********************

	public String getFromXPath() {
		return fFrom;
	}

	public String getToXPath() {
		return fTo;
	}

	public void setCopiedValue(String value) {
		fCopiedValue= value;
	}

	public String getCopiedValue() {
		return fCopiedValue != null ? fCopiedValue : "(none)";
	}

	public boolean isFailure() {
		return fStatus.isFailure();
	}

	public boolean isError() {
		return fStatus.isError();
	}

	// ******************** ITestArtefact **********************

	public String getName() {
		return "Copy operation";
	}

	public ITestArtefact getParent() {
		return fParent;
	}

	public List<ITestArtefact> getChildren() {
		return new ArrayList<ITestArtefact>();
	}

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		stateData.add(new StateData("From expression", fFrom));
		stateData.add(new StateData("To expression", fTo));
		stateData.add(new StateData("Copy value", getCopiedValue()));
		return stateData;
	}

	public void reportProgress(ITestArtefact artefact) {
		fParent.reportProgress(artefact);
	}

	// ************************** Other Stuff ***********************

	@Override
	public String toString() {
		StringBuffer r= new StringBuffer();
		r.append("Expression from: \"" + fFrom + "\" to: \"" + fTo + "\"");
		if (!fStatus.isInitial()) {
			r.append(" => Copied value: \"").append(getCopiedValue()).append("\"");
		}

		r.append(" => Evaluation: ").append(fStatus);
		return r.toString();
	}
}
