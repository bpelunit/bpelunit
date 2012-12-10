package net.bpelunit.framework.coverage.instrumentation;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.service.MarkerService;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.execution.IBPELUnitContext;
import net.bpelunit.framework.execution.ITestLifeCycleElement;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.ICreateInstance;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MarkerToActivityConverter {

	public void convertMarkersToActivities(IBPELUnitContext context)
			throws DeploymentException {

		for (IBPELProcess process : context.getDeployment().getBPELProcesses()) {
			addWsdlToProcess(process);
			addPartnerlinkToProcess(process, context);

		}
	}

	private void addPartnerlinkToProcess(IBPELProcess process,
			IBPELUnitContext context) {
		MarkerService markerService = new MarkerService(
				filterInstrumenters(context.getElementsInPrepareProcesses()));
		process.addPartnerlink(
				CoverageConstants.MARKER_SERVICE_PARTNERLINK,
				CoverageConstants.COVERAGE_PARTNERLINK_TYPE,
				null,
				null,
				CoverageConstants.MARKER_SERVICE_PARTNERLINK_PARTNERROLE,
				CoverageConstants.COVERAGE_SERVICE_SERVICE,
				CoverageConstants.COVERAGE_SERVICE_PORT,
				context.addService(
						CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME,
						markerService).toExternalForm());
	}

	private List<? extends AbstractInstrumenter> filterInstrumenters(
			List<? extends ITestLifeCycleElement> elementsInPrepareProcesses) {

		List<AbstractInstrumenter> result = new ArrayList<AbstractInstrumenter>();
		for (ITestLifeCycleElement e : elementsInPrepareProcesses) {
			if (e instanceof AbstractInstrumenter) {
				result.add((AbstractInstrumenter) e);
			}
		}
		return result;
	}

	private void addWsdlToProcess(IBPELProcess p) throws DeploymentException {
		p.addWSDLImport(CoverageConstants.WSDL_NAME,
				CoverageConstants.MARKER_SERVICE_NAMESPACE, getClass()
						.getResourceAsStream("/" + CoverageConstants.WSDL_NAME));
		p.addXSDImport(CoverageConstants.XSD_NAME,
				CoverageConstants.MARKER_SERVICE_NAMESPACE, getClass()
						.getResourceAsStream("/" + CoverageConstants.XSD_NAME));
	}

	IScope createScopeForMarkers(IActivity a) {
		IActivityContainer parent = a.getParent();

		IScope scope = parent.wrapActivityInNewScope(a);
		scope.setName(CoverageConstants.INSTRUMENTATION_SCOPE_NAME_PREFIX
				+ a.getName());
		ISequence sequence = scope.wrapActivityInNewSequence(a);

		IVariable v = scope.addVariable();
		v.setName(CoverageConstants.VARIABLE_MARK_REQUEST);
		v.setMessageType(CoverageConstants.MARKER_SERVICE_MARK_REQUEST_MESSAGE_TYPE);

		IAssign assign = sequence.addAssign();
		assign.setName(a.getName() + "_Prepare_Mark_Request");
		ICopy copy = assign.addCopy();
		Element msgElement = copy.getFrom().setNewLiteral(
				CoverageConstants.MARKER_SERVICE_NAMESPACE,
				CoverageConstants.COVERAGE_MSG_ELEMENT);
		buildCoverageMarkerMessage(msgElement, a);
		copy.getTo().setVariable(v);
		copy.getTo()
				.setPart(CoverageConstants.MARKER_SERVICE_MARK_REQUEST_PART);
		copy.getTo().setExpression("/");

		IInvoke invoke = sequence.addInvoke();
		invoke.setName(a.getName() + "_Invoke_Marker_Service");
		invoke.setInputVariable(v);
		invoke.setOperation(CoverageConstants.COVERAGE_SERVICE_MARK_OPERATION);
		invoke.setPartnerLink(CoverageConstants.MARKER_SERVICE_PARTNERLINK);

		if (startsInstance(a)) {
			sequence.moveBefore(assign, a);
			sequence.moveBefore(invoke, a);
		}
		return scope;
	}

	private boolean startsInstance(IActivity a) {
		return !(a instanceof ICreateInstance)
				|| !((ICreateInstance) a).isCreateInstance();
	}

	Element buildCoverageMarkerMessage(Element msgElement, IActivity a) {
		Document msgDoc = msgElement.getOwnerDocument();
		List<? extends IDocumentation> documentation = a.getDocumentation();

		if (documentation != null && documentation.size() > 0) {
			IDocumentation firstDoc = documentation.get(0);
			for (Node n : firstDoc.getDocumentationElements()) {
				if (isMarkerElement(n)) {
					Element markerElement = msgDoc.createElementNS(
							CoverageConstants.MARKER_SERVICE_NAMESPACE,
							CoverageConstants.COVERAGE_MSG_MARKER_ELEMENT);
					XMLUtil.appendTextNode(markerElement,
							XMLUtil.getTextContent(n));
					msgElement.appendChild(markerElement);
				}
			}
		}
		return msgElement;
	}

	private boolean isMarkerElement(Node n) {
		return AbstractInstrumenter.QNAME_MARKER.getNamespaceURI().equals(
				n.getNamespaceURI())
				&& AbstractInstrumenter.QNAME_MARKER.getLocalPart().equals(
						n.getLocalName());
	}
}
